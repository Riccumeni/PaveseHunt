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
import com.example.testapp.domain.viewmodels.UserViewModel
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class FriendAdapter(var users: List<User>, var friends: String, var userViewModel: UserViewModel, var uuid: String): RecyclerView.Adapter<FriendAdapter.CustomViewHolder>() {
    class CustomViewHolder(val view: ViewGroup) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_friend, parent, false) as ViewGroup
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val user = users[position]

        holder.view.findViewById<TextView>(R.id.usernameTextView).text = user.username

        val button = holder.view.findViewById<Button>(R.id.addFriendButton)

        val userFriends: MutableList<Int> = Json.decodeFromString(friends)

        var isFriend = false

        userFriends.forEach {
            if(user.id == it){
                isFriend = true
            }
        }

        if(isFriend){
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_24, 0)
        }else{
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_add_24, 0)
        }

        button.setOnClickListener {
            if(isFriend){

                userFriends.forEachIndexed { index, userFav ->
                    if(userFav == user.id){
                        userFriends.removeAt(index)
                        friends = Json.encodeToString(userFriends)

                        userViewModel.setFriends(friends, uuid)
                    }
                }

                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_add_24, 0)
            }else{
                userFriends.add(user.id!!)

                friends = Json.encodeToString(userFriends)

                userViewModel.setFriends(friends, uuid)

                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_24, 0)
            }
        }
    }

    override fun getItemCount(): Int = users.size
}
