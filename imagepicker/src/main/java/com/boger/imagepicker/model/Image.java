package com.boger.imagepicker.model;

/**
 * Created by liubo on 2017/3/29.
 */

public class Image {
    public long id;
    public String path;
    public String name;
    public boolean isSelect;

    public Image(long id, String path, String name, boolean isSelect) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.isSelect = isSelect;
    }
}
