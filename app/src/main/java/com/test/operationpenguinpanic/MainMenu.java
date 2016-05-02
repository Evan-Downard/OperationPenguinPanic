package com.test.operationpenguinpanic;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;
import com.test.operationpenguinpanic.PlayerSave;



public class MainMenu extends AppCompatActivity {
    //Player's score
     PlayerScore playerScore = new PlayerScore();

    // initialization of variables
    public static int i = 0;
    public static int j = 0;
    public static int k = 0;
    public static int p = 0;
    boolean music; boolean sound;

    Context context;
    Toast toast; CharSequence text; CharSequence error;
    int duration = Toast.LENGTH_SHORT;
    // initialization of players save information
    PlayerSave save = new PlayerSave();

    // int array of ship list
    int shipImages[][] = {{R.drawable.mach1,R.drawable.mach1blue,R.drawable.mach1green,
                    R.drawable.mach1black,R.drawable.mach1white,R.drawable.mach1silver,
                    R.drawable.mach1gold},{R.drawable.mach2,R.drawable.mach2_blue,
                    R.drawable.mach2_green, R.drawable.mach2_black, R.drawable.mach2_white,
                    R.drawable.mach2_silver, R.drawable.mach2_gold},{R.drawable.mach3_red,
                    R.drawable.mach3_blue, R.drawable.mach3_green,R.drawable.mach3_black,
                    R.drawable.mach3_white, R.drawable.mach3_silver,R.drawable.mach3_gold}};

    // in array of penguin list
    int penguinImages[] = {R.drawable.normalpenguin,R.drawable.ladypenguin,
            R.drawable.superpenguin,R.drawable.ninjapenguin, R.drawable.cyberpenguin,
            R.drawable.pika_penguin, R.drawable.dootdootpenguin,R.drawable.ssp};

    // int array of cockpits of the ships
    int pitImages[] = {R.drawable.normalpenguin_pit, R.drawable.ladypenguin_pit,
            R.drawable.superpenguin_pit, R.drawable.ninjapenguin_pit, R.drawable.cyberpenguin_pit,
            R.drawable.pika_penguin_pit, R.drawable.dootdootpenguin_pit};

    // for radio buttons
    RadioButton soundY;    RadioButton soundN;
    RadioButton musicY;    RadioButton musicN;

    // for image views
    ImageView ship; ImageView penguin;
    ImageView cockpit; ImageView ssp;
    // for text views
    TextView color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
        // for radio button
        soundY = (RadioButton) options.findViewById(R.id.SoundY);
        soundN = (RadioButton) options.findViewById(R.id.SoundN);
        buttonY = (RadioButton) options.findViewById(R.id.ButtonY);
        buttonN = (RadioButton) options.findViewById(R.id.ButtonN);
        musicY = (RadioButton) options.findViewById(R.id.MusicY);
        musicN = (RadioButton) options.findViewById(R.id.MusicN);
        */

        Button pauseButton = (Button) findViewById(R.id.pause_btn);
        /*pauseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainMenu.this);
                dialog.setTitle("Pause Menu");
                dialog.setContentView(R.layout.pausemenu);
                dialog.show();
            }
        });*/

    }
    //===============================================
    // Menu screens
    //
    // on click sends user to game list screen
    public void sendGame(View view){
        //Intent intent = new Intent(this, DisplayGame.class);
        setContentView(R.layout.game_select);
    }
    // on click sends user to customization page
    public void sendCustom(View view){
        setContentView(R.layout.customization_menu);

        k = save.sendSaveColor();
        j = save.sendSavePenguin();
        i = save.sendSaveShip();
        p = save.sendSavePit();

        ship = (ImageView) findViewById(R.id.changeShip);
        penguin = (ImageView) findViewById(R.id.changePenguin);
        cockpit = (ImageView) findViewById(R.id.changePit);
        ssp = (ImageView) findViewById(R.id.SSP);
        color = (TextView) findViewById(R.id.colorText);

        color.setRotation(90);

        if(j == 7){
            ssp.setImageResource(penguinImages[j]);
        }
        else {
            cockpit.setImageResource(pitImages[p]);
            ship.setImageResource(shipImages[i][k]);
            penguin.setImageResource(penguinImages[j]);
        }

    }
    // on click sends user to options page
    public void sendOption(View view){
        setContentView(R.layout.options_screen);

        music = save.sendSaveMusic();
        sound = save.sendSaveSounds();

        soundY = (RadioButton) findViewById(R.id.SoundY);
        soundN = (RadioButton) findViewById(R.id.SoundN);
        musicY = (RadioButton) findViewById(R.id.MusicY);
        musicN = (RadioButton) findViewById(R.id.MusicN);

        if(music == true){
            musicY.setChecked(true);
        }else{
            musicN.setChecked(true);
        }

        if(sound == true){
            soundY.setChecked(true);
        }else{
            soundN.setChecked(true);
        }
    }
    // on click sends user to scores page
    public void sendScore(View view){
        setContentView(R.layout.score_menu);
    }
    // on click sends user to difficulty screen for racing
    public void sendRacing(View view){
        setContentView(R.layout.difficulty_screen);
    }

    //==========================================
    // Starting games
    //
    // on click starts Racing game on easy
    public void sendDifficulty(View view) {
        setContentView(new GamePanelRacing(this));
    }
    // on click starts Racing game on medium
    public void sendDifficultyM(View view){
        setContentView(new GamePanelRacing_Medium(this));
    }
    // on click starts Racing game on hard mode
    public void sendDifficultyH(View view){
        setContentView(new GPRacing_Hard(this));
    }
    // on click starts Marathon game
    public void sendMarathon(View view){
        setContentView(new MarathonGP(this));
    }
    // on click starts Asteroids game
    public void sendAsteroids(View view){ setContentView(new AsteroidsGP(this)); }

    // on click open up web page
    public void openWeb(View view){
        Uri uri = Uri.parse("http://csprofessionalcarl.github.io/OperationPenguinWeb/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //===========================================
    // for back buttons
    //
    // on click it will return user to main menu screen
    public void sendMain(View view){
        setContentView(R.layout.activity_main_menu);
    }

    //============================================
    // high scores page
    //
    // on click it shows socres for Marathon
    public void sendRacingScores(View view){
        setContentView(R.layout.score_menu);
    }
    // on click it shows scores for Marathon
    public void sendMarathonScores(View view){
        setContentView(R.layout.scores_marathon_menu);
    }
    // on click shows scores for Asteroids
    public void sendAsteroidScores(View view){
        setContentView(R.layout.scores_asteroid_menu);
    }

    //=============================================
    // Customization page
    //
    // on click scrolls through the ships list from the left
    public void sendShipleft(View view){
        ship = (ImageView) findViewById(R.id.changeShip);
        error = "Super Saiyan Penguin is the ship.";
        context = getApplicationContext();
        if(j!=7) {
            if (i == 0) {
                i = 2;
                if (ship != null) {
                    ship.setImageResource(shipImages[i][k]);
                }
            } else {
                i = i - 1;
                if (ship != null) {
                    ship.setImageResource(shipImages[i][k]);
                }
            }
        }else{
           // for displaying error
            toast = Toast.makeText(context, error, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
    // on click scrolls through the ships list from the right
    public void sendShipRight(View view){
        ship = (ImageView) findViewById(R.id.changeShip);
        error = "Super Saiyan Penguin is the ship.";
        context = getApplicationContext();
        if(j!=7) {
            if (i == 2) {
                i = 0;
                if (ship != null) {
                    ship.setImageResource(shipImages[i][k]);
                }
            } else {
                i = i + 1;
                if (ship != null) {
                    ship.setImageResource(shipImages[i][k]);
                }
            }
        }else{
            // for displaying error
            toast = Toast.makeText(context, error, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
    // on click scrolls through the penguin list from the left
    public void sendPenguinLeft(View view){
        ship = (ImageView) findViewById(R.id.changeShip);
        penguin = (ImageView) findViewById(R.id.changePenguin);
        cockpit = (ImageView) findViewById(R.id.changePit);
        ssp = (ImageView) findViewById(R.id.SSP);

        if(j==0){
            j = 7;
            p = 6;
                if(ssp != null){
                    penguin.setImageResource(android.R.color.transparent);
                    ship.setImageResource(android.R.color.transparent);
                    cockpit.setImageResource(android.R.color.transparent);
                    ssp.setImageResource(penguinImages[j]);
                }
        }else if(j==7){
            j = j -1;
            if(ssp != null){
                    ssp.setImageResource(android.R.color.transparent);
                    ship.setImageResource(shipImages[i][k]);
                    cockpit.setImageResource(pitImages[p]);
                    penguin.setImageResource(penguinImages[j]);
            }
        }else{
            j = j -1;
            p = p - 1;
            if(penguin != null){
                penguin.setImageResource(penguinImages[j]);
                cockpit.setImageResource(pitImages[p]);
            }
        }
    }
    // on click scrolls through the penguin list from the right
    public void sendPenguinRight(View view) {
        ship = (ImageView) findViewById(R.id.changeShip);
        penguin = (ImageView) findViewById(R.id.changePenguin);
        cockpit = (ImageView) findViewById(R.id.changePit);
        ssp = (ImageView) findViewById(R.id.SSP);

        if (j == 7) {
            j = 0;
            p = 0;
            if (ssp != null) {
                ssp.setImageResource(android.R.color.transparent);
                penguin.setImageResource(penguinImages[j]);
                ship.setImageResource(shipImages[i][k]);
                cockpit.setImageResource(pitImages[p]);
            }
        } else if (j == 6) {
            j = j + 1;
            if (ssp != null) {
                ssp.setImageResource(penguinImages[j]);
                penguin.setImageResource(android.R.color.transparent);
                ship.setImageResource(android.R.color.transparent);
                cockpit.setImageResource(android.R.color.transparent);
            }
        } else {
            j = j + 1;
            p = p + 1;
            if (penguin != null) {
                penguin.setImageResource(penguinImages[j]);
                cockpit.setImageResource(pitImages[p]);
            }
        }
    }

    // on click scrolls through the colors for that ship down the list
    public void sendColorDown(View view) {
        ship = (ImageView) findViewById(R.id.changeShip);
        error = "Super Saiyan Penguin does not have color options.";
        context = getApplicationContext();
        if (j != 7) {
            if (k == 0) {
                k = 6;
                if (ship != null) {
                    ship.setImageResource(shipImages[i][k]);
                }
            } else {
                k = k - 1;
                if (ship != null) {
                    ship.setImageResource(shipImages[i][k]);
                }
            }
        }else{
            // display error
            toast = Toast.makeText(context, error, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
    // on click scrolls through the colors for that ship up the list
    public void sendColorUp(View veiw) {
        ship = (ImageView) findViewById(R.id.changeShip);
        error = "Super Saiyan Penguin does not have color options.";
        context = getApplicationContext();
        if (j != 7) {
            if (k == 6) {
                k = 0;
                if (ship != null) {
                    ship.setImageResource(shipImages[i][k]);
                }
            } else {
                k = k + 1;
                if (ship != null) {
                    ship.setImageResource(shipImages[i][k]);
                }
            }
        }else{
            // displays error
            toast = Toast.makeText(context, error, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
    //================================================
    // Saving player information
    //
    // on clicking SAVE in customization menu saves the type of ship && penguin
    public void sendSave(View view){
        ship = (ImageView) findViewById(R.id.changeShip);
        penguin = (ImageView) findViewById(R.id.changePenguin);
        text = "The selected layout has been saved successfully.";
        error = "An error has occured while saving.";
        context = getApplicationContext();

        if(ship != null && penguin != null){
            save.getSaveShip(i);
            save.getSaveColor(k);
            save.getSavePenguin(j);
            save.getSavePit(p);
            playerScore.setPenguin(j);  //sets the saved penguin for player's penguin for score
            // displays that system has saved
            toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }else{
            // displays error with saving
            toast = Toast.makeText(context, error, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }

    }
    // on clicking SAVE in the options menu saves the players information of the options
    public void sendSaveOptions(View view){
        soundY = (RadioButton) findViewById(R.id.SoundY);
        soundN = (RadioButton) findViewById(R.id.SoundN);
        musicY = (RadioButton) findViewById(R.id.MusicY);
        musicN = (RadioButton) findViewById(R.id.MusicN);
        text = "The options selected has successfully been saved.";
        context = getApplicationContext();

        if(soundY.isChecked() == true && soundN.isChecked() == false){
            save.getSaveSounds(true);
            // display the system had saved
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }else if(soundN.isChecked() == true && soundY.isChecked() == false){
            save.getSaveSounds(false);
            // display the system has saved
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        if(musicY.isChecked() == true && musicN.isChecked() == false){
            save.getSaveMusic(true);
            // display the system has saved
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }else if(musicN.isChecked() == true && musicY.isChecked() == false){
            save.getSaveMusic(false);
            // display the system has saved
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
    //===========================================================
    //Sends player save information of ship
    //
    // sends the location for ship
    public static int sendPlayerShip(){
        return i;
    }
    // sends the location for color of ship
    public static int sendPlayerColor(){
        return k;
    }
}