package com.example.pavesehunt.data.models

data class Response (
    val status: Status,
    val data: Any? = null

)

enum class Status{
    SUCCESS,
    LOADING,
    ERROR
}