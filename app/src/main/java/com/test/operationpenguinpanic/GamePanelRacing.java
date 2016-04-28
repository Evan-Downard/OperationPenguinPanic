package com.test.operationpenguinpanic;

/*** Edited by Oswald ***/

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;


public class GamePanelRacing extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;
    public static final int MOVESPEED = 25;

    private MainThreadRacing thread;
    private BackgroundMarathon bg;
    private BackgroundMarathon l2;
    private BackgroundMarathon l3;
    private PlayerRacing player;
    private OpponentsEasy opponentsEasy;
    private ArrayList<Projectile> asteroids;
    private long raceStartTimer;
    private long asteroidStartTime;
    private Random random;
    private int i;

    //Getting screen size
    DisplayMetrics display = this.getResources().getDisplayMetrics();
    final int screenWidth = display.heightPixels;
    final int screenHeight = display.widthPixels;

    public GamePanelRacing(Context context) {
        super(context);

        // add callback to the surfaceholder to intercept events such as touches of the screen
        getHolder().addCallback(this);

        thread = new MainThreadRacing(getHolder(), this);

        random = new Random();

        i = 5;



        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //Create background
        bg = new BackgroundMarathon(BitmapFactory.decodeResource(getResources(), R.drawable.layer1));
        //1st Star Layer
        l2 = new BackgroundMarathon(BitmapFactory.decodeResource(getResources(), R.drawable.layer2));
        //2nd Star Layer
        l3 = new BackgroundMarathon(BitmapFactory.decodeResource(getResources(), R.drawable.layer3));
        // create player's spaceship
        player = new PlayerRacing(BitmapFactory.decodeResource(getResources(), R.drawable.gameship));
        // create opponents
        opponentsEasy = new OpponentsEasy(BitmapFactory.decodeResource(getResources(), R.drawable.a_iship));
        // Initialize asteroids array list
        asteroids = new ArrayList<Projectile>();

        opponentsEasy.acceleration *=
//        opponentStartTimer = System.nanoTime();

        // timers for race and asteroids
        raceStartTimer = System.nanoTime();
        asteroidStartTime = System.nanoTime();




        // start game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && (counter < 1000)) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch(InterruptedException e){e.printStackTrace();}
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();

        if (event.getAction() == MotionEvent.ACTION_DOWN){      // the first time the player touched the screen the game begins
            if(!player.getPlaying()){
                player.setPlaying(true);
            }

            if (x <= (screenWidth/4) - (screenWidth/16)) {// if the player touches the left of the screen the ship moves left
                player.setLeft(true);
            }
            else if (x >= (screenWidth/4) + (screenWidth / 6)) {    // else it moves right
                player.setRight(true);
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {       // when he/she removes his/her finger the ship stops
            player.setRight(false);
            player.setLeft(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {

        if (player.getPlaying()) {      // when the player touches the screen we begin updating
            bg.update1();
            l2.update2();
            l3.update3();
            player.update();
            opponentsEasy.update();

            long asteroidElapsed = (System.nanoTime() - asteroidStartTime) / 1000000000;

            if (asteroidElapsed > 1.8) {            // a new asteroid is added every sec

                // adding asteroids to the array list
                // the type of asteroid added depends on the size of the array list

                if ((asteroids.size() % 3) == 0) {
                    asteroids.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.
                            asteroid), random.nextInt(500), -20, 40, 40, 0, 1));
                } else if ((asteroids.size() % 3) == 1) {
                    asteroids.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid2),
                            (random.nextInt(500)), -20, 65, 65, 0, 1));
                } else {
                    asteroids.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1),
                            (random.nextInt(500)), -20, 60, 60, 0, 1));
                }

                asteroidStartTime = System.nanoTime();
            }

            for (int j = 0; j < asteroids.size(); j++) {
                //update asteroid
                asteroids.get(j).update();

                if (collision(asteroids.get(j), player)) {                // if the player collides with an asteroid game over
                    PlayerScore.setScore(getPosition());            //saves the player's rank
                    i = 5;
                    resetGame();
                }

                //remove asteroid if it is way off the screen
                if (asteroids.get(j).getY() < -25) {
                    asteroids.remove(j);
                }
            }

            long raceTime = (System.nanoTime() - raceStartTimer) / 1000000000;
            if ((opponentsEasy.getY() >= 2000) && (i > 0)) {        // if the opponent's y = 2000 and you haven't passed 5 spaceships
                i--;
                opponentsEasy.resetPosition();                      // its position is reset
            }

            if (collision(opponentsEasy, player)) {             // if the player collides with an opponent the game is over
                PlayerScore.setScore(getPosition());            //saves the player's rank
                i = 5;
                resetGame();
            }

            if (raceTime == 120) {                               // the race lasts 120 seconds
                player.setPlaying(false);
            }
        }
    }

    public boolean collision (GameObject a, GameObject b){

        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw (Canvas canvas){

        final float scaleFactorX = getWidth() / (WIDTH * 1.0f);      // scale the width of the game to full screen
        final float scaleFactorY = getHeight() / (HEIGHT * 1.0f);    // scale the height of the game to FS

        if (canvas != null) {
            final int savedState = canvas.save();           // save the state(size) of the image before scale
            canvas.scale(scaleFactorX, scaleFactorY);        // scale the image to fit FS
            bg.draw(canvas);
            l2.draw(canvas);
            l3.draw(canvas);
            player.draw(canvas);
            opponentsEasy.draw(canvas);

            for (Projectile m : asteroids) {
                m.draw(canvas);
            }

            drawText(canvas);

            canvas.restoreToCount(savedState);              // restore the image after drawing it to original size
            // To prevent image to be scaled out of bond.
        }
    }

    public int getPosition() {
        return (i + 1);
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Rank: " + getPosition(), 10, HEIGHT - HEIGHT + 25, paint);
    }

    public void resetGame() {
        surfaceCreated(getHolder());
        player.setPlaying(false);
    }

}
