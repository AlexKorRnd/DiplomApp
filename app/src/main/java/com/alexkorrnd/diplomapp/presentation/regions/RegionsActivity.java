package com.alexkorrnd.diplomapp.presentation.regions;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.alexkorrnd.base.DelegationAdapter;
import com.alexkorrnd.base.decorators.LineDividerDecorator;
import com.alexkorrnd.base.pagination.InfiniteScrollListener;
import com.alexkorrnd.base.pagination.LoadMoreDelegate;
import com.alexkorrnd.diplomapp.R;
import com.alexkorrnd.diplomapp.domain.Region;
import com.alexkorrnd.diplomapp.presentation.base.BaseActivity;
import com.alexkorrnd.diplomapp.presentation.contact.list.ContactsListActivity;
import com.alexkorrnd.diplomapp.presentation.internal.di.DbModule;
import com.alexkorrnd.diplomapp.presentation.settings.SettingsActivity;
import com.alexkorrnd.diplomapp.presentation.utils.DimensionsUtils;

import java.util.List;

public class RegionsActivity extends BaseActivity implements
        InfiniteScrollListener.LoadMoreCallback, RegionsPresenter.View, RegionDelegate.Callback {

    private static final String EXTRA_REGION = "EXTRA_REGION";

    public static Intent createIntent(Context context, Region region) {
        final Intent intent = new Intent(context, RegionsActivity.class);
        intent.putExtra(EXTRA_REGION, region);
        return intent;
    }

    private RecyclerView rvItems;

    private DelegationAdapter adapter;
    private RegionsPresenter presenter;

    private Region parentRegion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        parentRegion = getIntent().getParcelableExtra(EXTRA_REGION);

        final SQLiteOpenHelper sqLiteOpenHelper = DbModule.provideSQLiteOpenHelper(this);
        presenter = new RegionsPresenter(this,
                sqLiteOpenHelper,
                DbModule.provideStorIOSQLite(sqLiteOpenHelper));

        if (parentRegion != null) {
            setTitle(parentRegion.getTitle());
            presenter.setParentId(parentRegion.getGid());
        }

        rvItems = (RecyclerView) findViewById(R.id.rvItems);

        initRecyclerView();
        presenter.loadRegions();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        return parentRegion == null;
    }

    private void initRecyclerView() {
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DelegationAdapter();
        adapter.getDelegatesManager()
                .addDelegate(new LoadMoreDelegate())
                .addDelegate(new RegionDelegate(this, this));
        rvItems.setAdapter(adapter);
        rvItems.addItemDecoration(new LineDividerDecorator(ContextCompat.getColor(this, R.color.line_divider_color),
                DimensionsUtils.toDP(0.5F),
                new Rect(DimensionsUtils.toDP(16F), 0, DimensionsUtils.toDP(16F), 0)));
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
        startActivity(RegionsActivity.createIntent(this, region));
    }

    @Override
    public void onNavigateToContacts(Region region) {
        startActivity(ContactsListActivity.createIntent(this, region));
    }
}
