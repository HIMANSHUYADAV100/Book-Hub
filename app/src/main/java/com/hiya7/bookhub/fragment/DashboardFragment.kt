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
import android.widget.ProgressBar
import android.widget.RelativeLayout
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
import org.json.JSONException

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    var bookInfoList = arrayListOf<Book>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(activity)

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    // handle the resposne

                    try {

                        progressLayout.visibility = View.GONE

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
                                    bookJsonObject.getString("image"),
                                    bookJsonObject.getString("book_id")
                                )

                                bookInfoList.add(bookObject)

                                recyclerAdapter = DashboardRecyclerAdapter(
                                    activity as Context,
                                    bookInfoList
                                )

                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager

                            }

                        } else {

                            Toast.makeText(
                                activity as Context,
                                "Some Error has Occured",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some unexpected Error Occured ! ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {

                    // handle the Errors
                    Toast.makeText(activity as Context, "Volley Error Occured", Toast.LENGTH_SHORT)
                        .show()

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