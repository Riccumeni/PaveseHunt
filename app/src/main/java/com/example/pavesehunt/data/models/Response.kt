package com.example.pavesehunt.data.models

import com.example.pavesehunt.domain.usecases.ErrorCodes
import com.example.pavesehunt.domain.usecases.STATUS

data class Response (
    var status: STATUS,
    var data: Any? = null,
    var code: ErrorCodes? = null,
    var message: String = ""

)

