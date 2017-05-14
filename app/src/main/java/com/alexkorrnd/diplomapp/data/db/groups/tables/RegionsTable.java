package com.alexkorrnd.diplomapp.data.db.groups.tables;


import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

public class RegionsTable {

    @NonNull
    public static final String TABLE = "regions";

    @NonNull
    public static final String COLUMN_ID = "gid";

    @NonNull
    public static final String COLUMN_TITLE = "title";

    @NonNull
    public static final String COLUMN_SHORT_TITLE = "short_title";

    @NonNull
    public static final String COLUMN_PARENT_ID = "parent_id";

    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    private RegionsTable() {
        throw new IllegalStateException("Not today");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_SHORT_TITLE + " TEXT, "
                + COLUMN_PARENT_ID + " TEXT "
                + ");";
    }

}
