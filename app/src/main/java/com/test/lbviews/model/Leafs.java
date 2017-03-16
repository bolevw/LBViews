package com.test.lbviews.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by liubo on 2017/3/13.
 */

public class Leafs {
    private int rotate;
    private float x;
    private float y;
    private int speed;

    public Leafs(int rotate, float x, float y, int speed) {
        this.rotate = rotate;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void draw(Canvas canvas, Bitmap bitmap, Paint mPaint) {
        Matrix matrix = new Matrix();
        matrix.postRotate(getRotate());
        x = x - speed;
        matrix.postTranslate(x, y);
        canvas.drawBitmap(bitmap, matrix, mPaint);
    }
}
