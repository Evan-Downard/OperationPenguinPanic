package com.test.operationpenguinpanic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.RadioButton;



public class MainMenu extends AppCompatActivity {
    public TextView color;

/*
    // for check boxes
    public CheckBox soundY;    CheckBox soundN;
    CheckBox buttonY;   CheckBox buttonN;
    CheckBox musicY;    CheckBox musicN;

    // for radio buttons
    RadioButton soundY;    RadioButton soundN;
    RadioButton buttonY;   RadioButton buttonN;
    RadioButton musicY;    RadioButton musicN;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // inflating the xml pages
        View custom = getLayoutInflater().inflate(R.layout.customization_menu, null, false);
        View options = getLayoutInflater().inflate(R.layout.options_screen, null, false);


        // setting ids to variables
        //color = (TextView) custom.findViewById (R.id.colorText);

/*      // for check boxes
        soundY = (CheckBox) options.findViewById(R.id.SoundY);
        soundN = (CheckBox) options.findViewById(R.id.SoundN);
        buttonY = (CheckBox) options.findViewById(R.id.ButtonY);
        buttonN = (CheckBox) options.findViewById(R.id.ButtonN);
        musicY = (CheckBox) options.findViewById(R.id.MusicY);
        musicN = (CheckBox) options.findViewById(R.id.MusicN);

        // for radio button
        soundY = (RadioButton) options.findViewById(R.id.SoundY);
        soundN = (RadioButton) options.findViewById(R.id.SoundN);
        buttonY = (RadioButton) options.findViewById(R.id.ButtonY);
        buttonN = (RadioButton) options.findViewById(R.id.ButtonN);
        musicY = (RadioButton) options.findViewById(R.id.MusicY);
        musicN = (RadioButton) options.findViewById(R.id.MusicN);

        */

    }

    public void sendGame(View view){
        //Intent intent = new Intent(this, DisplayGame.class);
        setContentView(R.layout.game_select);
    }
    public void sendCustom(View view){

        setContentView(R.layout.customization_menu);
    }

    public void sendOption(View view){

        setContentView(R.layout.options_screen);
    }
    public void sendScore(View view){

        setContentView(R.layout.score_menu);
    }

    public void sendRacing(View view){

        setContentView(R.layout.difficulty_screen);
    }

    public void sendDifficulty(View view) {
        setContentView(new GamePanel(this));

    }

    public void sendMarathon(View view){
        setContentView(new GamePanel(this));
    }

    public void sendAsteroids(View view){
        setContentView(new GamePanel(this));
    }

    public void sendMain(View view){
        setContentView(R.layout.activity_main_menu);
    }

    // sending the ids of the object to be changed
    public TextView getColorID(){
        return color;
    }

/*
    // for check boxes
    public CheckBox getSoundY(){
        return soundY;
    }

    public CheckBox getSoundN(){
        return soundN;
    }
    public CheckBox getMusicY(){
        return musicY;
    }
    public CheckBox getMusicN(){
        return musicN;
    }
    public CheckBox getButtonY(){
        return buttonY;
    }
    public CheckBox getButtonN(){
        return buttonN;
    }

    //for radio buttons
    public RadioButton getSoundY(){
        return soundY;
    }

    public RadioButton getSoundN(){
        return soundN;
    }
    public RadioButton getMusicY(){
        return musicY;
    }
    public RadioButton getMusicN(){
        return musicN;
    }
    public RadioButton getButtonY(){
        return buttonY;
    }
    public RadioButton getButtonN(){
        return buttonN;
    }

*/
    public void sendRacingScores(View view){
        setContentView(R.layout.score_menu);
    }

    public void sendMarathonScores(View view){
        setContentView(R.layout.scores_marathon_menu);
    }

    public void sendAsteroidScores(View view){
        setContentView(R.layout.scores_asteroid_menu);
    }
}

