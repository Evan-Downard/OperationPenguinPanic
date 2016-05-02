package com.test.operationpenguinpanic;


import android.graphics.Bitmap;
import android.graphics.Canvas;

//Background controller for asteroids mode
//Code from Evan
public class BackgroundAsteroids {

    private Bitmap image;
    private int x, y, dx;


    public BackgroundAsteroids(Bitmap res)
    {
        image = res;
        dx = MarathonGP.MOVESPEED;
    }

    public void update1()
    {
        y-=dx/4;
        if(y > 1){
            //keep this value equal to the location of the y of the
            //initial draw, so the background scrolls all the way
            y = y-3700;
        }
    }

    public void update2()
    {
        y-= (dx/2);
        if(y > 1){
            y = y-3700;
        }

    }

    public void update3()
    {
        y-= (dx);
        if(y > 1){
            y = y-3700;
        }
    }

    public void draw(Canvas canvas)
    {
        //subract the y width based on the image size, so the image
        //is drawn from the bottom instead of the top. Account for
        //drawing location of y it will be a little more than the
        //size of the image
        canvas.drawBitmap(image, x, y-3700 ,null);
        if(y<0)
        {
            canvas.drawBitmap(image, x, y, null);
        }
    }
}