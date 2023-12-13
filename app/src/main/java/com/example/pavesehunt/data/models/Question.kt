package com.example.pavesehunt.data.models

data class Question(
    val question: String,
    val answers: List<String>,
    val indexOfCorrectAnswer: Int
)
