package com.alexkorrnd.diplomapp.presentation.contact.list;


import android.database.sqlite.SQLiteOpenHelper;

import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactEntity;
import com.alexkorrnd.diplomapp.data.db.contact.tables.ContactsTable;
import com.alexkorrnd.diplomapp.domain.Contact;
import com.alexkorrnd.diplomapp.domain.Region;
import com.alexkorrnd.diplomapp.domain.mappers.Mapper;
import com.alexkorrnd.diplomapp.presentation.BasePresenter;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;


public class ContactPresenter implements BasePresenter {

    interface View {
        void showProgress();
        void hideProgress();
        void onContactsLoadedSuccess(List<Contact> contacts);
    }

    private final static int PAGE_SIZE = 15;

    private final View view;
    private final SQLiteOpenHelper sqLiteOpenHelper;
    private final StorIOSQLite storIOSQLite;

    private Region region;

    public ContactPresenter(View view, SQLiteOpenHelper sqLiteOpenHelper,
                           StorIOSQLite storIOSQLite) {
        this.view = view;
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.storIOSQLite = storIOSQLite;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void loadContacts() {
        loadContacts(0);
    }

    public void loadContacts(int offset) {
        storIOSQLite
                .get()
                .listOfObjects(ContactEntity.class)
                .withQuery(Query.builder()
                        .table(ContactsTable.TABLE)
                        .limit(offset, PAGE_SIZE)
                        .where(ContactsTable.COLUMN_REGION_ID + " =?")
                        .whereArgs(region.getGid())
                        .build())
                .prepare()
                .asRxSingle()
                .subscribe(entities -> {
                    view.hideProgress();
                    view.onContactsLoadedSuccess(listMapTo(entities));
                });
    }

    private List<Contact> listMapTo(List<ContactEntity> entities) {
        final List<Contact> contacts = new ArrayList<>();
        for (ContactEntity entity: entities) {
            final Contact contact = Mapper.mapTo(entity);
            contact.setRegion(region);
            contacts.add(contact);
        }
        return contacts;
    }

    @Override
    public void start() {
        view.showProgress();
        loadContacts();
    }

    @Override
    public void stop() {
    }

}
