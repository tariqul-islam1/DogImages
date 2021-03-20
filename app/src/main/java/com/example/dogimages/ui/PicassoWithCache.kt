package com.example.dogimages.ui

import android.content.Context
import com.squareup.picasso.Downloader
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso


object PicassoWithCache {

    /**
     * Creates Picasso instance with caching functionality
     * @param context
     * @return Picasso instance with OkHttp3Downloader support
     */
    fun getInstance(context: Context): Picasso {
        val downloader: Downloader = OkHttp3Downloader(context, Int.MAX_VALUE.toLong())
        val builder = Picasso.Builder(context)
        builder.downloader(downloader)
        return builder.build()
    }
}