package com.alexkorrnd.diplomapp.presentation.regions;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alexkorrnd.base.DelegationAdapter;
import com.alexkorrnd.base.pagination.InfiniteScrollListener;
import com.alexkorrnd.base.pagination.LoadMoreDelegate;
import com.alexkorrnd.diplomapp.R;
import com.alexkorrnd.diplomapp.domain.Region;
import com.alexkorrnd.diplomapp.presentation.base.BaseActivity;
import com.alexkorrnd.diplomapp.presentation.contact.list.ContactsListActivity;
import com.alexkorrnd.diplomapp.presentation.internal.di.DbModule;

import java.util.List;

public class RegionsActivity extends BaseActivity implements
        InfiniteScrollListener.LoadMoreCallback, RegionsPresenter.View, RegionDelegate.Callback {

    private static final String EXTRA_REGION_ID = "EXTRA_REGION_ID";
    private static final String EXTRA_GROUP_TITLE = "EXTRA_GROUP_TITLE";

    public static Intent createIntent(Context context, String regionId, String groupTitle) {
        final Intent intent = new Intent(context, RegionsActivity.class);
        intent.putExtra(EXTRA_REGION_ID, regionId);
        intent.putExtra(EXTRA_GROUP_TITLE, groupTitle);
        return intent;
    }

    private RecyclerView rvItems;

    private DelegationAdapter adapter;
    private RegionsPresenter presenter;

    private String parentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setTitle(getIntent().getStringExtra(EXTRA_GROUP_TITLE));

        final SQLiteOpenHelper sqLiteOpenHelper = DbModule.provideSQLiteOpenHelper(this);
        presenter = new RegionsPresenter(this,
                sqLiteOpenHelper,
                DbModule.provideStorIOSQLite(sqLiteOpenHelper));
        presenter.setParentId(getIntent().getStringExtra(EXTRA_REGION_ID));

        rvItems = (RecyclerView) findViewById(R.id.rvItems);

        initRecyclerView();
        presenter.loadRegions();
    }

    private void initRecyclerView() {
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DelegationAdapter();
        adapter.getDelegatesManager()
                .addDelegate(new LoadMoreDelegate())
                .addDelegate(new RegionDelegate(this, this));
        rvItems.setAdapter(adapter);
        rvItems.addOnScrollListener(new InfiniteScrollListener(rvItems, adapter, this));
    }

    @Override
    public void onLoadMore(int offset) {
        presenter.loadRegions(offset);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onRegionsLoadedSuccess(List<Region> groups) {
        adapter.addAll(groups);
    }

    @Override
    public void onItemClick(int adapterPosition) {
        final Region region = (Region) adapter.getItem(adapterPosition);
        presenter.navigateToNextScreen(region);
    }

    @Override
    public void onNavigateToRegions(Region region) {
        startActivity(RegionsActivity.createIntent(this, region.getGid(), region.getTitle()));
    }

    @Override
    public void onNavigateToContacts(Region region) {
        startActivity(ContactsListActivity.createIntent(this, region));
    }
}
