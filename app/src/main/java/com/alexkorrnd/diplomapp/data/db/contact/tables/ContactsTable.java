package com.alexkorrnd.diplomapp.data.db.contact.tables;


import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

public class ContactsTable {

    @NonNull
    public static final String TABLE = "contacts";

    @NonNull
    public static final String COLUMN_GID = "gid";

    @NonNull
    public static final String COLUMN_REGION_ID = "region_id";

    @NonNull
    public static final String COLUMN_FULL_NAME = "full_name";

    @NonNull
    public static final String COLUMN_COMMENT = "comment";

    @NonNull
    public static final String COLUMN_ADDRESS = "address";

    @NonNull
    public static final String COLUMN_GROUP_ID = "group_id";

    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    private ContactsTable() {
        throw new IllegalStateException("Not today");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + COLUMN_GID + " TEXT NOT NULL PRIMARY KEY, "
                + COLUMN_REGION_ID + " TEXT, "
                + COLUMN_FULL_NAME + " TEXT, "
                + COLUMN_COMMENT + " TEXT, "
                + COLUMN_ADDRESS + " TEXT, "
                + COLUMN_GROUP_ID + " TEXT "
                + ");";
    }

}
