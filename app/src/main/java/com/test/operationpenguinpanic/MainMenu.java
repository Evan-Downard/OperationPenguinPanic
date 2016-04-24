package com.test.operationpenguinpanic;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import com.test.operationpenguinpanic.PlayerSave;



public class MainMenu extends AppCompatActivity {
    //Player's score
     PlayerScore playerScore = new PlayerScore();

    // initialization of variables
    int i = 0; int j = 0; int k = 0;
    boolean music; boolean sound;
    // initialization of players save information
    PlayerSave save = new PlayerSave();
    // int array of ship list
    int shipImages[][] = {{R.drawable.mach1,R.drawable.mach1blue,R.drawable.mach1green,
                    R.drawable.mach1black,R.drawable.mach1white,R.drawable.mach1silver,
                    R.drawable.mach1gold},{R.drawable.mach3_red, R.drawable.mach3_blue,
                    R.drawable.mach3_green,R.drawable.mach3_black, R.drawable.mach3_white,
                    R.drawable.mach3_silver,R.drawable.mach3_gold}};
    // in array of penguin list
    int penguinImages[] = {R.drawable.normalpenguin,R.drawable.ladypenguin,
            R.drawable.mechanicpenguin,
            R.drawable.superpenguin,R.drawable.ninjapenguin,
            R.drawable.dootdootpenguin,R.drawable.ssp};

    // for radio buttons
    RadioButton soundY;    RadioButton soundN;
    RadioButton musicY;    RadioButton musicN;

    // for image views
    ImageView ship; ImageView penguin;
    //public TextView color;

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

        ship = (ImageView) findViewById(R.id.changeShip);
        penguin = (ImageView) findViewById(R.id.changePenguin);

        ship.setImageResource(shipImages[i][k]);
        penguin.setImageResource(penguinImages[j]);
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
        setContentView(new GamePanelRacing_Medium(this));
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
        if(i==0) {
            i = 1;
            if (ship != null) {
                ship.setImageResource(shipImages[i][k]);
            }
        }else{
            i = i-1;
            if (ship != null) {
                ship.setImageResource(shipImages[i][k]);
            }
        }
    }
    // on click scrolls through the ships list from the right
    public void sendShipRight(View view){
        ship = (ImageView) findViewById(R.id.changeShip);
        if(i==1){
            i = 0;
            if (ship != null) {
                ship.setImageResource(shipImages[i][k]);
            }
        }else{
            i = i +1;
            if (ship != null) {
                ship.setImageResource(shipImages[i][k]);
            }
        }
    }
    // on click scrolls through the penguin list from the left
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
    // on click scrolls through the colors for that ship down the list
    public void sendColorDown(View view){
        ship = (ImageView) findViewById(R.id.changeShip);
        if(k==0){
            k = 6;
            if(ship != null){
                ship.setImageResource(shipImages[i][k]);
            }
        }else{
            k = k - 1;
            if(ship != null){
                ship.setImageResource(shipImages[i][k]);
            }
        }
    }
    // on click scrolls through the colors for that ship up the list
    public void sendColorUp(View veiw){
        ship = (ImageView) findViewById(R.id.changeShip);
        if(k==6){
            k = 0;
            if(ship != null){
                ship.setImageResource(shipImages[i][k]);
            }
        }else{
            k = k + 1;
            if(ship != null){
                ship.setImageResource(shipImages[i][k]);
            }
        }
    }

    //================================================
    // Saving player information
    //
    // on clicking SAVE in customization menu saves the type of ship
    public void sendSave(View view){
        ship = (ImageView) findViewById(R.id.changeShip);
        penguin = (ImageView) findViewById(R.id.changePenguin);

        if(ship != null && penguin != null){
            save.getSaveShip(i);
            save.getSaveColor(k);
            save.getSavePenguin(j);
            playerScore.setPenguin(j);  //sets the saved penguin for player's penguin for score
        }

    }
    // on clicking SAVE in the options menu saves the playes infromation of the options
    public void sendSaveOptions(View view){
        soundY = (RadioButton) findViewById(R.id.SoundY);
        soundN = (RadioButton) findViewById(R.id.SoundN);
        musicY = (RadioButton) findViewById(R.id.MusicY);
        musicN = (RadioButton) findViewById(R.id.MusicN);

        if(soundY.isChecked() == true && soundN.isChecked() == false){
            save.getSaveSounds(true);
        }else if(soundN.isChecked() == true && soundY.isChecked() == false){
            save.getSaveSounds(false);
        }

        if(musicY.isChecked() == true && musicN.isChecked() == false){
            save.getSaveMusic(true);
        }else if(musicN.isChecked() == true && musicY.isChecked() == false){
            save.getSaveMusic(false);
        }
    }
}