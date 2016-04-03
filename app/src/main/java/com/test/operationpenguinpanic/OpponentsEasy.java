package com.test.operationpenguinpanic;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class OpponentsEasy extends GameObject {

    private Bitmap sprite;

    public OpponentsEasy(Bitmap image){
        Random random = new Random();
        sprite = image;
        x = GamePanelRacing.WIDTH / (random.nextInt(5) + 1);
        y = -1000;
        height = sprite.getHeight();
        width = sprite.getWidth();
        acceleration = 15.0f;


    }

    public void update (){
        y += acceleration;
    }

    public void draw (Canvas canvas){
        Paint paint = new Paint();

        canvas.drawBitmap(sprite, x, y, paint);
    }

}
