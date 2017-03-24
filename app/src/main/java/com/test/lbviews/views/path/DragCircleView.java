package com.test.lbviews.views.path;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
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
import android.view.animation.BounceInterpolator;

/**
 * Created by liubo on 2017/3/23.
 */

public class DragCircleView extends View {
    private static final String TAG = "DragCircleView";
    private float mTouchX, mTouchY;
    private Path mPath;
    private Paint mPaint;
    private Paint mTextPaint;
    private int mMaxRadius = 60;
    private float mLocationX, mLocationY;
    private int mMinRadius = 30;
    private int mMaxDragLength = mMaxRadius * 8;
    private boolean dismiss;

    public DragCircleView(Context context) {
        this(context, null);
    }

    public DragCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);

        mTextPaint = new Paint(mPaint);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(30f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTouchX = w / 2;
        mTouchY = h / 2;
        mLocationX = mTouchX;
        mLocationY = mTouchY;

    }

    private void initPath(float mTouchX, float mTouchY) {

        double angle = Math.atan((mTouchX - mLocationX) / (mTouchY - mLocationY)); //求出角度
        float startX1 = (float) (Math.cos(angle) * mMinRadius + mLocationX);
        float startY1 = (float) (-Math.sin(angle) * mMinRadius + mLocationY);
        float startX2 = (float) (-Math.cos(angle) * mMinRadius + mLocationX);
        float startY2 = (float) (Math.sin(angle) * mMinRadius + mLocationY);

        float ctrX = (mTouchX - mLocationX) / 2 + mLocationX;
        float ctrY = (mTouchY - mLocationY) / 2 + mLocationY;
        float endX1 = (float) (Math.cos(angle) * mMaxRadius + mTouchX);
        float endY1 = (float) (-Math.sin(angle) * mMaxRadius + mTouchY);
        float endX2 = (float) (-Math.cos(angle) * mMaxRadius + mTouchX);
        float endY2 = (float) (Math.sin(angle) * mMaxRadius + mTouchY);

        mPath.reset();
        mPath.moveTo(mLocationX, mLocationY);
        mPath.lineTo(startX1, startY1);
        mPath.quadTo(ctrX, ctrY, endX1, endY1);
        mPath.lineTo(endX2, endY2);
        mPath.quadTo(ctrX, ctrY, startX2, startY2);
        mPath.lineTo(mLocationX, mLocationY);
        mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }

    private boolean canbroke = false;

    private void drawCircle(Canvas canvas) {
        if (!dismiss) {
            canvas.drawCircle(mLocationX, mLocationY, mMinRadius, mPaint);
            canvas.drawPath(mPath, mPaint);
        }
        if (!canbroke) {
            canvas.drawCircle(mTouchX, mTouchY, mMaxRadius, mPaint);
            canvas.drawText("99+", mTouchX, mTouchY, mTextPaint);
        }

    }

    float x = 0;
    float y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mTouchX - x) < mMaxRadius && Math.abs(mTouchY - y) < mMaxRadius) {
                    x = event.getX();
                    y = event.getY();
                    mTouchX = x;
                    mTouchY = y;
                    initPath(mTouchX, mTouchY);
                    postInvalidate();

                    double moveDistance = Math.sqrt((x - mLocationX) * (x - mLocationX) + (y - mLocationY) * (y - mLocationY));
                    if (moveDistance >= mMaxDragLength) {
                        dismiss = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(mTouchX - x) < mMaxRadius && Math.abs(mTouchY - y) < mMaxRadius) {
                    x = event.getX();
                    y = event.getY();
                    double moveDistance = Math.sqrt((x - mLocationX) * (x - mLocationX) + (y - mLocationY) * (y - mLocationY));
                    if (moveDistance >= mMaxDragLength) {
                        canbroke = true;
                        dismiss = true;
                    } else {
                        reset(x, y);
                    }
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL: {
                mTouchX = mLocationX;
                mTouchY = mLocationY;
            }
            break;
        }

        return super.onTouchEvent(event);
    }

    private void reset(float x, float y) {
        PointF start = new PointF(x, y);
        PointF end = new PointF(mLocationX, mLocationY);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setObjectValues(start, end);
        valueAnimator.setEvaluator(new ResetEvaluator());
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pf = (PointF) animation.getAnimatedValue();
                mTouchX = pf.x;
                mTouchY = pf.y;
                initPath(mTouchX, mTouchY);
                postInvalidate();
            }
        });
    }

    class ResetEvaluator implements TypeEvaluator<PointF> {

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            PointF result = new PointF();
            result.x = (endValue.x - startValue.x) * fraction + startValue.x;
            result.y = (endValue.y - startValue.y) * fraction + startValue.y;
            return result;
        }
    }

    public void setPaintStyle(boolean fill) {
        if (fill) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        invalidate();
    }

    public void reset() {
        canbroke = false;
        dismiss = false;
        mPath.reset();
        mTouchX = mLocationX;
        mTouchY = mLocationY;
        invalidate();
    }
}
