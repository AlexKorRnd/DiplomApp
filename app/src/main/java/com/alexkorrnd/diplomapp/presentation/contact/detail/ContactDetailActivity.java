package com.alexkorrnd.diplomapp.presentation.contact.detail;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.alexkorrnd.base.DelegationAdapter;
import com.alexkorrnd.base.pagination.LoadMoreDelegate;
import com.alexkorrnd.diplomapp.R;
import com.alexkorrnd.diplomapp.domain.Contact;
import com.alexkorrnd.diplomapp.domain.DetailType;
import com.alexkorrnd.diplomapp.presentation.base.BaseActivity;
import com.alexkorrnd.diplomapp.presentation.internal.di.DbModule;

import java.util.List;

public class ContactDetailActivity extends BaseActivity implements
    DetailTypeDelegate.Callback, ContactDetailesPresenter.View {

    private static final String EXTRA_CONTACT = "EXTRA_CONTACT";

    public static Intent createIntent(Context context, Contact contact) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT, contact);
        return intent;
    }

    private TextView tvFullName;
    private TextView tvComment;
    private TextView tvAddress;
    private TextView tvRegion;
    private TextView tvGroup;
    private RecyclerView rvItems;

    private ContactDetailesPresenter presenter;

    private DelegationAdapter adapter;
    private Contact contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        contact = getIntent().getParcelableExtra(EXTRA_CONTACT);

        final SQLiteOpenHelper sqLiteOpenHelper = DbModule.provideSQLiteOpenHelper(this);
        presenter = new ContactDetailesPresenter(this,
                sqLiteOpenHelper,
                DbModule.provideStorIOSQLite(sqLiteOpenHelper));
        presenter.setContact(contact);

        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvComment = (TextView) findViewById(R.id.tvComment);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvRegion = (TextView) findViewById(R.id.tvRegion);
        tvGroup = (TextView) findViewById(R.id.tvGroup);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);

        tvFullName.setText(contact.getFullName());
        tvComment.setText(contact.getComment());
        tvAddress.setText(contact.getAddress());
        tvRegion.setText(contact.getRegion().getTitle());
        tvGroup.setText(contact.getRegion().getGroup().getTitle());

        initRecyclerView();
        presenter.loadDetailes();
    }

    private void initRecyclerView() {
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DelegationAdapter();
        adapter.getDelegatesManager()
                .addDelegate(new LoadMoreDelegate())
                .addDelegate(new DetailTypeDelegate(this, this));
        rvItems.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int adapterPosition) {
        final DetailType detailType = (DetailType) adapter.getItem(adapterPosition);
        switch (detailType.getKey()) {
            case DetailType.KEY_CELL:
            case DetailType.KEY_PHONE:
                makeCall(detailType.getValue());
                break;
            case DetailType.KEY_EMAIL:
                sendEmail(detailType.getValue());
                break;
        }
    }

    private void makeCall(String phone) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        } catch (Exception e) {
            Log.e("aaaaaaaa", e.getMessage(), e);
        }
    }

    private void sendEmail(String email) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + email));
            startActivity(emailIntent);
        } catch (Exception e) {
            Log.e("aaaaaaaa", e.getMessage(), e);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onDetailesLoadedSuccess(List<DetailType> detailTypes) {
        adapter.addAll(detailTypes);
    }
}
