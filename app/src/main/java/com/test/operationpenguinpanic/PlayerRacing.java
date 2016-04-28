package com.test.operationpenguinpanic;

/*** Edited by Oswald ***/

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class PlayerRacing extends GameObject {

    private Bitmap sprite;
    private boolean playing;
    private boolean left;
    private boolean right;


    public PlayerRacing(Bitmap res){
        sprite = res;
        x = GamePanelRacing.WIDTH / 2;
        y = GamePanelRacing.HEIGHT - GamePanelRacing.HEIGHT / 6;
        height = sprite.getHeight();
        width = sprite.getWidth();
    }

    public  void setLeft(boolean b){left = b;}

    public void setRight(boolean b){right = b;}



    public void update(){

        if (left && (x > 0)){
            dx = -1;
        }
        else if (right && (x < GamePanelRacing.WIDTH - width)){
            dx = 1;
        }

       // update position
        x += dx * 15;
        dx = 0;
    }

    public void draw (Canvas canvas){
        Paint paint = new Paint();

        canvas.drawBitmap(sprite, x, y, paint);
    }

    public boolean getPlaying(){
        return playing;
    }

    public void setPlaying(boolean b){
        playing = b;
    }

}
