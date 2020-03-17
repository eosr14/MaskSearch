package com.eosr14.masksearch.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eosr14.masksearch.R
import com.eosr14.masksearch.common.base.BaseRecyclerViewAdapter
import com.eosr14.masksearch.model.KaKaoKeyWord

class AutoCompleteAdapter(onItemClickListener: OnItemClickListener) :
    BaseRecyclerViewAdapter<KaKaoKeyWord.Document, AutoCompleteViewHolder>() {

    init {
        this.onItemClickListener = onItemClickListener
    }

    override fun onBindView(holder: AutoCompleteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AutoCompleteViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_auto_complete,
                parent,
                false
            )
        )
    }

}