package com.hiya7.bookhub.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hiya7.bookhub.R
import com.hiya7.bookhub.adapter.DashboardRecyclerAdapter
import com.hiya7.bookhub.model.Book
import com.hiya7.bookhub.util.ConnectionManager

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var btnCheckInternet: Button

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

    val bookInfoList = arrayListOf<Book>(
        Book("Harry Potter", "J.K Rowling", "1325", "4.5", R.drawable.bmoon),
        Book("A Brief History of time", "Stephan Hawkings", "4599", "4.7", R.drawable.bmoon),
        Book("Thunderbird", "Iam Fleming", "699", "4.2", R.drawable.bmoon),
        Book("The Epic Of Gilgamesh", "Gangest Remi", "1756", "3.2", R.drawable.bmoon),
        Book("The Divine Comedy", "Dante Aleigrihi", "950", "3.7", R.drawable.bmoon),
        Book("Things Fall Apart", "George Pinglet", "899", "4.0", R.drawable.bmoon),
        Book("Fairy tales", "Peter Pan", "2500", "4.1", R.drawable.bmoon),
        Book("The Last Letter", "Enzo Ferrari", "6550", "4.5", R.drawable.bmoon)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        btnCheckInternet = view.findViewById(R.id.btnCheckInternet)

        btnCheckInternet.setOnClickListener {
            if (ConnectionManager().checkConnectivity(activity as Context)) {
                // Internet is available
                val dialog = AlertDialog.Builder(activity as Context)

                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok") { text, listener ->
                    // Do Nothing
                }

                dialog.setNegativeButton("Cancel") { text, listener ->
                    // Do Nothing
                }

                dialog.create()
                dialog.show()

            } else {
                // Internet is not available
                val dialog = AlertDialog.Builder(activity as Context)

                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is NOT Found")

                dialog.setPositiveButton("Ok") { text, listener ->
                    // Do Nothing
                }

                dialog.setNegativeButton("Cancel") { text, listener ->
                    // Do Nothing
                }

                dialog.create()
                dialog.show()
            }
        }

        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = DashboardRecyclerAdapter(
            activity as Context,
            bookInfoList
        )

        recyclerDashboard.adapter = recyclerAdapter
        recyclerDashboard.layoutManager = layoutManager

        recyclerDashboard.addItemDecoration(
            DividerItemDecoration(
                recyclerDashboard.context,
                (layoutManager as LinearLayoutManager).orientation
            )
        )

        return view
    }
}