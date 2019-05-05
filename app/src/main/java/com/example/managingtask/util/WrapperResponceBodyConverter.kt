package com.example.managingtask.util

import com.example.managingtask.data.model.responce.AuthorizationResponce
import okhttp3.ResponseBody
import retrofit2.Converter

class WrapperResponceBodyConverter <AuthorizationResponce>(
    val converter: Converter<ResponseBody, AuthorizationResponce>?): Converter<ResponseBody, AuthorizationResponce> {

    override fun convert(value: ResponseBody): AuthorizationResponce? {
        val responce = converter?.convert(value)
        return responce
    }
}