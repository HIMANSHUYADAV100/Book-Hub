package com.hiya7.bookhub.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hiya7.bookhub.R
import com.hiya7.bookhub.adapter.DashboardRecyclerAdapter

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    val priceList = arrayListOf(
        "299",
        "456",
        "299",
        "299",
        "485",
        "299",
        "600",
        "299"
    )

    val authorList = arrayListOf(
        "Archery",
        "Fishing",
        "Hiking",
        "Bowling",
        "Tim",
        "Max",
        "Ronny",
        "Rhea"
    )


    val bookList = arrayListOf(
        "Harry Potter",
        "A Brief History of time",
        "Thunderbird",
        "The Epic Of Gilgamesh",
        "The Divine Comedy",
        "Things Fall Apart",
        "Fairy tales",
        "The Last Letter"
    )

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = DashboardRecyclerAdapter(
            activity as Context,
            bookList
        )

        recyclerDashboard.adapter = recyclerAdapter
        recyclerDashboard.layoutManager = layoutManager


        return view
    }
}