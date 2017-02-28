package com.test.lbviews.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liubo on 2017/2/27.
 */

public class MovePath extends View {
    private static final String TAG = "MovePath";

    private Paint mPaint;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private int mPathColor;
    private float mPathLength;
    private DashPathEffect mDashPathEffect;
    private float mPhase;

    public MovePath(Context context) {
        this(context, null);
    }

    public MovePath(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovePath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();

        mPathMeasure = new PathMeasure();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desireWidthSize = 0;
        int width = resolveSize(desireWidthSize, widthMeasureSpec);
        int desireHeightSize = 0;
        int height = resolveSize(desireHeightSize, heightMeasureSpec);
        setMeasuredDimension(width, height);

        mPath.moveTo(width / 4, height / 2);
        mPath.lineTo(width / 2, height * 3 / 4);
        mPath.lineTo(width * 11 / 12, height / 10);
        mPathMeasure.setPath(mPath, false);
        mPathLength = mPathMeasure.getLength();
        mPhase = mPathLength;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDashPathEffect = new DashPathEffect(new float[]{mPathLength, mPathLength}, mPhase);
        mPaint.setPathEffect(mDashPathEffect);
        canvas.save();
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        start();
    }

    public void start() {
        if (mPhase > 0) {
            mPhase--;
            postInvalidateDelayed(30);
        } else {
            mPhase = mPathLength;
        }
    }


    public float getPhase() {
        return mPhase;
    }

    public void setPhase(float mPhase) {
        this.mPhase = mPhase;
        invalidate();
    }
}
