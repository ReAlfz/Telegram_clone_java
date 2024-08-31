package alf.java.telegram.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {
    String icon, uid, username;

    public User() {}

    public User(String username, String uid, String icon) {
        this.icon = icon;
        this.uid = uid;
        this.username = username;
    }

    protected User(Parcel source) {
        username = source.readString();
        uid = source.readString();
        icon = source.readString();
    }

    public static User fromMap(Map<String, Object> map) {
        return new User(
                (String) map.get("username"),
                (String) map.get("uid"),
                (String) map.get("icon")

        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("uid", uid);
        map.put("icon", icon);
        return map;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(uid);
        dest.writeString(icon);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUid() {
        return uid;
    }
    public String getUsername() {
        return username;
    }
    public String getIcon() {
        return icon;
    }
}
