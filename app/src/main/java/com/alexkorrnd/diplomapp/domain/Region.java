package com.alexkorrnd.diplomapp.domain;


import android.os.Parcel;
import android.os.Parcelable;

public class Region implements Parcelable {
    String gid;

    String title;
    String shortTitle;

    Group group;

    public Region(String gid, String title, String shortTitle, Group group) {
        this.gid = gid;
        this.title = title;
        this.shortTitle = shortTitle;
        this.group = group;
    }

    protected Region(Parcel in) {
        gid = in.readString();
        title = in.readString();
        shortTitle = in.readString();
        group = in.readParcelable(Group.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gid);
        dest.writeString(title);
        dest.writeString(shortTitle);
        dest.writeParcelable(group, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Region> CREATOR = new Creator<Region>() {
        @Override
        public Region createFromParcel(Parcel in) {
            return new Region(in);
        }

        @Override
        public Region[] newArray(int size) {
            return new Region[size];
        }
    };

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGid() {
        return gid;
    }

    public String getTitle() {
        return title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public Group getGroup() {
        return group;
    }
}
