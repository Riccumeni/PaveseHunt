package com.example.testapp.data.models

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class User (
    val id: Int? = null,
    val username: String,
    var points: Int,
    val uuid: String? = null,
    var answer_given: Int? = 0,
    val created_at: String? = null,
    var imageUrl: String? = null,
    var isFriend: Boolean = false,
    var friends: String = ""
)