package com.example.myfirstprojectjava;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Obst extends Sprite{
    float height;
    float width;
    public Obst(int x, int y, float height, float width, Bitmap bitmap) {
        super(x, y, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), bitmap, 0, 0, 0, 0);
        this.height = height;
        this.width = width;
    }
}
