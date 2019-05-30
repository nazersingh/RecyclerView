package com.nazer.recyclerview.ui.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.nazer.recyclerview.R
import com.nazer.recyclerview.pojo.DataPojo
import com.nazer.recyclerview.utility.CommonUtils

class ListAdapter(val activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val listItems=ArrayList<DataPojo>()

    fun addItems(arrayList: ArrayList<DataPojo>)
    {
        listItems.addAll(arrayList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderItem {
        return ViewHolderItem(LayoutInflater.from(activity).inflate(R.layout.item_recycler, p0, false))
    }

    override fun getItemCount(): Int {
       return listItems.size
//       return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val viewHolder=holder as ViewHolderItem
            CommonUtils.loadPic(activity,listItems.get(position).avatar_url!!,viewHolder.image, ProgressBar(activity),R.drawable.ic_launcher_background)
            viewHolder.tvName.text=listItems.get(position).login
            viewHolder.tvDescription.text=listItems.get(position).description

    }

    inner class ViewHolderItem(view: View):RecyclerView.ViewHolder(view)
    {
        val image: ImageView =view.findViewById(R.id.image)
        val tvName:TextView=view.findViewById(R.id.tv_name)
        val tvDescription:TextView=view.findViewById(R.id.tv_description)
    }


}