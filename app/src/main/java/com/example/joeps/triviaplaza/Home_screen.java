package com.example.joeps.triviaplaza;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Home_screen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#D6896A"));
        mAuth = FirebaseAuth.getInstance();
        final Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                TextView emailv = findViewById(R.id.email);
                TextView passwordv = findViewById(R.id.password);
                email = emailv.getText().toString();
                password = passwordv.getText().toString();
                if (email.length() > 0 || password.length() > 0){
                    logIn();
                }
            }
        });
        final Button create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                TextView emailv = findViewById(R.id.email);
                TextView passwordv = findViewById(R.id.password);
                email = emailv.getText().toString();
                password = passwordv.getText().toString();
                if (email.length() > 0 || password.length() > 0) {
                    createUser();
                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!= null){
                    Log.d("Signed in", "onAuthStateChanged:signed_in:" + user.getUid());
                }else{
                    Log.d("Signed out", "onAuthStateChanged:signed_out");
                }
            }
        };

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void createUser(){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword) {
                                Log.d("weak_password", "onComplete: weak_password");
                                Toast.makeText(Home_screen.this, "Weak password! Password needs to be at least 6 characters  long!",
                                        Toast.LENGTH_LONG).show();
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                Log.d("malformed_email", "onComplete: malformed_email");
                                Toast.makeText(Home_screen.this,  "Malformed email!",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d("exist_email", "onComplete: exist_email");
                                Toast.makeText(Home_screen.this, email+ " already exists!",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d("Error", "onComplete: " + e.getMessage());
                            }
                        }else{
                            logIn();
                        }
                    }
                });
    }
    public void logIn(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("sign in succesfully", "signInWithEmail:success");
                            Toast.makeText(Home_screen.this, "User " +email+ " signed in!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Main.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Failed to login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Home_screen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
        });

    }
}
