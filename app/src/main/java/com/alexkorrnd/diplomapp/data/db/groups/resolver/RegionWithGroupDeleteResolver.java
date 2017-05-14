package com.alexkorrnd.diplomapp.data.db.groups.resolver;

import android.support.annotation.NonNull;

import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionsWithParentEntity;
import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;



public class RegionWithGroupDeleteResolver extends DeleteResolver<RegionsWithParentEntity> {

    @NonNull
    @Override
    public DeleteResult performDelete(@NonNull StorIOSQLite storIOSQLite, @NonNull RegionsWithParentEntity object) {
        storIOSQLite.delete()
                .object(object.getRegionEntity())
                .prepare()
                .executeAsBlocking();

        return DeleteResult.newInstance(1, RegionsTable.TABLE);
    }
}
