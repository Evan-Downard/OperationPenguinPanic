package com.test.operationpenguinpanic;


import android.graphics.Bitmap;
import android.graphics.Canvas;

//Background controller for asteroids mode
//Code from Evan
public class BackgroundAsteroids {

    private Bitmap image;
    private int x, y;

    public BackgroundAsteroids(Bitmap res)
    {
        image = res;
        x = 0;
        y = 0;

    }

    public void update()
    {
        //temp
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);
    }
}