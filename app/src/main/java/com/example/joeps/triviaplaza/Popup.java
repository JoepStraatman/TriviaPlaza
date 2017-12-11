package com.example.joeps.triviaplaza;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by joeps on 11-12-2017.
 */

public class Popup extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        TextView text = findViewById(R.id.goodfalse);
        if (Question.correctA == true){
            text.setText("Youre answer was correct!");
        }else{
            text.setText("Youre answer was incorrect!");
        }
    }
}
