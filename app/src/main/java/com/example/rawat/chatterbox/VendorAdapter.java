package com.example.rawat.chatterbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by WIN10 on 12/2/2017.
 */

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<Vendor> mVendors;

    public VendorAdapter(Context context, ArrayList<Vendor> vendors) {
        mContext = context;
        mVendors = vendors;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vendor, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Vendor vendor=mVendors.get(position);
        holder.view.setBackgroundColor();
        holder.name.setText(vendor.getCompanyName());
    }

    @Override
    public int getItemCount() {
        return mVendors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.vendor_color);
            name = itemView.findViewById(R.id.vendor_name);
        }
    }
}
