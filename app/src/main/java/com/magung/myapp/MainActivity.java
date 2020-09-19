package com.magung.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Customer> customers;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText search;
    Editable val_search;
    Button btnSearch, btnAddCust;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddCust = (Button) findViewById(R.id.btnAddCust);
        btnAddCust.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCustomer.class);
                MainActivity.this.startActivity(intent);
            }
        });

        search = (EditText) findViewById(R.id.search);
        this.val_search = search.getText();
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                load(val_search.toString());
            }
        });


        progressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load(val_search.toString());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        swipeRefreshLayout.setColorSchemeColors(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
                );
        load(val_search.toString());
    }

    public void load(String search){
        progressBar.setVisibility(ProgressBar.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        String urlCustomer = "https://project-base-team3.000webhostapp.com/api/customer.php?search=" + search;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlCustomer,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String idcust, namacust, telpcust;
                        customers = new ArrayList<Customer>();

                        try {
                            JSONArray jsonArray = response.getJSONArray("result");
                            Boolean isError = response.getBoolean("error");
                            customers.clear();

                            if(isError) {
//                                Toast.makeText(MainActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Warning")
                                        .setMessage("Data tidak ditemukan")
                                        .setCancelable(true)
                                        .show();
                            }

                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataCust = jsonArray.getJSONObject(i);
                                    idcust = dataCust.getString("idcustomer").toString().trim();
                                    namacust = dataCust.getString("namacustomer").toString().trim();
                                    telpcust = dataCust.getString("telpcustomer").toString().trim();

                                    customers.add(new Customer(idcust, namacust, telpcust));

                                }
                            }

                            showRecyclerGrid();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressBar.setVisibility(ProgressBar.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(progressBar.GONE);
                        Toast.makeText(MainActivity.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void showRecyclerGrid(){
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        CustomerAdapter customerAdapter = new CustomerAdapter(this, customers);
        recyclerView.setAdapter(customerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}