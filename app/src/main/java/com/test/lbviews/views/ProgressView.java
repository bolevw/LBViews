package com.test.lbviews.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.test.lbviews.R;
import com.test.lbviews.model.Leafs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by liubo on 2017/3/13.
 */

public class ProgressView extends View {
    private Paint mPaint;
    private Paint mWhitePaint;
    private Paint mTextPaint;

    private Bitmap leafsBitmap;
    private Bitmap mFengShanBitmap;
    private int mProgress;
    private int mDefaultWidthSize; //默认为屏幕宽度的一半, 高度为defaultSize的4分之一
    private int mDefaultHeightSize;
    private RectF mWhiteArc, mWhiteRect;
    private int mWhiteArcRadius;
    private RectF mProgressArc, mProgressRect;
    private RectF mFengShanRf;
    private int mProgressArcRadius;
    private int mMargin;
    private int mCurrentProgress;
    private int mTotalLength;
    private ValueAnimator fengRotateValueAnimator;
    private int rotate = 10;
    private int mTotalArcPorgressWidth;
    private int mProgressPosition;
    private List<Leafs> resource = new ArrayList<>();
    private int mLeafStartX;
    private int mLeafStartY;
    private Thread createThread;
    private boolean needInvilate = true;


    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        leafsBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.leafs);
        mFengShanBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_fengshan);
        mWhitePaint = new Paint();
        mWhitePaint.setStyle(Paint.Style.FILL);
        mWhitePaint.setColor(Color.parseColor("#FCE797")); // <color name="vis_vis">#FCE797</color>
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#FFA900")); //<color name="chrome_yellow">#FFA900</color>
        mTextPaint = new Paint(mPaint);


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mDefaultWidthSize = dm.widthPixels * 3 / 4;
        mDefaultHeightSize = mDefaultWidthSize / 4;

        initAnim();
    }

    boolean create = true;

    private void initResource() {
        createThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (create && resource.size() < 10) {
                    Leafs leafs = new Leafs(new Random().nextInt(360), mFengShanRf.left, mFengShanRf.top + mFengShanRf.height() / 2, Math.max(new Random().nextInt(50), 10));
                    resource.add(leafs);
                }
            }
        });
        createThread.start();
    }

    private void initAnim() {
       /* fengRotateValueAnimator = new ValueAnimator();
        fengRotateValueAnimator.setIntValues(0, 360);
        fengRotateValueAnimator.setInterpolator(new LinearInterpolator());
        fengRotateValueAnimator.setDuration(2 * 1000);
        fengRotateValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        fengRotateValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotate = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        fengRotateValueAnimator.setRepeatCount(10000);
        fengRotateValueAnimator.start();*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(mDefaultWidthSize, widthMeasureSpec), resolveSize(mDefaultHeightSize, heightMeasureSpec));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        createThread.interrupt();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMargin = h / 10;
        mWhiteArcRadius = h * 4 / 10;
        mProgressArcRadius = mWhiteArcRadius * 3 / 4;
        mProgressMargin = mMargin * 2;
        int fengshanWidth = mProgressArcRadius * 2;
        int fengshanMargin = mProgressMargin;
        mTotalLength = w - fengshanMargin - mProgressMargin * 2 - fengshanWidth; //总宽度-风扇的宽度-风扇的左边的margin-两侧的margin
        mTotalArcPorgressWidth = mProgressArcRadius;

        mWhiteArc = new RectF(mMargin, mMargin, mMargin + mWhiteArcRadius * 2, mMargin + mWhiteArcRadius * 2);
        mWhiteRect = new RectF(mMargin + mWhiteArcRadius, mWhiteArc.top, w - mMargin, mWhiteArc.bottom);
        mFengShanRf = new RectF(w - mProgressMargin - mProgressArcRadius * 2, mProgressMargin, w - mProgressMargin, mProgressMargin + mProgressArcRadius * 2); //固定在尾部

        mProgressArc = new RectF(mProgressMargin, mProgressMargin, mProgressMargin + mProgressArcRadius * 2, mProgressMargin + mProgressArcRadius * 2);
        mProgressRect = new RectF(mProgressMargin + mProgressArcRadius, mProgressArc.top, mProgressMargin + mProgressArcRadius + mProgressPosition, mProgressArc.bottom);
    }

    int mProgressMargin;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw white background
        initBackground(canvas);

        initFengShan(canvas);

        initProgress(canvas);

        initResource();

        canvas.save();
        for (int i = 0; i < resource.size(); i++) {
            Leafs leafs = resource.get(i);
            if (leafs.getX() > mProgressPosition + mProgressMargin) {
                leafs.draw(canvas, leafsBitmap, mPaint);
            } else {
                resource.remove(leafs);
                mCurrentProgress++;
            }
        }
        canvas.restore();
    }

    private void initProgress(Canvas canvas) {
        if (mCurrentProgress > 100 || mCurrentProgress < 0) {
            reset();
        }

        mProgressPosition = mCurrentProgress * mTotalLength / 100;

        if (mProgressPosition > mProgressArcRadius) {
            canvas.drawArc(mProgressArc, 90, 180, false, mPaint);
            mProgressRect.right = mProgressPosition + mProgressMargin;
            canvas.drawRect(mProgressRect, mPaint);
        } else {
            int angle = (int) Math.toDegrees(Math.acos((mProgressArcRadius - mProgressPosition) * 1f / mProgressArcRadius));
            int startAngle = 180 - angle;
            int swipeAngle = angle * 2;
            canvas.drawArc(mProgressArc, startAngle, swipeAngle, false, mPaint);
        }

    }

    private void reset() {
        mCurrentProgress = 100;
        create = false;
       /* mCurrentProgress = 0;
        mProgressPosition = 0;
        mProgressRect.right = mProgressMargin + mProgressArcRadius + mProgressPosition;*/
    }

    private void initBackground(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#FDD03F"));
        canvas.drawArc(mWhiteArc, 90, 180, false, mWhitePaint);
        canvas.drawRect(mWhiteRect, mWhitePaint);
    }

    int textsize = 0;

    int maxSize = 30;

    private float scale = 1f;
    private boolean start = false;

    private void initFengShan(Canvas canvas) {
g
        if (mCurrentProgress >= 100 && !start) {
            start = true;
            fengRotateValueAnimator = new ValueAnimator();
            fengRotateValueAnimator.setFloatValues(1f, 0f);
            fengRotateValueAnimator.setDuration(300);
            fengRotateValueAnimator.setInterpolator(new LinearInterpolator());
            fengRotateValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scale = (float) animation.getAnimatedValue();
                }
            });
            fengRotateValueAnimator.start();
        }

        Matrix matrix = new Matrix();
        matrix.postTranslate(mFengShanRf.left, mFengShanRf.top);
        matrix.preScale(mFengShanRf.width() / mFengShanBitmap.getWidth() * scale, mFengShanRf.height() / mFengShanBitmap.getHeight() * scale);
        matrix.postRotate(rotate, mFengShanRf.left + mFengShanRf.width() / 2, mFengShanRf.top + mFengShanRf.height() / 2);
        canvas.drawBitmap(mFengShanBitmap, matrix, mPaint);

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.GREEN);
        textsize = (int) ((1 - scale) * maxSize);
        mTextPaint.setTextSize(textsize);
        canvas.drawText("100%", mFengShanRf.left + mFengShanRf.width() / 2, mFengShanRf.top + mFengShanRf.height() / 2, mTextPaint);
        if (needInvilate) {
            postInvalidateDelayed(10);
        }
        rotate += 10;
    }
}
