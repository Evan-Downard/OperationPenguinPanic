package com.test.operationpenguinpanic;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Control extends GameObject{

    private Bitmap image;

    public Control(Bitmap res, int x, int y,  int w, int h){

        super.x = x;
        super.y = y;
        width = w;
        height = h;

        image = res;

    }
    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(image, x, y, null);
        }catch(Exception e){}
    }

}
