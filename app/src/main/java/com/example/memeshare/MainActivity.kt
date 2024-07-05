package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class MainActivity : AppCompatActivity() {

    var currentImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private fun loadMeme() {
        val nextButton = findViewById<Button>(R.id.nextButton)
        val shareButton = findViewById<Button>(R.id.shareButton)
        nextButton.isEnabled = false
        shareButton.isEnabled = false
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.com/gimme"
//        val queue = Volley.newRequestQueue(this)  ~Singleton
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                    response -> currentImageUrl = response.getString("url") //url
//                Log.d("Tag", "Log this message");
                val memeImageView = findViewById<ImageView>(R.id.memeImageView)

                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        nextButton.isEnabled = true
                        shareButton.isEnabled = true
                        return false
                    }

                }).into(memeImageView) //url
            },
            Response.ErrorListener{
                progressBar.visibility = View.GONE
                Toast.makeText(this,"Something went Wrong", Toast.LENGTH_LONG).show()
            })

//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET, url, null,
//            Response.Listener {response ->
//                val url = response.getString("url")
//                val memeImageView = findViewById<ImageView>(R.id.memeImageView)
//                Glide.with(this).load(url).into(memeImageView)
//            },
//            Response.ErrorListener {
//                Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show()
//            })

//        queue.add(jsonObjectRequest)  ~Singleton
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        //"Added the request to RequestQueue"
    }
    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain" //now try with jpg
        intent.putExtra(Intent.EXTRA_TEXT,"Yeh meme dekhna 😂 $currentImageUrl")
//        val chooser = Intent.createChooser(intent, "Share Meme via") and pass chooser below OR SIMPLY
        startActivity(Intent.createChooser(intent, "Share Meme via"))
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
//    https://meme-api.com/gimme
}