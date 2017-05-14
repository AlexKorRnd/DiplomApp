package com.alexkorrnd.diplomapp.data.db.groups.resolver;

import android.support.annotation.NonNull;

import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionsWithParentEntity;
import com.alexkorrnd.diplomapp.data.db.groups.tables.GroupsTable;
import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class RegionWithGroupPutResolver extends PutResolver<RegionsWithParentEntity> {

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull RegionsWithParentEntity object) {
        final PutResults<Object> putResults = storIOSQLite
                .put()
                .objects(Arrays.asList(object.getGroupEntity(), object.getRegionEntity()))
                .prepare() // BTW: it will use transaction!
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<String>(2);

        affectedTables.add(GroupsTable.TABLE);
        affectedTables.add(RegionsTable.TABLE);

        // Actually, it's not very clear what PutResult should we return here…
        // Because there is no table for this pair of tweet and user
        // So, let's just return Update Result
        return PutResult.newUpdateResult(putResults.numberOfUpdates(), affectedTables);
    }

}
