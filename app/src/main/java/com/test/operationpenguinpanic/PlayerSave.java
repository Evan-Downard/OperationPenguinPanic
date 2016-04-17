package com.test.operationpenguinpanic;

/**
 * Created by johnny on 4/11/16.
 */
public class PlayerSave {
    int ship = 0;       int color = 0;
    int penguin = 0;
    boolean music = true;
    boolean sounds = true;

    void getSaveShip(int playerShip){
        ship = playerShip;
    }

    int sendSaveShip(){
        return ship;
    }

    void getSaveColor(int playerColor){
        color = playerColor;
    }

    int sendSaveColor(){
        return color;
    }

    void getSavePenguin(int playerPenguin){
        penguin = playerPenguin;
    }

    int sendSavePenguin(){
        return penguin;
    }

    void getSaveMusic(boolean playerMusic){
        music = playerMusic;
    }

    boolean sendSaveMusic(){
        return music;
    }

    void getSaveSounds(boolean playerSounds){
        sounds = playerSounds;
    }

    boolean sendSaveSounds(){
        return sounds;
    }

}
