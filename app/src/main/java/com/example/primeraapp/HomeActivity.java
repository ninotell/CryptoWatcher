package com.example.primeraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private FirebaseAuth mAuth;
    private Date lastRefresh;
    Long secondsSinceLastRefresh = 0L;
    RequestQueue requestQueue;
    private static String apiKey = "8af1c334-f60e-4ee6-994e-dcaf71c37aa2";
    private static String URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?limit=100";
    private RecyclerView recyclerView;
    ArrayList<MyModel> coins = new ArrayList<>();
    SwipeRefreshLayout coinsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_home);


        requestQueue = Volley.newRequestQueue(this);


        Intent intent = getIntent();
        String userEmail = intent.getStringExtra(MainActivity.USER_EMAIL);

        /** Obtains email and password */
        TextView emailTextView = findViewById(R.id.userEmailTextView);
        emailTextView.setText(userEmail);


        Button btn = findViewById(R.id.buttonTest);
        coinsContainer = findViewById(R.id.coinsContainer);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coinsContainer.setRefreshing(true);
                jsonObjectRequest();

            }
        });


        coinsContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsonObjectRequest();

            }
        });

    }

    public void jsonObjectRequest() {
        DecimalFormat df = new DecimalFormat("#.###");


        recyclerView = findViewById(R.id.coinsRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);

        Date actualDate = new Date();

        if (lastRefresh != null) {
            secondsSinceLastRefresh = (actualDate.getTime()/1000) - (lastRefresh.getTime()/1000);
        }

        if (secondsSinceLastRefresh > 60 || lastRefresh == null) {
            lastRefresh = new Date();

            coins.clear();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Float price = Float.parseFloat(jsonObject.getJSONObject("quote").getJSONObject("USD").getString("price"));
                                    String symbol = jsonObject.getString("symbol");
                                    String name = jsonObject.getString("name");
                                    String logoUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/" + jsonObject.getString("id") + ".png";


                                    MyModel mm = new MyModel(logoUrl, symbol, name, "$" + Float.valueOf(df.format(price)).toString());
                                    coins.add(mm);
                                }
                                MyAdapter ma = new MyAdapter(coins);
                                recyclerView.setAdapter(ma);
                                coinsContainer.setRefreshing(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TAGAPITEST", error.toString());
                        }
                    }
            ) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap params = new HashMap();
                    params.put("limit", 5);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap header = new HashMap();
                    header.put("Content-type", "application/json");
                    header.put("X-CMC_PRO_API_KEY", apiKey);
                    return header;


                }

            };
            requestQueue.add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "Only 60 seconds per refresh allowed." +  " Last refresh: " + secondsSinceLastRefresh +" seconds ago", Toast.LENGTH_LONG).show();
            coinsContainer.setRefreshing(false);
        }

    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    protected void onStart() {
        super.onStart();
        mAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.getInstance().removeAuthStateListener(this);
    }


    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d("onAuthStateChangedTAG", "Enter");
        if (firebaseAuth.getCurrentUser() == null) {
            Log.d("onAuthStateChangedTAG", "User NULL");
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

}