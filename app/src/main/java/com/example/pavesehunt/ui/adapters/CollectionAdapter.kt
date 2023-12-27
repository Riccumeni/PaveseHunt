package com.example.pavesehunt.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Collection
import com.example.testapp.data.models.User

class CollectionAdapter(val collections: List<Collection>): RecyclerView.Adapter<CollectionAdapter.CustomViewHolder>() {
    class CustomViewHolder(val view: ViewGroup) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_collection, parent, false) as ViewGroup
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val collection = collections[position]

        holder.view.findViewById<TextView>(R.id.collectionTitleTextView).text = collection.title
        holder.view.findViewById<TextView>(R.id.collectionTypeTextView).text = collection.category
        Glide.with(holder.view.context).load(collection.image).into(holder.view.findViewById(R.id.collectionImageView))
    }

    override fun getItemCount(): Int = collections.size
}
