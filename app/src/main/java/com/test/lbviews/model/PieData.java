package com.test.lbviews.model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by liubo on 2017/3/10.
 */

public class PieData implements Serializable {
    private String name; //名字
    private float percentage; //百分比
    private float value; //数值

    private int color; //颜色
    private float angle; //角度

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
        angle = 360 * percentage;
        Log.d("PieView", " " + angle);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getAngle() {
        Log.d("PieView", "getAngle " + angle);
        return angle;
    }

    @Override
    public String toString() {
        return "PieData{" +
                "name='" + name + '\'' +
                ", percentage=" + percentage +
                ", value=" + value +
                ", color=" + color +
                ", angle=" + angle +
                '}';
    }
}
