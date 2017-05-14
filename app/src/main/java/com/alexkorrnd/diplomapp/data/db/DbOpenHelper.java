package com.alexkorrnd.diplomapp.data.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alexkorrnd.diplomapp.Consts;
import com.alexkorrnd.diplomapp.data.db.contact.tables.ContactDetailesTable;
import com.alexkorrnd.diplomapp.data.db.contact.tables.ContactsTable;
import com.alexkorrnd.diplomapp.data.db.contact.tables.DetailTypesTable;
import com.alexkorrnd.diplomapp.data.db.groups.tables.GroupsTable;
import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.R.attr.path;

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = Consts.LOG_TAG + DbOpenHelper.class.getSimpleName();


    private String localDBPath;
    private String externalDBPath;
    public static final String NAME = "geo-phone.sqlite3";
    private static final int VERSION = 1;

    private SQLiteDatabase database;


    public DbOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        localDBPath = context.getDatabasePath(NAME).getPath();
        Log.d(LOG_TAG, "path = " + path);
    }

    public void setExternalDBPath(String externalDBPath) {
        this.externalDBPath = externalDBPath;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GroupsTable.getCreateTableQuery());
        db.execSQL(RegionsTable.getCreateTableQuery());
        db.execSQL(ContactsTable.getCreateTableQuery());
        db.execSQL(DetailTypesTable.getCreateTableQuery());
        db.execSQL(ContactDetailesTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        Log.d(LOG_TAG, "createDataBase:: dbExist = " + dbExist);

        if(dbExist){
            //do nothing - database already exist
        }else{
            updateDatabase();
        }

    }

    public void updateDatabase()throws IOException {
        //By calling this method and empty database will be created into the default system path
        //of your application so we are gonna be able to overwrite that database with our database.
        this.getReadableDatabase();
        loadDataBase();
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            Log.d(LOG_TAG, "checkDataBase:: path = " + localDBPath);
            checkDB = SQLiteDatabase.openDatabase(localDBPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null;
    }


    private void loadDataBase() throws IOException {
        if (externalDBPath == null) {
            return;
        }
        Log.d(LOG_TAG, "copyDataBase:: ");
        //Open your local db as the input stream
        InputStream myInput = new FileInputStream(externalDBPath);

        // Path to the just created empty db
        String outFileName = localDBPath;

        Log.d(LOG_TAG, "copyDataBase:: from myInput = " + externalDBPath + " to " + outFileName);

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void saveDataBase() throws IOException {
        Log.d(LOG_TAG, "copyDataBase:: ");
        //Open your local db as the input stream
        InputStream myInput = new FileInputStream(localDBPath);

        // Path to the just created empty db
        String outFileName = externalDBPath;

        Log.d(LOG_TAG, "copyDataBase:: from myInput = " + localDBPath + " to " + outFileName);

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public String getOutDbFilePath() {
        return Environment.getExternalStorageDirectory()
                + File.separator
                + Consts.APP_NAME
                + File.separator
                + NAME;
    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = localDBPath;
        Log.d(LOG_TAG, "openDataBase:: myPath = " + myPath);
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if(database != null)
            database.close();
        super.close();
    }


    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void dropTablesIfExist(SQLiteDatabase db, String... tables) {
        for (String table : tables) {
            db.execSQL(getDropTableQuery(table));
        }
    }

    @NonNull
    private static String getDropTableQuery(@NonNull String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }


}
