package com.test.operationpenguinpanic;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Oswald on 4/11/2016.
 */
public class GamePanelRacing_Medium extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;
    public static final int MOVESPEED = 25;

    private MainThread_Medium thread;
    private BackgroundRacing background;
    private PlayerRacing player;
    private OpponentsMedium opponentsMedium;
    private ArrayList<Projectile> asteroids;
    private long raceStartTimer;
    private long asteroidStartTime;
    private long opponentTimer;
    private Random random;
    private int i;


    public GamePanelRacing_Medium(Context context) {
        super(context);

        // add callback to the surfaceholder to intercept events such as touches of the screen
        getHolder().addCallback(this);

        thread = new MainThread_Medium(getHolder(), this);

        random = new Random();

        i = 0;



        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // create new background
        background = new BackgroundRacing(BitmapFactory.decodeResource(getResources(), R.drawable.spacesky));
        // create player's spaceship
        player = new PlayerRacing(BitmapFactory.decodeResource(getResources(), R.drawable.gameship));
        // create opponents
        opponentsMedium = new OpponentsMedium(BitmapFactory.decodeResource(getResources(), R.drawable.enemyship));
        // Initialize asteroids array list
        asteroids = new ArrayList<Projectile>();

//        opponentStartTimer = System.nanoTime();

        // timers for race and asteroids
        raceStartTimer = System.nanoTime();
        asteroidStartTime = System.nanoTime();
        opponentTimer = System.nanoTime();




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
        if (event.getAction() == MotionEvent.ACTION_DOWN){      // the first time the player touched the screen the game begins
            if(!player.getPlaying()){
                player.setPlaying(true);
            }

            if((event.getX()) < (WIDTH / 2)){           // if the player touches the left of the screen the ship moves left
                player.setLeft(true);
            }
            else if ((event.getX()) >= (WIDTH / 2)){    // else it moves right
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
            background.update();
            player.update();
            opponentsMedium.update_y();

            long delay = (System.nanoTime() - opponentTimer) / 100000000;
            if (((opponentsMedium.getX() - player.getX()) < 10) && (delay >= 1)) {
                opponentsMedium.update_xRight();
                opponentTimer = System.nanoTime();
            } else if (((opponentsMedium.getX() - player.getX()) > 10) && (delay >= 1)) {
                opponentsMedium.update_xLeft();
                opponentTimer = System.nanoTime();
            }

            long asteroidElapsed = (System.nanoTime() - asteroidStartTime) / 1000000000;

            if (asteroidElapsed > 1.8) {            // a new asteroid is added every sec

                // adding asteroids to the array list
                // the type of asteroid added depends on the size of the array list

                if ((asteroids.size() % 3) == 0) {
                    asteroids.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.
                            asteroid), random.nextInt(400) + 50, -20, 40, 40, 0, 1));
                } else if ((asteroids.size() % 3) == 1) {
                    asteroids.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid2),
                            (random.nextInt(400) + 50), -20, 65, 65, 0, 1));
                } else {
                    asteroids.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1),
                            (random.nextInt(400) + 50), -20, 60, 60, 0, 1));
                }

                asteroidStartTime = System.nanoTime();
            }

            for (int i = 0; i < asteroids.size(); i++) {
                //update asteroid
                asteroids.get(i).update();

                if (collision(asteroids.get(i), player)) {                // if the player collides with an asteroid game over
                    asteroids.remove(i);
                    player.setPlaying(false);
                    break;
                }

                if (collision(asteroids.get(i), opponentsMedium)){
                    asteroids.remove(i);
                }

                //remove asteroid if it is way off the screen
                if (asteroids.get(i).getY() < -100) {
                    asteroids.remove(i);
                    break;
                }
            }

            long raceTime = (System.nanoTime() - raceStartTimer) / 1000000000;
            if ((opponentsMedium.getY() >= 2000) && (i < 4)) {        // if the opponent's y = 2000 and you haven't passed it 5 times
                opponentsMedium.resetPosition();                      // its position is reset
                i++;
            }

            if (collision(opponentsMedium, player)) {             // if the player collides with an opponent the game is over
                player.setPlaying(false);
            }

            if (raceTime == 70) {                               // the race lasts 70 seconds
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

    @Override
    public void draw (Canvas canvas){

        final float scaleFactorX = getWidth() / (WIDTH * 1.0f);      // scale the width of the game to full screen
        final float scaleFactorY = getHeight() / (HEIGHT * 1.0f);    // scale the height of the game to FS

        if (canvas != null) {
            final int savedState = canvas.save();           // save the state(size) of the image before scale
            canvas.scale(scaleFactorX, scaleFactorY);        // scale the image to fit FS
            background.draw(canvas);
            player.draw(canvas);
            opponentsMedium.draw(canvas);

            for (Projectile m : asteroids) {
                m.draw(canvas);
            }

            canvas.restoreToCount(savedState);              // restore the image after drawing it to original size
            // To prevent image to be scaled out of bond.
        }
    }

  /*  public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Score: " + (player.getPosition()), 10, HEIGHT - HEIGHT + 25, paint);
    }*/
}
