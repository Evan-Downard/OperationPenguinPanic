package com.test.operationpenguinpanic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

    public static final int WIDTH = 480;
    public static final int HEIGHT = 856;
    public static final int MOVESPEED = -5;

    private long asteroidStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Asteroid> asteroids;
    private Random rand = new Random();
    private boolean newGameCreated;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;



    //Difficulty setting
    private int progressDenom = 20;

    public GamePanel(Context context)
    {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);


        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        //Create background
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.spacesky));
        //Create left control

        //Create right control

        //Create player
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.spaceship), 40, 40, 1);

        asteroids = new ArrayList<Asteroid>();
        asteroidStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
        //start the game loop
        thread.setRunning(true);
        thread.start();

    }
    //When the screen is touched
    //Controls
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            System.out.println(x + " is the x position");
            //System.out.println(y + " is the y position");
            if(x <= GamePanel.WIDTH) {
                if (!player.getPlaying()) {
                    player.setPlaying(true);
                    player.setLeft(true);
                    player.setRight(false);
                    player.setStop(false);
                } else {
                    player.setLeft(true);
                    player.setRight(false);
                    player.setStop(false);
                }
                //System.out.print("Left! ");
            }
            if(x > GamePanel.WIDTH){
                if(!player.getPlaying()){
                    player.setPlaying(true);
                    player.setLeft(false);
                    player.setRight(true);
                    player.setStop(false);
                }
                else {
                    player.setLeft(false);
                    player.setRight(true);
                    player.setStop(false);
                }
                //System.out.print("Right! ");
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            player.setLeft(false);
            player.setRight(false);
            player.setStop(true);
            System.out.println("NO_HOLD!");
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update()
    {
        if(player.getPlaying()) {

            bg.update();
            player.update();

            long asteroidElapsed = (System.nanoTime()-asteroidStartTime)/1000000;

            if(asteroidElapsed > (2000 - player.getScore()/4)){
                System.out.println("creating asteroid");

                //Projectile Spawning
                //first asteroid goes down the center of the screen forcing the player to move
                if(asteroids.size()==0) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.
                            asteroid), HEIGHT, -20, 40, 40, player.getScore(), 1));
                }else if(rand.nextDouble() <= 0.25){
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid),
                            (int) (rand.nextDouble() * (WIDTH)), -20 , 40, 40, player.getScore(), 1));
                }else if(rand.nextDouble() < 0.5 && rand.nextDouble() > 0.25){
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid8),
                            (int) (rand.nextDouble() * (WIDTH)), -20 , 90, 70, player.getScore(), 1));
                }else if(rand.nextDouble() < 0.75 && rand.nextDouble() >= 0.5 ){
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid9),
                            (int) (rand.nextDouble() * (WIDTH)), -20 , 85, 62, player.getScore(), 1));
                }else if(rand.nextDouble() >= 0.75){
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid10),
                            (int) (rand.nextDouble() * (WIDTH)), -20 , 86, 62, player.getScore(), 1));
                }else{
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1),
                            (int) (rand.nextDouble() * (WIDTH)), -20 , 40, 40, player.getScore(), 1));
                }

                asteroidStartTime = System.nanoTime();
            }
            //loop through every asteroid and check collision and remove
            for(int i = 0; i<asteroids.size();i++)
            {
                //update asteroid
                asteroids.get(i).update();

                if(collision(asteroids.get(i), player))
                {
                    asteroids.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //remove asteroid if it is way off the screen
                if(asteroids.get(i).getY()<-100)
                {
                    asteroids.remove(i);
                    break;
                }
            }
        }
    }

    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);

            //draw asteroids
            for(Asteroid m: asteroids)
            {
                m.draw(canvas);
            }


            canvas.restoreToCount(savedState);
        }
    }
    public void newGame()
    {
        asteroids.clear();

        //player.resetDY();
        player.resetScore();
        player.setX(WIDTH);

        //create initial borders



        newGameCreated = true;


    }

}