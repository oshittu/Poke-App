package com.tomi.pokeapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
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
    TextView pName, pType;
    ImageView pIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // links: https://pokeapi.co/api/v2/pokemon/pikachu
        // https://pokeapi.co/

        pName = findViewById(R.id.pName);
        pIcon = findViewById(R.id.pIcon);
        pType = findViewById(R.id.pType);
        show();
    }

    public void show()
    {
        String url = "https://pokeapi.co/api/v2/pokemon/pikachu";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url , null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject species = response.getJSONObject("species");
                    JSONObject sprites = response.getJSONObject("sprites");
                    JSONArray array = response.getJSONArray("abilities");

                    //pokemon name
                    String name = species.getString("name");
                    pName.setText(name);

                    // pokemon icon
                    String iconUrl = sprites.getString("front_default");
                    Picasso.with(getApplicationContext()).load(iconUrl).into(pIcon);         // this needs to be fixed

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
