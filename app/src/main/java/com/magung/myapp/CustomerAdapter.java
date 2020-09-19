package com.magung.myapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.GridViewHolder> {
    private List<Customer> customers;
    private Context context;
    public CustomerAdapter(Context context, List<Customer> customers) {
        this.context = context;
        this.customers = customers;
    }
    @NonNull
    @Override
    public CustomerAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_layout, parent, false);
        GridViewHolder gridViewHolder = new GridViewHolder(view);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.GridViewHolder holder, int position) {
        final String id = customers.get(position).getIdcustomer(), nama = customers.get(position).getNamacustomer(), telp = customers.get(position).getTelpcustomer();

        holder.tvId.setText(" : " + id);
        holder.tvName.setText(" : " + nama);
        holder.tvTelp.setText(" : " + telp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, nama, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, DetailCustomer.class);
                i.putExtra("i_id", id);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvTelp;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tvIdCust);
            tvName = (TextView) itemView.findViewById(R.id.tvNamaCust);
            tvTelp = (TextView) itemView.findViewById(R.id.tvTelpCust);
        }
    }
}
