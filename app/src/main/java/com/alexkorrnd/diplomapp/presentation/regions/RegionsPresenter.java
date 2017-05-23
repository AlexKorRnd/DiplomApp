package com.alexkorrnd.diplomapp.presentation.regions;


import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionEntity;
import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable;
import com.alexkorrnd.diplomapp.domain.Region;
import com.alexkorrnd.diplomapp.domain.mappers.Mapper;
import com.alexkorrnd.diplomapp.presentation.BasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import rx.Single;

public class RegionsPresenter implements BasePresenter {

    interface View {
        void showProgress();
        void hideProgress();
        void onRegionsLoadedSuccess(List<Region> groups);
        void onNavigateToRegions(Region region);
        void onNavigateToContacts(Region region);
    }

    private final static String TAG = RegionsPresenter.class.getSimpleName();

    private final static int PAGE_SIZE = 15;

    private final View view;
    private final SQLiteOpenHelper sqLiteOpenHelper;
    private final StorIOSQLite storIOSQLite;

    private String parentId;

    public RegionsPresenter(View view, SQLiteOpenHelper sqLiteOpenHelper,
                           StorIOSQLite storIOSQLite) {
        this.view = view;
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.storIOSQLite = storIOSQLite;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void loadRegions() {
        loadRegions(0);
    }

    public void loadRegions(int offset) {
        getRegions(parentId, offset)
                .subscribe(regions -> {
                    view.hideProgress();
                    view.onRegionsLoadedSuccess(listMapTo(regions));
                });
    }

    private Single<List<RegionEntity>> getRegions(String parentId, int offset) {
        String whereQuery = RegionsTable.COLUMN_PARENT_ID + " =?";
        if (parentId == null) {
            whereQuery += " OR " + RegionsTable.COLUMN_PARENT_ID + " = \"\"";
        }
        Query query = Query.builder()
                .table(RegionsTable.TABLE)
                .limit(offset, PAGE_SIZE)
                .where(whereQuery)
                .whereArgs(parentId)
                .orderBy(RegionsTable.COLUMN_TITLE)
                .build();
        Log.d(TAG, query.toString());
        return storIOSQLite
                .get()
                .listOfObjects(RegionEntity.class)
                .withQuery(query)
                .prepare()
                .asRxSingle();
    }



    private static List<Region> listMapTo(List<RegionEntity> entities) {
        final List<Region> regions = new ArrayList<>();
        for (RegionEntity entity: entities) {
            regions.add(Mapper.mapTo(entity));
        }
        return regions;
    }



    public void navigateToNextScreen(Region region) {
        setParentId(region.getGid());
        getRegions(parentId, 0)
                .subscribe(groups -> {
                    if (groups.isEmpty()) {
                        view.onNavigateToContacts(region);
                    } else {
                        view.onNavigateToRegions(region);
                    }
                });
    }

    @Override
    public void start() {
        view.showProgress();
        loadRegions();
    }

    @Override
    public void stop() {
    }
}
