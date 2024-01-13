package com.example.pavesehunt.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int? = null,
    val question: String,
    val answer: String,
    val correct_answer: Int,
    val poem: String
)
