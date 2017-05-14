package com.alexkorrnd.diplomapp.presentation.regions;


import android.database.sqlite.SQLiteOpenHelper;

import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionsWithParentEntity;
import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable;
import com.alexkorrnd.diplomapp.domain.Region;
import com.alexkorrnd.diplomapp.presentation.BasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import rx.Single;

import static com.alexkorrnd.diplomapp.domain.mappers.Mapper.mapTo;

public class RegionsPresenter implements BasePresenter {

    interface View {
        void showProgress();
        void hideProgress();
        void onRegionsLoadedSuccess(List<Region> groups);
        void onNavigateToRegions(Region region);
        void onNavigateToContacts(Region region);
    }

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
        getRegions(offset)
                .subscribe(groups -> {
                    view.hideProgress();
                    view.onRegionsLoadedSuccess(listMapTo(groups));
                });
    }

    private Single<List<RegionsWithParentEntity>> getRegions(int offset) {
        return storIOSQLite
                .get()
                .listOfObjects(RegionsWithParentEntity.class)
                .withQuery(Query.builder()
                        .table(RegionsTable.TABLE)
                        .limit(offset, PAGE_SIZE)
                        .where(RegionsTable.COLUMN_PARENT_ID + " =?")
                        .whereArgs(parentId)
                        .build())
                .prepare()
                .asRxSingle();
    }

    private static List<Region> listMapTo(List<RegionsWithParentEntity> entities) {
        final List<Region> regions = new ArrayList<>();
        for (RegionsWithParentEntity entity: entities) {
            regions.add(mapTo(entity));
        }
        return regions;
    }



    public void navigateToNextScreen(Region region) {
        getRegions(0)
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
