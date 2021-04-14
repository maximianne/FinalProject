package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class Activity2 extends AppCompatActivity {
    private Button search;
    private Button history;
    private SearchView searchView;
    private String urltt1 ="https://api.spotify.com/v1/artists/";
    private String urltt2="/top-tracks";
    private String searchText;
    private String CompleteURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        search = findViewById(R.id.button_search);
        history = findViewById(R.id.button_history);
        searchView = findViewById(R.id.searchView);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText=searchView.getQuery().toString();
                if(!searchText.equals("")){
                    if(!getArtistID(searchText).equals("")){
                        CompleteURL=makeURLtopTracks(getArtistID(searchText));
                    }
                }
            }
        });

    }
    private String makeURLtopTracks(String id){
        return urltt1+id+urltt2;
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
