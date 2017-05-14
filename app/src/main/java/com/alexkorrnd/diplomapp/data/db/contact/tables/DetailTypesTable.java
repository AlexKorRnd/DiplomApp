package com.alexkorrnd.diplomapp.data.db.contact.tables;


import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

public class DetailTypesTable {

    @NonNull
    public static final String TABLE = "detail_types";

    @NonNull
    public static final String COLUMN_KEY = "key";

    @NonNull
    public static final String COLUMN_TITLE = "title";

    @NonNull
    public static final String COLUMN_SHORT_TITLE = "short_title";


    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    private DetailTypesTable() {
        throw new IllegalStateException("Not today");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + COLUMN_KEY + " TEXT NOT NULL PRIMARY KEY, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_SHORT_TITLE + " TEXT "
                + ");";
    }

}
