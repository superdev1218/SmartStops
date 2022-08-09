package com.creativeinfoway.smartstops.app.ui.models;

import android.graphics.Bitmap;

public class MapModel {

    private String id;
    private Bitmap bitmap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
