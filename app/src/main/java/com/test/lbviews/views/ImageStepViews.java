package com.test.lbviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.test.lbviews.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liubo on 2017/2/23.
 */

public class ImageStepViews extends View {

    private static final String TAG = "ImageStepViews"; //android studio 中输出logt就会生成这行代码

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int DEFAULT_CURRENT_STEP = 0;
    public static final int DEFAULT_LINE_LENGTH = 36;
    public static final int DEFAULT_TEXT_SIZE = 14;
    public static final int DEFAULT_OFFSET = 10;

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    private List<Point> mStepPoint = new ArrayList<>();
    private Paint mPaint;
    private Path mPath;
    private int mOrientation;
    private List<String> mSteps = new ArrayList<>();
    private int mCurrentStep;
    private Bitmap mBitmap;
    private OnStepClickListener mOnStepClickListener;
    private int mWidth;
    private int mHeight;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private DashPathEffect mDashPathEffect;
    private int mStepCount;
    private int mLineLength;
    private int mTextSize;
    private Rect mTextBounds = new Rect();
    private int mOffSet;


    public ImageStepViews(Context context) {
        this(context, null);
    }

    public ImageStepViews(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageStepViews(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageStepViews);
        int index = ta.getInt(R.styleable.StepViews_orientation, -1);
        if (index >= 0) {
            setOrientation(index);
        }
        int stepId = ta.getResourceId(R.styleable.ImageStepViews_steps, R.array.default_steps);
        mSteps = Arrays.asList(getResources().getStringArray(stepId));
        mCurrentStep = ta.getInt(R.styleable.ImageStepViews_currentStep, DEFAULT_CURRENT_STEP);
        ta.recycle();

        mStepCount = mSteps.size();
        mTextSize = sp2px(DEFAULT_TEXT_SIZE);

        mPath = new Path();
        mDashPathEffect = new DashPathEffect(new float[]{8, 4}, 0);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(mDashPathEffect);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_step_1);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();

        mLineLength = (int) (1.5f * mBitmapWidth);
        mPaint.getTextBounds(mSteps.get(0), 0, mSteps.get(0).length(), mTextBounds);
        mOffSet = dip2px(DEFAULT_OFFSET);
    }


    private float mMultiple;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mStepCount == 0) {
            setMeasuredDimension(0, 0);
        } else {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (mOrientation == HORIZONTAL) {
                int desireWidth = mBitmapWidth * mStepCount + (mStepCount - 1) * mLineLength; //根据图片的大小，文字的大小求出来的真实宽高
                int desireHeight = mBitmapHeight + mTextBounds.height() + mOffSet * 3;
                if (widthMode == MeasureSpec.EXACTLY) {
                    mWidth = widthSize;
                } else if (widthMode == MeasureSpec.AT_MOST) {
                    mWidth = Math.min(desireWidth, widthSize);
                } else {
                    mWidth = widthSize;
                }
                if (desireWidth > mWidth) {//真实宽度大于求出来的宽度，那么需要将原来的宽度进行缩放
                    mMultiple = desireWidth * 1f / mWidth; //求出倍数
                    mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, (int) (mBitmapWidth / mMultiple), (int) (mBitmapHeight / mMultiple)); //缩放图片
                    mBitmapWidth = mBitmap.getWidth(); //重置原来的图片大小
                    mBitmapHeight = mBitmap.getHeight();
                    mLineLength = (int) (1.5f * mBitmapWidth); //重置图片之间的直线长度
                }

                if (heightMode == MeasureSpec.EXACTLY) {
                    mHeight = heightSize;
                } else if (heightMode == MeasureSpec.AT_MOST) {
                    mHeight = Math.min(heightSize, desireHeight);
                } else {
                    mHeight = heightSize;
                }
                if (desireHeight > mHeight) {
                    mMultiple = desireHeight * 1f / mHeight;
                    mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, (int) (mBitmapWidth / mMultiple), (int) (mBitmapHeight / mMultiple));
                    mBitmapWidth = mBitmap.getWidth();
                    mBitmapHeight = mBitmap.getHeight();
                    mLineLength = (int) (1.5f * mBitmapWidth);
                    mTextSize = (int) (mTextSize * 1f / mMultiple);
                    mOffSet = (int) (mOffSet / mMultiple);
                    mPaint.getTextBounds(mSteps.get(0), 0, mSteps.get(0).length(), mTextBounds);
                }
                setMeasuredDimension(mWidth, mHeight);
            } else {
                int desireWidth = mBitmapWidth + mOffSet * 3 + mTextBounds.height();
                int desireHeight = mBitmapHeight * mStepCount + (mStepCount - 1) * mLineLength;
                if (widthMode == MeasureSpec.EXACTLY) {
                    mWidth = widthSize;
                } else if (widthMode == MeasureSpec.AT_MOST) {
                    mWidth = Math.min(widthSize, desireWidth);
                } else {
                    mWidth = widthSize;
                }

                if (heightMode == MeasureSpec.EXACTLY) {
                    mHeight = heightSize;
                } else if (heightMode == MeasureSpec.AT_MOST) {
                    mHeight = Math.max(heightSize, desireHeight);
                } else {
                    mHeight = heightSize;
                }

                if (desireHeight > mHeight) {
                    mMultiple = desireHeight * 1f / mHeight;
                    mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, (int) (mBitmapWidth / mMultiple), (int) (mBitmapHeight / mMultiple));
                    mBitmapWidth = mBitmap.getWidth();
                    mBitmapHeight = mBitmap.getHeight();
                    mLineLength = (int) (1.5f * mBitmapWidth);
                    mTextSize = (int) (mTextSize * 1f / mMultiple);
                    mOffSet = (int) (mOffSet / mMultiple);
                    mPaint.getTextBounds(mSteps.get(0), 0, mSteps.get(0).length(), mTextBounds);
                }
                setMeasuredDimension(mWidth, mHeight);
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mStepCount == 0) {
            super.onDraw(canvas);
        } else {
            int realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            int realHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
            int averageDis;
            float left = 0;
            float right = 0;
            float top = 0;
            float bottom = 0;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) mMultiple;
            mPaint.setTextAlign(Paint.Align.CENTER);
            if (mOrientation == HORIZONTAL) {
                averageDis = realWidth / mStepCount;
                left = (averageDis - mBitmapWidth) / 2;
                right = left + mBitmapWidth;
                top = realHeight / 2 - mOffSet / 2 - mBitmapHeight;
                bottom = realHeight / 2 - mOffSet / 2;
                for (int i = 0; i < mStepCount; i++) {
                    if (i < mCurrentStep) {
                        mPaint.setPathEffect(null);
                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_step_1, options);
                    } else if (i == mCurrentStep) {
                        mPaint.setPathEffect(mDashPathEffect);
                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_step_2, options);
                    } else {
                        mPaint.setPathEffect(mDashPathEffect);
                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_step_3, options);
                    }
                    RectF dst = new RectF();
                    dst.left = left;
                    dst.right = right;
                    dst.top = top;
                    dst.bottom = bottom;
                    canvas.drawBitmap(mBitmap, null, dst, mPaint);
                    if (i < mStepCount - 1) {
                        mPaint.setColor(Color.BLACK);
                        mPaint.setStyle(Paint.Style.STROKE);
                        mPath.reset();
                        mPath.moveTo(dst.right, dst.top + mBitmapHeight / 2);
                        mPath.lineTo(dst.left + averageDis, dst.top + mBitmapHeight / 2);
                        canvas.drawPath(mPath, mPaint);
                    }

                    canvas.drawText(mSteps.get(i), dst.left + mBitmapWidth / 2, dst.bottom + mOffSet, mPaint);
                    left = left + averageDis;
                    right = right + averageDis;
                }
            } else {
                averageDis = realHeight / mStepCount;
                left = realWidth / 2 - mOffSet / 2 - mBitmapWidth;
                right = left + mBitmapWidth;
                top = (averageDis - mBitmapHeight) / 2;
                bottom = top + mBitmapHeight;
                for (int i = 0; i < mStepCount; i++) {
                    if (i < mCurrentStep) {
                        mPaint.setPathEffect(null);
                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_step_1, options);
                    } else if (i == mCurrentStep) {
                        mPaint.setPathEffect(mDashPathEffect);
                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_step_2, options);
                    } else {
                        mPaint.setPathEffect(mDashPathEffect);
                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_step_3, options);
                    }
                    RectF dst = new RectF();
                    dst.left = left;
                    dst.right = right;
                    dst.top = top;
                    dst.bottom = bottom;
                    canvas.drawBitmap(mBitmap, null, dst, mPaint);
                    if (i < mStepCount - 1) {
                        mPaint.setColor(Color.BLACK);
                        mPaint.setStyle(Paint.Style.STROKE);
                        mPath.reset();
                        mPath.moveTo(dst.left + mBitmapWidth / 2, dst.bottom);
                        mPath.lineTo(dst.left + mBitmapWidth / 2, dst.top + averageDis);
                        canvas.drawPath(mPath, mPaint);
                    }
                    canvas.drawText(mSteps.get(i), dst.right + mOffSet, dst.top + mBitmapHeight / 2,mPaint);
                    top = top + averageDis;
                    bottom = bottom + averageDis;
                }


            }
        }
    }

    private void setBitmapResouce(@DrawableRes int res) {
        mBitmap = BitmapFactory.decodeResource(getContext().getResources(), res);
    }

    private float mDownX;
    private float mDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                mDownX = event.getX();
                mDownY = event.getY();
                if (mOnStepClickListener != null) {
                    float index;
                    if (mOrientation == VERTICAL) {
                        index = mDownY * mStepCount / mHeight;
                    } else {
                        index = mDownX * mStepCount / mWidth;
                    }
                    if (String.valueOf(index).contains(".")) {
                        mOnStepClickListener.stepClick(((int) index) + 1);
                    } else {
                        mOnStepClickListener.stepClick(((int) index));
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }


    public interface OnStepClickListener {
        void stepClick(int clickStep);
    }

    public void setOnStepClickListener(OnStepClickListener onStepClickListener) {
        this.mOnStepClickListener = onStepClickListener;
    }

    public void setOrientation(@OrientationMode int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    @OrientationMode
    public int getOrientation() {
        return mOrientation;
    }


    public List<String> getSteps() {
        return mSteps;
    }

    public void setSteps(List<String> mSteps) {
        this.mSteps = mSteps;
        this.mStepCount = this.mSteps.size();
        requestLayout();
    }

    public int getCurrentStep() {
        return mCurrentStep;
    }

    public void setCurrentStep(int mCurrentStep) {
        this.mCurrentStep = mCurrentStep;
        this.requestLayout();
    }

    private int sp2px(int value) {
        return (int) (getContext().getResources().getDisplayMetrics().scaledDensity * value + 0.5f);
    }

    private int dip2px(int value) {
        return (int) (getContext().getResources().getDisplayMetrics().density * value + 0.5f);
    }
}
