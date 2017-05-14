package com.alexkorrnd.diplomapp.presentation.contact.detail;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactDetailsEntity;
import com.alexkorrnd.diplomapp.data.db.contact.entity.DetailTypeEntity;
import com.alexkorrnd.diplomapp.data.db.contact.tables.ContactDetailesTable;
import com.alexkorrnd.diplomapp.data.db.contact.tables.DetailTypesTable;
import com.alexkorrnd.diplomapp.domain.Contact;
import com.alexkorrnd.diplomapp.domain.DetailType;
import com.alexkorrnd.diplomapp.domain.mappers.Mapper;
import com.alexkorrnd.diplomapp.presentation.BasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;


public class ContactDetailesPresenter implements BasePresenter {

    private static final String TAG = ContactDetailesPresenter.class.getSimpleName();

    interface View {
        void showProgress();

        void hideProgress();

        void onDetailesLoadedSuccess(List<DetailType> detailTypes);
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

    public void loadDetailes() {
        Single.zip(getDetails(), getDetailTypes(), Pair::create)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listListPair -> {
                    final List<DetailType> detailTypes = new ArrayList<>();
                    for (ContactDetailsEntity contactDetailsEntity: listListPair.first) {
                        detailTypes.add(Mapper.mapTo(getDetailTypeEntityByKey(listListPair.second, contactDetailsEntity.getTypeId()),
                                contactDetailsEntity));
                    }
                    view.onDetailesLoadedSuccess(detailTypes);
                });
    }

    private DetailTypeEntity getDetailTypeEntityByKey(List<DetailTypeEntity> detailTypeEntities,
                                                      String key) {
        Log.d(TAG, "getDetailTypeEntityByKey:: key = " + key);
        for (DetailTypeEntity entity: detailTypeEntities) {
            if (entity.getKey().equals(key)) {
                return entity;
            }
        }
        return null;
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
