package com.alexkorrnd.diplomapp.presentation.groups;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.alexkorrnd.base.DelegationAdapter;
import com.alexkorrnd.base.pagination.InfiniteScrollListener;
import com.alexkorrnd.base.pagination.LoadMoreDelegate;
import com.alexkorrnd.diplomapp.R;
import com.alexkorrnd.diplomapp.domain.Group;
import com.alexkorrnd.diplomapp.presentation.base.BaseActivity;
import com.alexkorrnd.diplomapp.presentation.internal.di.DbModule;
import com.alexkorrnd.diplomapp.presentation.regions.RegionsActivity;
import com.alexkorrnd.diplomapp.presentation.settings.SettingsActivity;

import java.util.List;

public class GroupsActivity extends BaseActivity implements
        InfiniteScrollListener.LoadMoreCallback, GroupsPresenter.View,
        GroupDelegate.Callback {

    public static Intent createIntent(Context context) {
        return new Intent(context, GroupsActivity.class);
    }

    private RecyclerView rvItems;

    private DelegationAdapter adapter;
    private GroupsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(getString(R.string.groups_title));

        final SQLiteOpenHelper sqLiteOpenHelper = DbModule.provideSQLiteOpenHelper(this);
        presenter = new GroupsPresenter(this,
                sqLiteOpenHelper,
                DbModule.provideStorIOSQLite(sqLiteOpenHelper));

        rvItems = (RecyclerView) findViewById(R.id.rvItems);

        initRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadGroups();
    }

    private void initRecyclerView() {
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DelegationAdapter();
        adapter.getDelegatesManager()
                .addDelegate(new LoadMoreDelegate())
                .addDelegate(new GroupDelegate(this, this));
        rvItems.setAdapter(adapter);
        rvItems.addOnScrollListener(new InfiniteScrollListener(rvItems, adapter, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(SettingsActivity.createIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onLoadMore(int offset) {
        presenter.loadGroups(offset);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onGroupsLoadedSuccess(List<Group> groups) {
        adapter.addAll(groups);
    }

    @Override
    public void onItemClick(int adapterPosition) {
        final Group group = (Group) adapter.getItem(adapterPosition);
        startActivity(RegionsActivity.createIntent(this, group.getGid(), group.getTitle()));
    }
}
