package com.flash.light.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseMultiTypeViewAdapter<T> : Adapter<ViewHolder>() {
    val dataSet by lazy {
        initData()
    }
    var onClick: ((T) -> Unit)? = null

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    abstract fun getViewType(position: Int): Int

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return getViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binData(holder, dataSet[position], position)
    }

    open fun setData(listItem: ArrayList<T>) {
        dataSet.clear()
        dataSet.addAll(listItem)
        notifyDataSetChanged()
    }

    fun getListData():ArrayList<T>{
        return dataSet
    }

    open fun binData(viewHolder: ViewHolder, item: T, position: Int){}

    open fun initData(): ArrayList<T> {
        return arrayListOf()
    }
}