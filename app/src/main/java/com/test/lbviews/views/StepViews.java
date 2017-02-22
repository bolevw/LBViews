package com.test.lbviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.test.lbviews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubo on 2017/2/21.
 */

public class StepViews extends View {

    private static final String TAG = "StepViews";

    public static final int DEFAULT_RADIUS = 18;
    public static final int DEFAULT_LENGTH = 36;
    public static final int DEFAULT_TEXT_SIZE = 14;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int FINISH_COLOR = Color.BLUE;
    private static final int CURRENT_COLOR = Color.RED;
    private static final int LAST_COLOR = Color.GRAY;

    private int mOrientation;
    private Paint mPaint;
    private Path mLinePath;
    private int mWidth;
    private int mHeight;
    private int mCircleRadius;
    private List<String> mSteps = new ArrayList<>();
    private int mCurrentSteps = 7;
    private int mLineLength;
    private int mScreenWidth;
    private int mStepCount;
    private DashPathEffect mDashPathEffect;
    private int mTextSize;
    private Bitmap mBitmap;


    public StepViews(Context context) {
        this(context, null);
    }

    public StepViews(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public StepViews(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StepViews);
        mOrientation = ta.getInt(R.styleable.StepViews_orientation, 1);
        ta.recycle();

        mCircleRadius = dip2px(DEFAULT_RADIUS);
        mLineLength = dip2px(DEFAULT_LENGTH);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mTextSize = sp2px(DEFAULT_TEXT_SIZE);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mLinePath = new Path();
        mDashPathEffect = new DashPathEffect(new float[]{8, 4}, 0);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mStepCount = mSteps.size();
        if (mStepCount == 0) {
            setMeasuredDimension(0, 0);
        } else {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            if (heightMode == MeasureSpec.EXACTLY) {
                mHeight = heightSize;
            } else if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mCircleRadius * 2 + 8 + mTextSize, heightSize);
            } else {
                mHeight = heightSize;
            }

            int desireWidth = (mCircleRadius * mSteps.size() + mLineLength * (mSteps.size() - 1)) * 2;
            if (widthMode == MeasureSpec.EXACTLY) {
                if (desireWidth > widthSize) {
                    float v = desireWidth * 1f / widthSize;
                    mCircleRadius = (int) (mCircleRadius * 1f / v);
                    mLineLength = (int) (mLineLength * 1f / v);
                }
                mWidth = widthSize;
            } else if (widthMode == MeasureSpec.AT_MOST) {
                if (desireWidth > mScreenWidth) {
                    float v = desireWidth * 1f / mScreenWidth;
                    mCircleRadius = (int) (mCircleRadius * 1f / v);
                    mLineLength = (int) (mLineLength * 1f / v);
                    desireWidth = (mCircleRadius * mSteps.size() + mLineLength * (mSteps.size() - 1)) * 2;
                }
                mWidth = Math.min(desireWidth, widthSize);
            } else {
                mWidth = widthSize;
            }
            setMeasuredDimension(mWidth, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mStepCount == 0) {
            super.onDraw(canvas);
        } else {
            int realWidth = mWidth - getPaddingLeft() - getPaddingRight();
            int averageWidth = realWidth / mStepCount;
            int realHeight = mHeight - getPaddingTop() - getPaddingBottom();
            float x = averageWidth / 2;
            float y = mCircleRadius + 4;

            float textX = 0;
            float textY = mCircleRadius * 2 + 8;
            for (int i = 0; i < mStepCount; i++) {
                Rect rect = new Rect();
                mPaint.getTextBounds(mSteps.get(i), 0, mSteps.get(i).length(), rect);

                if (i < mCurrentSteps) {
                    mPaint.setColor(FINISH_COLOR);
                    mPaint.setPathEffect(null);
                } else if (i == mCurrentSteps) {
                    mPaint.setColor(CURRENT_COLOR);
                    mPaint.setPathEffect(mDashPathEffect);
                } else {
                    mPaint.setColor(LAST_COLOR);
                    mPaint.setPathEffect(mDashPathEffect);
                }
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(x, y, mCircleRadius, mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawText(mSteps.get(i), x - rect.width() / 2, textY + rect.height(), mPaint);
                mPaint.setStyle(Paint.Style.STROKE);

                if (i < mStepCount - 1) {
                    float moveStart = x + mCircleRadius;
                    float lineStart = x + averageWidth - mCircleRadius;
                    mLinePath.reset();
                    mLinePath.moveTo(moveStart, y);
                    mLinePath.lineTo(lineStart, y);
                    mPaint.setColor(Color.WHITE);
                    canvas.drawPath(mLinePath, mPaint);
                }
                x = x + averageWidth;
                textX = textX + averageWidth / 2;
            }
        }

    }


    public StepViews setSteps(List<String> steps) {
        this.mSteps.clear();
        this.mSteps.addAll(steps);
        return this;
    }

    public StepViews setCurrentSteps(int step) {
        this.mCurrentSteps = step;
        return this;
    }

    public void build() {
        invalidate();
    }

    private int dip2px(int value) {
        return (int) (getContext().getResources().getDisplayMetrics().density * value + 0.5f);
    }

    private int sp2px(int value) {
        return (int) (getContext().getResources().getDisplayMetrics().scaledDensity * value + 0.5f);
    }
}
