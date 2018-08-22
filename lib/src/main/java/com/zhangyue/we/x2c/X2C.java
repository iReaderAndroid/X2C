package com.zhangyue.we.x2c;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * @author：chengwei 2018/8/9
 * @description
 */
public class X2C {

    private static X2C sInstance;
    private HashMap<String, IViewCreator> mMap = new HashMap<>();

    private X2C() {

    }

    public static X2C instance() {
        if (sInstance == null) {
            synchronized (X2C.class) {
                if (sInstance == null) {
                    sInstance = new X2C();
                }
            }
        }
        return sInstance;
    }

    /**
     * 设置contentview，检测如果有对应的java文件，使用java文件，否则使用xml
     *
     * @param activity 上下文
     * @param layoutId layout的资源id
     */
    public void setContentView(Activity activity, int layoutId) {
        if (activity == null) {
            throw new IllegalArgumentException(" activity must not be null");
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
    public View inflate(Context context, int layoutId, ViewGroup parent) {
        if (context == null) {
            throw new IllegalArgumentException(" context must not be null");
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

    private View getView(Context context, int layoutId) {
        String group = generateGroupId(layoutId);
        IViewCreator creator = mMap.get(group);
        if (creator == null) {
            try {
                creator = (IViewCreator) context.getClassLoader().loadClass("com.zhangyue.we.x2c.X2C_" + group).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (creator == null) {
                creator = new DefaultCreator();
            }
            mMap.put(group, creator);
        }
        return creator.createView(context, layoutId);
    }

    private String generateGroupId(int layoutId) {
        return "A" + Integer.toHexString(layoutId >> 24);
    }

    private static class DefaultCreator implements IViewCreator {

        @Override
        public View createView(Context context, int layoutId) {
            return null;
        }
    }


}
