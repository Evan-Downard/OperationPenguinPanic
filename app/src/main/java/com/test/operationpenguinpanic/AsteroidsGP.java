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
    public static final int WIDTH = 500;
    public static final int HEIGHT = 800;

    public static final int MOVESPEED = -5;


    private long projectileStartTime;
    private AsteroidsThread thread;
    private BackgroundAsteroids bg;
    private Control counterClock;
    private Control clockWise;
    private Control fireControl;
    private Control moveControl;
    private PlayerAsteroids player;
    private ArrayList<Asteroid> asteroids;
    private Random rand = new Random();
    private boolean newGameCreated;

    int shipImages[][] = {{R.drawable.mach1g,R.drawable.mach1blueg,R.drawable.mach1greeng,
            R.drawable.mach1blackg,R.drawable.mach1whiteg,R.drawable.mach1silverg,
            R.drawable.mach1goldg},{R.drawable.mach2g,R.drawable.mach2_blueg,
            R.drawable.mach2_greeng, R.drawable.mach2_blackg, R.drawable.mach2_whiteg,
            R.drawable.mach2_silverg, R.drawable.mach2_goldg},{R.drawable.mach3_redg,
            R.drawable.mach3_blueg, R.drawable.mach3_greeng,R.drawable.mach3_blackg,
            R.drawable.mach3_whiteg, R.drawable.mach3_silverg,R.drawable.mach3_goldg}};

    int ship; int color;

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
        // determining what the players ship is
        ship = MainMenu.sendPlayerShip();
        color = MainMenu.sendPlayerColor();
        //Create background
        bg = new BackgroundAsteroids(BitmapFactory.decodeResource(getResources(), R.drawable.spacex));
        //Create counterclockwise control
        counterClock = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.smallrotate), 15, AsteroidsGP.HEIGHT - 125, 0, 0);
        //Create clockwise control
        clockWise = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.smallrotate), 60, AsteroidsGP.HEIGHT - 75, 0, 0);
        // create move control
        moveControl = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.smallrotate), AsteroidsGP.WIDTH - 120, AsteroidsGP.HEIGHT - 75, 0, 0);
        // create fire control
        fireControl = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.smallrotate), AsteroidsGP.WIDTH - 60, AsteroidsGP.HEIGHT - 125, 0, 0);

        //Create player
        //parameters: (Intial Position x, Initial Position y, size x, size y, num animation frames)
        player = new PlayerAsteroids(BitmapFactory.decodeResource(getResources(), shipImages[ship][color]), screenWidth/8, screenHeight/3, 45, 58, 1);

        asteroids = new ArrayList<Asteroid>();
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

        if (event.getAction() == MotionEvent.ACTION_DOWN){      // the first time the player touched the screen the game begins
            if(!player.getPlaying()){
                player.setPlaying(true);
            }

            if (Math.abs(x - AsteroidsGP.WIDTH + 60) <= 30 && Math.abs(y - AsteroidsGP.HEIGHT - 125) <= 30) {// if the player touches the left of the screen the ship moves left
                player.setShoot(true);
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
        if (player.getPlaying()) {

            bg.update();
            player.update();

            long projectileElapsed = (System.nanoTime() - projectileStartTime) / 1000000;

            if (projectileElapsed > (2000 - player.getScore() / 4)) {
                System.out.println("creating projectile");

                //Projectile Spawning
                if (asteroids.size() == 0) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.
                            asteroid), HEIGHT, -20, 40, 40, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.05) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid),
                            (int) ((-rand.nextDouble() * 100)), (int) (-rand.nextDouble()* 100), 40, 40, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.1 && rand.nextDouble() > 0.05) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1),
                            (int) ((rand.nextDouble() * 100) + screenWidth/4), (int) ((rand.nextDouble() * 100) + screenHeight/2), 60, 60, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.15 && rand.nextDouble() > 0.1) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid2),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 65, 65, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.2 && rand.nextDouble() > 0.15) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid3),
                            (int) (rand.nextDouble() * (screenWidth / 3)), (screenHeight + 50), 60, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.25 && rand.nextDouble() > 0.2) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid4),
                            (int) ((-rand.nextDouble() * 100)), (int) (-rand.nextDouble()* 100), 96, 78, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.3 && rand.nextDouble() > 0.25) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid5),
                            (int) ((rand.nextDouble() * 100) + screenWidth/4), (int) ((rand.nextDouble() * 100) + screenHeight/2), 83, 77, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.35 && rand.nextDouble() > 0.3) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid6),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 53, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.4 && rand.nextDouble() > 0.35) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid7),
                            (int) (rand.nextDouble() * (screenWidth / 3)), (screenHeight + 50), 60, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.45 && rand.nextDouble() > 0.4) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid8),
                            (int) ((-rand.nextDouble() * 100)), (int) (-rand.nextDouble()* 100), 39, 30, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.5 && rand.nextDouble() > 0.45) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid9),
                            (int) ((rand.nextDouble() * 100) + screenWidth/4), (int) ((rand.nextDouble() * 100) + screenHeight/2), 30, 23, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.55 && rand.nextDouble() > 0.5) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid10),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 53, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.6 && rand.nextDouble() > 0.55) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej1),
                            (int) (rand.nextDouble() * (screenWidth / 3)), (screenHeight + 50), 60, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.65 && rand.nextDouble() > 0.6) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej2),
                            (int) ((rand.nextDouble() * 100) + screenWidth/4), (int) ((rand.nextDouble() * 100) + screenHeight/2), 87, 100, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.7 && rand.nextDouble() > 0.65) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej3),
                            (int) ((-rand.nextDouble() * 100)), (int) (-rand.nextDouble()* 100), 39, 30, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.75 && rand.nextDouble() > 0.7) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej4),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 53, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.8 && rand.nextDouble() > 0.75) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej5),
                            (int) (rand.nextDouble() * (screenWidth / 3)), (screenHeight + 50), 60, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.85 && rand.nextDouble() > 0.8) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej6),
                            (int) ((rand.nextDouble() * 100) + screenWidth/4), (int) ((rand.nextDouble() * 100) + screenHeight/2), 87, 87, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.9 && rand.nextDouble() > 0.85) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej7),
                            (int) ((-rand.nextDouble() * 100)), (int) (-rand.nextDouble()* 100), 39, 30, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.95 && rand.nextDouble() > 0.9) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej8),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 53, 51, player.getScore(), 1));
                } else {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1),
                            (int) (rand.nextDouble() * (screenWidth / 3)), (screenHeight + 50), 60, 51, player.getScore(), 1));
                }

                projectileStartTime = System.nanoTime();
            }

            //loop through every asteroid and check collision and remove
            for (int i = 0; i < asteroids.size(); i++) {
                if(asteroids.get(i).getY() > -150 && asteroids.get(i).getY() < screenHeight/4 && asteroids.get(i).getX() > -150) {
                    asteroids.get(i).south();
                }else if(asteroids.get(i).getY() > -150 && asteroids.get(i).getY() > screenHeight/4 && asteroids.get(i).getX() > -150){
                    asteroids.get(i).north();
                }else if(asteroids.get(i).getY() > -150 && asteroids.get(i).getX() < screenWidth/8 && asteroids.get(i).getX() > -150){
                    asteroids.get(i).east();
                }else if(asteroids.get(i).getY() > -150 && asteroids.get(i).getX() > screenWidth/8 && asteroids.get(i).getX() > -150){
                    asteroids.get(i).west();
                }else if(asteroids.get(i).getY() > -150 && asteroids.get(i).getY() > screenHeight/4 && asteroids.get(i).getX() < screenWidth/8 && asteroids.get(i).getX() > -150){
                    asteroids.get(i).nw();
                }else if(asteroids.get(i).getY() > -150 && asteroids.get(i).getY() > screenHeight/4 && asteroids.get(i).getX() < screenWidth/8 && asteroids.get(i).getX() > -150){
                    asteroids.get(i).ne();
                }else if(asteroids.get(i).getY() > -150 && asteroids.get(i).getY() < screenHeight/4 && asteroids.get(i).getX() > screenWidth/8 && asteroids.get(i).getX() > -150){
                    asteroids.get(i).se();
                }else if(asteroids.get(i).getY() > -150 && asteroids.get(i).getY() < screenHeight/4 && asteroids.get(i).getX() < screenWidth/8 && asteroids.get(i).getX() > -150){
                    asteroids.get(i).sw();
                }else{
                    asteroids.get(i).south();
                }
                if (collision(asteroids.get(i), player)) {
                    asteroids.remove(i);
                    //saves score
                    PlayerScore.setScore(player.getScore());
                    player.resetScore();
                    asteroids.clear();
                    player.setX(screenWidth / 8);
                    player.setPlaying(false);
                    break;
                }
                //remove asteroid if it is way off the screen
                if (asteroids.get(i).getY() < -100 || asteroids.get(i).getX() < -100 ||
                        asteroids.get(i).getX() > 1500 || asteroids.get(i).getY() > 2500){
                    asteroids.remove(i);
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
            counterClock.draw(canvas);
            clockWise.draw(canvas);
            fireControl.draw(canvas);
            moveControl.draw(canvas);

            //draw asteroids
            for (Asteroid m : asteroids) {
                m.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void newGame() {
        asteroids.clear();

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