package com.alexkorrnd.base.pagination;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alexkorrnd.diplomapp.presentation.utils.DimensionsUtils;
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class LoadMoreDelegate extends AbsListItemAdapterDelegate<LoadMoreItem, Object, LoadMoreDelegate.Holder> {

    private static final int DEFAULT_VERTICAL_MARGINS = DimensionsUtils.toDP(8);

    private int verticalMargins;

    public LoadMoreDelegate() {
    }

    public LoadMoreDelegate(int verticalMargins) {
        this.verticalMargins = verticalMargins;
    }

    @Override
    protected boolean isForViewType(@NonNull Object item, @NonNull List<Object> items, int position) {
        return item instanceof LoadMoreItem;
    }

    @NonNull
    @Override
    protected Holder onCreateViewHolder(@NonNull ViewGroup parent) {
        final RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.topMargin = verticalMargins;
        params.bottomMargin = verticalMargins;
        final ProgressBar view = new ProgressBar(parent.getContext(), null, android.R.attr.progressBarStyle);
        view.setLayoutParams(params);
        return new Holder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull LoadMoreItem item, @NonNull Holder viewHolder, @NonNull List<Object> payloads) {

    }

    static class Holder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;

        public Holder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView;
        }

    }
}
