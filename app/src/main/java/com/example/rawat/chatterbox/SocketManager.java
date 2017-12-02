package com.example.rawat.chatterbox;

import android.content.Context;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WIN10 on 11/30/2017.
 */

public class SocketManager {
    public static final String SERVER_URI = "http://192.168.1.24:3000";

    private static SocketManager socketManager = null;
    private static Retrofit retrofit = null;

    private Context context;
    private Socket mSocket;
    private boolean isBuyer;
    private int userId;
    private String userName;


    private SocketManager(Context context) {
        this.context = context;
        try {
            mSocket = IO.socket(SERVER_URI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static SocketManager get(Context context) {
        if (socketManager == null) {
            socketManager = new SocketManager(context);
        }
        return socketManager;
    }

    public Socket getSocket() {
        return mSocket;
    }

    public boolean isBuyer() {
        return isBuyer;
    }

    public void setBuyer(boolean buyer) {
        isBuyer = buyer;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
