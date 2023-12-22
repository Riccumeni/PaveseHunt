package com.example.pavesehunt.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pavesehunt.R
import com.example.testapp.data.models.User


class RankAdapter(var users: List<User>): RecyclerView.Adapter<RankAdapter.CustomViewHolder>() {
    class CustomViewHolder(val view: ViewGroup) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_rank_user, parent, false) as ViewGroup
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val user = users[position]

        val positionText = holder.view.findViewById<TextView>(R.id.positionTextView)
        val usernameText = holder.view.findViewById<TextView>(R.id.usernameRankTextView)
        val pointsText = holder.view.findViewById<TextView>(R.id.pointsRankTextView)
        val profileImage = holder.view.findViewById<ImageView>(R.id.rankUserImageView)


        Glide.with(holder.view.context).load(user.imageUrl).into(profileImage)


        positionText.text = "${(position + 1)}º"
        usernameText.text = user.username
        pointsText.text = user.points.toString()

    }

    override fun getItemCount(): Int = users.size
}
