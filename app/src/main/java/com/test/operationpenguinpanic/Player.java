package com.test.operationpenguinpanic;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends GameObject{
    private Bitmap spritesheet;
    private int score;

    //acceleration
    private double dxa;
    private boolean left;
    private boolean right;
    private boolean stopped;

    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;

    public Player(Bitmap res, int w, int h, int numFrames) {

        x = GamePanel.WIDTH / 2;
        y = GamePanel.HEIGHT - 100;
        dx = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();

    }
    public void setLeft(boolean b){left = b;}
    public void setRight(boolean c){right = c;}
    public void setStop(boolean d){stopped = d;}

    public void update()
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100)
        {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        if(left && x > 0){
            x -=4;
        }
        if(right && x < GamePanel.WIDTH){
            x +=4;

        }else{
            x +=0;
        }

        if(dx>14)dx = 14;
        if(dx<-14)dx = -14;
        //if(x > 0 && x < GamePanel.WIDTH/2){
        //x += dx;
        //}
        //x += 0;
    }

    public void draw(Canvas canvas)
    {

        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDXA(){dxa = 0;}
    public void resetScore(){score = 0;}
}

