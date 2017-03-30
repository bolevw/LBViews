package com.boger.imagepicker;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by liubo on 2017/3/29.
 */

public class MyImageview extends AppCompatImageView {
    public MyImageview(Context context) {
        super(context);
    }

    public MyImageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}
