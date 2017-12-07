package com.example.joeps.triviaplaza;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import static java.lang.Thread.sleep;

public class Intro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#601E13"));
        Thread myIntro = new Thread() {
            @Override
            public void run() {try {
                    sleep(200);
                    next();
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        };
        myIntro.start();
    }
    public void next() {
        Intent intent = new Intent(getApplicationContext(), Home_screen.class);
        startActivity(intent);
        finish();
    }
}
