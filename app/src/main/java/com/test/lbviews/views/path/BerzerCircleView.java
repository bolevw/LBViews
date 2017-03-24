package com.test.lbviews.views.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liubo on 2017/3/20.
 */

public class BerzerCircleView extends View {
    private static final float C = 0.551915024494f;
    private Paint mPaint;
    private int mCenterX, mCenterY;
    private PointF center = new PointF(0, 0);

    private float[] mData = new float[8]; //start points x,y,x,y....
    private float[] mCtrl = new float[16]; //control points x,y,x,y......
    private float mRadius = 200;
    private float mDiff = mRadius * C;
    private float changeValue = 1f;


    public BerzerCircleView(Context context) {
        this(context, null);
    }

    public BerzerCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BerzerCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4f);

        mData[0] = mRadius;
        mData[1] = 0;

        mData[2] = 0;
        mData[3] = mRadius;

        mData[4] = -mRadius;
        mData[5] = 0;

        mData[6] = 0;
        mData[7] = -mRadius;

        mCtrl[0] = mRadius;
        mCtrl[1] = mDiff;

        mCtrl[2] = mDiff;
        mCtrl[3] = mRadius;

        mCtrl[4] = -mDiff;
        mCtrl[5] = mRadius;

        mCtrl[6] = -mRadius;
        mCtrl[7] = mDiff;

        mCtrl[8] = -mRadius;
        mCtrl[9] = -mDiff;

        mCtrl[10] = -mDiff;
        mCtrl[11] = -mRadius;

        mCtrl[12] = mDiff;
        mCtrl[13] = -mRadius;

        mCtrl[14] = mRadius;
        mCtrl[15] = -mDiff;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        Path p = new Path();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        p.moveTo(mData[0], mData[1]);
        p.cubicTo(mCtrl[0], mCtrl[1], mCtrl[2], mCtrl[3] * changeValue, mData[2], mData[3]);
        p.cubicTo(mCtrl[4], mCtrl[5] * changeValue, mCtrl[6], mCtrl[7], mData[4], mData[5]);
        p.cubicTo(mCtrl[8], mCtrl[9], mCtrl[10], mCtrl[11] * changeValue, mData[6], mData[7]);
        p.cubicTo(mCtrl[12], mCtrl[13] * changeValue, mCtrl[14], mCtrl[15], mData[0], mData[1]);
        canvas.drawPath(p, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(10f);
        canvas.drawPoints(mData, mPaint);
        canvas.drawPoints(mCtrl, mPaint);

        mPaint.setStrokeWidth(4f);
        canvas.drawLine(mCtrl[14], mCtrl[15], mCtrl[0], mCtrl[1], mPaint);
        canvas.drawLine(mCtrl[2], mCtrl[3], mCtrl[4], mCtrl[5], mPaint);
        canvas.drawLine(mCtrl[6], mCtrl[7], mCtrl[8], mCtrl[9], mPaint);
        canvas.drawLine(mCtrl[10], mCtrl[11], mCtrl[12], mCtrl[13], mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
