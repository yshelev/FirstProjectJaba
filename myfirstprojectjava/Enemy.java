package com.example.myfirstprojectjava;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

public class Enemy extends Unit{
    int FOV = 60;
    int reloadTime = 20;
    int attackTime = -1;
    int currentTimeAggred = 0;
    int agrTime = 300;

    int[][] mps;



    public Enemy(int hp, int playerAngle, int x, int y, int towardX, int towardY, Gun gun, Bitmap bitmap, int a) {
        super(hp, playerAngle, x, y, towardX, towardY, gun, bitmap, 20, 10, 10, 10);
        this.mps = new int[][]{{x, y}, {x + 100, y}, {x + 100, y + 100}, {x, y + 100}};
        for (int[] mp : mps) {
            movePoints.addLast(mp);
        }
        itr = movePoints.iterator();
        reloadTime += 10 * a;
    }
    public Enemy(int hp, int playerAngle, int x, int y, int towardX, int towardY, Gun gun, Bitmap bitmap, int[][] movePoints, int a) {
        super(hp, playerAngle, x, y, towardX, towardY, gun, bitmap, 20, 10, 10, 10);
        this.mps = movePoints;
        for (int[] mp : mps) {
            this.movePoints.addLast(mp);
        }
        itr = this.movePoints.iterator();
        reloadTime += 10 * a;
    }

    public void setAggr(boolean aggr) {
        isAggr = aggr;
    }

    public void update (int ms) {
        timeForCurrentFrame += ms;
        if (!isMoving){
            currentFrame = 0;
        }
        else {
            if (timeForCurrentFrame >= frameTime) {
                currentFrame = (currentFrame + 1) % frames.size();
                timeForCurrentFrame = timeForCurrentFrame - frameTime;
            }
        }
        if (currentTimeAggred > agrTime && isAggr) {
            isAggr = false;
            int[] nextCoords = itr.next();
            towardY = nextCoords[1];
            towardX = nextCoords[0];
        }


    }

    public void setCurrentTimeAggred(int currentTimeAggred) {
        this.currentTimeAggred = currentTimeAggred;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        Rect destination = new Rect((int)x, (int)y, (int)(x + w), (int)(y + h));
        Rect frame = frames.get(currentFrame);
        canvas.save();
        canvas.rotate((float) playerAngle, x + w / 2, y + h / 2);
        if (isAggr) {
            paint.setStrokeWidth(10);
            paint.setColor(Color.RED);
            canvas.drawLine(x + w / 2, y, x + w / 2, y - 20, paint);
            paint.setStrokeWidth(1);
        }

        canvas.drawBitmap(bitmap, frame, destination, paint);
        canvas.restore();

        attackTime += 1;
        currentTimeAggred += 1;

        for (int i = 0; i < mps.length; i++) {
            if (movePoints.peekLast() == mps[i]) {
                if (i + 1 == mps.length) {
                    movePoints.addLast(mps[0]);
                }
                else movePoints.addLast(mps[i + 1]);
            }
        }

    }

    public void attack(MainHero mainHero, Bitmap bitmapForShot, ArrayList<Shot> shots, ArrayList<Sprite> sprites) {

        if (attackTime % reloadTime == 0) {
            if (gun.range >= Math.sqrt(Math.pow(x - mainHero.x, 2) + Math.pow(y - mainHero.y, 2))) {
                Shot shot = new Shot(x + w / 2 , y + h / 2, mainHero.x + mainHero.w / 2, mainHero.y + mainHero.h / 2, bitmapForShot, (int) gun.damage);
                shots.add(shot);
                sprites.add(shot);
            }
        }


    }






}
