package com.example.rawat.chatterbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.socket.emitter.Emitter;

/**
 * Created by WIN10 on 12/2/2017.
 */

public class BaseActivity extends AppCompatActivity {
    SocketManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        socketManager = SocketManager.get(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.activity_base_frame_layout, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            if (getFragmentManager().getBackStackEntryCount() == 2) {
                if (socketManager.getUserName() != null) {
                    socketManager.getSocket().once("log out back", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            if (args.toString().equalsIgnoreCase("deleted")) {
                                socketManager.setUserId(-222);
                                socketManager.setUserName(null);
                                socketManager.setBuyer(false);
                            }
                        }
                    });
                    socketManager.getSocket().emit("log out", "deleted");
                }
            }
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            finish();
        }
    }
}