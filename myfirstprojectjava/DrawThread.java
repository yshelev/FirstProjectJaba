package com.example.myfirstprojectjava;

import static com.example.myfirstprojectjava.GameThread.displayWidth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.util.ArrayList;



public class DrawThread extends Thread {
    Context context;
    GameThread gameThread;
    public static Paint paint = new Paint();
    DrawView drawView;
    RayCasting rayCasting;
    Bonus bonus;
    public static int mapObstructionHeight;
    public static int mapObstructionWidth;

    private ArrayList<Shot> shots;
    private ArrayList<Sprite> sprites;
    private ArrayList<Enemy> enemies;
    private final SurfaceHolder surfaceHolder;
    private boolean running = true;
    private final int timerInterval = 5;
    private int t = 0;


    @SuppressLint("ResourceAsColor")
    public DrawThread(Context context, SurfaceHolder surfaceHolder, GameThread gameThread, DrawView drawView) {
        paint.setTextSize(30);
        rayCasting = new RayCasting();
        this.context = context;
        this.surfaceHolder = surfaceHolder;
        this.gameThread = gameThread;
        paint.setColor(Color.BLACK);
        this.drawView = drawView;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {

                int w = gameThread.mainHero.bitmap.getWidth();
                int h = gameThread.mainHero.bitmap.getHeight() / 3;
                Rect frame = new Rect(0, i * h, w, i * h + h);
                gameThread.mainHero.addFrame(frame);
                for (Enemy enemy : gameThread.enemies) {
                    enemy.addFrame(frame);
                }

            }
        }


    }

    public void requestStop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {

            Canvas canvas = surfaceHolder.lockCanvas();
            bonus = gameThread.bonus;
            shots = (ArrayList<Shot>) gameThread.shots.clone();
            sprites = (ArrayList<Sprite>) gameThread.sprites.clone();
            enemies = (ArrayList<Enemy>) gameThread.enemies.clone();
            if (canvas != null) {
                if (enemies.size() == 0) {
                    try {
                        Thread.sleep(101);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mapObstructionHeight = canvas.getHeight() / GameThread.map[0].length;
                mapObstructionWidth = canvas.getWidth() / GameThread.map.length;
                try {
                    t += 1;
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

                    bonus.onDraw(canvas, paint);

                    for (Enemy enemy: enemies) {
                        enemy.onDraw(canvas, paint);
                        rayCasting.rayCast(canvas, paint, enemy, GameThread.mapOfObst, gameThread.mainHero);
                    }

                    if (!gameThread.mainHero.isDead) {
                        gameThread.mainHero.onDraw(canvas, paint);
                    }

                    for (int i = 0; i < gameThread.mapOfObst.length; i++) {
                        for (int j = 0; j < gameThread.mapOfObst[i].length; j++) {
                            if (gameThread.mapOfObst[i][j] != null) {
                                gameThread.mapOfObst[i][j].onDraw(canvas, paint);
                            }
                        }
                    }
                    {
                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        canvas.drawRect(0, 0, canvas.getWidth() / 2, mapObstructionHeight / 3 * 2, paint);
                        paint.setColor(Color.RED);
                        canvas.drawRect(displayWidth - 4 * mapObstructionWidth, 0, displayWidth, mapObstructionHeight / 3 * 2, paint);
                        paint.setColor(Color.BLACK);
                        paint.setStrokeWidth(1);

                        canvas.drawText("points: " + gameThread.mainHero.points + " hps: " + gameThread.mainHero.hp, 0, mapObstructionHeight / 2.5f, paint);
                        canvas.drawText("removepoints", displayWidth - 3 * mapObstructionWidth, mapObstructionHeight / 2.5f, paint);
                    }

                    for (Shot shot : shots) {
                        shot.onDraw(canvas, paint);
                    }

                    if (t % 10 == 0) update();


                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            if (!(gameThread.isRunning())) {
                requestStop();
                break;
            }
        }
    }
    public void update() {

        gameThread.mainHero.update(timerInterval);
        for (Enemy enemy : gameThread.enemies) {
            enemy.update(timerInterval);
        }
    }

}

