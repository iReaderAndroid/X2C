package com.zhangyue.we.x2c;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

/**
 * @author 7hens
 */
public final class X2CUtils {

    public static int getResourceIdFromAttr(Context ctx, int attr) {
        TypedValue outValue = new TypedValue();
        Resources.Theme theme = ctx.getTheme();
        theme.resolveAttribute(attr, outValue, true);
        TypedArray typedArray = theme.obtainStyledAttributes(outValue.resourceId, new int[]{attr});
        try {
            return typedArray.getResourceId(0, 0);
        } catch (Exception e) {
            typedArray.recycle();
            return 0;
        }
    }
}
