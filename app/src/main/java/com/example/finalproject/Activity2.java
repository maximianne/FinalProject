package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Activity2 extends AppCompatActivity {
    private Button search;
    private Button history;
    private SearchView searchView;
    private String searchText;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        databaseHelper = new DatabaseHelper(Activity2.this, "history.db",null, 1);

        search = findViewById(R.id.button_search);
        history = findViewById(R.id.button_history);
        searchView = findViewById(R.id.searchView);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Activity2.this, Activity3.class);
                searchText=searchView.getQuery().toString();

                databaseHelper.addHistory(new History(searchText));

                String artistID = getArtistID(searchText);
                    if(!getArtistID(searchText).equals("")){
                        Log.d("artistID:", artistID);
                       intent.putExtra("artistID", artistID);
                }
                else{
                    Toast.makeText(Activity2.this, "Artist does not exist", Toast.LENGTH_LONG).show();
               }
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Activity2.this, historyActivity.class);

                startActivity(intent);
            }
        });
    }

    private String getArtistID(String artist){
        String id="";
        try {
            JSONObject SpotJSON = new JSONObject(loadJSONFromAsset("spotify.json"));
            JSONArray spotArray = SpotJSON.getJSONArray("spotifyArtists");

            for(int i = 0; i< spotArray.length(); i++){
                JSONObject artObject = spotArray.getJSONObject(i);
                if(artObject.getString("name").equals(artist)){
                    id= artObject.getString("id");
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    private String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}