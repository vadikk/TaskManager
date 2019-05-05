package com.example.managingtask.util

import com.example.managingtask.data.model.responce.AuthorizationResponce
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class WrapperConverterFactory(val factory: GsonConverterFactory): Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val wrappedType = object : ParameterizedType {
            override fun getRawType(): Type  = AuthorizationResponce::class.java

            override fun getOwnerType(): Type? = null

            override fun getActualTypeArguments(): Array<Type> = arrayOf(type)
        }

        val gsonConverter = factory.responseBodyConverter(wrappedType, annotations, retrofit)

        return WrapperResponceBodyConverter(gsonConverter)
    }
}