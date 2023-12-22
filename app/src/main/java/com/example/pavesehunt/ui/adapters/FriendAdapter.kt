package com.example.pavesehunt.ui.adapters

import android.content.Context
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

        val shared = holder.view.context.getSharedPreferences("shared", Context.MODE_PRIVATE)

        var friends = shared.getString("friends", "[]")

        if(friends!!.contains(user.username)){
            button.text = "-"
        }

        button.setOnClickListener {
            when(button.text){
                "-" -> {
                    val friendObjects: MutableList<User> = Json.decodeFromString(friends!!)
                    friendObjects.remove(user)
                    friends = Json.encodeToString(friendObjects)

                    with(shared.edit()){
                        putString("friends", friends)
                        apply()
                    }

                    button.text = "+"
                }
                "+" -> {
                    val friendObjects: MutableList<User> = Json.decodeFromString(friends!!)
                    friendObjects.add(user)

                    friends = Json.encodeToString(friendObjects)

                    with(shared.edit()){
                        putString("friends", friends)
                        apply()
                    }

                    button.text = "-"
                }
            }
        }
    }

    override fun getItemCount(): Int = users.size
}
