package alf.java.telegram.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Chat implements Parcelable {
    String fromUId, uid, text;
    long timestamp;
    boolean sender;

    public Chat() {}

    public Chat(String uid, String fromUid, String text, long timestamp, boolean sender) {
        this.uid = uid;
        this.fromUId = fromUid;
        this.text = text;
        this.timestamp = timestamp;
        this.sender = sender;
    }

    protected Chat(Parcel source) {
        uid = source.readString();
        fromUId = source.readString();
        text = source.readString();
        timestamp = source.readInt();
    }

    public static Chat fromMap(Map<String, Object> map) {
        return new Chat(
                (String) map.get("uid"),
                (String) map.get("fromUid"),
                (String) map.get("text"),
                (Long) map.get("timestamp"),
                (Boolean) map.get("sender")
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("fromUid", fromUId);
        map.put("text", text);
        map.put("timestamp", timestamp);
        map.put("sender", sender);
        return map;
    }

    public static Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel source) {
            return new Chat(source);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(fromUId);
        dest.writeString(text);
        dest.writeLong(timestamp);
    }

    public String getText() {
        return text;
    }
    public Boolean getSender() {return sender;}
}
