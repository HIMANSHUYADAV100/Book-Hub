package com.hiya7.bookhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
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
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import com.hiya7.bookhub.database.BookDatabase
import com.hiya7.bookhub.database.BookEntity
import com.hiya7.bookhub.util.ConnectionManager

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFav: MaterialButton
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

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


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

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

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    // println("Response is $it")

                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")

                            val bookImageUrl = bookJsonObject.getString("image")

                            Picasso.get().load(bookImageUrl)
                                .error(R.drawable.book_app_icon_web).into(imgBookImage);
                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuthor.text = bookJsonObject.getString("author")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookDesc.text = bookJsonObject.getString("description")


                            progressLayout.visibility = View.GONE

                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookDesc.text.toString(),
                                bookImageUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()

                            if (isFav) {
                                btnAddToFav.text = "Remove from Favourites"
                                val favColor = ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colorFavourite
                                )
                                btnAddToFav.setBackgroundColor(favColor)
                            } else {
                                btnAddToFav.text = "Add to Favourites"
                                val noFavColor =
                                    ContextCompat.getColor(applicationContext, R.color.teal_700)
                                btnAddToFav.setBackgroundColor(noFavColor)
                            }

                            btnAddToFav.setOnClickListener {

                                if (!DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                        .get()
                                ) {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book Added to Favourites",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        btnAddToFav.text = "Remove from Favourites"
                                        val favColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colorFavourite
                                        )

                                        btnAddToFav.setBackgroundColor(favColor)

                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error Occured 1",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {

                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                    val result = async.get()

                                    if (result) {

                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book Removed from Favourites",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        btnAddToFav.text = "Add to Favourites Favourites"
                                        val noFavColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.teal_700
                                        )
                                        btnAddToFav.setBackgroundColor(noFavColor)
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error Occured 2",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }


                            }

                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some Error Occured 3",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Error Occured 4",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                },
                    Response.ErrorListener {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Volley Error Occured",
                            Toast.LENGTH_SHORT
                        )
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

        } else {

            val dialog2 = AlertDialog.Builder(this@DescriptionActivity)

            dialog2.setTitle("Error")
            dialog2.setMessage("Internet Connection is NOT Found")

            dialog2.setPositiveButton("Open Settings") { text, listener ->
                // open settings (implicit intent
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }

            dialog2.setNegativeButton("Exit") { text, listener ->
                // close app
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }

            dialog2.create()
            dialog2.show()

        }
    }


    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        // check if book is fav M1, save book as fav M2 & remove book from fav M3
        override fun doInBackground(vararg p0: Void?): Boolean {

            when (mode) {

                1 -> {
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                }

                2 -> {
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }

                3 -> {
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }

            return false
        }


    }


}