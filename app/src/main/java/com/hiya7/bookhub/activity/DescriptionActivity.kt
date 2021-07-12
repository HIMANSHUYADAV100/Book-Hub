package com.hiya7.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.Response
import com.hiya7.bookhub.R
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")

        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occured",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occured",
                Toast.LENGTH_SHORT
            ).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        val jsonRequest =
            object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                // println("Response is $it")

                try {
                    val success = it.getBoolean("success")
                    if (success) {
                        val bookJsonObject = it.getJSONObject("book_data")

                        Picasso.get().load(bookJsonObject.getString("image"))
                            .error(R.drawable.book_app_icon_web).into(imgBookImage);
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookDesc.text = bookJsonObject.getString("description")


                        progressLayout.visibility = View.GONE


                    } else {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Error Occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Some Error Occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            },
                Response.ErrorListener {
                    Toast.makeText(this@DescriptionActivity, "Volley Error Occured", Toast.LENGTH_SHORT)
                        .show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {

                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "2774371f1180aa"

                    return headers
                }
            }

        queue.add(jsonRequest)

    }
}