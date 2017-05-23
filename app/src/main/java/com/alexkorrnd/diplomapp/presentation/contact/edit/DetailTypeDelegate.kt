package com.alexkorrnd.diplomapp.presentation.contact.edit

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexkorrnd.base.DelegationAdapter
import com.alexkorrnd.base.extensions.textWithSelection
import com.alexkorrnd.diplomapp.R
import com.alexkorrnd.diplomapp.domain.DetailType
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate

class DetailTypeDelegate(val adapter: DelegationAdapter,
                         context: Context
) : AbsListItemAdapterDelegate<DetailType, Any, DetailTypeDelegate.Holder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
        return item is DetailType
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val holder = Holder(inflater.inflate(R.layout.item_edit_detail_type, parent, false))
        holder.etText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                (adapter.items[holder.adapterPosition] as DetailType).value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        return holder
    }

    override fun onBindViewHolder(item: DetailType, viewHolder: Holder, payloads: List<Any>) {
        with(viewHolder) {
            etText.textWithSelection = item.value
            etText.hint = item.title
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var etText: TextInputEditText = itemView.findViewById(R.id.etText) as TextInputEditText

    }
}
