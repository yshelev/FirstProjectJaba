package com.example.myfirstprojectjava;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class RayCasting {
    float[] dot = new float[2];
    ArrayList<float[]> dots = new ArrayList<>();

    public void rayCast(Canvas canvas, Paint paint, Enemy enemy, Obst[][] mapOfObst, MainHero mainHero) {
        paint.setARGB(50, 255, 0, 0);


        double startDrawingX = enemy.x + (double)enemy.bitmap.getWidth() / 2;
        double startDrawingY = enemy.y + (double)enemy.bitmap.getHeight() / 6;
        double currentX;
        double currentY;
        double cosa;
        double sina;
        int rayLength = 500;
        int FOV = enemy.FOV;
        double deltaX = enemy.towardX - startDrawingX;
        double deltaY = enemy.towardY - startDrawingY;
        int num_rays = 500;
        double delta_angle = (float) FOV / (float) num_rays;
        double current_angle = (Math.atan(((float)deltaX) / (deltaY))) * 180 / Math.PI - (float) FOV / 2 + 90;


        for (int i = 0; i < num_rays; i++) {

            cosa = Math.cos(Math.toRadians(current_angle));
            sina = Math.sin(Math.toRadians(current_angle));

            if (deltaY > 0) {
                currentX = (int) (startDrawingX - rayLength * cosa);
                currentY = (int) (startDrawingY + rayLength * sina);
            }
            else {
                currentX = (int) (startDrawingX + rayLength * cosa);
                currentY = (int) (startDrawingY - rayLength * sina);
            }

            if (i == num_rays / 2) {
                int dx = (int) (currentX - startDrawingX);
                int dy = (int) (currentY - startDrawingY);
                enemy.playerAngle = Math.atan(Math.abs(dy) / 1.0 / Math.abs(dx)) * 180 / Math.PI;
                if (startDrawingX > currentX) {
                    enemy.playerAngle = 180 - enemy.playerAngle;

                }

                if (startDrawingY > currentY) {
                    enemy.playerAngle = 360 - enemy.playerAngle;
                }
            }

            for (Obst[] obsts : mapOfObst) {
                for (Obst obst : obsts) {
                    if (obst != null) {
                        if (cross(
                                (float) startDrawingX, (float) startDrawingY,
                                (float) currentX, (float) currentY,
                                obst.x, obst.y,
                            obst.x+obst.width, obst.height + obst.y)
                        ) {
                            if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                    ((dot[0] <=currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                    ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                    dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                dots.add(dot.clone());
                            }
                        }
                        if (cross(
                                (float) startDrawingX, (float) startDrawingY,
                                (float) currentX, (float) currentY,
                                obst.x + obst.width, obst.y,
                                obst.x, obst.height + obst.y)
                        ) {
                            if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                    ((dot[0] <=currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                    ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                    dot[1] <= obst.y + obst.height && dot[1] >= obst.y){
                                dots.add(dot.clone());
                            }
                        }
                        if (cross(
                                (float) startDrawingX, (float) startDrawingY,
                                (float) currentX, (float) currentY,
                                obst.x, obst.y,
                            obst.x+obst.width, obst.y)
                        ) {
                            if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                    ((dot[0] <=currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                    ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                    dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                dots.add(dot.clone());
                            }
                        }
                        if (cross(
                                (float) startDrawingX, (float) startDrawingY,
                                (float) currentX, (float) currentY,
                                obst.x + obst.width, obst.y + obst.height,
                                obst.x+obst.width, obst.y)
                        ) {
                            if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                    ((dot[0] <=currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                    ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                    dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                dots.add(dot.clone());
                            }
                        }
                        if (cross(
                                (float) startDrawingX, (float) startDrawingY,
                                (float) currentX, (float) currentY,
                                obst.x + obst.width, obst.y + obst.height,
                                obst.x, obst.y + obst.height)
                        ) {
                            if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                    ((dot[0] <=currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                    ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                    dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                dots.add(dot.clone());
                            }
                        }
                        if (cross(
                                (float) startDrawingX, (float) startDrawingY,
                                (float) currentX, (float) currentY,
                                obst.x, obst.y + obst.height,
                                obst.x, obst.y)
                        ) {
                            if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                    ((dot[0] <=currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) || (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                    ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                    dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                dots.add(dot.clone());
                            }
                        }
                    }

                }
            }

            for (float[] dot : dots) {
                if (Math.sqrt((startDrawingX - currentX) * (startDrawingX - currentX) + (startDrawingY - currentY) * (startDrawingY - currentY)) >
                        Math.sqrt((startDrawingX - dot[0]) * (startDrawingX - dot[0]) + (startDrawingY - dot[1]) * (startDrawingY - dot[1]))) {
                    currentX = dot[0];
                    currentY = dot[1];
                }
            }
            canvas.drawLine((float)startDrawingX, (float)startDrawingY, (float)currentX, (float)currentY, paint);
            dots.clear();
            current_angle += delta_angle;
        }

        paint.setColor(Color.BLACK);
        }



        public boolean check(Enemy enemy, Obst[][] mapOfObst, MainHero mainHero) {

            boolean flag = true;
            double startDrawingX = enemy.x + (double)enemy.bitmap.getWidth() / 2;
            double startDrawingY = enemy.y + (double)enemy.bitmap.getHeight() / 6;
            double currentX = mainHero.x + mainHero.w / 2;
            double currentY = mainHero.y + mainHero.h / 2;
            int rayLength = 500;
            int FOV = enemy.FOV;
            double deltaX = enemy.towardX - startDrawingX;
            double deltaY = enemy.towardY - startDrawingY;
            double deltaXHero = mainHero.x + mainHero.w / 2 - startDrawingX;
            double deltaYHero = mainHero.y + mainHero.h / 2 - startDrawingY;
            double current_angle1 = (Math.atan(((float)deltaX) / (deltaY))) * 180 / Math.PI - FOV / 2;
            double current_angle2 = (Math.atan(((float)deltaX) / (deltaY))) * 180 / Math.PI + FOV / 2;
            double angleToHero = (Math.atan(((float)deltaXHero) / (deltaYHero))) * 180 / Math.PI;

            if (deltaY * deltaYHero > 0 ) {
                if (angleToHero >= current_angle1 && angleToHero <= current_angle2) {
                    if (Math.sqrt(Math.pow(deltaXHero, 2) + Math.pow(deltaYHero, 2)) < rayLength) {
                        outer: for (Obst[] obsts : mapOfObst) {
                            for (Obst obst : obsts) {
                                if (obst != null) {
                                    if (cross(
                                            (float) startDrawingX, (float) startDrawingY,
                                            (float) currentX, (float) currentY,
                                            obst.x, obst.y,
                                            obst.x+obst.width, obst.height + obst.y)
                                    ) {
                                        if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                                ((dot[0] <= currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                                ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                                dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                            flag = false;
                                            break outer;
                                        }
                                    }
                                    if (cross(
                                            (float) startDrawingX, (float) startDrawingY,
                                            (float) currentX, (float) currentY,
                                            obst.x + obst.width, obst.y,
                                            obst.x, obst.height + obst.y)
                                    ) {
                                        if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                                ((dot[0] <= currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                                ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                                dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                            flag = false;
                                            break outer;
                                        }
                                    }
                                    if (cross(
                                            (float) startDrawingX, (float) startDrawingY,
                                            (float) currentX, (float) currentY,
                                            obst.x, obst.y,
                                            obst.x+obst.width, obst.y)
                                    ) {
                                        if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                                ((dot[0] <= currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                                ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                                dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                            flag = false;
                                            break outer;
                                        }
                                    }
                                    if (cross(
                                            (float) startDrawingX, (float) startDrawingY,
                                            (float) currentX, (float) currentY,
                                            obst.x + obst.width, obst.y + obst.height,
                                            obst.x+obst.width, obst.y)
                                    ) {
                                        if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                                ((dot[0] <= currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                                ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                                dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                            flag = false;
                                            break outer;
                                        }
                                    }
                                    if (cross(
                                            (float) startDrawingX, (float) startDrawingY,
                                            (float) currentX, (float) currentY,
                                            obst.x + obst.width, obst.y + obst.height,
                                            obst.x, obst.y + obst.height)
                                    ) {
                                        if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                                ((dot[0] <= currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) ||  (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                                ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentY >= enemy.y + enemy.h / 2 - 5 && currentY <= enemy.y + enemy.h / 2 + 5)) &&
                                                dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                            flag = false;
                                            break outer;
                                        }
                                    }
                                    if (cross(
                                            (float) startDrawingX, (float) startDrawingY,
                                            (float) currentX, (float) currentY,
                                            obst.x, obst.y + obst.height,
                                            obst.x, obst.y)
                                    ) {
                                        if (dot[0] <= obst.x + obst.width && dot[0] >= obst.x &&
                                                ((dot[0] <= currentX && dot[0] >= enemy.x + enemy.w / 2) || (dot[0] >= currentX && dot[0] <= enemy.x + enemy.w / 2) || (currentX >= enemy.x + enemy.w / 2 - 5 && currentX <= enemy.x + enemy.w / 2 + 5)) &&
                                                ((dot[1] <= currentY && dot[1] >= enemy.y + enemy.h / 2) || (dot[1] >= currentY && dot[1] <= enemy.y + enemy.h / 2) || (currentX >= enemy.y + enemy.h / 2 - 5 && currentX >= enemy.x + enemy.w / 2 + 5)) &&
                                                dot[1] <= obst.y + obst.height && dot[1] >= obst.y) {
                                            flag = false;
                                            break outer;
                                        }
                                    }
                                }

                            }
                        }

                    }
                    else {
                        flag = false;
                    }
                }
                else {
                    flag = false;
                }

            }
            else {
                flag = false;
            }
            if (flag) {
                enemy.setTowardPoint(mainHero.x + mainHero.bitmap.getWidth() / 2, mainHero.y + mainHero.bitmap.getHeight() / 2, false);
            }

            return flag;
        }

    public boolean cross(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float n;
        if (y2 - y1 != 0) {
            float q = (x2 - x1) / (y1 - y2);
            float sn = (x3 - x4) + (y3 - y4) * q;
            if (sn == 0) {
                return false;
            }
            float fn = (x3 - x1) + (y3 - y1) * q;
            n = fn / sn;
        }
        else {
            if (y3 - y4 == 0) {
                return false;
            }
            n = (y3 - y1) / (y3 - y4);
        }
        dot[0] = x3 + (x4 - x3) * n;
        dot[1] = y3 + (y4 - y3) * n;
        return true;

    }

}
