package com.test.operationpenguinpanic;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


public class GamePanelRacing extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;
    public static final int MOVESPEED = 25;

    private MainThreadRacing thread;
    private BackgroundRacing background;
    private PlayerRacing player;
    private OpponentsEasy opponentsEasy;
    private long opponentStartTimer;
    private long raceStartTimer;
    private Random random;
    private int i;


    public GamePanelRacing(Context context) {
        super(context);

        // add callback to the surfaceholder to intercept events such as touches of the screen
        getHolder().addCallback(this);

       thread = new MainThreadRacing(getHolder(), this);

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
        player = new PlayerRacing(BitmapFactory.decodeResource(getResources(), R.drawable.enemyship));
        // create opponents
        opponentsEasy = new OpponentsEasy(BitmapFactory.decodeResource(getResources(), R.drawable.enemyship));

        opponentStartTimer = System.nanoTime();
        raceStartTimer = System.nanoTime();



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
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if(!player.getPlaying()){
                player.setPlaying(true);
            }

            if((event.getX()) < (WIDTH / 2)){
                player.setLeft(true);
            }
            else if ((event.getX()) >= (WIDTH / 2)){
                player.setRight(true);
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setRight(false);
            player.setLeft(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {

        if(player.getPlaying()) {
            background.update();
            player.update();
            opponentsEasy.update();

            if(collision(opponentsEasy,player)){
                player.setPlaying(false);

            }

            long elapsed = (System.nanoTime() - opponentStartTimer) / 1000000000;
            long raceTime = (System.nanoTime() - raceStartTimer) / 1000000000;
            if((elapsed >= 10) &&(i < 5)){
                opponentsEasy.setY(-1000);
                opponentsEasy.setX(random.nextInt(GamePanelRacing.WIDTH * 2) / 2);
                i++;
                opponentStartTimer = System.nanoTime();
            }

            if(raceTime == 70){
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
            canvas.scale(scaleFactorX,scaleFactorY);        // scale the image to fit FS
            background.draw(canvas);
            player.draw(canvas);
            opponentsEasy.draw(canvas);
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
