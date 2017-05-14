package com.alexkorrnd.base.pagination;


import android.support.v7.widget.LinearLayoutManager;

public class LinearLayoutManagerAdapter extends LayoutManagerAdapter<LinearLayoutManager> {

    public LinearLayoutManagerAdapter(LinearLayoutManager manager) {
        super(manager);
    }

    @Override
    public int getFirstVisibleItemPosition() {
        return manager.findFirstVisibleItemPosition();
    }

    @Override
    public int getLastVisibleItemPosition() {
        return manager.findLastVisibleItemPosition();
    }

    @Override
    public int getThresholdMultiplier() {
        return 1;
    }
}
