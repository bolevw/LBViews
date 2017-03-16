package com.test.lbviews.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubo on 2017/3/1.
 */

public class SpiderView extends View {
    private Paint mPaint;
    private Path mPath;
    private int mWindowWidth;
    private int mWindowHeight;
    private List<String> values = new ArrayList<>();


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

        mPath = new Path();
        if (context instanceof Activity) {
            Activity a = (Activity) context;
            DisplayMetrics dm = getResources().getDisplayMetrics();
            a.getWindowManager().getDefaultDisplay().getMetrics(dm);
            mWindowWidth = dm.widthPixels;
            mWindowHeight = dm.heightPixels;
        }

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
        Rect rect = new Rect();

        mPaint.getTextBounds(values.get(0), 0, values.get(0).length(), rect);
        int offset = 4;
        int width = getWidth() - getPaddingLeft() - getPaddingRight() - 2 * rect.width() - 2 * offset;
        int height = getHeight() - getPaddingBottom() - getPaddingTop() - 2 * rect.height() - 2 * offset;
        canvas.translate(getWidth() / 2, getHeight() / 2);
        int minRadius = width / (2 * 5);
        mPath.moveTo(0, 0);

        double three = Math.sqrt(2 * 2 - 1);
        for (int i = 1; i <= 5; i++) {
            mPath.lineTo(minRadius * i / 2, (float) (minRadius * i / 2 * three));

            mPath.lineTo(minRadius * i, 0);

            mPath.lineTo(minRadius * i / 2, -(float) (minRadius * i / 2 * three));

            mPath.lineTo(-minRadius * i / 2, -(float) (minRadius * i / 2 * three));

            mPath.lineTo(-minRadius * i, 0);

            mPath.lineTo(-minRadius * i / 2, (float) (minRadius * i / 2 * three));

            mPath.lineTo(minRadius * i / 2, (float) (minRadius * i / 2 * three));

            canvas.drawPath(mPath, mPaint);

            if (i == 5) {
                float x = minRadius * 5 / 2 + offset + rect.width() / 2;
                float y = (float) (minRadius * i / 2 * three) + offset + rect.height() / 2;
                float lineX = minRadius * 5 + offset + rect.width() / 2;
                canvas.drawText(values.get(0), x, y, mPaint);
                canvas.drawText(values.get(1), lineX, 0, mPaint);
                canvas.drawText(values.get(2), x, -y, mPaint);
                canvas.drawText(values.get(3), -x, -y, mPaint);
                canvas.drawText(values.get(4), -lineX, 0, mPaint);
                canvas.drawText(values.get(5), -x, y, mPaint);

            }
        }

        int i = 5;
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(minRadius * i, 0);
        mPath.moveTo(0, 0);
        mPath.lineTo(minRadius * i / 2, -(float) (minRadius * i / 2 * three));
        mPath.moveTo(0, 0);
        mPath.lineTo(-minRadius * i / 2, -(float) (minRadius * i / 2 * three));
        mPath.moveTo(0, 0);
        mPath.lineTo(-minRadius * i, 0);
        mPath.moveTo(0, 0);
        mPath.lineTo(-minRadius * i / 2, (float) (minRadius * i / 2 * three));

        canvas.drawPath(mPath, mPaint);

        test(canvas);
    }

    private void test(Canvas canvas) {
        canvas.save();
        RectF rectF = new RectF(1, 1 , 10, 10);

        canvas.drawRoundRect(rectF, 30 , 30 , mPaint);

        canvas.restore();
    }

    private int dip2px(int value) {
        return (int) (getResources().getDisplayMetrics().density * value + 0.5f);
    }
}
