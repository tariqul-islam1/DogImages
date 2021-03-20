package com.example.dogimages.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogimages.DetailsActivity
import com.example.dogimages.R


class ImageListAdapter(
    private val urls: List<String>,
    private val names: List<String>,
    private val characteristics: List<String>
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view, urls, names, characteristics)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleView.text = names[position]
        holder.subTitleView.text = characteristics[position]
        PicassoWithCache.getInstance(holder.imgView.context).load(urls[position])
            .into(holder.imgView)
    }

    override fun getItemCount() = urls.size

    class ViewHolder(
        view: View,
        private val urls: List<String>,
        private val names: List<String>,
        private val characteristics: List<String>
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val imgView: ImageView = view.findViewById(R.id.img)
        val titleView: TextView = view.findViewById(R.id.title)
        val subTitleView: TextView = view.findViewById(R.id.subTitle)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(v?.context, DetailsActivity::class.java)
            intent.putExtra(urlKey, urls[adapterPosition])
            intent.putExtra(
                titleKey,
                names[adapterPosition] + ": " + characteristics[adapterPosition]
            )
            v?.context?.startActivity(intent)
        }
    }

    companion object {
        const val urlKey = "url"
        const val titleKey = "title"
    }
}