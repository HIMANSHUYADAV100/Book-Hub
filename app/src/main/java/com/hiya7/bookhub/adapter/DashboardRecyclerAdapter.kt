package com.hiya7.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiya7.bookhub.R

class DashboardRecyclerAdapter(val context:Context, val itemList : ArrayList<String>, val authList: ArrayList<String>, val priceView: ArrayList<String>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtRecyclerView)
        val authView: TextView = view.findViewById(R.id.txtAuthor)
        val priceView: TextView = view.findViewById(R.id.txtPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        // responsible for creating the first n holders
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val text = itemList[position]
        val auth = authList[position]
        val price= priceView[position]

        holder.textView.text = text
        holder.authView.text = auth
        holder.priceView.text= price
    }

}