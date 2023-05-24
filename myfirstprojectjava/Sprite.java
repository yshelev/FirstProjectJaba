package com.example.myfirstprojectjava;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.security.KeyStore;
import java.util.ArrayList;

class Sprite {
    int x, y;
    Bitmap bitmap;
    protected boolean isMoving = false;
    ArrayList<Rect> frames;
    double timeForCurrentFrame;
    double frameTime;
    int currentFrame;
    int w;
    int h;
    int rpadding;
    int lpadding;
    int tpadding;
    int bpadding;

    public Sprite(int x, int y, Rect initialFrame, Bitmap bitmap, int rpadding, int lpadding, int tpadding, int bpadding) {
        this.x = x;
        this.y = y;
        this.frames = new ArrayList<>();
        this.frames.add(initialFrame);
        this.bitmap = bitmap;
        this.timeForCurrentFrame = 0.0;
        this.frameTime = 0.1;
        this.currentFrame = 0;
        this.w = initialFrame.width();
        this.h = initialFrame.height();
        this.rpadding = rpadding;
        this.lpadding = lpadding;
        this.tpadding = tpadding;
        this.bpadding = bpadding;

    }

    public void onDraw(Canvas canvas, Paint paint) {

        Rect destination = new Rect((int)x, (int)y, (int)(x + w), (int)(y + h));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, paint);
    }

    public void addFrame (Rect frame) {
        frames.add(frame);
    }

    public Rect getFrameRect() {
        return new Rect(x + lpadding, y + tpadding, (int) (x + w - rpadding), (int) (y + h - rpadding));
    }

    public boolean intersect (Sprite s) {
        return getFrameRect().intersect(s.getFrameRect());
    }

    public String toString() {
        return bitmap.toString();
    }

    public boolean setCoords(int x, int y, Obst[][] map) {
        for (Obst[] obsts : map) {
            for (Obst obst : obsts) {
                if (obst != null) {
                    if (Rect.intersects(obst.getFrameRect(), new Rect(x, y, x, y))) {
                        return false;
                    }
                }

            }
        }
        this.x = x;
        this.y = y;
        return true;
    }
}