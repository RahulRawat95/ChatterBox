package com.example.rawat.chatterbox;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WIN10 on 11/30/2017.
 */

public class MessageFragment extends Fragment {
    private EditText messageEditText;
    private FloatingActionButton sendFab;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private SocketManager socketManager;
    private Socket socket;
    private long eventId = 3584;
    private ArrayList<Message> messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        messageEditText = view.findViewById(R.id.message);
        sendFab = view.findViewById(R.id.sendFab);
        recyclerView = view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new Adapter(getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom != oldBottom) {
                    Log.d("dexter", "onLayoutChange: ");
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });

        recyclerView.setNestedScrollingEnabled(false);

        RetrofitApiInterface apiInterface = SocketManager.getClient().create(RetrofitApiInterface.class);

        Call<ArrayList<Message>> call = apiInterface.retrieveMessages(socketManager.getUserId(), 0, 0, 0);
        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                adapter.setMessages(response.body(), socketManager.getUserId() == 1 ? 2 : 1);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {

            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                messageEditText.setSelection(charSequence.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = messageEditText.getText().toString();
                messageEditText.setText("");
                if (TextUtils.isEmpty(text) || !socket.connected())
                    return;
                final Message message = new Message();
                message.setMessage(text);
                message.setFrom(socketManager.getUserId());
                message.setTo(socketManager.getUserId() != 2 ? 2 : 1);
                message.setEventId(eventId);

                long time = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
                message.setTime(time);

                addMessage(message);

                JSONObject jsonObject = message.toJson(time);

                if (jsonObject == null) {
                    Toast.makeText(MessageFragment.this.getActivity(), "Error Sending Message", Toast.LENGTH_LONG).show();
                    return;
                }
                socket.emit("new message", jsonObject);
            }
        });

        socket.on("recieved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    final JSONObject jsonObject = (JSONObject) args[0];
                    final Message message = Message.fromJson(jsonObject);
                    MessageFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addMessage(message);
                            Log.d("dexter", "call: mymessage" + jsonObject + "   " + message);
                        }
                    });
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messages = new ArrayList<>();

        socketManager = SocketManager.get(getActivity());
        socket = socketManager.getSocket();

    }

    public void addMessage(Message message) {
        messages.add(message);

        if (message.getFrom() == socketManager.getUserId() || message.getFrom() == 1 || message.getFrom() == 2) {
            adapter.addMessage(message);
            int size = adapter.getItemCount();
            adapter.notifyItemInserted(size == 0 ? 0 : size - 1);
            recyclerView.scrollToPosition(size - 1);
        }
    }
}
