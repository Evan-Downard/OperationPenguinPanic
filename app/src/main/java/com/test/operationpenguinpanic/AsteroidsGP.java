package com.test.operationpenguinpanic;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
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
    private long bossTimer;
    private int boss =0;
    private AsteroidsThread thread;
    private BackgroundAsteroids bg;
    private BackgroundMarathon l2;
    private BackgroundMarathon l3;
    private Control leftControl;
    private Control rightControl;
    private Control fireControl;
    private Control upControl;
    private Control downControl;
    private PlayerAsteroids player;
    private Bosses seal;
    private Bosses bear;
    private Bosses orca;
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

    int ship; int color; int penguin;

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
        penguin = MainMenu.sendPlayerPen();
        //Create background
        bg = new BackgroundAsteroids(BitmapFactory.decodeResource(getResources(), R.drawable.spacex));
        //1st Star Layer
        l2 = new BackgroundMarathon(BitmapFactory.decodeResource(getResources(), R.drawable.layer2));
        //2nd Star Layer
        l3 = new BackgroundMarathon(BitmapFactory.decodeResource(getResources(), R.drawable.layer3));
        //Create counterclockwise control
        leftControl = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.smallrotate), 15, AsteroidsGP.HEIGHT - AsteroidsGP.HEIGHT / 8, 0, 0);
        //Create right control control
        rightControl = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.smallrotate), 95, AsteroidsGP.HEIGHT - AsteroidsGP.HEIGHT / 8, 0, 0);
        // create move control
        /*upControl = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.smallrotate), 55 , 650, 0, 0);
        // create down control
        downControl = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.smallrotate), 55, 750, 0, 0);
        // create fire control
        fireControl = new Control(BitmapFactory.decodeResource(getResources(),
                R.drawable.smallrotate), 440, AsteroidsGP.HEIGHT - AsteroidsGP.HEIGHT / 8, 0, 0);
*/

        //Create player
        //parameters: (Intial Position x, Initial Position y, size x, size y, num animation frames)
        if(penguin == 7){
            // if player selects super saiyan penguin
            player = new PlayerAsteroids(BitmapFactory.decodeResource(getResources(), R.drawable.ssp_flight));
        }else {
            player = new PlayerAsteroids(BitmapFactory.decodeResource(getResources(), shipImages[ship][color]));
        }

        //Bosses
        seal = new Bosses(BitmapFactory.decodeResource(getResources(), R.drawable.sealyin));
        bear = new Bosses(BitmapFactory.decodeResource(getResources(), R.drawable.polarbear));
        orca = new Bosses(BitmapFactory.decodeResource(getResources(), R.drawable.orcagalaga));

        asteroids = new ArrayList<Asteroid>();
        projectileStartTime = System.nanoTime();
        bossTimer = System.nanoTime();

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

            if ((y > (screenHeight/4)) && (y < (screenHeight - screenHeight / 8))) {
                player.setUp(true);
            }
            else if (((x < screenWidth / 4) && (x > screenWidth / 8))) {    // else it moves right
                player.setRight(true);
            }
            else if ((x < screenWidth / 8) && (x > 0)) {
                player.setLeft(true);
            }
            else if ((y > (screenHeight/4)) && (y < (screenHeight - screenHeight / 8))) {
                player.setUp(true);
            }
            else {
                player.setDown(true);
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {       // when he/she removes his/her finger the ship stops
            player.setRight(false);
            player.setLeft(false);
            player.setDown(false);
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(Canvas canvas) {
        if (player.getPlaying()) {

            bg.update1();
            l2.update2();
            l3.update3();
            player.update();
            long time = (System.nanoTime()-bossTimer)/1000000000;
            if(time >=15){
                orca.update();
                //bear.update();
                //orca.update();
            }

            long projectileElapsed = (System.nanoTime() - projectileStartTime) / 1000000;

            if (projectileElapsed > (2000 - player.getScore() / 4)) {
                System.out.println("creating projectile");
                
                //Projectile Spawning
                if (asteroids.size() == 0) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.
                            asteroid), HEIGHT, -20, 40, 40, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.05) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 40, 40, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.1 && rand.nextDouble() > 0.05) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 60, 60, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.15 && rand.nextDouble() > 0.1) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid2),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 65, 65, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.2 && rand.nextDouble() > 0.15) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid3),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 60, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.25 && rand.nextDouble() > 0.2) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid4),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 96, 78, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.3 && rand.nextDouble() > 0.25) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid5),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 83, 77, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.35 && rand.nextDouble() > 0.3) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid6),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 53, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.4 && rand.nextDouble() > 0.35) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid7),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 60, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.45 && rand.nextDouble() > 0.4) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid8),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 39, 30, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.5 && rand.nextDouble() > 0.45) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid9),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 30, 23, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.55 && rand.nextDouble() > 0.5) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid10),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 53, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.6 && rand.nextDouble() > 0.55) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej1),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 60, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.65 && rand.nextDouble() > 0.6) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej2),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 87, 100, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.7 && rand.nextDouble() > 0.65) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej3),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 39, 30, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.75 && rand.nextDouble() > 0.7) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej4),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 53, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.8 && rand.nextDouble() > 0.75) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej5),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 60, 51, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.85 && rand.nextDouble() > 0.8) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej6),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 87, 87, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.9 && rand.nextDouble() > 0.85) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej7),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 39, 30, player.getScore(), 1));
                } else if (rand.nextDouble() <= 0.95 && rand.nextDouble() > 0.9) {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.spacej8),
                            (int) (rand.nextDouble() * (screenWidth / 3)), -50, 53, 51, player.getScore(), 1));
                } else {
                    asteroids.add(new Asteroid(BitmapFactory.decodeResource(getResources(), R.drawable.asteroid1),
                            (int) (rand.nextDouble() * (screenWidth/3)), -50, 60, 51, player.getScore(), 1));
                }

                projectileStartTime = System.nanoTime();
            }

            //loop through every asteroid and check collision and remove
            //loop through every asteroid and check collision and remove
            for (int i = 0; i < asteroids.size(); i++) {
                //update asteroid
                asteroids.get(i).south();

                //if player and object collide stop playing
                if (collision(asteroids.get(i), player)) {
                    asteroids.remove(i);
                    //saves score
                    boss =0;
                    bossTimer = System.nanoTime();
                    PlayerScore.setScore(player.getScore());
                    player.resetScore();
                    asteroids.clear();
                    player.setX(screenWidth / 8);
                    player.setPlaying(false);

                    break;
                }
                //remove asteroid if it is way off the screen
                if (asteroids.get(i).getY() < -100 || asteroids.get(i).getX() < -100 || asteroids.get(i).getX() > 1500 || asteroids.get(i).getY() > 2500){

                    asteroids.remove(i);
                    break;
                }
            }
        }
    }

    public void clearBoss(Canvas canvas){
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
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
            /*fireControl.draw(canvas);
            upControl.draw(canvas);
            downControl.draw(canvas);
           */ if (player.getPlaying()) {
                long time = (System.nanoTime() - bossTimer) / 1000000000;
                if (time >= 15) {
                    orca.draw(canvas);
                    boss = 1;
                    //bear.draw(canvas);
                    //orca.draw(canvas);
                }
            }

            //draw asteroids
            for (Asteroid m : asteroids) {
                m.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Score: " + (player.getScore() * 3), 10, HEIGHT - HEIGHT + 25, paint);
    }

}