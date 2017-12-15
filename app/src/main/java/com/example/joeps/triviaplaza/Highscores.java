package com.example.joeps.triviaplaza;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by joeps on 13-12-2017.
 * This activity shows karma scores from other users.
 */

public class Highscores extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth authTest;
    int tot;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private static final String Tag = "Firebase_test";
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        ImageButton top = findViewById(R.id.top);
        top.setOnClickListener(this);
        authTest = FirebaseAuth.getInstance();
        setListener(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Button sub = findViewById(R.id.submit);
        sub.setOnClickListener(this);
        Button gethigh = findViewById(R.id.high);
        gethigh.setOnClickListener(this);
        getFromFirebase();
    }
    private void setListener(final Context context){ // Check if the user is logged in.
        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(Tag, "onAuthStateChanged:signed_in"+user.getUid());
                }else{
                    Log.d(Tag, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(context, Home_screen.class));finish();
                }
            }
        };
    }
    public void onClick(View v) { //On the click of the star icon finish this activity and go back to the previous one.
        if (v.getId() == R.id.top) {
            finish();
        }else if (v.getId() == R.id.submit){
            dataToFirebase();
            Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT);
        }else if (v.getId() == R.id.high){
            getHighscore();
        }
    }
    public void send(int tot){
        EditText score = findViewById(R.id.name);
        mDatabase.child("highscores").setValue(score.getText().toString());
        mDatabase.child("highscores").child(score.getText().toString()).child("Karma").setValue((tot));

    }
    public void dataToFirebase(){//Get the current karmapoints of the user.
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = authTest.getCurrentUser();
                Data dataa = dataSnapshot.child("users/"+user.getUid()).getValue(Data.class);
                if (dataa != null){
                    send(dataa.Karma);}}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("oops","Oops something went wrong: ",databaseError.toException());
                startActivity(new Intent(getApplicationContext(), Home_screen.class));finish();
            }
        });

    }
    public void getFromFirebase(){//Get the current karmapoints of the user.
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = authTest.getCurrentUser();
                Data dataa = dataSnapshot.child("users/"+user.getUid()).getValue(Data.class);
                if (dataa != null){
                    TextView karma = findViewById(R.id.karma);
                    karma.setText(dataa.Karma+"");
                }}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("oops","Oops something went wrong: ",databaseError.toException());
                startActivity(new Intent(getApplicationContext(), Home_screen.class));finish();
            }
        });
    }
    public void getHighscore(){
        final EditText userr = findViewById(R.id.user);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                High highscore = dataSnapshot.child("highscores/"+userr.getText().toString()).getValue(High.class);
                if (highscore != null){
                    TextView show = findViewById(R.id.show);
                    show.setText(highscore.Karma+"");
                }}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("oops","Oops something went wrong: ",databaseError.toException());
                startActivity(new Intent(getApplicationContext(), Home_screen.class));finish();
            }
        });
    }
}
