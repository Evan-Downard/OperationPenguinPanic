package com.test.operationpenguinpanic;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Oswald on 4/24/2016.
 */
public class MainThread_Hard extends Thread{
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
        private GPRacing_Hard gamePanelRacing_Hard;
    private boolean running;
    public static Canvas canvas;

    public MainThread_Hard(SurfaceHolder surfaceHolder, GPRacing_Hard gamePanelRacing_Hard) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanelRacing_Hard = gamePanelRacing_Hard;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        long targetTime = 1000/FPS;
        int frameCount = 0;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            // try locking canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanelRacing_Hard.update();
                    this.gamePanelRacing_Hard.draw(canvas);
                }
            } catch (Exception e) {
            }

            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);          // make the system wait for "waitTime"
            } catch (Exception e){}

            totalTime += System.nanoTime() - startTime;
            frameCount ++;
            if (frameCount == FPS) {
                averageFPS = 1000/((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }

    public void setRunning(boolean b){
        running = b;
    }
}

