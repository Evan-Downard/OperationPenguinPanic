package com.test.operationpenguinpanic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;



public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void sendGame(View view){
        //Intent intent = new Intent(this, DisplayGame.class);
        setContentView(R.layout.game_select);
    }
    public void sendCustom(View view){
        setContentView(R.layout.activity_main_menu);
    }
    public void sendOption(View view){
        setContentView(R.layout.activity_main_menu);
    }
    public void sendScore(View view){
        setContentView(R.layout.activity_main_menu);
    }

    public void sendRacing(View view){

        setContentView(new GamePanel(this));
    }
    public void sendMarathon(View view){
        setContentView(new GamePanel(this));
    }
    public void sendAsteroids(View view){
        setContentView(new GamePanel(this));
    }
}

