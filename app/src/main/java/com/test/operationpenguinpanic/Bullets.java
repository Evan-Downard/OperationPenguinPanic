package com.test.operationpenguinpanic;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Oswald on 5/4/2016.
 */
public class Bullets extends GameObject{
    private int speed;
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public Bullets(Bitmap res, int x, int y, int w, int h, int numFrames)
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;

        speed = 30;


        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length;i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100 - speed);

    }
    //Angles that the bullets spawn
    public void update(){
        y-=speed;
        animation.update();
    }

    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch(Exception e){}
    }

    @Override
    public int getWidth()
    {
        return width;
    }
}

