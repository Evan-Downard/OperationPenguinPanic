package com.test.operationpenguinpanic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

//Evan
public class PlayerMarathon extends GameObject{
    private Bitmap spritesheet;
    private int score;
    private Context context;

    //acceleration
    private boolean left;
    private boolean right;
    private boolean stopped;

    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;


    public PlayerMarathon(Bitmap res, int x, int y, int w, int h, int numFrames) {

        super.x = x;
        super.y = y;
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
        //Score counter
        if(elapsed>100)
        {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        //Left and right movement
        if(left && x > 0){
            x -=8;
        }
        if(right && x < (MarathonGP.WIDTH-40)){
            x +=8;

        }else{
            x +=0;
        }
    }

    public void draw(Canvas canvas)
    {

        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetScore(){score = 0;}
}

