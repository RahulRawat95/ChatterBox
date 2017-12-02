package com.example.rawat.chatterbox;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WIN10 on 12/2/2017.
 */

public class VendorListFragment extends Fragment {
    private RecyclerView recyclerView;
    private long eventId = 3584;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView=view.findViewById(R.id.vendor_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Call<ArrayList<Vendor>> vendorer=SocketManager.get(getActivity()).getClient().create(RetrofitApiInterface.class).getVendorList(eventId);
        vendorer.enqueue(new Callback<ArrayList<Vendor>>() {
            @Override
            public void onResponse(Call<ArrayList<Vendor>> call, Response<ArrayList<Vendor>> response) {
                ArrayList<Vendor> vendors=response.body();

            }

            @Override
            public void onFailure(Call<ArrayList<Vendor>> call, Throwable t) {

            }
        });
    }

}
