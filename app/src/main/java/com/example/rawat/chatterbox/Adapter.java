package com.example.rawat.chatterbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by WIN10 on 11/30/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private static final int OUTGOING_FIRST = 0;
    private static final int OUTGOING_NOT_FIRST = 1;
    private static final int INCOMING_FIRST = 2;
    private static final int INCOMING_NOT_FIRST = 3;

    ArrayList<Message> messages = new ArrayList<>();
    SocketManager socketManager;

    public Adapter(Context context) {
        socketManager = SocketManager.get(context);
    }

    public void setMessages(ArrayList<Message> messages, int otherId) {
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            if (message.getFrom() == otherId || message.getTo() == otherId)
                this.messages.add(message);
        }
    }

    public void addMessage(Message message) {
        if (message == null)
            return;
        messages.add(message);
        Log.d("dexter", "addMessage: " + message);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int id = 0;
        switch (viewType) {
            case OUTGOING_FIRST:
                id = R.layout.row_message_outgoing_first;
                break;
            case OUTGOING_NOT_FIRST:
                id = R.layout.row_message_outgoing;
                break;
            case INCOMING_FIRST:
                id = R.layout.row_message_incoming_first;
                break;
            case INCOMING_NOT_FIRST:
                id = R.layout.row_message_incoming;
                break;
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("dexter", "onBindViewHolder: " + position);
        holder.textView.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("dexter", "getItemViewType: " + position + "   " + messages.size());
        if (messages.get(position).getFrom() == socketManager.getUserId()) {
            if (position != 0 && messages.get(position - 1).getFrom() == socketManager.getUserId())
                return OUTGOING_NOT_FIRST;
            return OUTGOING_FIRST;
        } else {
            if (position == 0 || messages.get(position - 1).getFrom() == socketManager.getUserId())
                return INCOMING_FIRST;
            return INCOMING_NOT_FIRST;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
