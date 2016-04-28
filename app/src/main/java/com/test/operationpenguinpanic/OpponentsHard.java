package com.test.operationpenguinpanic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Oswald on 4/24/2016.
 */
public class OpponentsHard extends GameObject{
    private Bitmap sprite;
    Random random = new Random();
    private boolean left;
    private boolean right;

    public OpponentsHard(Bitmap image){

        sprite = image;
        x = GPRacing_Hard.WIDTH / (random.nextInt(5) + 1);
        y = -1500;
        height = sprite.getHeight();
        width = sprite.getWidth();
        acceleration = 10.0f;


    }

    public  void setLeft(boolean b){left = b;}

    public void setRight(boolean b){right = b;}


    public void resetPosition() {
        setY(-1500);
        setX(random.nextInt(400) + 50);
    }

    public void update_xLeft () {
        dx = -1;
        // update position
        x += dx * 10;
    }

    public void update_xRight () {
        dx = 1;
        // update position
        x += dx * 10;
    }

    public void update_y (){
        if((y < (GPRacing_Hard.HEIGHT/4)) || (y > GamePanelRacing.HEIGHT)) {
            y += acceleration;
        }
        y += acceleration - 5;
    }

    public void draw (Canvas canvas){
        Paint paint = new Paint();

        canvas.drawBitmap(sprite, x, y, paint);
    }

}

