package com.example.pavesehunt.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pavesehunt.R
import com.example.testapp.data.models.User
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class FriendAdapter(var users: List<User>): RecyclerView.Adapter<FriendAdapter.CustomViewHolder>() {
    class CustomViewHolder(val view: ViewGroup) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_friend, parent, false) as ViewGroup
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val user = users[position]

        holder.view.findViewById<TextView>(R.id.usernameTextView).text = user.username

        val button = holder.view.findViewById<Button>(R.id.addFriendButton)

        if(user.isFriend){
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_24, 0)
        }else{
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_add_24, 0)
        }

        val shared = holder.view.context.getSharedPreferences("shared", Context.MODE_PRIVATE)

        var friends = shared.getString("friends", "[]")

        if(friends!!.contains(user.username)){
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_24, 0)
        }

        button.setOnClickListener {
            if(user.isFriend){
                val friendObjects: MutableList<User> = Json.decodeFromString(friends!!)

                friendObjects.forEachIndexed { index, userFav ->
                    if(userFav.username.lowercase() == user.username.lowercase()){
                        friendObjects.removeAt(index)
                    }
                }

                friends = Json.encodeToString(friendObjects)

                user.isFriend = false

                with(shared.edit()){
                    putString("friends", friends)
                    apply()
                }

                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_add_24, 0)
            }else{
                val friendObjects: MutableList<User> = Json.decodeFromString(friends!!)
                friendObjects.add(user)

                friends = Json.encodeToString(friendObjects)

                user.isFriend = true

                with(shared.edit()){
                    putString("friends", friends)
                    apply()
                }

                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_24, 0)
            }
        }
    }

    override fun getItemCount(): Int = users.size
}
