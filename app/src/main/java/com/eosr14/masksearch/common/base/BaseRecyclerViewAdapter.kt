package com.eosr14.masksearch.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, H : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var onItemClickListener: OnItemClickListener? = null
    private var listItems: MutableList<T> = mutableListOf()
    private var clickViewId: Int = View.NO_ID

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val clickView = getClickView(holder)
        clickView.setOnClickListener {
            preProcessOnItemClick(holder.layoutPosition)
            onItemClickListener?.onItemClick(
                holder.itemView,
                holder.layoutPosition,
                this@BaseRecyclerViewAdapter
            )
        }
        preProcessOnBindView(holder, position)
        onBindView(holder as H, position)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun getItem(position: Int): T {
        return listItems[position]
    }

    fun addItem(item: T) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

    fun addItem(item: T, position: Int) {
        listItems.add(position, item)
        notifyItemInserted(position)
    }

    fun addItems(items: MutableList<T>) {
        val position = this.listItems.size
        listItems.addAll(items)
        notifyItemRangeInserted(position, items.size)
    }

    fun removeItem(position: Int) {
        listItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clearItems() {
        listItems.clear()
        notifyDataSetChanged()
    }

    fun setItems(items: MutableList<T>) {
        listItems = items
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<T> {
        return listItems
    }

    fun setClickViewId(clickViewId: Int) {
        this.clickViewId = clickViewId
    }

    fun pushItems(items: MutableList<T>) {
        listItems.addAll(items)
        notifyItemInserted(listItems.size)
    }

    protected open fun preProcessOnBindView(holder: RecyclerView.ViewHolder, position: Int) {}

    protected open fun preProcessOnItemClick(position: Int) {}

    protected abstract fun onBindView(holder: H, position: Int)

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, adapter: BaseRecyclerViewAdapter<*, *>)
    }

    private fun getClickView(holder: RecyclerView.ViewHolder): View {
        return if (clickViewId == View.NO_ID) {
            holder.itemView
        } else {
            holder.itemView.findViewById(clickViewId) ?: throw Exception("ID is invalid")
        }
    }
}