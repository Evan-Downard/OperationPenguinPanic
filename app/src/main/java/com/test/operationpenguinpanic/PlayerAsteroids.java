package com.test.operationpenguinpanic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

//Evan & Johny
public class PlayerAsteroids extends GameObject{
    private Bitmap sprite;
    private int score;

    //acceleration
    private boolean playing;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean shoot;
    private boolean stopped;
    private long startTime;

    public PlayerAsteroids(Bitmap res) {

        sprite = res;
        x = GamePanelRacing.WIDTH / 2;
        y = GamePanelRacing.HEIGHT - GamePanelRacing.HEIGHT / 6;
        height = sprite.getHeight();
        width = sprite.getWidth();


        startTime = System.nanoTime();

    }
    public void setLeft(boolean b){left = b;}

    public void setRight(boolean c){right = c;}

    public void setUp(boolean b) { up = b;}

    public void setDown(boolean b) { down = b;}

    public void setStop(boolean d){stopped = d;}

    public void setShoot(boolean b) { shoot = b;}

    public void update()
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        //Score counter
        if(elapsed>100)
        {
            score++;
            startTime = System.nanoTime();
        }

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
        Paint paint = new Paint();
        canvas.drawBitmap(sprite, x, y, paint);
    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetScore(){score = 0;}
}

