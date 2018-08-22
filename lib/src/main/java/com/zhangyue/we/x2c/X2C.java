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

    private static X2C sInstance;
    private SparseArray<IViewCreator> mSparseArray = new SparseArray<>();

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
        IViewCreator creator = null;
        if (mSparseArray.indexOfKey(layoutId) >= 0) {
            creator = mSparseArray.get(layoutId);
        } else {
            try {
                creator = (IViewCreator) context.getClassLoader()
                        .loadClass("com.zhangyue.we.x2c.X2C_" + layoutId).newInstance();
            } catch (Exception ignored) {//
            }
            mSparseArray.put(layoutId, creator);
        }
        return creator == null ? null : creator.createView(context, layoutId);
    }
}
