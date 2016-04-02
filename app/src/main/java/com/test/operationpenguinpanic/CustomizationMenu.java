package com.test.operationpenguinpanic;

/**
 * Created by johnny on 3/29/16.
 */
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;


public class CustomizationMenu extends MainMenu{
    // fill arrays when items are present.
    int ships[][] = {{}};
    int penguins[] = {};

    int penguincount = 0;
    int i = 0; int j = 0;

    // Accessing The text Color's id to alter.
    // Don't know if this will be used.
    MainMenu menu = new MainMenu();
    TextView verticalText = menu.getColorID();


    public void sendShipleft(View view){
        // fill
        if(i>0){
            i = i -1;
        }
    }

    public void sendShipRight(View view){
        // fill
        if(i<5){
            i = i + 1;
        }
    }

    public void sendColorUp(View view){
        // fill
        if(j<5){
            j = j + 1;
        }
    }

    public void sendColorDown(View veiw){
        // fill
    }

    public void sendPenguinLeft(View view){
        // fill
    }

    public void sendPenguinRight(View view){
        // fill
    }

}
