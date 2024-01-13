package com.example.pavesehunt.domain.usecases

enum class ErrorCodes {
    INTERNET_CONNECTION,
    PASSWORD_TOO_SHORT,
    PASSWORDS_NOT_EQUAL,
    EMAIL_WRONG,
    USERNAME_ALREADY_TOKEN
}

class ErrorException(val errorCode: ErrorCodes) : Exception()