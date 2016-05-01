package com.test.operationpenguinpanic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

//Evan
public class PlayerMarathon extends GameObject{
    private Bitmap sprite;
    private int score;

    //acceleration
    private boolean playing;
    private boolean left;
    private boolean right;
    private boolean stopped;
    private long startTime;


    public PlayerMarathon(Bitmap res) {
        sprite = res;
        x = GamePanelRacing.WIDTH / 2;
        y = GamePanelRacing.HEIGHT - GamePanelRacing.HEIGHT / 6;
        height = sprite.getHeight();
        width = sprite.getWidth();

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

        //Left and right movement
        if(left && x > 0){
            x -=10;
        }
        if(right && x < (MarathonGP.WIDTH-40)){
            x +=10;

        }else{
            x +=0;
        }
    }

    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        canvas.drawBitmap(sprite, x, y, paint);
    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetScore(){score = 0;}
}

