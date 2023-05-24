



package com.example.myfirstprojectjava;


import android.graphics.Bitmap;
import android.graphics.Rect;
import static com.example.myfirstprojectjava.GameThread.mapOfObst;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

import kotlin.jvm.Volatile;

public class Unit extends Sprite{
    double playerAngle;
    int hp, towardX, towardY;
    Gun gun;
    int w, h;
    @Volatile
    LinkedBlockingDeque<int[]> movePoints = new LinkedBlockingDeque<>();
    @Volatile
    Iterator<int[]> itr;
    protected int[] canMove;
    int currentForMoving = 0;
    int countForMoving = 400;
    boolean isAggr = false;

    public Unit(int hp, int playerAngle, int x, int y, int towardX, int towardY, Gun gun, Bitmap bitmap, int rpadding, int lpadding, int tpadding, int bpadding) {
        super(x, y, new Rect(0, bitmap.getHeight() / 3 * 2, bitmap.getWidth(), bitmap.getHeight()), bitmap, rpadding, lpadding, tpadding, bpadding);
        w = bitmap.getWidth();
        h = bitmap.getHeight() / 3;
        this.towardX = towardX;
        this.towardY = towardY;
        this.hp = hp;
        this.playerAngle = playerAngle;
        this.gun = gun;
    }

    public void setTowardPoint(int x, int y, boolean type) {
        if (type){
            for (Obst[] obsts : mapOfObst) {
                for (Obst obst : obsts) {
                    if (obst != null) {
                        if (Rect.intersects(
                                obst.getFrameRect(),
                                new Rect(
                                        x, y, x, y
                                )
                        )) return;
                    }
                }
            }
            movePoints.addLast(new int[]{x, y});
        }
        else {
            for (Obst[] obsts : mapOfObst) {
                for (Obst obst : obsts) {
                    if (obst != null) {
                        if (Rect.intersects(
                                obst.getFrameRect(),
                                new Rect(
                                        x, y, x, y
                                )
                        )) return;
                    }
                }
            }
            towardX = x;
            towardY = y;
        }

    }


    public void move(Obst[][] obsts) {
        currentForMoving += 1;
        if (currentForMoving > countForMoving) {
            isAggr = false;
            isMoving = false;
            if (movePoints.size() != 0) {
                currentForMoving = 0;
                if (itr == null) {
                    itr = movePoints.iterator();
                }
                if (!itr.hasNext()) {
                    itr = movePoints.iterator();
                }
                int[] xy = itr.next();
                towardX = xy[0];
                towardY = xy[1];
                movePoints.removeFirst();
            }
        }
        if ((towardX != -1 && towardY != -1) && !Rect.intersects(
                new Rect(x + w / 4, y + h / 4, x + w / 4 * 3, y + h / 4 * 3),
                new Rect(towardX, towardY, towardX, towardY)
        )) {
            int dx = x + w / 2 - towardX;
            int dy = y + h / 2 - towardY;
            if (dx != 0 && dy != 0)
                playerAngle = Math.atan(Math.abs(dy) / 1.0 / Math.abs(dx)) * 180 / Math.PI;
            else if (dx == 0) playerAngle = Math.asin(1) * 180 / Math.PI;
            else playerAngle = Math.acos(1) * 180 / Math.PI;


            canMove = canMove(obsts);
            isMoving = true;


            if ((x + w / 2 < towardX) && (canMove[0] > 0)) {
                x += gun.carrierMoveSpeed * Math.cos(Math.toRadians(playerAngle));
            }
            if (x + w / 2 > towardX) {
                playerAngle = 180 - playerAngle;
                if (canMove[1] > 0) {
                    x += gun.carrierMoveSpeed * Math.cos(Math.toRadians(playerAngle));
                }

            }

            if ((y + h / 2 < towardY) && (canMove[2] > 0)) {
                y += gun.carrierMoveSpeed * Math.sin(Math.toRadians(playerAngle));
            }
            if (y + h / 2 > towardY) {
                playerAngle = 360 - playerAngle;
                if (canMove[3] > 0) {
                    y += gun.carrierMoveSpeed * Math.sin(Math.toRadians(playerAngle));
                }
            }

        } else {
            isMoving = false;
            if (movePoints.size() != 0) {
                currentForMoving = 0;
                if (itr == null) {
                    itr = movePoints.iterator();
                }
                if (!itr.hasNext()) {
                    itr = movePoints.iterator();
                }
                int[] xy = itr.next();
                towardX = xy[0];
                towardY = xy[1];
                movePoints.removeFirst();
            }
        }
    }

    int[] canMove(Obst[][] obsts) {
        int[] toReturn = {1, 1, 1, 1}; // 1 - вправо, 2 - влево, 3 - вниз, 4 - влево
        for (Obst[] obst : obsts) {
            for (Obst value : obst) {
                if (value != null) {
                    if (Rect.intersects(
                            new Rect((int) (x + gun.carrierMoveSpeed + lpadding), y + tpadding, (int) (x + gun.carrierMoveSpeed + w - rpadding), y + h - bpadding),
                            value.getFrameRect()
                    )) {
                        toReturn[0] = -1;
                    }
                    if (Rect.intersects(
                            new Rect((int) (x - gun.carrierMoveSpeed + lpadding), y + tpadding, (int) (x - gun.carrierMoveSpeed + w - rpadding), y + h - bpadding),
                            value.getFrameRect()
                    )) {
                        toReturn[1] = -1;
                    }
                    if (Rect.intersects(
                            new Rect(x + lpadding, (int) (y + gun.carrierMoveSpeed + tpadding), x + w - rpadding, (int) (y + h + gun.carrierMoveSpeed - bpadding)),
                            value.getFrameRect()
                    )) {
                        toReturn[2] = -1;
                    }
                    if (Rect.intersects(
                            new Rect(x + lpadding, (int) (y - gun.carrierMoveSpeed + tpadding), x + w - rpadding, (int) (y + h - gun.carrierMoveSpeed - bpadding)),
                            value.getFrameRect()
                    )) {
                        toReturn[3] = -1;
                    }
                }
            }
        }
        return toReturn;

    }

    public void death(ArrayList sprites) {
        sprites.remove(this);
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

    }

}
