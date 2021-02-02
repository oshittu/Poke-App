package com.tomi.pokeapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView pName, pType, ab1, ab2, m1, m2, m3;
    ImageView pIcon, pIcon2;
    MediaPlayer mediaPlayer;
    Button stop, start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pName = findViewById(R.id.pName);
        pType = findViewById(R.id.pType);
        ab1 = findViewById(R.id.ab1);
        ab2 = findViewById(R.id.ab2);
        m1 = findViewById(R.id.m1);
        m2 = findViewById(R.id.m2);
        m3 = findViewById(R.id.m3);
        stop = findViewById(R.id.stop);
        start = findViewById(R.id.start);

        pIcon = findViewById(R.id.pIcon);
        pIcon2 = findViewById(R.id.pIcon2);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.pokemon);
        mediaPlayer.start();

        show();

        stop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mediaPlayer.stop();
               // mediaPlayer.release();
            }

        });

        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mediaPlayer.isPlaying())
                {
                    // do nothing
                }
                else
                {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.pokemon);
                    mediaPlayer.start();
                }
            }

        });
    }

    String PokemonName = "pikachu";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Write Pokemon name here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                PokemonName=query;
                show();

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); // for da keyboard
                if (getCurrentFocus() !=null)
                {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void show()
    {
        String url = "https://pokeapi.co/api/v2/pokemon/"+PokemonName ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url , null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject species = response.getJSONObject("species");
                    JSONObject sprites = response.getJSONObject("sprites");
                    JSONArray tabTypes = response.getJSONArray("types");
                    JSONArray abilities = response.getJSONArray("abilities");
                    JSONArray moves = response.getJSONArray("moves");

                    //ability 1
                    JSONObject zero1 = abilities.getJSONObject(0);
                    JSONObject abil1 = zero1.getJSONObject("ability");
                    String aName1 = abil1.getString("name");
                    ab1.setText(aName1);

                    //ability 2
                    JSONObject zero2 = abilities.getJSONObject(1);
                    JSONObject abil2 = zero2.getJSONObject("ability");
                    String aName2 = abil2.getString("name");
                    ab2.setText(aName2);

                    //move 1
                    JSONObject zaro0 = moves.getJSONObject(0);
                    JSONObject move0 = zaro0.getJSONObject("move");
                    String mName0 = move0.getString("name");
                    m1.setText(mName0);

                    //move 2
                    JSONObject zaro1 = moves.getJSONObject(1);
                    JSONObject move1 = zaro1.getJSONObject("move");
                    String mName1 = move1.getString("name");
                    m2.setText(mName1);

                    //move 3
                    JSONObject zaro2 = moves.getJSONObject(2);
                    JSONObject move2 = zaro2.getJSONObject("move");
                    String mName2 = move2.getString("name");
                    m3.setText(mName2);

                    //pokemon name
                    String name = species.getString("name");
                    pName.setText(name);

                    // pokemon icons
                    String iconUrl = sprites.getString("front_default");
                    Picasso.with(getApplicationContext()).load(iconUrl).into(pIcon);
                    String iconUrl2 = sprites.getString("back_default");
                    Picasso.with(getApplicationContext()).load(iconUrl2).into(pIcon2);

                    //pokemon type
                    JSONObject jsonobj = (JSONObject) tabTypes.get(0);
                    JSONObject Typexx = (JSONObject) jsonobj.get("type");
                    String typeName = Typexx.getString("name");
                    pType.setText(typeName);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error)
        {

        }
    });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
