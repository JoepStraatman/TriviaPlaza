package com.example.joeps.triviaplaza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Question extends AppCompatActivity implements View.OnClickListener{
    JSONObject jobject;
    Intent intent;
    String jsonString;
    String answer;
    String input;
    public static Boolean correctA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        loadIntent();
        final ImageButton login = findViewById(R.id.trueButton);
        final ImageButton create = findViewById(R.id.falseButton);
        login.setOnClickListener(this);
        create.setOnClickListener(this);

    }
    public void onClick(View v) {
        Toast correct = Toast.makeText(Question.this, "Youre answer is correct!", Toast.LENGTH_LONG);
        Toast incorrect = Toast.makeText(Question.this, "Youre answer is INcorrect!", Toast.LENGTH_LONG);
        if(v.getId() == R.id.trueButton) {
            input = "True";
        }else if(v.getId() == R.id.falseButton) {
            input = "False";}
        if (answer.equals(input)){
            correct.show();
            correctA = true;
            startActivity(new Intent(Question.this,Popup.class));
        }else{
            incorrect.show();
            correctA = false;
            startActivity(new Intent(Question.this,Popup.class));}
    }
    public void loadIntent(){
        intent = getIntent();
        jsonString = intent.getStringExtra("item");
        try {
            jobject = new JSONObject(jsonString);
            TextView question = findViewById(R.id.question);
            question.setText(jobject.getString("question").replaceAll("&quot;", "'").replaceAll("&#039;", "'"));
            answer = jobject.getString("correct_answer");
        }catch (JSONException e) {e.printStackTrace();}
    }
}
