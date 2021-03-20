package com.example.dogimages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.dogimages.ui.ImageListAdapter
import com.example.dogimages.ui.PicassoWithCache

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = ""
        setContentView(R.layout.activity_details)
        val imageView: ImageView = findViewById(R.id.bigImage)
        val titleTV: TextView = findViewById(R.id.title)
        PicassoWithCache.getInstance(this).load(intent.getStringExtra(ImageListAdapter.urlKey))
            .into(
                imageView
            )
        titleTV.text = intent.getStringExtra(ImageListAdapter.titleKey)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun goBack(view: View) {
        finish()
    }
}