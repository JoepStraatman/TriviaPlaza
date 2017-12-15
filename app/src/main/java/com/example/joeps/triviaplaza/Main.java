package com.example.joeps.triviaplaza;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by joeps on 13-12-2017.
 * This activity is the activity where you can choose 1 of 5 questions to earn karma points.
 * The questions are retrieved from the opentdb api. Every time this activity is loaded 5 random will be retrieved again.
 */
public class Main extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authlistener;
    private static final String Tag = "Firebase_test";
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String url = "https://opentdb.com/api.php?amount=5&type=boolean";
    public JSONArray ja_data = null;
    ArrayList<String> listdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authTest = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        openCategory();
        ImageButton top = findViewById(R.id.top);
        TextView explain = findViewById(R.id.explain);
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);
        top.setOnClickListener(this);
        explain.setOnClickListener(this);
        authlistener();
        getFromFirebase();
    }
    public void onClick(View v) {
        if(v.getId() == R.id.top) {startActivity(new Intent(Main.this,Highscores.class));// Go to highscore place.
        }else if(v.getId() == R.id.explain){startActivity(new Intent(Main.this,Explain.class));}
        else if (v.getId() == R.id.logout){authTest.signOut();Log.d(Tag, "onAuthStateChanged:signed_out2");}}//Open the explain popup window.

    public void authlistener(){ //Check if the user is logged in.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(Tag, "onAuthStateChanged:signed_inmain"+user.getUid());
                }else{
                    Log.d(Tag, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(getApplicationContext(), Home_screen.class));finish();
                }
            }
        };
    }
    public void openCategory() {//Create a new volley request to the api.
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                saveToAdapter(response);
            }}, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);}
        });
        queue.add(stringRequest);// Add the request to the RequestQueue.
        listClick();
    }
    public void listClick() { //On listview item click go to a new activity and open the question.
        final ListView list = findViewById(R.id.home);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    JSONObject arrayPicked = new JSONObject(ja_data.getString(position));
                    Intent intent = new Intent(Main.this, Question.class);
                    intent.putExtra("item", arrayPicked.toString());
                    startActivity(intent);finish();
                }catch (JSONException e){throw new RuntimeException(e);}
            }
        });
    }
    public void saveToAdapter(JSONObject response){ //Set the questions to the listview adapter
        listdata = new ArrayList<>();  // load data from file
        try {
            JSONObject jsonObj = new JSONObject(response.toString());
            ja_data = jsonObj.getJSONArray("results"); // Get objects of request
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }getQuestion();
        ArrayList<String> myArray = listdata;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Main.this, R.layout.main_layout, myArray);
        final ListView list = findViewById(R.id.home);
        list.setAdapter(adapter);
    }
    public void getQuestion(){//Get the questions from the response.
        JSONArray jArray = ja_data;
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {try {
                JSONObject jsonArray2 = new JSONObject(jArray.getString(i));
                listdata.add(jsonArray2.getString("question").replaceAll("&quot;", "'").replaceAll("&#039;", "'"));
            } catch (JSONException e) {throw new RuntimeException(e);}
            }
        }
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
}
