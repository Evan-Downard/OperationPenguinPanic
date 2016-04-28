package com.test.operationpenguinpanic;

/*** Edited by Oswald ***/

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class OpponentsEasy extends GameObject {

    private Bitmap sprite;
    Random random = new Random();

    public OpponentsEasy(Bitmap image){

        sprite = image;
        x = GamePanelRacing.WIDTH / (random.nextInt(5) + 1);
        y = -1500;
        height = sprite.getHeight();
        width = sprite.getWidth();
        acceleration = 10.0f ;


    }

    public void resetPosition() {
        setY(-1500);
        setX(random.nextInt(400) + 50);
    }

    public void update (){
        if((y < (GamePanelRacing.HEIGHT/4)) || (y > GamePanelRacing.HEIGHT)) {
            y += acceleration;
        }
        y += acceleration - 2;
    }

    public void draw (Canvas canvas){
        Paint paint = new Paint();

        canvas.drawBitmap(sprite, x, y, paint);
    }

}
