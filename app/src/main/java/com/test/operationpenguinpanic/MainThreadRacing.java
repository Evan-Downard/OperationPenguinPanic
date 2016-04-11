package com.test.operationpenguinpanic;

/*** Edited by Oswald ***/

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThreadRacing extends Thread{
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanelRacing gamePanelRacing;
    private boolean running;
    public static Canvas canvas;

    public MainThreadRacing(SurfaceHolder surfaceHolder, GamePanelRacing gamePanelRacing) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanelRacing = gamePanelRacing;
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
                    this.gamePanelRacing.update();
                    this.gamePanelRacing.draw(canvas);
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
