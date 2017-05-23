package com.alexkorrnd.diplomapp.presentation.contact.list;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alexkorrnd.base.DelegationAdapter;
import com.alexkorrnd.base.decorators.LineDividerDecorator;
import com.alexkorrnd.base.pagination.InfiniteScrollListener;
import com.alexkorrnd.base.pagination.LoadMoreDelegate;
import com.alexkorrnd.diplomapp.R;
import com.alexkorrnd.diplomapp.domain.Contact;
import com.alexkorrnd.diplomapp.domain.Region;
import com.alexkorrnd.diplomapp.presentation.base.BaseActivity;
import com.alexkorrnd.diplomapp.presentation.contact.detail.ContactDetailActivity;
import com.alexkorrnd.diplomapp.presentation.contact.edit.AddOrEditContactActivity;
import com.alexkorrnd.diplomapp.presentation.internal.di.DbModule;
import com.alexkorrnd.diplomapp.presentation.utils.DimensionsUtils;

import java.util.List;

public class ContactsListActivity extends BaseActivity implements ContactPresenter.View,
        InfiniteScrollListener.LoadMoreCallback, ContactDelegate.Callback{

    private static final String EXTRA_REGION = "EXTRA_REGION";

    private final static int REQUEST_ADD_CONTACT = 11;

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
        setContentView(R.layout.activity_contacts_list);

        region = getIntent().getParcelableExtra(EXTRA_REGION);

        setTitle(region.getTitle());

        final SQLiteOpenHelper sqLiteOpenHelper = DbModule.provideSQLiteOpenHelper(this);
        presenter = new ContactPresenter(this,
                sqLiteOpenHelper,
                DbModule.provideStorIOSQLite(sqLiteOpenHelper));
        presenter.setRegion(region);

        rvItems = (RecyclerView) findViewById(R.id.rvItems);

        findViewById(R.id.fabAddContact).setOnClickListener(v -> {
            startActivityForResult(AddOrEditContactActivity.Companion.addContact(this, region), REQUEST_ADD_CONTACT);
        });

        initRecyclerView();
        presenter.loadContacts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_ADD_CONTACT) {
            Contact contact = AddOrEditContactActivity.Companion.unpackContact(data);
            adapter.add(contact);

        }
    }

    private void initRecyclerView() {
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DelegationAdapter();
        adapter.getDelegatesManager()
                .addDelegate(new LoadMoreDelegate())
                .addDelegate(new ContactDelegate(this, this));
        rvItems.setAdapter(adapter);
        rvItems.addOnScrollListener(new InfiniteScrollListener(rvItems, adapter, this));
        rvItems.addItemDecoration(new LineDividerDecorator(ContextCompat.getColor(this, R.color.line_divider_color),
                DimensionsUtils.toDP(0.5F),
                new Rect(DimensionsUtils.toDP(16F), 0, DimensionsUtils.toDP(16F), 0)));

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
