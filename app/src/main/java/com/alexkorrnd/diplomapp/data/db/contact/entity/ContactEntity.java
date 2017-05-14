package com.alexkorrnd.diplomapp.data.db.contact.entity;


import com.alexkorrnd.diplomapp.data.db.contact.tables.ContactsTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = ContactsTable.TABLE)
public class ContactEntity {

    @StorIOSQLiteColumn(name = ContactsTable.COLUMN_GID, key = true)
    String key;

    @StorIOSQLiteColumn(name = ContactsTable.COLUMN_REGION_ID)
    String regionId;

    @StorIOSQLiteColumn(name = ContactsTable.COLUMN_FULL_NAME)
    String fullName;

    @StorIOSQLiteColumn(name = ContactsTable.COLUMN_COMMENT)
    String comment;

    @StorIOSQLiteColumn(name = ContactsTable.COLUMN_ADDRESS)
    String address;

    @StorIOSQLiteColumn(name = ContactsTable.COLUMN_GROUP_ID)
    String groupId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
