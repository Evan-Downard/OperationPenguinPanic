package com.test.operationpenguinpanic;

/**
 * Created by johnny on 4/11/16.
 */
public class PlayerSave {
    public static int ship;      public static int color;
    public static int penguin;    public static int pit;
    public static boolean music;
    public static boolean sounds;

    public static void getSaveShip(int playerShip){
        ship = playerShip;
    }

    public static int sendSaveShip(){
        return ship;
    }

    public static void getSaveColor(int playerColor){
        color = playerColor;
    }

    public static int sendSaveColor(){
        return color;
    }

    public static void getSavePenguin(int playerPenguin){
        penguin = playerPenguin;
    }

    public static int sendSavePenguin(){
        return penguin;
    }

    public static void getSavePit(int playerPit){
        pit = playerPit;
    }

    public static int sendSavePit(){
        return pit;
    }

    public static void getSaveMusic(boolean playerMusic){
        music = playerMusic;
    }

    public static boolean sendSaveMusic(){
        return music;
    }

    public static void getSaveSounds(boolean playerSounds){
        sounds = playerSounds;
    }

    public static boolean sendSaveSounds(){
        return sounds;
    }

}
