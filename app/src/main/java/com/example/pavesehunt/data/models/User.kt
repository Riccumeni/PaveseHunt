package com.example.testapp.data.models

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class User (
    val id: Int,
    val username: String,
    var points: Int,
    val uuid: String?,
    val created_at: String
)