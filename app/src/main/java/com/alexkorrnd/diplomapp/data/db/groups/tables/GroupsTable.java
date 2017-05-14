package com.alexkorrnd.diplomapp.data.db.groups.tables;


import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

public class GroupsTable {

    @NonNull
    public static final String TABLE = "groups";

    @NonNull
    public static final String COLUMN_ID = "gid";

    @NonNull
    public static final String COLUMN_TITLE = "title";

    @NonNull
    public static final String COLUMN_SHORT_TITLE = "short_title";


    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    private GroupsTable() {
        throw new IllegalStateException("Not today");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_SHORT_TITLE + " TEXT "
                + ");";
    }

}
