package com.alexkorrnd.diplomapp.data.db.contact.entity;


import com.alexkorrnd.diplomapp.data.db.contact.tables.ContactDetailesTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = ContactDetailesTable.TABLE)
public class ContactDetailsEntity {

    @StorIOSQLiteColumn(name = ContactDetailesTable.COLUMN_GID, key = true)
    String gid;

    @StorIOSQLiteColumn(name = ContactDetailesTable.COLUMN_CONTACT_ID)
    String contactId;

    @StorIOSQLiteColumn(name = ContactDetailesTable.COLUMN_TYPE_ID)
    String typeId;

    @StorIOSQLiteColumn(name = ContactDetailesTable.COLUMN_VALUE)
    String value;

    public String getGid() {
        return gid;
    }

    public String getContactId() {
        return contactId;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getValue() {
        return value;
    }
}
