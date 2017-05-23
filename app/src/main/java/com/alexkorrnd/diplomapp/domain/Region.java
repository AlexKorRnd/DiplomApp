package com.alexkorrnd.diplomapp.domain;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class Region implements Parcelable{
    private String gid;

    private String title;
    private String shortTitle;

    @Nullable
    private String parentRegionId;

    public Region(String gid, String title, String shortTitle) {
        this(gid, title, shortTitle, null);
    }

    public Region(String gid, String title, String shortTitle, String parentRegionId) {
        this.gid = gid;
        this.title = title;
        this.shortTitle = shortTitle;
        this.parentRegionId = parentRegionId;
    }

    protected Region(Parcel in) {
        gid = in.readString();
        title = in.readString();
        shortTitle = in.readString();
        parentRegionId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gid);
        dest.writeString(title);
        dest.writeString(shortTitle);
        dest.writeString(parentRegionId);
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

    public String getGid() {
        return gid;
    }

    public String getTitle() {
        return title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    @Nullable
    public String getParentRegionId() {
        return parentRegionId;
    }

    public void setParentRegionId(@Nullable String parentRegionId) {
        this.parentRegionId = parentRegionId;
    }
}
