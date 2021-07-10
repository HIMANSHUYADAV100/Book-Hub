package com.hiya7.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
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

    var bookInfoList = arrayListOf<Book>()

    /*
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
*/


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
                val dialog2 = AlertDialog.Builder(activity as Context)

                dialog2.setTitle("Error")
                dialog2.setMessage("Internet Connection is NOT Found")

                dialog2.setPositiveButton("Ok") { text, listener ->
                    // Do Nothing
                }

                dialog2.setNegativeButton("Cancel") { text, listener ->
                    // Do Nothing
                }

                dialog2.create()
                dialog2.show()
            }
        }

        layoutManager = LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    // handle the resposne
                    println("Response is $it")

                    val success = it.getBoolean("success")

                    if (success) {

                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("image")
                            )

                            bookInfoList.add(bookObject)

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

                        }

                    } else {

                        Toast.makeText(
                            activity as Context,
                            "Some Error has Occured",
                            Toast.LENGTH_SHORT
                        ).show()

                    }


                }, Response.ErrorListener {

                    // handle the Errors
                    println("Error is $it")

                }) {

                    override fun getHeaders(): MutableMap<String, String> {

                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "2774371f1180aa"

                        return headers
                    }
                }

            queue.add(jsonObjectRequest)
        } else {

            val dialog2 = AlertDialog.Builder(activity as Context)

            dialog2.setTitle("Error")
            dialog2.setMessage("Internet Connection is NOT Found")

            dialog2.setPositiveButton("Open Settings") { text, listener ->
                // open settings (implicit intent
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }

            dialog2.setNegativeButton("Exit") { text, listener ->
                // close app
                ActivityCompat.finishAffinity(activity as Activity)
            }

            dialog2.create()
            dialog2.show()
        }

        return view
    }
}