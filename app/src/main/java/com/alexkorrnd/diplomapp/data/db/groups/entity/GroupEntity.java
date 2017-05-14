package com.alexkorrnd.diplomapp.data.db.groups.entity;


import com.alexkorrnd.diplomapp.data.db.groups.tables.GroupsTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = GroupsTable.TABLE)
public class GroupEntity {

    @StorIOSQLiteColumn(name = GroupsTable.COLUMN_ID, key = true)
    String gid;

    @StorIOSQLiteColumn(name = GroupsTable.COLUMN_TITLE)
    String title;

    @StorIOSQLiteColumn(name = GroupsTable.COLUMN_SHORT_TITLE)
    String shortTitle;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
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
