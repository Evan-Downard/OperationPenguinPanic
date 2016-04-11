package com.test.operationpenguinpanic;


import android.graphics.Bitmap;
import android.graphics.Canvas;

//Background controller for marathon mode
//Code from Evan
public class BackgroundMarathon {

    private Bitmap image;
    private int x, y, dx;

    public BackgroundMarathon(Bitmap res)
    {
        image = res;
        dx = MarathonGP.MOVESPEED;
    }

    public void update()
    {
        y-=dx;
        if(y > 1){
            y = -MarathonGP.WIDTH;
        }
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y ,null);
        if(y<0)
        {
            canvas.drawBitmap(image, x, y+MarathonGP.WIDTH, null);
        }
    }
}