package com.test.operationpenguinpanic;

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

//Most of this code of from Evan
//This is the main controller for the marathon mode of the game
public class MarathonGP extends SurfaceView implements SurfaceHolder.Callback {

    //Assuming portrait layout
    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;

    public static final int MOVESPEED = -30;

    int shipImages[][] = {{R.drawable.mach1g,R.drawable.mach1blueg,R.drawable.mach1greeng,
            R.drawable.mach1blackg,R.drawable.mach1whiteg,R.drawable.mach1silverg,
            R.drawable.mach1goldg},{R.drawable.mach2g,R.drawable.mach2_blueg,
            R.drawable.mach2_greeng, R.drawable.mach2_blackg, R.drawable.mach2_whiteg,
            R.drawable.mach2_silverg, R.drawable.mach2_goldg},{R.drawable.mach3_redg,
            R.drawable.mach3_blueg, R.drawable.mach3_greeng,R.drawable.mach3_blackg,
            R.drawable.mach3_whiteg, R.drawable.mach3_silverg,R.drawable.mach3_goldg}};

    int ship; int color;

    private long projectileStartTime;
    private MarathonThread thread;
    private BackgroundMarathon bg;
    private BackgroundMarathon l2;
    private BackgroundMarathon l3;
    private Control leftControl;
    private Control rightControl;
    private PlayerMarathon player;
    private ArrayList<Projectile> projectiles;
    private Random rand = new Random();
    private boolean newGameCreated;


    //Difficulty setting
    private int progressDenom = 20;


    //Getting screen size
    DisplayMetrics display = this.getResources().getDisplayMetrics();
    final int screenWidth = display.heightPixels;
    final int screenHeight = display.widthPixels;


    public MarathonGP(Context context) {
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);


        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

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
        leftControl = new Control(BitmapFactory.decodeResource(getResources(), R.drawable.arrowleft), (MarathonGP.WIDTH/4)-65, MarathonGP.HEIGHT - 75, 140, 69);
        //Create right control
        rightControl = new Control(BitmapFactory.decodeResource(getResources(), R.drawable.arrowright), (MarathonGP.WIDTH - MarathonGP.WIDTH/4)-65, MarathonGP.HEIGHT - 75, 140, 69);
        //Create player
        player = new PlayerMarathon(BitmapFactory.decodeResource(getResources(), shipImages[ship][color]), screenWidth / 8, MarathonGP.HEIGHT - 150, 51, 58, 1);


        projectiles = new ArrayList<Projectile>();
        projectileStartTime = System.nanoTime();

        thread = new MarathonThread(getHolder(), this);
        //start the game loop
        thread.setRunning(true);
        thread.start();

    }

    //When the screen is touched
    //Controls
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("tap at: " + x + ", " + y);
            //System.out.println(y + " is the y position");
            if (x <= (screenWidth/4) - (screenWidth/16)) {
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
            if (x >= (screenWidth/4) + (screenWidth / 6)) {
                if (!player.getPlaying()) {
                    player.setPlaying(true);
                    player.setLeft(false);
                    player.setRight(true);
                    player.setStop(false);
                } else {
                    player.setLeft(false);
                    player.setRight(true);
                    player.setStop(false);
                }
                //System.out.print("Right! ");
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setLeft(false);
            player.setRight(false);
            player.setStop(true);
            //System.out.println("NO_HOLD!");
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (player.getPlaying()) {

            bg.update1();
            l2.update2();
            l3.update3();
            player.update();

            long projectileElapsed = (System.nanoTime() - projectileStartTime) / 1000000;
            //Projectile spawns based on player score
            if (projectileElapsed > (2000 - player.getScore() / 4)) {
                System.out.println("creating projectile");

                //Projectile Spawning
                //first asteroid goes down the center of the screen forcing the player to move
                if (projectiles.size() == 0) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.
                            asteroid), HEIGHT, -20, 40, 40, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.1) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 40, 40, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.2 && rand.nextDouble() > 0.1) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid8),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 39, 30, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.3 && rand.nextDouble() > 0.2) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid3),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 60, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.4 && rand.nextDouble() > 0.3) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid2),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 65, 65, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.5 && rand.nextDouble() > 0.4) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.spacej6),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 87, 87, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.6 && rand.nextDouble() > 0.5) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.spacej5),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 114, 121, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.7 && rand.nextDouble() > 0.6) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.spacej4),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 43, 21, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.8 && rand.nextDouble() > 0.7) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.spacej3),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 39, 160, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.9 && rand.nextDouble() > 0.8) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.spacej2),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 87, 100, player.getScore(), 1));
                } else if (rand.nextDouble() > 0.9) {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.spacej1),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 65, 65, player.getScore(), 1));
                } else {
                    projectiles.add(new Projectile(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1),
                            (int) (rand.nextDouble() * (screenWidth / 4)), -20, 60, 60, player.getScore(), 1));
                }

                projectileStartTime = System.nanoTime();
            }
            //loop through every asteroid and check collision and remove
            for (int i = 0; i < projectiles.size(); i++) {
                //update asteroid
                projectiles.get(i).update();

                //if player and object collide stop playing
                if (collision(projectiles.get(i), player)) {
                    projectiles.remove(i);
                    //saves score
                    PlayerScore.setScore(player.getScore());
                    player.resetScore();
                    projectiles.clear();
                    player.setX(screenWidth/8);
                    player.setPlaying(false);


                    break;
                }
                //remove asteroid if it is way off the screen
                if (projectiles.get(i).getY() < -25) {
                    projectiles.remove(i);
                    break;
                }
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            l2.draw(canvas);
            l3.draw(canvas);
            player.draw(canvas);
            leftControl.draw(canvas);
            rightControl.draw(canvas);

            //draw asteroids
            for (Projectile m : projectiles) {
                m.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void newGame() {

        projectiles.clear();

        //player.resetDY();
        player.resetScore();
        player.setX(WIDTH);

        newGameCreated = true;

    }
    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        //canvas.drawText("Tap to start", screenWidth / 8, screenHeight / 4, paint);
        //canvas.drawText("PAUSED: Tap to start", screenWidth / 8, screenHeight / 4, paint);
        canvas.drawText("Score: " + (player.getScore() * 3), 10, HEIGHT - HEIGHT + 25, paint);
    }

}