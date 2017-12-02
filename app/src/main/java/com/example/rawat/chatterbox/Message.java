package com.example.rawat.chatterbox;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

/**
 * Created by WIN10 on 11/30/2017.
 */

public class Message {
    private static final String MESSAGE_TO = "MESSAGE_TO";
    private static final String MESSAGE_FROM = "MESSAGE_FROM";
    private static final String MESSAGE_TEXT = "MESSAGE";
    private static final String MESSAGE_TIME = "TIME";
    private static final String MESSAGE_EVENT_ID = "EVENT_ID";

    @SerializedName("MESSAGE_TO")
    private int to;
    @SerializedName("MESSAGE_FROM")
    private int from;
    @SerializedName("TIME")
    private long time;
    @SerializedName("MESSAGE")
    private String message;
    @SerializedName("EVENT_ID")
    private long eventId;

    public static Message fromJson(JSONObject jsonObject) {
        Message message = new Message();
        try {
            message.setTo(jsonObject.getInt(MESSAGE_TO));
            message.setFrom(jsonObject.getInt(MESSAGE_FROM));
            message.setMessage(jsonObject.getString(MESSAGE_TEXT));
            message.setTime(jsonObject.getLong(MESSAGE_TIME));
            message.setEventId(jsonObject.getLong(MESSAGE_EVENT_ID));
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
            return message;
        }
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time + TimeZone.getDefault().getOffset(time);
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public JSONObject toJson(long time) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(MESSAGE_TO, to);
            jsonObject.put(MESSAGE_FROM, from);
            jsonObject.put(MESSAGE_TEXT, message);
            jsonObject.put(MESSAGE_TIME, time);
            jsonObject.put(MESSAGE_EVENT_ID, eventId);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}