package com.boger.pictureutils.model;

import java.util.ArrayList;

/**
 * Created by liubo on 2017/3/28.
 */

public class Album {
    public String cover;
    public String name;
    public int count;
    public ArrayList<String> child;

    public Album(String cover, String name, int count, ArrayList<String> child) {
        this.cover = cover;
        this.name = name;
        this.count = count;
        this.child = child;
    }
}
