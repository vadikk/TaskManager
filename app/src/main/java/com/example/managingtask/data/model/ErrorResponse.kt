package com.example.managingtask.data.model

import java.lang.Exception

class ErrorResponse(override val message: String?, val fields: ErrorModel?): Exception() {

}