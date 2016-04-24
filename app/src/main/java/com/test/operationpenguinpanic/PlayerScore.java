package com.test.operationpenguinpanic;

/**
 * Created by Ama on 4/21/2016.
 */
public class PlayerScore {
    private String name = "Player";
    private int score = 0;
    private String penguin;

    public String getName() {
        return name;
    }

   public void setName(String name) {
       this.name = name;
    }

    public int getScore() {
        return score;
    }

    public static void setScore(int score) {
        score = score;
    }

    public String getPenguin() {
        return penguin;
    }

    public void setPenguin(int penguin) {
        switch (penguin){   //finds the index of penguin array and sets player's penguin
                            //to the penguin that they save
            case 1:
                this.penguin = "Lady";
                break;
            case 2:
                this.penguin = "Mechanic";
                break;
            case 3:
                this.penguin = "Super Penguin";
                break;
            case 4:
                this.penguin = "Ninja";
                break;
            case 5:
                this.penguin = "Doot Doot";
                break;
            case 6:
                this.penguin = "Super Saiyan";
                break;
            default:
                this.penguin = "Normal";
                break;
        }
    }
}
