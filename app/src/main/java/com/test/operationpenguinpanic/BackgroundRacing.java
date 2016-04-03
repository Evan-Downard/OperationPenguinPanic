package com.test.operationpenguinpanic;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BackgroundRacing {

    private Bitmap image;
    private int x, y, dy;

    public BackgroundRacing(Bitmap res){

        image = res;
        dy = GamePanelRacing.MOVESPEED;
    }

    public void update(){

        y += dy;

        // if the image is completely off-screen, reset
        if (y > GamePanelRacing.HEIGHT){
            y = 0;
        }
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image, x, y, null);

        // make the scrolling look continuous by drawing a second image to fill the
        // space left by scrolling background
        if (y > 0){
            canvas.drawBitmap(image, x, y - GamePanelRacing.HEIGHT, null);
        }
    }

}
