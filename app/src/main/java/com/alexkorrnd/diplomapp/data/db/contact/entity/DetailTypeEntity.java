package com.alexkorrnd.diplomapp.data.db.contact.entity;


import com.alexkorrnd.diplomapp.data.db.contact.tables.DetailTypesTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = DetailTypesTable.TABLE)
public class DetailTypeEntity {

    @StorIOSQLiteColumn(name = DetailTypesTable.COLUMN_KEY, key = true)
    String key;

    @StorIOSQLiteColumn(name = DetailTypesTable.COLUMN_TITLE)
    String title;

    @StorIOSQLiteColumn(name = DetailTypesTable.COLUMN_SHORT_TITLE)
    String shortTitle;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }
}
