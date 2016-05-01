package com.test.operationpenguinpanic;

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

/**
 * Created by Oswald on 4/24/2016.
 */
public class GPRacing_Hard extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;
    public static final int MOVESPEED = 25;

    private MainThread_Hard thread;
    private BackgroundMarathon bg;
    private BackgroundMarathon l2;
    private BackgroundMarathon l3;
    private Control leftControl;
    private Control rightControl;
    private PlayerRacing player;
    private OpponentsHard opponentsHard;
    private ArrayList<Projectile> asteroids;
    private long raceStartTimer;
    private double asteroidStartTime;
    private long opponentTimer;
    private Random random;
    private int i;

    int shipImages[][] = {{R.drawable.mach1g,R.drawable.mach1blueg,R.drawable.mach1greeng,
            R.drawable.mach1blackg,R.drawable.mach1whiteg,R.drawable.mach1silverg,
            R.drawable.mach1goldg},{R.drawable.mach2g,R.drawable.mach2_blueg,
            R.drawable.mach2_greeng, R.drawable.mach2_blackg, R.drawable.mach2_whiteg,
            R.drawable.mach2_silverg, R.drawable.mach2_goldg},{R.drawable.mach3_redg,
            R.drawable.mach3_blueg, R.drawable.mach3_greeng,R.drawable.mach3_blackg,
            R.drawable.mach3_whiteg, R.drawable.mach3_silverg,R.drawable.mach3_goldg}};

    int ship; int color;

    //Getting screen size
    DisplayMetrics display = this.getResources().getDisplayMetrics();
    final int screenWidth = display.heightPixels;
    final int screenHeight = display.widthPixels;


    public GPRacing_Hard(Context context) {
        super(context);

        // add callback to the surfaceholder to intercept events such as touches of the screen
        getHolder().addCallback(this);

        thread = new MainThread_Hard(getHolder(), this);

        random = new Random();

        i = 5;



        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // determining what the players ship is
        ship = MainMenu.sendPlayerShip();
        color = MainMenu.sendPlayerColor();
        //Create background
        bg = new BackgroundMarathon(BitmapFactory.decodeResource(getResources(), R.drawable.layer1));
        //1st Star Layer
        l2 = new BackgroundMarathon(BitmapFactory.decodeResource(getResources(), R.drawable.layer2));
        //2nd Star Layer
        l3 = new BackgroundMarathon(BitmapFactory.decodeResource(getResources(), R.drawable.layer3));
        //Create left control
        leftControl = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.arrowleft), (MarathonGP.WIDTH/4)-65, MarathonGP.HEIGHT - 75, 140, 69);
        //Create right control
        rightControl = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.arrowright), (MarathonGP.WIDTH - MarathonGP.WIDTH/4)-65, MarathonGP.HEIGHT - 75, 140, 69);
        // create player's spaceship
        player = new PlayerRacing(BitmapFactory.decodeResource(getResources(), shipImages[ship][color]));
        // create opponents
        opponentsHard = new OpponentsHard(BitmapFactory.decodeResource(getResources(), R.drawable. a_iship));
        // Initialize asteroids array list
        asteroids = new ArrayList<Projectile>();

        // timers for race, asteroids and opponent
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

            if ((Math.abs(opponentsHard.getX() - player.getX()) >= 20) ||
                    (opponentsHard.getY() <= (HEIGHT - HEIGHT / 2))) {
                opponentsHard.update_y();

                if ((opponentsHard.getY() >= (HEIGHT - HEIGHT / 2))
                        && (opponentsHard.getY() < player.getY())) {
                    moveOpponent();
                }
            }

            long raceTime = (System.nanoTime() - raceStartTimer) / 1000000000;
            if ((opponentsHard.getY() >= 2000) && (i > 0)) {        // if the opponent's y = 2000 and you haven't passed it 5 times
                opponentsHard.resetPosition();                      // its position is reset
                i--;
            }

            if (collision(opponentsHard, player)) {             // if the player collides with an opponent the game is over
                PlayerScore.setScore(getPosition());            //saves the player's rank
                i = 5;
                resetGame();
            }

            if (raceTime == 120) {                               // the race lasts 120 seconds
                player.setPlaying(false);
            }

            double asteroidElapsed = (System.nanoTime() - asteroidStartTime) / 1000000000;

            if (asteroidElapsed > 0.6) {            // a new asteroid is added every 1/2 sec

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
                    asteroids.remove(j);
                    i = 5;
                    resetGame();
                }

                //remove asteroid if it is way off the screen
                if (asteroids.get(j).getY() < -25) {
                    asteroids.remove(j);
                    break;
                }
            }
        }
    }

    public boolean collision (GameObject a, GameObject b){

        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;
    }

    public int getPosition() {
        return (i + 1);
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Rank: " + getPosition(), 10, 25, paint);
    }

    public void resetGame() {
        surfaceCreated(getHolder());
        player.setPlaying(false);
    }

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
            leftControl.draw(canvas);
            rightControl.draw(canvas);
            player.draw(canvas);
            opponentsHard.draw(canvas);

            for (Projectile m : asteroids) {
                m.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);              // restore the image after drawing it to original size
            // To prevent image to be scaled out of bond.
        }
    }

    // makes the opponent move in front of the player in order to prevent him from passing
    public void moveOpponent() {
        double delay = (System.nanoTime() - opponentTimer) / 100000000;
        if (((opponentsHard.getX() - player.getX()) < 10) && (delay >= 0.5)) {
            opponentsHard.update_xRight();
            opponentTimer = System.nanoTime();
        } else if (((opponentsHard.getX() - player.getX()) > 10) && (delay >= 0.5)) {
            opponentsHard.update_xLeft();
            opponentTimer = System.nanoTime();
        }
    }

}
