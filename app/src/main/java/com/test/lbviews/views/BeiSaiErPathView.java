package com.test.lbviews.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liubo on 2017/3/1.
 */

public class BeiSaiErPathView extends View {

    private Paint mPaint;
    private Path mPath;

    public BeiSaiErPathView(Context context) {
        this(context, null);
    }

    public BeiSaiErPathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeiSaiErPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
