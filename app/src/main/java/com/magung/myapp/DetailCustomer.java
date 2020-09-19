package com.magung.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class DetailCustomer extends AppCompatActivity {
    EditText idCust, namaCust, telpCust;
    Button btnSave, btnDelete;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customer);
        getSupportActionBar().setTitle("Detail Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idCust = (EditText) findViewById(R.id.et_id_cust);
        namaCust = (EditText) findViewById(R.id.et_nama_cust);
        telpCust = (EditText) findViewById(R.id.et_telp_cust);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        Intent i = getIntent();
        final String id = i.getStringExtra("i_id");
        getDataDetail(id);

        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("Data", etID.getText().toString() + ", " + etNama.getText().toString() + ", " + etTelp.getText().toString());
                editCust(id, namaCust.getText().toString(), telpCust.getText().toString());
            }
        });

        btnDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCust(id);
            }
        });


    }

    private void getDataDetail(String id) {
        progressDialog = new ProgressDialog(DetailCustomer.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Load Data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlCustomerDetail = "https://project-base-team3.000webhostapp.com/api/customer_detail.php?id=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlCustomerDetail,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("result");
                            Log.d("ISI", response.toString());
                            idCust.setText(jsonObject.getString("idcustomer"));
                            namaCust.setText(jsonObject.getString("namacustomer"));
                            telpCust.setText(jsonObject.getString("telpcustomer"));
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Events : ", error.toString());
                        Toast.makeText(getApplicationContext(), "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void editCust(String id, String nama, String telp){
        btnSave.setEnabled(false);
        progressDialog = new ProgressDialog(DetailCustomer.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Update Data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        String urlCustomer = "https://project-base-team3.000webhostapp.com/api/customer_edit.php?nama="+nama+"&telp="+telp+"&id="+id;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                urlCustomer,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            Boolean isError = response.getBoolean("error");

                            if(isError) {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(DetailCustomer.this)
                                        .setTitle("Failed")
                                        .setMessage("gagal update data")
                                        .setCancelable(true)
                                        .show();
                            }else{
                                progressDialog.dismiss();
                                new AlertDialog.Builder(DetailCustomer.this)
                                        .setTitle("Success")
                                        .setMessage("berhasil update data")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                            }
                            btnSave.setEnabled(true);
                        } catch (JSONException e) {
                            btnSave.setEnabled(true);
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btnSave.setEnabled(true);
                        Toast.makeText( DetailCustomer.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void deleteCust(String id){
        btnDelete.setEnabled(false);
        progressDialog = new ProgressDialog(DetailCustomer.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Delete Data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        String urlCustomer = "https://project-base-team3.000webhostapp.com/api/customer_delete.php?id="+id;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                urlCustomer,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            Boolean isError = response.getBoolean("error");

                            if(isError) {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(DetailCustomer.this)
                                        .setTitle("Failed")
                                        .setMessage("gagal delete data")
                                        .setCancelable(true)
                                        .show();
                            }else{
                                progressDialog.dismiss();
                                new AlertDialog.Builder(DetailCustomer.this)
                                        .setTitle("Success")
                                        .setMessage("berhasil delete data")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                            }
                            btnDelete.setEnabled(true);
                        } catch (JSONException e) {
                            btnDelete.setEnabled(true);
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btnDelete.setEnabled(true);
                        Toast.makeText( DetailCustomer.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

}