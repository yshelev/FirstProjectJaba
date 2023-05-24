package com.example.myfirstprojectjava;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;


public class Shot extends Sprite{
    double playerAngle;
    int towardX, towardY;
    int MOVESPEED;
    double xMOVESPEED = 0;
    double yMOVESPEED = 0;
    boolean flag = false;
    int damage;
    public Shot(int x, int y, int towardX, int towardY, Bitmap bitmap, int damage) {
        super(x, y, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), bitmap, 0, 0, 0, 0);
        this.towardX = towardX;
        this.towardY = towardY;
        this.MOVESPEED = 7;
        this.damage = damage;
        int dx = (int) (x + w / 2 - towardX);
        int dy = (int) (y + h / 2 - towardY);
        if (dx != 0 && dy != 0)
            playerAngle = Math.atan(Math.abs(dy) / 1.0 / Math.abs(dx)) * 180 / Math.PI;
        else if (dx == 0) playerAngle = Math.asin(1) * 180 / Math.PI;
        else playerAngle = Math.acos(1) * 180 / Math.PI;

        if (dx < 0) {
            xMOVESPEED += MOVESPEED * Math.cos(Math.toRadians(playerAngle));
        }

        if (dx > 0) {
            playerAngle = 180 - playerAngle;
            xMOVESPEED += MOVESPEED * Math.cos(Math.toRadians(playerAngle));
        }

        if (dy < 0) {
            yMOVESPEED += MOVESPEED * Math.sin(Math.toRadians(playerAngle));
        }

        if (dy > 0) {
            playerAngle = 360 - playerAngle;
            yMOVESPEED += MOVESPEED * Math.sin(Math.toRadians(playerAngle));
        }
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        Rect destination = new Rect((int)x, (int)y, (int)(x + w), (int)(y + h));
        Rect frame = frames.get(currentFrame);
        canvas.save();
        canvas.rotate((float) playerAngle + 90, x + w / 2, y + h/2);

        canvas.drawBitmap(bitmap, frame, destination, paint);
        canvas.restore();
    }

    public boolean move(ArrayList<Sprite> sprites, MainHero mainHero) {
        flag = false;
        x += xMOVESPEED;
        y += yMOVESPEED;

        for (Sprite sprite : sprites) {
            if (sprite != null) {
                if (sprite.intersect(this)){
                    switch (sprite.getClass().toString()) {
                        case "class com.example.myfirstprojectjava.Enemy":
                            break;
                        case "class com.example.myfirstprojectjava.MainHero":
                            flag = true;
                            mainHero.hp -= damage;
                            break;
                        case "class com.example.myfirstprojectjava.Obst":
                            flag = true;
                            break;
                        case "class com.example.myfirstprojectjava.Shot":
                            break;
                    }
                }
            }

        }

        if (Rect.intersects(new Rect(x, y, bitmap.getWidth() + x, bitmap.getHeight() + y), mainHero.getFrameRect())) {
            flag = true;
        }

        return flag;
    }
}

