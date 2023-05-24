package com.example.myfirstprojectjava;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

public class Bonus extends Sprite{
    int bonusReward = 1;
    public Bonus(int x, int y, Bitmap bitmap) {
        super(x, y, new Rect(0, 0, bitmap.getHeight(), bitmap.getWidth()), bitmap, 0, 0, 0, 0);
    }


}
