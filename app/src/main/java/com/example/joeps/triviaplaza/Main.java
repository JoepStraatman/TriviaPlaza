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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {
    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private static final String Tag = "Firebase_test";
    private DatabaseReference mDatabase;
    private String url = "https://opentdb.com/api.php?amount=5&type=boolean";
    public JSONArray ja_data = null;
    ArrayList<String> listdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authTest = FirebaseAuth.getInstance();
        setListener(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        openCategory();
    }
    private void setListener(final Context context){
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
    public void openCategory() {
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
    public void listClick() {
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
    public void saveToAdapter(JSONObject response){
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
    public void getQuestion(){
        JSONArray jArray = ja_data;
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {try {
                JSONObject jsonArray2 = new JSONObject(jArray.getString(i));
                listdata.add(jsonArray2.getString("question").replaceAll("&quot;", "'").replaceAll("&#039;", "'"));
            } catch (JSONException e) {throw new RuntimeException(e);}
            }
        }
    }
}
