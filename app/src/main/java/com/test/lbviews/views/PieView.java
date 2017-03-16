package com.test.lbviews.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.test.lbviews.model.PieData;

import java.util.ArrayList;

/**
 * Created by liubo on 2017/3/10.
 */

public class PieView extends View {
    private static final String TAG = "PieView";

    private int[] colors = new int[]{Color.GREEN, Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW, Color.DKGRAY};

    private ArrayList<PieData> mViewData = new ArrayList<>();
    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private int mMinWidth;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
            mMinWidth = dm.widthPixels / 2;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(mMinWidth, widthMeasureSpec), resolveSize(mMinWidth, heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = mWidth - getPaddingLeft() - getPaddingRight();
        mHeight = mHeight - getPaddingTop() - getPaddingBottom();
        float startAngle = 0;
        float swipeAngle = 0;
        RectF rect = new RectF(
                getPaddingLeft(),
                getPaddingTop(),
                getPaddingLeft() + mWidth,
                getPaddingTop() + mHeight

        );
        for (int i = 0; i < mViewData.size(); i++) {
            PieData d = mViewData.get(i);
            mPaint.setColor(d.getColor());
            swipeAngle = d.getAngle();
            Log.d(TAG, " " + d.getAngle());
            canvas.drawArc(rect, startAngle, swipeAngle, true, mPaint);
            startAngle = startAngle + swipeAngle;
        }
    }

    public void setmViewData(ArrayList<PieData> mViewData) {
        this.mViewData = mViewData;
        Log.d(TAG, "size " + mViewData.size());
        invalidate();
    }
}
