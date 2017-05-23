package com.alexkorrnd.diplomapp.presentation.contact.detail;

import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactDetailsEntity;
import com.alexkorrnd.diplomapp.data.db.contact.entity.DetailTypeEntity;
import com.alexkorrnd.diplomapp.data.db.contact.tables.ContactDetailesTable;
import com.alexkorrnd.diplomapp.data.db.contact.tables.DetailTypesTable;
import com.alexkorrnd.diplomapp.data.db.groups.entity.GroupEntity;
import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionEntity;
import com.alexkorrnd.diplomapp.data.db.groups.tables.GroupsTable;
import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable;
import com.alexkorrnd.diplomapp.domain.Contact;
import com.alexkorrnd.diplomapp.domain.DetailType;
import com.alexkorrnd.diplomapp.domain.Group;
import com.alexkorrnd.diplomapp.domain.Region;
import com.alexkorrnd.diplomapp.domain.mappers.Mapper;
import com.alexkorrnd.diplomapp.presentation.BasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ContactDetailesPresenter implements BasePresenter {

    private static final String TAG = ContactDetailesPresenter.class.getSimpleName();

    interface View {
        void showProgress();

        void hideProgress();

        void onDetailesLoadedSuccess(List<DetailType> detailTypes);

        void onParentsLoaded(Region childSearchableRegion, List<Region> parents);
        void onGroupLoaded(Group group);
    }

    private final static int PAGE_SIZE = 15;

    private final View view;
    private final SQLiteOpenHelper sqLiteOpenHelper;
    private final StorIOSQLite storIOSQLite;

    private Contact contact;

    public ContactDetailesPresenter(View view, SQLiteOpenHelper sqLiteOpenHelper,
                                    StorIOSQLite storIOSQLite) {
        this.view = view;
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.storIOSQLite = storIOSQLite;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    private Single<List<ContactDetailsEntity>> getDetails() {
        return storIOSQLite.get()
                .listOfObjects(ContactDetailsEntity.class)
                .withQuery(Query.builder()
                        .table(ContactDetailesTable.TABLE)
                        .where(ContactDetailesTable.COLUMN_CONTACT_ID + " =?")
                        .whereArgs(contact.getId())
                        .build())
                .prepare()
                .asRxSingle();
    }

    private Single<List<DetailTypeEntity>> getDetailTypes() {
        return storIOSQLite
                .get()
                .listOfObjects(DetailTypeEntity.class)
                .withQuery(Query.builder()
                        .table(DetailTypesTable.TABLE)
                        .build())
                .prepare()
                .asRxSingle();
    }

    private void getGroupById(String id) {
        storIOSQLite
                .get()
                .object(GroupEntity.class)
                .withQuery(GroupsTable.QUERY_ALL)
                .prepare()
                .asRxSingle()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    view.onGroupLoaded(Mapper.mapTo(entity));
                });
    }

    public void loadDetailes() {
        Single.zip(getDetails(), getDetailTypes(), Pair::create)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listListPair -> {
                    final List<DetailType> detailTypes = new ArrayList<>();
                    for (ContactDetailsEntity contactDetailsEntity : listListPair.first) {
                        detailTypes.add(Mapper.mapTo(getDetailTypeEntityByKey(listListPair.second, contactDetailsEntity.getTypeId()),
                                contactDetailsEntity));
                    }
                    getGroupById(contact.getId());
                    view.onDetailesLoadedSuccess(detailTypes);
                });
    }


    private DetailTypeEntity getDetailTypeEntityByKey(List<DetailTypeEntity> detailTypeEntities,
                                                      String key) {
        Log.d(TAG, "getDetailTypeEntityByKey:: key = " + key);
        for (DetailTypeEntity entity : detailTypeEntities) {
            if (entity.getKey().equals(key)) {
                return entity;
            }
        }
        return null;
    }

    private Single<List<RegionEntity>> getRegions(String parentId, int offset) {
        Query query = Query.builder()
                .table(RegionsTable.TABLE)
                .limit(offset, PAGE_SIZE)
                .where(RegionsTable.COLUMN_ID + " =?")
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

    public void findAllParents(Region childSearchableRegion) {

        findAllParents(childSearchableRegion, new ArrayList<>(), childSearchableRegion);
    }

    private void findAllParents(Region childSearchableRegion, List<Region> curParents, Region curChildRegion) {
        if (TextUtils.isEmpty(curChildRegion.getParentRegionId())) {
            view.onParentsLoaded(childSearchableRegion, curParents);
        } else {
            getRegions(curChildRegion.getParentRegionId(), 0)
                    .map(ContactDetailesPresenter::listMapTo)
                    .subscribe(new SingleSubscriber<List<Region>>() {
                        @Override
                        public void onSuccess(List<Region> regions) {
                            final Region curParent = regions.get(0);
                            curParents.add(curParent);
                            findAllParents(childSearchableRegion, curParents, curParent);
                        }

                        @Override
                        public void onError(Throwable error) {

                        }
                    });
        }

    }

    private static List<Region> listMapTo(List<RegionEntity> entities) {
        final List<Region> regions = new ArrayList<>();
        for (RegionEntity entity : entities) {
            regions.add(Mapper.mapTo(entity));
        }
        return regions;
    }

    @Override
    public void start() {
        view.showProgress();
        loadDetailes();
    }


    @Override
    public void stop() {

    }
}
