package com.example.joeps.triviaplaza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Created by joeps on 13-12-2017.
 * This activity is a popup window to explain how the app works.
 * The class will be called from the Main Activity.
 */

public class Explain extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explain_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels; //set window width
        int height = dm.heightPixels; // set window height

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }
}