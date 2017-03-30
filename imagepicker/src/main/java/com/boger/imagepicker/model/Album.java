package com.boger.imagepicker.model;

/**
 * Created by liubo on 2017/3/29.
 */

public class Album {
    public long id;
    public String path;
    public String cover;

    public Album(long id, String path, String cover) {
        this.id = id;
        this.path = path;
        this.cover = cover;
    }
}
