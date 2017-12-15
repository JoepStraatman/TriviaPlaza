package com.example.joeps.triviaplaza;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import static java.lang.Thread.sleep;
/**
 * Created by joeps on 13-12-2017.
 * This activity is the intro activity.
 * On starting the app, this window will show for a short time and then close to go to the login screen.
 */
public class Intro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#601E13"));
        Thread myIntro = new Thread() {
            @Override
            public void run() {try {
                    sleep(200); //The time before the activity closes.
                    next();
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        };
        myIntro.start();
    }
    public void next() { //Go to the login screen.
        Intent intent = new Intent(getApplicationContext(), Main.class);
        startActivity(intent);
        finish();
    }
}
