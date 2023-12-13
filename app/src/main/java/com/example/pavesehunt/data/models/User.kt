package com.example.testapp.data.models

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class User (
    val id: Int? = null,
    val username: String,
    var points: Int,
    val uuid: String? = null,
    val created_at: String? = null
)