package com.alexkorrnd.diplomapp.presentation.contact.detail;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexkorrnd.diplomapp.R;
import com.alexkorrnd.diplomapp.domain.DetailType;
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate;

import java.util.List;

public class DetailTypeDelegate extends AbsListItemAdapterDelegate<DetailType, Object, DetailTypeDelegate.Holder> {

    public interface Callback {
        void onItemClick(int adapterPosition);
    }

    private final LayoutInflater inflater;
    private final Callback callback;

    public DetailTypeDelegate(Context context, Callback callback) {
        inflater = LayoutInflater.from(context);
        this.callback = callback;
    }

    @Override
    protected boolean isForViewType(@NonNull Object item, @NonNull List<Object> items, int position) {
        return item instanceof DetailType;
    }

    @NonNull
    @Override
    protected Holder onCreateViewHolder(@NonNull ViewGroup parent) {
        final Holder holder = new Holder(inflater.inflate(R.layout.item_detail_type, parent, false));
        holder.itemView.setOnClickListener(v -> callback.onItemClick(holder.getAdapterPosition()));
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull DetailType item, @NonNull Holder viewHolder, @NonNull List<Object> payloads) {
        viewHolder.tvLabel.setText(item.getTitle());
        viewHolder.tvValue.setText(item.getValue());
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView tvLabel;
        TextView tvValue;

        public Holder(View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
            tvValue = (TextView) itemView.findViewById(R.id.tvValue);
        }
    }
}
