package com.example.joeps.triviaplaza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by joeps on 11-12-2017.
 * This class is a popup window in the question activity.
 * It will show if the user answered the question correct or not.
 */

public class Popup extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels; //set window width.
        int height = dm.heightPixels; //set window height.

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
        TextView text = findViewById(R.id.goodfalse);//Check if the answer was correct or not and display it in the textview.
        if (Question.correctA == true) {
            text.setText("Youre answer was correct!");
        } else {
            text.setText("Youre answer was incorrect!");
        }
    }
        @Override
        protected void onDestroy () {//When this class is closed, go to 5 new questions in the main class.
            super.onDestroy();
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
        }
    }
