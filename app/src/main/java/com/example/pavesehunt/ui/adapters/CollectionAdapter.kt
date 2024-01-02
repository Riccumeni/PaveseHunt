package com.example.pavesehunt.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Collection
import com.example.pavesehunt.domain.viewmodels.CollectionViewModel
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController

class CollectionAdapter(val collections: List<Collection>, val collectionViewModel: CollectionViewModel): RecyclerView.Adapter<CollectionAdapter.CustomViewHolder>() {
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

        holder.view.findViewById<LinearLayout>(R.id.collectionView).setOnClickListener {
            collectionViewModel.setCollection(collection)
            holder.view.findNavController().navigate(R.id.action_searchFragment2_to_detailFragment2)
        }
    }

    override fun getItemCount(): Int = collections.size
}
