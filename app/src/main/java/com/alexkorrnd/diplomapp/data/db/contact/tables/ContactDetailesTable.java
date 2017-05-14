package com.alexkorrnd.diplomapp.data.db.contact.tables;


import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import com.pushtorefresh.storio.sqlite.queries.Query;


public class ContactDetailesTable {

    @NonNull
    public static final String TABLE = "contact_details";

    @NonNull
    public static final String COLUMN_GID = "gid";

    @NonNull
    public static final String COLUMN_CONTACT_ID = "contact_id";

    @NonNull
    public static final String COLUMN_TYPE_ID = "type_id";

    @NonNull
    public static final String COLUMN_VALUE = "value";

    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    private ContactDetailesTable() {
        throw new IllegalStateException("Not today");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + COLUMN_GID + " TEXT NOT NULL PRIMARY KEY, "
                + COLUMN_CONTACT_ID + " TEXT NOT NULL, "
                + COLUMN_TYPE_ID + " TEXT NOT NULL, "
                + COLUMN_VALUE + " TEXT NOT NULL, "
                + "CONSTRAINT " + TABLE  + "_" + ContactsTable.TABLE + "_fk FOREIGN KEY (" + COLUMN_CONTACT_ID + ")"
                + "REFERENCES "
                + ContactsTable.TABLE
                + "(" + ContactsTable.COLUMN_GID + ")"
                + " ON DELETE CASCADE ON UPDATE CASCADE "
                + ");";
    }

}
