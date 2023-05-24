package com.example.myfirstprojectjava;

import static com.example.myfirstprojectjava.DrawThread.mapObstructionHeight;
import static com.example.myfirstprojectjava.DrawThread.mapObstructionWidth;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import kotlin.jvm.Volatile;

public class GameThread extends Thread {
    final static Random random = new Random();
    Context context;
    DisplayMetrics metrics;
    public static int displayWidth;
    public static int displayHeight;
    RayCasting rayCasting = new RayCasting();
    UserCallback userCallback;
    Bitmap mapBitmap;
    boolean isneed = true;

    boolean running = true;


    public static int[][][] toDel = new int[][][]{
            {{4, 1}, {4, 2}, {4, 3}},
            {{4, 5}, {4, 6}, {4, 7}},
            {{4, 9}, {4, 10}, {4, 11}},
            {{8, 1}, {8, 2}, {8, 3}},
            {{8, 5}, {8, 6}, {8, 7}},
            {{8, 9}, {8, 10}, {8, 11}},
            {{1, 4}, {2, 4}, {3, 4}},
            {{5, 4}, {6, 4}, {7, 4}},
            {{9, 4}, {10, 4}, {11, 4}},
            {{1, 8}, {2, 8}, {3, 8}},
            {{5, 8}, {6, 8}, {7, 8}},
            {{9, 8}, {10, 8}, {11, 8}},
    };


    public static char[][] map = newMap();

    public static Obst[][] mapOfObst = new Obst[map.length][map[0].length];
    public User user;

//    "-" - стенка, "." - пол
    Gun[] guns = {
            new Gun(
            1000,
        10,
        2)
    };
    MainHero mainHero;
    @Volatile
    ArrayList<Enemy> enemies = new ArrayList<>();
    @Volatile
    ArrayList<Shot> shots = new ArrayList<>();
    @Volatile
    ArrayList<Sprite> sprites = new ArrayList<>();
    @Volatile
    Bitmap bulletBitmap;
    @Volatile
    Bonus bonus;




    public GameThread(Context context, User user, UserCallback userCallback) {
        this.userCallback = userCallback;
        mapBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.obstruction);
        this.user = user;
        this.context = context;
        bulletBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.qwe);
        mainHero = new MainHero(
                100,
                360,
                850,
                250,
                -1,
                -1,
                guns[0],
                BitmapFactory.decodeResource(context.getResources(), R.drawable.mainhero),
                user.toSend[0]
        );
        bonus = new Bonus(
                500,
                500,
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.diamond1), 50, 50, false)
        );
        enemies.add(new Enemy(
                1000,
                0,
                160,
                320,
                160,
                320,
                guns[0],
                BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy),
                user.toSend[1]
        ));
        metrics = context.getResources().getDisplayMetrics();
        displayWidth = metrics.widthPixels;
        displayHeight = metrics.heightPixels;
    }



    public boolean isRunning() {
        return running;
    }
    public void requestStop() {
        running = false;
    }


    @Override
    public void run() {
        sprites.add(mainHero);
        sprites.addAll(enemies);
        sprites.add(bonus);
        for (Obst[] obsts : mapOfObst) {
            sprites.addAll(Arrays.asList(obsts));

        }
        while (running) {
            while (mapObstructionHeight == 0 || mapObstructionWidth == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mapToObst(map, isneed);
            isneed = false;
            mainHero.move(mapOfObst);
            if (mainHero.check(bonus, user.toSend[2])) {
                int bx = random.nextInt(mapObstructionWidth * 12) + mapObstructionWidth;
                int by = random.nextInt(mapObstructionHeight * 12) + mapObstructionHeight;
                while (!bonus.setCoords(bx, by, mapOfObst)) {
                    bx = random.nextInt(mapObstructionWidth * 12) + mapObstructionWidth;
                    by = random.nextInt(mapObstructionHeight * 12) + mapObstructionHeight;
                }
            }

            for (Enemy enemy : enemies) {
                if (rayCasting.check(enemy, mapOfObst, mainHero)) {
                    enemy.attack(mainHero, bulletBitmap, shots, sprites);
                    enemy.isMoving = false;
                    enemy.setCurrentTimeAggred(0);
                    enemy.setAggr(true);
                }
                else {
                    enemy.move(mapOfObst);
                }
                mainHero.attack(enemy, enemies);
            }



            shots.removeIf(shot -> shot.move(sprites, mainHero));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mainHero.isDead) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                user.money += mainHero.points;
                if (userCallback != null) {
                    try {
                        userCallback.onUserDeath(user);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                        e.printStackTrace();

                    }
                }
            }
            if (enemies.size() == 0) {
                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainHero.points += 10;
                map = newMap();
                deleteObst();
                mapToObst(map, true);
                int choice = random.nextInt(4);
                int[][] startPoints = new int[][]{
                        {mapObstructionWidth * 10, mapObstructionHeight * 10},
                        {mapObstructionWidth * 6, mapObstructionHeight * 10},
                        {mapObstructionWidth * 10, mapObstructionHeight * 6},
                        {mapObstructionWidth * 6, mapObstructionHeight * 6},
                };
                mainHero.setTowardPoint(mainHero.x, mainHero.y, false);
                mainHero.movePoints.clear();
                mainHero.setCoords(startPoints[choice][0], startPoints[choice][1], mapOfObst);
                enemies.add(new Enemy(
                        1000,
                        0,
                        (int) (mapObstructionWidth * 2.5),
                        (int) (mapObstructionHeight * 2.5),
                        (int) (mapObstructionWidth * 2.5),
                        (int) (mapObstructionHeight * 2.5),
                        guns[0],
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy),
                        new int[][]{{(int) (mapObstructionWidth * 2.5), (int) (mapObstructionHeight * 2.5)},
                                    {(int) (mapObstructionWidth * 2.5), (int) (mapObstructionHeight * 6.5)},
                                    {(int) (mapObstructionWidth * 6.5), (int) (mapObstructionHeight * 6.5)},
                                    {(int) (mapObstructionWidth * 6.5), (int) (mapObstructionHeight * 2.5)},},
                        user.toSend[1]
                ));
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 1; j++) {

                        int w = mainHero.bitmap.getWidth();
                        int h = mainHero.bitmap.getHeight() / 3;
                        Rect frame = new Rect(0, i * h, w, i * h + h);
                        for (Enemy enemy : enemies) {
                            enemy.addFrame(frame);
                        }

                    }
                }
            }

        }

    }

    public static char[][] newMap() {
        char[][] toReturn = {
                {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'},
                {'-', '.', '.', '.', '-', '.', '.', '.', '-', '.', '.', '.', '-'},
                {'-', '.', '.', '.', '-', '.', '.', '.', '-', '.', '.', '.', '-'},
                {'-', '.', '.', '.', '.', '.', '.', '.', '-', '.', '.', '.', '-'},
                {'-', '.', '-', '.', '-', '.', '-', '-', '-', '-', '-', '-', '-'},
                {'-', '.', '.', '.', '.', '.', '.', '.', '-', '.', '.', '.', '-'},
                {'-', '.', '.', '.', '-', '.', '.', '.', '-', '.', '.', '.', '-'},
                {'-', '.', '.', '.', '-', '.', '.', '.', '-', '.', '.', '.', '-'},
                {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'},
                {'-', '.', '.', '.', '-', '.', '.', '.', '-', '.', '.', '.', '-'},
                {'-', '.', '.', '.', '-', '.', '.', '.', '-', '.', '.', '.', '-'},
                {'-', '.', '.', '.', '-', '.', '.', '.', '-', '.', '.', '.', '-'},
                {'-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-'},
        };
        for (int[][] points : toDel) {
            toReturn[points[1][0]][points[1][1]] = '.';
            if (random.nextBoolean()) {                      // убираем первую или последнюю?
                toReturn[points[0][0]][points[0][1]] = '.';
                if (random.nextBoolean()) {
                    toReturn[points[2][0]][points[2][1]] = '.';
                }
            }
            else {
                toReturn[points[2][0]][points[2][1]] = '.';
            }
        }

        return toReturn;
    }


    public void mapToObst(char[][] map, boolean isneed) {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] == '-') {
                        mapOfObst[i][j] = new Obst(
                                j * mapObstructionWidth,
                                i * mapObstructionHeight,
                                mapObstructionHeight,
                                mapObstructionWidth,
                                Bitmap.createScaledBitmap(mapBitmap, mapObstructionWidth, mapObstructionHeight, false)
                        );
                        if (isneed) sprites.add(mapOfObst[i][j]);
                    }
                    else {
                        mapOfObst[i][j] = null;
                    }
                }
            }
        }
        public void deleteObst() {
            for (Obst[] obsts : mapOfObst) {
                for (Obst obst : obsts) {
                    sprites.remove(obst);
                }

            }
        }

    }





