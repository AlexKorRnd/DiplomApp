package com.alexkorrnd.diplomapp.data.db.groups.entity;


import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = RegionsTable.TABLE)
public class RegionEntity {

    @StorIOSQLiteColumn(name = RegionsTable.COLUMN_ID, key = true)
    String gid;

    @StorIOSQLiteColumn(name = RegionsTable.COLUMN_TITLE)
    String title;

    @StorIOSQLiteColumn(name = RegionsTable.COLUMN_SHORT_TITLE)
    String shortTitle;

    @StorIOSQLiteColumn(name = RegionsTable.COLUMN_PARENT_ID)
    String parentId;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }
}
