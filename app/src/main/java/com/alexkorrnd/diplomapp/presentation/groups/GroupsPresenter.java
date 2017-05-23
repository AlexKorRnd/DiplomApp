package com.alexkorrnd.diplomapp.presentation.groups;


import android.database.sqlite.SQLiteOpenHelper;

import com.alexkorrnd.diplomapp.data.db.groups.entity.GroupEntity;
import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable;
import com.alexkorrnd.diplomapp.domain.Group;
import com.alexkorrnd.diplomapp.domain.mappers.Mapper;
import com.alexkorrnd.diplomapp.presentation.BasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;


public class GroupsPresenter implements BasePresenter {

    interface View {
        void showProgress();
        void hideProgress();
        void onGroupsLoadedSuccess(List<Group> groups);
    }

    private final static int PAGE_SIZE = 15;

    private final View view;
    private final SQLiteOpenHelper sqLiteOpenHelper;
    private final StorIOSQLite storIOSQLite;

    public GroupsPresenter(View view, SQLiteOpenHelper sqLiteOpenHelper,
                           StorIOSQLite storIOSQLite) {
        this.view = view;
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.storIOSQLite = storIOSQLite;
    }

    public void loadGroups() {
        loadGroups(0);
    }

    public void loadGroups(int offset) {
        storIOSQLite
                .get()
                .listOfObjects(GroupEntity.class)
                .withQuery(Query.builder()
                        .table(RegionsTable.TABLE)
                        .limit(offset, PAGE_SIZE)
                        .build())
                .prepare()
                .asRxSingle()
                .subscribe(new Action1<List<GroupEntity>>() {
                    @Override
                    public void call(List<GroupEntity> groups) {
                        view.hideProgress();
                        view.onGroupsLoadedSuccess(listMapTo(groups));
                    }
                });
    }

    private List<Group> listMapTo(List<GroupEntity> groupEntities) {
        final List<Group> groups = new ArrayList<>();
        for (GroupEntity groupEntity: groupEntities) {
            groups.add(Mapper.mapTo(groupEntity));
        }
        return groups;
    }

    @Override
    public void start() {
        view.showProgress();
        loadGroups();
    }

    @Override
    public void stop() {
    }
}
