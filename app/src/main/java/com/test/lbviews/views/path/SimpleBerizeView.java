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

public class SimpleBerizeView extends View {
    private float mCurX, mCury;
    private PointF start, end, control;
    private Paint mPaint;

    public SimpleBerizeView(Context context) {
        super(context);
    }

    public SimpleBerizeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        start = new PointF();
        end = new PointF();
        control = new PointF();
    }

    public SimpleBerizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCurX = w / 2;
        mCury = h / 2;
        start.x = mCurX - 200;
        start.y = mCury;
        end.x = mCurX + 100;
        end.y = mCury;
        control.x = mCurX;
        control.y = mCury;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        control.x = event.getX();
        control.y = event.getY();
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(start.x, start.y, mPaint);
        canvas.drawPoint(control.x, control.y, mPaint);
        canvas.drawPoint(end.x, end.y, mPaint);

        mPaint.setStrokeWidth(4);
        canvas.drawLine(start.x, start.y, control.x, control.y, mPaint);
        canvas.drawLine(control.x, control.y, end.x, end.y, mPaint);

        Path p = new Path();
        p.moveTo(start.x, start.y);
        p.quadTo(control.x, control.y, end.x, end.y);
        canvas.drawPath(p, mPaint);
    }
}
