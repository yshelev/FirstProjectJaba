package com.example.myfirstprojectjava;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class MainHero extends Unit{

    int points = 0;
    Iterator<int[]> iter;
    boolean isDead = false;
    public MainHero(int hp, int playerAngle, int x, int y, int towardX, int towardY, Gun gun, Bitmap bitmap, int a) {
        super(hp, playerAngle, x, y, towardX, towardY, gun, bitmap, 20, 10, 5, 15);
        this.gun.carrierMoveSpeed += 20 * a;
    }
    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        if (movePoints.size() != 0 || isMoving) {
            iter = movePoints.iterator();
            int[] xy = new int[]{x + w / 2, y + h / 2};
            int[] xy1 = new int[]{towardX, towardY};
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(10);
            canvas.drawLine(xy[0], xy[1], xy1[0], xy1[1], paint);
            canvas.drawCircle(xy1[0], xy1[1], 10, paint);
            while (iter.hasNext()){
                xy = xy1;
                xy1 = iter.next();
                canvas.drawLine(xy[0], xy[1], xy1[0], xy1[1], paint);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(xy1[0], xy1[1], 10, paint);
            }
            paint.setStrokeWidth(1);
            paint.setColor(Color.BLACK);
        }

        Rect destination = new Rect((int)x, (int)y, (int)(x + w), (int)(y + h));
        Rect frame = frames.get(currentFrame);
        canvas.save();
        canvas.rotate((float) playerAngle, x + w / 2, y + h / 2);

        canvas.drawBitmap(bitmap, frame, destination, paint);
        canvas.restore();
    }

    public void attack(Enemy enemy, ArrayList<Enemy> enemies) {
        if (this.hp <= 0) {
            this.isDead = true;
        }
        if (this.getFrameRect().intersect(enemy.getFrameRect())) {
            enemy.death(enemies);
        }

    }

    public boolean check(Bonus bonus, int e) {
        if (this.getFrameRect().intersect(bonus.getFrameRect())) {
            this.points += bonus.bonusReward + e;
            return true;
        }
        return false;
    }

}
