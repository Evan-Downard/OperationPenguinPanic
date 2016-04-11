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

//Asteroids mode controller
public class AsteroidsGP extends SurfaceView implements SurfaceHolder.Callback {

    //Assuming portrait layout
    public static final int WIDTH = 480;
    public static final int HEIGHT = 856;

    public static final int MOVESPEED = -5;


    private long projectileStartTime;
    private AsteroidsThread thread;
    private BackgroundAsteroids bg;
    private Control leftControl;
    private Control rightControl;
    private PlayerAsteroids player;
    private ArrayList<Projectile> projectiles;
    private Random rand = new Random();
    private boolean newGameCreated;


    //Difficulty setting
    private int progressDenom = 20;


    //Getting screen size
    DisplayMetrics display = this.getResources().getDisplayMetrics();
    final int screenWidth = display.heightPixels;
    final int screenHeight = display.widthPixels;

    public AsteroidsGP(Context context) {
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

        //Create background
        bg = new BackgroundAsteroids(BitmapFactory.decodeResource(getResources(), R.drawable.spacesky));
        //Create left control
        leftControl = new Control(BitmapFactory.decodeResource(getResources(), R.drawable.arrowleft), (AsteroidsGP.WIDTH/4)-65, AsteroidsGP.HEIGHT - 75, 140, 69);
        //Create right control
        rightControl = new Control(BitmapFactory.decodeResource(getResources(), R.drawable.arrowright), (AsteroidsGP.WIDTH - AsteroidsGP.WIDTH/4)-65, AsteroidsGP.HEIGHT - 75, 140, 69);
        //Create player
        player = new PlayerAsteroids(BitmapFactory.decodeResource(getResources(), R.drawable.gameship), screenWidth / 8, AsteroidsGP.HEIGHT - 150, 45, 58, 1);


        projectiles = new ArrayList<Projectile>();
        projectileStartTime = System.nanoTime();

        thread = new AsteroidsThread(getHolder(), this);
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

            bg.update();
            player.update();

            long projectileElapsed = (System.nanoTime() - projectileStartTime) / 1000000;

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

                if (collision(projectiles.get(i), player)) {
                    projectiles.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //remove asteroid if it is way off the screen
                if (projectiles.get(i).getY() < -100) {
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
        canvas.drawText("Score: " + (player.getScore() * 3), 10, HEIGHT - HEIGHT + 25, paint);
    }

}