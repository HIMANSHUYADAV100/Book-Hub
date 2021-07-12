package com.hiya7.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hiya7.bookhub.R
import com.hiya7.bookhub.activity.DescriptionActivity
import com.hiya7.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtRecyclerView)
        val authView: TextView = view.findViewById(R.id.txtAuthor)
        val priceView: TextView = view.findViewById(R.id.txtPrice)
        val imageView: ImageView = view.findViewById(R.id.imgRecyclerView)

        val llContent: RelativeLayout = view.findViewById(R.id.recycler_dashboard_single_row)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        // responsible for creating the first n holders
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val text = itemList[position].bookName
        val auth = itemList[position].bookAuthor
        val price = itemList[position].bookCost
        val image = itemList[position].bookImage

        holder.textView.text = text
        holder.authView.text = auth
        holder.priceView.text = price


        Picasso.get().load(image).error(R.drawable.book_app_icon_web).into(holder.imageView);
        //holder.imageView.setImageResource(image)

        holder.llContent.setOnClickListener {
            // WHEN CLICKED ON BOOKS

            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", itemList[position].book_Id)
            context.startActivity(intent)
        }
    }

}