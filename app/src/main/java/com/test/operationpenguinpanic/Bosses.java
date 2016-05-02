package com.test.operationpenguinpanic;

//Carl did dis
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bosses extends GameObject {
    private Bitmap image;
    private int x, y, dx;
    private AsteroidsGP gp;


    public Bosses(Bitmap res){


        image = res;
        dx = MarathonGP.MOVESPEED;
    }
    public void Sealyin()
    {
        int hp = 10;
        System.out.println("SEALTATTACK");
    }

    public void Bearii()
    {
        int hp = 25;
    }

    public void Orcagalaga()
    {
        int hp = 50;
    }

    public void update()
    {

    }

    public void draw(Canvas canvas)
    {

        canvas.drawBitmap(image,x,y,null);
    }
}


