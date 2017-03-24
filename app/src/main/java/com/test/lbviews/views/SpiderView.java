package com.test.lbviews.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubo on 2017/3/1.
 */

public class SpiderView extends View {
    private float mAngle = (float) (Math.PI * 2 / 6);
    private Paint mPaint;
    private Paint mValuePaint;
    private Path mPath;
    private int mWindowWidth;
    private int mWindowHeight;
    private List<String> values = new ArrayList<>();
    private float[] showValues = new float[]{30, 66, 50, 80, 78, 100};
    private float mRadius;


    public SpiderView(Context context) {
        this(context, null);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SpiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(dip2px(14));
        mPaint.setColor(Color.WHITE);

        mValuePaint = new Paint(mPaint);
        mValuePaint.setStyle(Paint.Style.FILL);
        mValuePaint.setColor(Color.argb(124, 145, 24, 55));

        mPath = new Path();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mWindowWidth = dm.widthPixels;
        mWindowHeight = dm.heightPixels;

        values.add("推进");
        values.add("输出");
        values.add("发育");
        values.add("团战");
        values.add("生存");
        values.add("战绩");

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(mWindowWidth / 2, widthMeasureSpec), resolveSize(mWindowHeight / 2, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255, 34, 24, 124);
        Rect rect = new Rect();
        mPaint.getTextBounds(values.get(0), 0, values.get(0).length(), rect);
        int offset = 4;
        int width = getWidth() - getPaddingLeft() - getPaddingRight() - 2 * rect.width() - 2 * offset;
        int height = getHeight() - getPaddingBottom() - getPaddingTop() - 2 * rect.height() - 2 * offset;
        canvas.translate(getWidth() / 2, getHeight() / 2);
        mRadius = Math.min(width / 2, height / 2);
        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawValues(canvas);
    }

    private void drawValues(Canvas canvas) {
        mPath.reset();
        for (int j = 0; j < 6; j++) {
            float radius = showValues[j] * mRadius / 100;
            if (j == 0) {
                mPath.moveTo((float) Math.cos(mAngle * j) * radius, (float) Math.sin(mAngle * j) * radius);
            } else {
                mPath.lineTo((float) Math.cos(mAngle * j) * radius, (float) Math.sin(mAngle * j) * radius);
            }
        }
        canvas.drawPath(mPath, mValuePaint);
    }

    private void drawText(Canvas canvas) {
        Rect rect = new Rect();

        mPaint.getTextBounds(values.get(0), 0, values.get(0).length(), rect);
        int offset = 4;
        for (int i = 0; i < 6; i++) {
            float x = (float) Math.cos(mAngle * i) * (mRadius + offset + rect.width() / 2);
            float y = (float) Math.sin(mAngle * i) * (mRadius + offset + rect.height() / 2);
            String value = values.get(i);
            canvas.drawText(value, x, y, mPaint);
        }
    }

    private void drawLines(Canvas canvas) {
        for (int i = 0; i < 6; i++) {
            mPath.reset();
            mPath.moveTo(0, 0);
            mPath.lineTo((float) Math.cos(mAngle * i) * mRadius, (float) Math.sin(mAngle * i) * mRadius);
            canvas.drawPath(mPath, mPaint);
        }
    }

    private void drawPolygon(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            float radius = mRadius * (i + 1) / 5;
            mPath.reset();
            for (int j = 0; j < 6; j++) {
                if (j == 0) {
                    mPath.moveTo((float) Math.cos(mAngle * j) * radius, (float) Math.sin(mAngle * j) * radius);
                } else {
                    mPath.lineTo((float) Math.cos(mAngle * j) * radius, (float) Math.sin(mAngle * j) * radius);
                }
            }
            mPath.close();
            canvas.drawPath(mPath, mPaint);
        }
    }

    private int dip2px(int value) {
        return (int) (getResources().getDisplayMetrics().density * value + 0.5f);
    }
}
