package com.alexkorrnd.base.pagination;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alexkorrnd.base.DelegationAdapter;


public class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    public interface LoadMoreCallback {
        void onLoadMore(int offset);
    }

    public static final int DEFAULT_ITEM_THRESHOLD = 5;

    private final RecyclerView recyclerView;
    private final LayoutManagerAdapter manager;
    private final DelegationAdapter adapter;
    private final int itemThreshold;
    private final LoadMoreCallback callback;

    private int previousTotalItemCount;
    private boolean isLoading;
    private boolean isDisabled;
    private final LoadMoreItem loadMoreItem = new LoadMoreItem();
    private boolean isAddProgressIndicatorQueued;

    private final Runnable addProgressIndicatorTask = new Runnable() {
        @Override
        public void run() {
            adapter.add(loadMoreItem, getProgressItemAddPosition());
            previousTotalItemCount++;
            isAddProgressIndicatorQueued = false;
        }
    };

    public InfiniteScrollListener(RecyclerView recyclerView, DelegationAdapter adapter, LoadMoreCallback callback) {
        this(recyclerView, initLayoutManagerAdapter(recyclerView.getLayoutManager()), adapter, callback);
    }

    public InfiniteScrollListener(RecyclerView recyclerView, LayoutManagerAdapter manager, DelegationAdapter adapter, LoadMoreCallback callback) {
        this(recyclerView, manager, adapter, callback, DEFAULT_ITEM_THRESHOLD * manager.getThresholdMultiplier());
    }

    public InfiniteScrollListener(RecyclerView recyclerView, LayoutManagerAdapter manager, DelegationAdapter adapter, LoadMoreCallback callback, int itemThreshold) {
        this.recyclerView = recyclerView;
        this.manager = manager;
        this.adapter = adapter;
        this.callback = callback;
        this.itemThreshold = itemThreshold;
    }

    private static LayoutManagerAdapter initLayoutManagerAdapter(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return new LinearLayoutManagerAdapter((LinearLayoutManager) layoutManager);
        } else {
            return null;
        }
    }

    private int getProgressItemAddPosition() {
        return adapter.getItemCount();
    }

    private int getProgressItemRemovePosition() {
        return adapter.getItems().lastIndexOf(loadMoreItem);
    }

    public void showProgressIndicator() {
        isAddProgressIndicatorQueued = true;
        recyclerView.post(addProgressIndicatorTask);
    }

    public void hideProgressIndicator() {
        if (isAddProgressIndicatorQueued) {
            recyclerView.removeCallbacks(addProgressIndicatorTask);
        } else {
            removeProgressIndicatorIfExist();
        }
    }

    private void removeProgressIndicatorIfExist() {
        final int position = getProgressItemRemovePosition();
        if (position > 0) {
            adapter.remove(position);
        }
    }

    public void resetState() {
        isLoading = true;
        previousTotalItemCount = 0;
    }

    public void enable() {
        isDisabled = false;
    }

    public void disable() {
        isDisabled = true;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (isDisabled) {
            return;
        }
        final int totalItemCount = manager.getItemCount();
        final int lastVisibleItem = manager.getLastVisibleItemPosition();

        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
        }
        if (!isLoading && (lastVisibleItem + itemThreshold) >= totalItemCount) {
            isLoading = true;
            callback.onLoadMore(totalItemCount);
        }
    }
}
