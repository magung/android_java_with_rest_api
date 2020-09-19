package com.magung.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddCustomer extends AppCompatActivity {

    EditText etID, etNama, etTelp;
    Button btnSave;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        getSupportActionBar().setTitle("Add New Customer"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        etID = (EditText) findViewById(R.id.et_id_cust);
        etNama = (EditText) findViewById(R.id.et_nama_cust);
        etTelp = (EditText) findViewById(R.id.et_telp_cust);

        btnSave = (Button) findViewById(R.id.btnSave);



        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("Data", etID.getText().toString() + ", " + etNama.getText().toString() + ", " + etTelp.getText().toString());
                addCust(etID.getText().toString(), etNama.getText().toString(), etTelp.getText().toString());
            }
        });
    }

    public void addCust(String id, String nama, String telp){
        progressDialog = new ProgressDialog(AddCustomer.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Menambah Data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        String urlCustomer = "https://project-base-team3.000webhostapp.com/api/customer.php?nama="+nama+"&telp="+telp+"&id="+id;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                urlCustomer,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        btnSave.setEnabled(false);
                        try {


                            Boolean isError = response.getBoolean("error");

                            if(isError) {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(AddCustomer.this)
                                        .setTitle("Failed")
                                        .setMessage("gagal menambahkan data")
                                        .setCancelable(true)
                                        .show();
                            }else{
                                progressDialog.dismiss();
                                new AlertDialog.Builder(AddCustomer.this)
                                        .setTitle("Success")
                                        .setMessage("berhasil menambahkan data")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText( AddCustomer.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}