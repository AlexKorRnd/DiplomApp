package com.alexkorrnd.base.pagination;


import android.support.v7.widget.RecyclerView;

public abstract class LayoutManagerAdapter<T extends RecyclerView.LayoutManager> {

    protected final T manager;

    public LayoutManagerAdapter(T manager) {
        this.manager = manager;
    }

    public abstract int getFirstVisibleItemPosition();

    public abstract int getLastVisibleItemPosition();

    public abstract int getThresholdMultiplier();

    public int getItemCount() {
        return manager.getItemCount();
    }

    public T getManager() {
        return manager;
    }
}
