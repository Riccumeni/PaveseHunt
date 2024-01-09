package com.example.pavesehunt.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val question: String,
    val answers: List<String>,
    val indexOfCorrectAnswer: Int
)
