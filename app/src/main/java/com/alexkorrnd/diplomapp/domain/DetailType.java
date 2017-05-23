package com.alexkorrnd.diplomapp.domain;


import android.os.Parcel;
import android.os.Parcelable;

public class DetailType implements Parcelable{

    public static final String KEY_PHONE = "PHONE";
    public static final String KEY_CELL = "CELL";
    public static final String KEY_EMAIL = "EMAIL";

    String id;
    String key;
    String title;
    String shortTitle;
    String value;

    public DetailType(String id, String key, String title, String shortTitle) {
        this.id = id;
        this.key = key;
        this.title = title;
        this.shortTitle = shortTitle;
    }

    public DetailType(String id, String key, String title, String shortTitle, String value) {
        this.id = id;
        this.key = key;
        this.title = title;
        this.shortTitle = shortTitle;
        this.value = value;
    }

    protected DetailType(Parcel in) {
        id = in.readString();
        key = in.readString();
        title = in.readString();
        shortTitle = in.readString();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(title);
        dest.writeString(shortTitle);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DetailType> CREATOR = new Creator<DetailType>() {
        @Override
        public DetailType createFromParcel(Parcel in) {
            return new DetailType(in);
        }

        @Override
        public DetailType[] newArray(int size) {
            return new DetailType[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DetailType) {
            return this.getKey().equals(((DetailType) obj).getKey());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return key;
    }
}
