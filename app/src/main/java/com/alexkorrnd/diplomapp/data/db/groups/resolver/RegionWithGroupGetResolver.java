package com.alexkorrnd.diplomapp.data.db.groups.resolver;


import android.database.Cursor;
import android.support.annotation.NonNull;

import com.alexkorrnd.diplomapp.data.db.groups.entity.GroupEntity;
import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionEntity;
import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionsWithParentEntity;
import com.alexkorrnd.diplomapp.data.db.groups.tables.GroupsTable;
import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

public class RegionWithGroupGetResolver extends DefaultGetResolver<RegionsWithParentEntity> {

    @NonNull
    @Override
    public RegionsWithParentEntity mapFromCursor(@NonNull Cursor cursor) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setGid(cursor.getString(cursor.getColumnIndexOrThrow(GroupsTable.COLUMN_ID)));
        groupEntity.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(GroupsTable.COLUMN_TITLE)));
        groupEntity.setShortTitle(cursor.getString(cursor.getColumnIndexOrThrow(GroupsTable.COLUMN_SHORT_TITLE)));

        RegionEntity regionEntity = new RegionEntity();
        regionEntity.setGid(cursor.getString(cursor.getColumnIndexOrThrow(RegionsTable.COLUMN_ID)));
        regionEntity.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(RegionsTable.COLUMN_TITLE)));
        regionEntity.setShortTitle(cursor.getString(cursor.getColumnIndexOrThrow(RegionsTable.COLUMN_SHORT_TITLE)));
        regionEntity.setGroupId(cursor.getString(cursor.getColumnIndexOrThrow(RegionsTable.COLUMN_PARENT_ID)));

        return new RegionsWithParentEntity(regionEntity, groupEntity);
    }
}
