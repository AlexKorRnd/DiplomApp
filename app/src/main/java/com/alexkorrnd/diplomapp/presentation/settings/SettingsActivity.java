package com.alexkorrnd.diplomapp.presentation.settings;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.alexkorrnd.diplomapp.Consts;
import com.alexkorrnd.diplomapp.R;
import com.alexkorrnd.diplomapp.data.db.DbOpenHelper;
import com.alexkorrnd.diplomapp.presentation.base.BaseActivity;
import com.alexkorrnd.diplomapp.presentation.internal.di.DbModule;

import java.io.File;
import java.io.IOException;

public class SettingsActivity extends BaseActivity {

    private final static int REQUEST_LOAD_DB = 10;
    private final static int REQUEST_SAVE_DB = 11;

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    Button btnSave;
    Button btnLoad;

    private DbOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnLoad = (Button) findViewById(R.id.btnLoad);

        dbOpenHelper = new DbOpenHelper(this);

        btnSave.setOnClickListener(v -> {
            saveDatabaseToDir();
        });

        btnLoad.setOnClickListener(v -> {
            loadDBFromFile();
        });
    }

    private void loadDBFromFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, REQUEST_LOAD_DB);
    }

    private void saveDatabaseToDir() {
        saveDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }


        switch (requestCode) {
            case REQUEST_LOAD_DB:
                String path = data.getData().getPath();
                Log.d("aaaaaaaaaaaaaa", "path = " + path
                    + "     getOutDbFilePath = " + dbOpenHelper.getOutDbFilePath());
                dbOpenHelper.setExternalDBPath(path);
                try {
                    dbOpenHelper.updateDatabase();
                    DbModule.reset();
                    Toast.makeText(this, "БД успешно обновлена", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "При обновлении БД произошла ошибка", Toast.LENGTH_LONG).show();
                }
                break;


        }
    }

    private void saveDatabase() {
        String path = getOutDbFilePath();
        dbOpenHelper.setExternalDBPath(path);
        try {
            dbOpenHelper.saveDataBase();
            Toast.makeText(this, "БД успешно сохранена в файл", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "При сохранении БД произошла ошибка", Toast.LENGTH_LONG).show();
        }
    }

    public String getOutDbFilePath() {
        return Environment.getExternalStorageDirectory()
                + File.separator
                + Consts.APP_NAME
                + File.separator
                + DbOpenHelper.NAME;
    }
}
