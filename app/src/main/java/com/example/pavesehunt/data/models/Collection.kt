package com.example.pavesehunt.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Collection (
    val id: Int,
    val title: String,
    val text: String,
    var image: String,
    val category: String
)