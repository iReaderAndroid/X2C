package com.zhangyue.we.x2c;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author：chengwei 2018/8/9
 * @description
 */
public class X2C {
    private static final SparseArray<IViewCreator> sSparseArray = new SparseArray<>();

    /**
     * 设置contentview，检测如果有对应的java文件，使用java文件，否则使用xml
     *
     * @param activity 上下文
     * @param layoutId layout的资源id
     */
    public static void setContentView(Activity activity, int layoutId) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity must not be null");
        }
        View view = getView(activity, layoutId);
        if (view != null) {
            activity.setContentView(view);
        } else {
            activity.setContentView(layoutId);
        }
    }

    /**
     * 加载xml文件，检测如果有对应的java文件，使用java文件，否则使用xml
     *
     * @param context  上下文
     * @param layoutId layout的资源id
     */
    public static View inflate(Context context, int layoutId, ViewGroup parent) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        View view = getView(context, layoutId);
        if (view != null) {
            if (parent != null) {
                parent.addView(view);
            }
            return view;
        } else {
            return LayoutInflater.from(context).inflate(layoutId, parent);
        }
    }

    public static View getView(Context context, int layoutId) {
        int group = generateGroupId(layoutId);
        IViewCreator creator = sSparseArray.get(group);
        if (creator == null) {
            try {
                creator = (IViewCreator) context.getClassLoader().loadClass("com.zhangyue.we.x2c.X2C_" + group).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //如果creator为空，放一个默认进去，防止每次都调用反射方法耗时
            if (creator == null) {
                creator = new DefaultCreator();
            }
            sSparseArray.put(group, creator);
        }
        return creator.createView(context, layoutId);
    }

    private static class DefaultCreator implements IViewCreator {

        @Override
        public View createView(Context context, int layoutId) {
            return null;
        }
    }

    private static int generateGroupId(int layoutId) {
        return  layoutId >> 24;
    }
}
