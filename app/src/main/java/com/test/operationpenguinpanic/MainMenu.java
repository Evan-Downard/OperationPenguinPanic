package com.test.operationpenguinpanic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.RadioButton;



public class MainMenu extends AppCompatActivity {
    // initialization of variables
    int i = 0; int j = 0;
    // int array of ship list
    int shipImages[] = {R.drawable.mach1,R.drawable.mach1blue,R.drawable.mach1green,
            R.drawable.mach1back,R.drawable.mach1silver,R.drawable.mach1gold};
    // in array of penguin list
    int penguinImages[] = {R.drawable.normalpenguin,R.drawable.ladypenguin,
            R.drawable.mechanicpenguin,
            R.drawable.superpenguin,R.drawable.ninjapenguin,
            R.drawable.dootdootpenguin,R.drawable.ssp};
/*
    // for radio buttons
    RadioButton soundY;    RadioButton soundN;
    RadioButton buttonY;   RadioButton buttonN;
    RadioButton musicY;    RadioButton musicN;
*/
    // for image views
    ImageView ship; ImageView penguin;
    //public TextView color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // inflating the xml pages
        //View custom = getLayoutInflater().inflate(R.layout.customization_menu, null, false);
        //View options = getLayoutInflater().inflate(R.layout.options_screen, null, false);
        // setting ids to variables
        //color = (TextView) custom.findViewById (R.id.colorText);
        /*
        // for radio button
        soundY = (RadioButton) options.findViewById(R.id.SoundY);
        soundN = (RadioButton) options.findViewById(R.id.SoundN);
        buttonY = (RadioButton) options.findViewById(R.id.ButtonY);
        buttonN = (RadioButton) options.findViewById(R.id.ButtonN);
        musicY = (RadioButton) options.findViewById(R.id.MusicY);
        musicN = (RadioButton) options.findViewById(R.id.MusicN);
        */

    }
    //===============================================
    // Menu screens
    //
    // on click sneds user to game list screen
    public void sendGame(View view){
        //Intent intent = new Intent(this, DisplayGame.class);
        setContentView(R.layout.game_select);
    }
    // on click sends user to customization page
    public void sendCustom(View view){
        setContentView(R.layout.customization_menu);
    }
    // on click sends user to options page
    public void sendOption(View view){
        setContentView(R.layout.options_screen);
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
    // on click starts Racing game
    public void sendDifficulty(View view) {
        setContentView(new GamePanelRacing(this));
    }
    // on click starts Marathon game
    public void sendMarathon(View view){
        setContentView(new GamePanel(this));
    }
    // on click starts Asteroids game
    public void sendAsteroids(View view){
        setContentView(new GamePanel(this));
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
        if(i==0) {
            i = 5;
            if (ship != null) {
                ship.setImageResource(shipImages[i]);
            }
        }else{
            i = i-1;
            if (ship != null) {
                ship.setImageResource(shipImages[i]);
            }
        }
    }
    // on click scrolls through the ships list from the right
    public void sendShipRight(View view){
        ship = (ImageView) findViewById(R.id.changeShip);
        if(i==5){
            i = 0;
            if (ship != null) {
                ship.setImageResource(shipImages[i]);
            }
        }else{
            i = i +1;
            if (ship != null) {
                ship.setImageResource(shipImages[i]);
            }
        }
    }
    // on click crolls through the penguin list from the left
    public void sendPenguinLeft(View view){
        penguin = (ImageView) findViewById(R.id.changePenguin);
        if(j==0){
            j = 6;
            if(penguin != null){
                penguin.setImageResource(penguinImages[j]);
            }
        }else{
            j = j -1;
            if(penguin != null){
                penguin.setImageResource(penguinImages[j]);
            }
        }
    }
    // on click scrolls through the penguin list from the right
    public void sendPenguinRight(View view){
        penguin = (ImageView) findViewById(R.id.changePenguin);
        if(j == 6){
            j = 0;
            if(penguin != null){
                penguin.setImageResource(penguinImages[j]);
            }
        }else{
            j = j +1;
            if(penguin != null){
                penguin.setImageResource(penguinImages[j]);
            }
        }
    }

}

