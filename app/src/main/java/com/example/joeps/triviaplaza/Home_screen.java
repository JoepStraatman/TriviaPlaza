package com.example.joeps.triviaplaza;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by joeps on 13-12-2017.
 * This activity is the Loginscreen for the app.
 * Here you can either login to triviaplaza or create an account for triviaplaza.
 */
public class Home_screen extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email;
    String password;
    private DatabaseReference mDatabase;
    private FirebaseAuth authTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authTest = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_home_screen);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#D6896A"));
        mAuth = FirebaseAuth.getInstance();
        final Button login = findViewById(R.id.login);
        final Button create = findViewById(R.id.create);
        login.setOnClickListener(this);
        create.setOnClickListener(this);
        authlistener();
    }
    public void onClick(View v) {
        TextView emailv = findViewById(R.id.email);
        TextView passwordv = findViewById(R.id.password);
        email = emailv.getText().toString();
        password = passwordv.getText().toString();
           if (email.length() > 0 && password.length() > 0) { //If email or password input isnt empty.
               if (v.getId() == R.id.login) { //When clicked login, do login.
                   logIn();
               } else if (v.getId() == R.id.create) { //When clicked create, create account and login.
                   createUser();}
           } else {
               Toast.makeText(Home_screen.this, "Email or password is empty.", Toast.LENGTH_LONG).show();}
       }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void createUser(){ //Create a user in firebase.
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d("exist_email", "onComplete: exist_email");
                                Toast.makeText(Home_screen.this, email+ " already exists!",
                                        Toast.LENGTH_SHORT).show();}// if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword) {
                                Log.d("weak_password", "onComplete: weak_password");
                                Toast.makeText(Home_screen.this, "Weak password! Password needs to be at least 6 characters  long!",
                                        Toast.LENGTH_LONG).show();}// if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                Log.d("malformed_email", "onComplete: malformed_email");
                                Toast.makeText(Home_screen.this,  "Malformed email!",
                                        Toast.LENGTH_SHORT).show();
                            }  catch (Exception e) {
                                Log.d("Error", "onComplete: " + e.getMessage());}
                        }else{
                            logIn(); //Log the user in after creating the account.
                            dataToFirebase();
                        }
                    }
                });
    }
    public void logIn(){ //Login the user into firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {// Sign in success, update UI with the signed-in user's information
                            Log.d("sign in succesfully", "signInWithEmail:success");
                            Toast.makeText(Home_screen.this, "User " +email+ " signed in!",Toast.LENGTH_SHORT).show();
                            next();
                        } else {// If sign in fails, display a message to the user.
                            Log.w("Failed to login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Home_screen.this, "Authentication failed.",Toast.LENGTH_SHORT).show();}
                    }
        });
    }
    public void next(){ //Go to the Main class. Called after login is complete.
        Intent intent = new Intent(getApplicationContext(), Main.class);
        startActivity(intent);
        finish();
    }
    public void authlistener(){ //Check if the user is logged in.
        //FirebaseAuth.AuthStateListener mAuthListener;
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
    public void dataToFirebase(){//Set the karmapoints of the user when answered correctly.
        FirebaseUser user = authTest.getCurrentUser();
        mDatabase.child("users").child(user.getUid()).child("Karma").setValue(0);

    }
}
