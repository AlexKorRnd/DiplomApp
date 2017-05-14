package com.alexkorrnd.diplomapp.presentation.contact.list;


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
import com.alexkorrnd.diplomapp.domain.Contact;
import com.alexkorrnd.diplomapp.domain.Region;
import com.alexkorrnd.diplomapp.presentation.base.BaseActivity;
import com.alexkorrnd.diplomapp.presentation.contact.detail.ContactDetailActivity;
import com.alexkorrnd.diplomapp.presentation.internal.di.DbModule;

import java.util.List;

public class ContactsListActivity extends BaseActivity implements ContactPresenter.View,
        InfiniteScrollListener.LoadMoreCallback, ContactDelegate.Callback{

    private static final String EXTRA_REGION = "EXTRA_REGION";

    public static Intent createIntent(Context context, Region region) {
        Intent intent = new Intent(context, ContactsListActivity.class);
        intent.putExtra(EXTRA_REGION, region);
        return intent;
    }

    private RecyclerView rvItems;

    private DelegationAdapter adapter;
    private ContactPresenter presenter;

    private Region region;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        region = getIntent().getParcelableExtra(EXTRA_REGION);

        setTitle(region.getTitle());

        final SQLiteOpenHelper sqLiteOpenHelper = DbModule.provideSQLiteOpenHelper(this);
        presenter = new ContactPresenter(this,
                sqLiteOpenHelper,
                DbModule.provideStorIOSQLite(sqLiteOpenHelper));
        presenter.setRegion(region);

        rvItems = (RecyclerView) findViewById(R.id.rvItems);

        initRecyclerView();
        presenter.loadContacts();
    }

    private void initRecyclerView() {
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DelegationAdapter();
        adapter.getDelegatesManager()
                .addDelegate(new LoadMoreDelegate())
                .addDelegate(new ContactDelegate(this, this));
        rvItems.setAdapter(adapter);
        rvItems.addOnScrollListener(new InfiniteScrollListener(rvItems, adapter, this));
    }

    @Override
    public void onLoadMore(int offset) {
        presenter.loadContacts(offset);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onContactsLoadedSuccess(List<Contact> groups) {
        adapter.addAll(groups);
    }

    @Override
    public void onItemClick(int adapterPosition) {
        final Contact contact = (Contact) adapter.getItem(adapterPosition);
        startActivity(ContactDetailActivity.createIntent(this, contact));
    }
}
