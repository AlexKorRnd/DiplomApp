package com.alexkorrnd.diplomapp.presentation.internal.di;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alexkorrnd.diplomapp.data.db.DbOpenHelper;
import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactDetailsEntity;
import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactDetailsEntitySQLiteTypeMapping;
import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactEntity;
import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactEntitySQLiteTypeMapping;
import com.alexkorrnd.diplomapp.data.db.contact.entity.DetailTypeEntity;
import com.alexkorrnd.diplomapp.data.db.contact.entity.DetailTypeEntitySQLiteTypeMapping;
import com.alexkorrnd.diplomapp.data.db.groups.entity.GroupEntity;
import com.alexkorrnd.diplomapp.data.db.groups.entity.GroupEntitySQLiteTypeMapping;
import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionEntity;
import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionEntitySQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import java.io.IOException;

public final class DbModule {

    private final static String LOG_TAG = DbModule.class.getSimpleName();

    private DbModule() {
    }

    private static volatile StorIOSQLite storIOSQLite;
    private static volatile DbOpenHelper sqLiteOpenHelper;

    public static void reset() {
        storIOSQLite = null;
        sqLiteOpenHelper = null;
    }

    @NonNull
    public static StorIOSQLite provideStorIOSQLite(@NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        if (storIOSQLite == null) {
            synchronized (DbModule.class) {
                if (storIOSQLite == null) {
                    storIOSQLite = DefaultStorIOSQLite.builder()
                            .sqliteOpenHelper(sqLiteOpenHelper)
                            .addTypeMapping(RegionEntity.class, new RegionEntitySQLiteTypeMapping())
                            .addTypeMapping(GroupEntity.class, new GroupEntitySQLiteTypeMapping())
                            .addTypeMapping(ContactEntity.class, new ContactEntitySQLiteTypeMapping())
                            .addTypeMapping(DetailTypeEntity.class, new DetailTypeEntitySQLiteTypeMapping())
                            .addTypeMapping(ContactDetailsEntity.class, new ContactDetailsEntitySQLiteTypeMapping())
                            .build();
                }
            }
        }
        return storIOSQLite;
    }

    @NonNull
    public static SQLiteOpenHelper provideSQLiteOpenHelper(@NonNull Context context) {
        Log.d(LOG_TAG, "provideSQLiteOpenHelper:: sqLiteOpenHelper = " + sqLiteOpenHelper);
        if (sqLiteOpenHelper == null) {
            synchronized (DbModule.class) {
                Log.d(LOG_TAG, "provideSQLiteOpenHelper2:: sqLiteOpenHelper = " + sqLiteOpenHelper);
                if (sqLiteOpenHelper == null) {
                    sqLiteOpenHelper = new DbOpenHelper(context);
                    try {
                        sqLiteOpenHelper.createDataBase();
                    } catch (IOException ioe) {
                        throw new Error("Unable to create database");
                    }
                    try {
                        sqLiteOpenHelper.openDataBase();

                    }catch(SQLException sqle){

                        throw sqle;

                    }
                }
            }
        }
        sqLiteOpenHelper.getReadableDatabase();
        return sqLiteOpenHelper;
    }

}
