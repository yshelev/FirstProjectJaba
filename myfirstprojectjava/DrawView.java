package com.example.myfirstprojectjava;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.example.myfirstprojectjava.GameThread.displayWidth;
import static com.example.myfirstprojectjava.DrawThread.mapObstructionHeight;
import static com.example.myfirstprojectjava.DrawThread.mapObstructionWidth;


import androidx.annotation.NonNull;

@SuppressLint("ViewConstructor")
public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    DrawThread drawThread;
    GameThread gameThread;
    User user;
    private UserCallback callback;

    public DrawView(Context context, User user) {
        super(context);
        this.user = user;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameThread = new GameThread(getContext(), user, callback);
        drawThread = new DrawThread(getContext(), getHolder(), gameThread, this);
        drawThread.start();
        gameThread.start();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        stopDrawThread();
        stopGameThread();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getX() > displayWidth - 4 * mapObstructionWidth) && (event.getY() < mapObstructionHeight)){
            gameThread.mainHero.movePoints.clear();
            gameThread.mainHero.towardX = gameThread.mainHero.x  + gameThread.mainHero.w / 2;
            gameThread.mainHero.towardY = gameThread.mainHero.y + gameThread.mainHero.h / 2;
            return false;
        }

        gameThread.mainHero.setTowardPoint((int)event.getX(),(int)event.getY(), true);
        return false;
    }

    public void setCallBack(UserCallback callback) {
        this.callback = callback;
    }

    public void removeCallBack() {
        this.callback = null;
    }

    public void stopDrawThread(){
        drawThread.requestStop();
    }



    public void stopGameThread(){
        gameThread.requestStop();
    }
}





