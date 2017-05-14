package com.alexkorrnd.diplomapp.data.db.contact.resolver;


import android.database.Cursor;
import android.support.annotation.NonNull;

import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactWithDetailEntity;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

public class ContactsWithDetailsGetResolver extends DefaultGetResolver<ContactWithDetailEntity> {

    @NonNull
    @Override
    public ContactWithDetailEntity mapFromCursor(@NonNull Cursor cursor) {
        return null;
    }
}
