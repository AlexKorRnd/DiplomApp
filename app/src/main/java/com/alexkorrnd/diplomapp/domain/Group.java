package com.alexkorrnd.diplomapp.domain;


import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {

    String gid;

    String title;
    String shortTitle;

    public Group(String gid, String title, String shortTitle) {
        this.gid = gid;
        this.title = title;
        this.shortTitle = shortTitle;
    }

    protected Group(Parcel in) {
        gid = in.readString();
        title = in.readString();
        shortTitle = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gid);
        dest.writeString(title);
        dest.writeString(shortTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }
}
