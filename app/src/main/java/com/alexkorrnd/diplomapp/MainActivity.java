package com.alexkorrnd.diplomapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alexkorrnd.diplomapp.presentation.groups.GroupsActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnGoToGroups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GroupsActivity.createIntent(MainActivity.this));
            }
        });

/*
        final SQLiteOpenHelper sqLiteOpenHelper = DbModule.provideSQLiteOpenHelper(this);
        final StorIOSQLite storIOSQLite = DbModule.provideStorIOSQLite(sqLiteOpenHelper);

        storIOSQLite
                .get()
                .listOfObjects(RegionsWithParentEntity.class)
                .withQuery(Query.builder()
                    .table(RegionsTable.TABLE)
                        .where(RegionsTable.COLUMN_PARENT_ID + " =?")
                        .whereArgs("1")
                    .build())
                .prepare()
                .asRxSingle()
                .subscribe(new Action1<List<RegionsWithParentEntity>>() {
                    @Override
                    public void call(List<RegionsWithParentEntity> groups) {
                        for (RegionsWithParentEntity entity : groups) {
                            Log.d(Consts.LOG_TAG, "group entity = " + entity.getGroupEntity().getTitle());
                            Log.d(Consts.LOG_TAG, "region entity = " + entity.getRegionEntity().getTitle());
                        }
                    }
                });*/
    }
}
