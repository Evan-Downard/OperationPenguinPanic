package com.test.operationpenguinpanic;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    private Bitmap image;
    private int x, y, dx;

    public Background(Bitmap res)
    {
        image = res;
        dx = GamePanel.MOVESPEED;
    }

    public void update()
    {
        y-=dx;
        if(y > 1){
            y = -GamePanel.WIDTH;
        }
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y ,null);
        if(y<0)
        {
            canvas.drawBitmap(image, x, y+GamePanel.WIDTH, null);
        }
    }
}