package com.alexkorrnd.diplomapp.domain;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Contact implements Parcelable {

    private String id;

    private Region region;

    private String fullName;

    private String comment;

    private String address;

    private List<DetailType> detailTypes;

    private Group group;

    public Contact(String id, Region region, String fullName, String comment, String address) {
        this.id = id;
        this.region = region;
        this.fullName = fullName;
        this.comment = comment;
        this.address = address;
    }

    protected Contact(Parcel in) {
        id = in.readString();
        region = in.readParcelable(Region.class.getClassLoader());
        fullName = in.readString();
        comment = in.readString();
        address = in.readString();
        detailTypes = in.createTypedArrayList(DetailType.CREATOR);
        group = in.readParcelable(Group.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(region, flags);
        dest.writeString(fullName);
        dest.writeString(comment);
        dest.writeString(address);
        dest.writeTypedList(detailTypes);
        dest.writeParcelable(group, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<DetailType> getDetailTypes() {
        return detailTypes;
    }

    public void setDetailTypes(List<DetailType> detailTypes) {
        this.detailTypes = detailTypes;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
