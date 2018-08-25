package com.zhangyue.we.x2c.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author：chengwei 2018/8/25
 * @description
 */
public class CustomView extends View {

    public static final int RED = 1;
    public static final int BLACK = 1 << 1;
    public static final int MIX = RED | BLACK;

    private int mMixColor;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        int N = typedArray.length();
        int index;
        for (int i = 0; i < N; i++) {
            switch (index = typedArray.getIndex(i)) {
                case R.styleable.CustomView_mixColor:
                    setMixColor(typedArray.getInteger(index, RED));
                    break;
            }
        }
        typedArray.recycle();
    }


    public void setMixColor(int mixColor) {
        this.mMixColor = mixColor;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        switch (mMixColor) {
            case RED:
                canvas.drawColor(Color.RED);
                break;
            case BLACK:
                canvas.drawColor(Color.BLACK);
                break;
            case MIX:
                int clip = getMeasuredWidth() >> 1;
                canvas.save();
                canvas.clipRect(0, 0, clip, getMeasuredHeight());
                canvas.drawColor(Color.BLACK);
                canvas.restore();

                canvas.save();
                canvas.clipRect(clip, 0, getMeasuredWidth(), getMeasuredHeight());
                canvas.drawColor(Color.RED);
                canvas.restore();
                break;
            default:
                break;
        }
    }
}
