package com.example.pavesehunt.data.models

data class Response (
    var status: Status,
    var data: Any? = null

)

enum class Status{
    SUCCESS,
    LOADING,
    ERROR,
    NOT_STARTED
}