package com.test.operationpenguinpanic;

/**
 * Created by Ama on 4/21/2016.
 */
public class PlayerScore {
    private String name = "Player";
    private int score = 0;
    private String penguin = "Noraml";

    public PlayerScore(){}

    public PlayerScore(String n, int s, String p){
        name = n;
        score = s;
        penguin = p;
    }

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
                this.penguin = "Super Penguin";
                break;
            case 3:
                this.penguin = "Ninja";
                break;
            case 4:
                this.penguin = "Cyber";
                break;
            case 5:
                this.penguin = "Pika-guin";
                break;
            case 6:
                this.penguin = "Doot Doot";
                break;
            case 7:
                this.penguin = "Super Saiya";
                break;
            default:
                this.penguin = "Normal";
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PlayerScore){
            PlayerScore other = (PlayerScore) o;
            return score == other.getScore() && name.contentEquals(other.getName());
        }
        return false;
    }
}
