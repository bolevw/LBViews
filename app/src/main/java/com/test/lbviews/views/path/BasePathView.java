package com.test.lbviews.views.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liubo on 2017/3/16.
 */

public class BasePathView extends View {
    private Paint mPaint;
    private Path mPath;

    public BasePathView(Context context) {
        this(context, null);
    }

    public BasePathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasePathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.lineTo(200, 200);
        mPath.setLastPoint(200, 100);

        mPath.lineTo(100, 300);
        RectF rf = new RectF(300, 300, 500, 400);
//        mPath.addArc(rf, 0, 270);//add是直接添加上
        mPath.arcTo(rf, 0, 270, false);

        canvas.drawPath(mPath, mPaint);
    }
}
