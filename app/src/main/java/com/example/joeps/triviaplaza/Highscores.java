package com.example.joeps.triviaplaza;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Highscores extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth authTest;
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
        setListener();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    private void setListener(){
        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(Tag, "onAuthStateChanged:signed_in"+user.getUid());
                }else{
                    Log.d(Tag, "onAuthStateChanged:signed_out");
                    goToHome();
                }
            }
        };
    }
    private void goToHome() {
        startActivity(new Intent(getApplicationContext(), Home_screen.class));finish();
    }
    public void onClick(View v) {
        if (v.getId() == R.id.top) {
            finish();
        }
    }
}
