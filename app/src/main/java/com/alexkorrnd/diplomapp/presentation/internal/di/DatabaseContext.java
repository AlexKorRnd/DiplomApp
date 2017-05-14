package com.alexkorrnd.diplomapp.presentation.internal.di;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import static com.alexkorrnd.diplomapp.Consts.LOG_TAG;


class DatabaseContext extends ContextWrapper {

    private static final String TAG = LOG_TAG + "Database";
    private static final String APP_NAME =  "geoPhone";

    public DatabaseContext(Context base) {
        super(base);
        Log.d(TAG, "DatabaseContext");
    }

    @Override
    public File getDatabasePath(String name) {
        File sdcard = Environment.getExternalStorageDirectory();
        String dbfile = sdcard.getAbsolutePath() + File.separator + APP_NAME + File.separator + name;

        if (!dbfile.endsWith(".db"))
        {
            dbfile += ".db" ;
        }

        File result = new File(dbfile);

        if (!result.getParentFile().exists())
        {
            result.getParentFile().mkdirs();
        }

        Log.d(TAG, "getDatabasePath(" + name + ") = " + result.getAbsolutePath());

        return result;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory)
    {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        // SQLiteDatabase result = super.openOrCreateDatabase(name, mode, factory);

        Log.w(TAG, "openOrCreateDatabase(" + name + ",,) = " + result.getPath());

        return result;
    }


}
