package com.test.operationpenguinpanic;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class PlayerAsteroids extends GameObject{
    private Bitmap spritesheet;
    private int score;
    private Context context;

    //acceleration
    private double dxa;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean stopped;

    private boolean playing;
    private Animation animation = new Animation();
    private long startTime;


    public PlayerAsteroids(Bitmap res, int x, int y, int w, int h, int numFrames) {

        super.x = x;
        super.y = y;
        dx = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();

    }
    public void setLeft(boolean b){left = b;}

    public void setRight(boolean c){right = c;}

    public void setUp(boolean b) { up = b;}

    public void setDown(boolean b) { down = b;}

    public void setStop(boolean d){stopped = d;}

    public void update()
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        //Score counter
        if(elapsed>100)
        {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        //Left and right movement
        if(left){
            x -= 8;
            y += 0;

            if (x < -100){                      // if the ship goes off screen to the left it spawns to the right of the screen
                x = AsteroidsGP.WIDTH + 60;
            }
        }

        else if(right){
            x += 8;
            y += 0;

            if (x > AsteroidsGP.WIDTH + 100){   // if the ship goes off screen to the left it spawns to the right of the screen
                x = -60;
            }
        }

        else if (up){
            y -= 8;
            x += 0;

            if (y < -100){                      // if the ship goes off screen to the left it spawns to the right of the screen
                y = AsteroidsGP.HEIGHT + 60;
            }
        }

        else if(down){
            y += 8;
            x += 0;

            if (y > AsteroidsGP.HEIGHT + 100){   // if the ship goes off screen to the left it spawns to the right of the screen
                y = -60;
            }
        }

        else {
            x += 0;
            y += 0;
        }
    }

    public void draw(Canvas canvas)
    {

        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDXA(){dxa = 0;}
    public void resetScore(){score = 0;}
}

