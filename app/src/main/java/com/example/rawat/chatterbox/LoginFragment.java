package com.example.rawat.chatterbox;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoginFragment extends Fragment {

    private SocketManager socketManager;
    private EditText editText;
    private FloatingActionButton fab;
    private Socket mSocket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editText = view.findViewById(R.id.username);
        fab = view.findViewById(R.id.fab);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_DONE:
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(LoginFragment.this.getActivity().getCurrentFocus().getWindowToken(), 0);
                        authenticate();
                        return true;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticate();
            }
        });

        connect();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        socketManager = SocketManager.get(getActivity());
    }

    public void authenticate() {
        String st = editText.getText().toString();
        if (mSocket != null && !TextUtils.isEmpty(st)) {
            mSocket.emit("username", st);
        }
    }

    public void connect() {
        if (mSocket == null) {
            mSocket = socketManager.getSocket();

            mSocket.on("usernameBack", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        char ch = data.getString("role").charAt(0);
                        socketManager.setUserName(data.getString("name"));
                        socketManager.setUserId(Integer.parseInt(data.getString("userId")));
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        switch (ch) {
                            case 'b':
                                socketManager.setBuyer(true);
                                ft.replace(R.id.activity_base_frame_layout,new VendorListFragment()).addToBackStack(null);
                                break;
                            case 'v':
                                socketManager.setBuyer(false);
                                ft.replace(R.id.activity_base_frame_layout, new MessageFragment()).addToBackStack(null);
                                break;
                        }
                        ft.commit();
                    } catch (ClassCastException e) {
                        char ch = args[0].toString().charAt(0);
                        String st;
                        switch (ch) {
                            case 'c':
                                st = "Already Logged In";
                                break;
                            case 'n':
                                st = "No Such User";
                                break;
                            default:
                                st = "Database Error";
                                break;
                        }
                        final String s = st;
                        LoginFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginFragment.this.getActivity(), s, Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {

                    }
                }
            });
            mSocket.connect();
        }
    }
}
