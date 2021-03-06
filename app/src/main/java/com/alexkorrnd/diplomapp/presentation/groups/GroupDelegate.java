package com.alexkorrnd.diplomapp.presentation.groups;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexkorrnd.diplomapp.R;
import com.alexkorrnd.diplomapp.domain.Group;
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate;

import java.util.List;

public class GroupDelegate extends AbsListItemAdapterDelegate<Group, Object, GroupDelegate.Holder> {

    public interface Callback {
        void onItemClick(int adapterPosition);
    }

    private final LayoutInflater inflater;
    private final Callback callback;

    public GroupDelegate(Context context, Callback callback) {
        inflater = LayoutInflater.from(context);
        this.callback = callback;
    }

    @Override
    protected boolean isForViewType(@NonNull Object item, @NonNull List<Object> items, int position) {
        return item instanceof Group;
    }

    @NonNull
    @Override
    protected Holder onCreateViewHolder(@NonNull ViewGroup parent) {
        final Holder holder = new Holder(inflater.inflate(R.layout.item_simple_text, parent, false));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull Group item, @NonNull Holder viewHolder, @NonNull List<Object> payloads) {
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
