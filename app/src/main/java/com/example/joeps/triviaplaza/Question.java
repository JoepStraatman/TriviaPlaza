package com.example.joeps.triviaplaza;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by joeps on 13-12-2017.
 * This activity shows the question picked in the Main activity.
 * You can choose if the answer is true or false.
 * If you answer correctly you will be awarded with 1 karmapoint.
 */
public class Question extends AppCompatActivity implements View.OnClickListener{
    JSONObject jobject;
    Intent intent;
    String jsonString;
    String answer;
    String input;
    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private static final String Tag = "Firebase_test";
    private DatabaseReference mDatabase;
    public static Boolean correctA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        loadIntent();
        final ImageButton login = findViewById(R.id.trueButton);
        final ImageButton create = findViewById(R.id.falseButton);
        authTest = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setListener();
        login.setOnClickListener(this);
        create.setOnClickListener(this);
    }
    private void setListener(){ //Check if the user is logged in.
        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(Tag, "onAuthStateChanged:signed_in"+user.getUid());
                    getFromFirebase();
                }else{
                    Log.d(Tag, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(getApplicationContext(), Home_screen.class));finish();
                }
            }
        };
    }
    public void onClick(View v) {
        if(v.getId() == R.id.trueButton) { //On click of the true button set input to true
            input = "True";
        }else if(v.getId() == R.id.falseButton) { //On click of the false button set input to false.
            input = "False";}
        if (answer.equals(input)){ //If the answer to the question is correct.
            correctA = true;
            dataToFirebase();
            startActivity(new Intent(Question.this,Popup.class));//Open popup.
        }else{ //If the answer to the question is incorrect.
            correctA = false;
            startActivity(new Intent(Question.this,Popup.class));}//Open popup.
    }
    public void loadIntent(){//Get the question from the intent.
        intent = getIntent();
        jsonString = intent.getStringExtra("item");
        try {
            jobject = new JSONObject(jsonString);
            TextView question = findViewById(R.id.question);
            question.setText(jobject.getString("question").replaceAll("&quot;", "'").replaceAll("&#039;", "'"));
            answer = jobject.getString("correct_answer");
        }catch (JSONException e) {e.printStackTrace();}
    }
    public void dataToFirebase(){//Set the karmapoints of the user when answered correctly.
        FirebaseUser user = authTest.getCurrentUser();
        mDatabase.child("users").setValue(user.getUid());
        mDatabase.child("users/"+user.getUid()).child("Karma").setValue((3));

    }
    public void getFromFirebase(){//Get the current karmapoints of the user.
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = authTest.getCurrentUser();
                Data dataa = dataSnapshot.child("users/"+user.getUid()).getValue(Data.class);
                TextView karma = findViewById(R.id.karma);
                karma.setText(dataa.Karma);}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("oops","Oops something went wrong: ",databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }
}
