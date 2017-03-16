package com.test.lbviews.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liubo on 2017/3/13.
 */

public class CanvasOperation extends View {
    private Paint mPaint;

    public CanvasOperation(Context context) {
        this(context, null);
    }

    public CanvasOperation(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasOperation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawCircle(0, 0, 380, mPaint);
        canvas.drawCircle(0, 0, 400, mPaint);
        for (int i = 0; i < 360; i += 10) {
            canvas.drawLine(0, 380, 0, 400, mPaint);
            canvas.rotate(10);
        }

    }
}
