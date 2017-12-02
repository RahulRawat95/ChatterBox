package com.example.rawat.chatterbox;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by WIN10 on 12/2/2017.
 */

public interface RetrofitApiInterface {
    @GET("/retrieveMessages/{myId}/{to}/{from}/{time}/")
    Call<ArrayList<Message>> retrieveMessages(@Path("myId") int myId, @Path("to") int to, @Path("from") int from, @Path("time") long time);

    @GET("/getVendorList/{eventId}/")
    Call<ArrayList<Vendor>> getVendorList(@Path("eventId") long eventId);
}
