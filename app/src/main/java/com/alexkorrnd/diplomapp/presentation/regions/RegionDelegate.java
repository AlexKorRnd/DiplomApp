package com.alexkorrnd.diplomapp.presentation.regions;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexkorrnd.diplomapp.R;
import com.alexkorrnd.diplomapp.domain.Region;
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate;

import java.util.List;

public class RegionDelegate extends AbsListItemAdapterDelegate<Region, Object, RegionDelegate.Holder> {

    public interface Callback {
        void onItemClick(int adapterPosition);
    }

    private final LayoutInflater inflater;
    private final Callback callback;

    public RegionDelegate(Context context, Callback callback) {
        inflater = LayoutInflater.from(context);
        this.callback = callback;
    }

    @Override
    protected boolean isForViewType(@NonNull Object item, @NonNull List<Object> items, int position) {
        return item instanceof Region;
    }

    @NonNull
    @Override
    protected Holder onCreateViewHolder(@NonNull ViewGroup parent) {
        final Holder holder = new Holder(inflater.inflate(R.layout.item_simple_text, parent, false));
        holder.itemView.setOnClickListener(v -> callback.onItemClick(holder.getAdapterPosition()));
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull Region item, @NonNull Holder viewHolder, @NonNull List<Object> payloads) {
        viewHolder.textView.setText(item.getTitle());
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView textView;

        public Holder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvText);
        }
    }
}