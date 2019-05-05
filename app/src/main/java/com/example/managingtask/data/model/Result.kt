package com.example.managingtask.data.model

import com.example.managingtask.data.model.responce.AuthorizationResponce
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function

class Result<T>(val data: T? = null, val error: Throwable? = null) {

    companion object {
        fun <T> fromData(data: T): Result<T> = Result(data, null)
        fun <T> fromError(error: Throwable): Result<T> = Result(null, error)
    }
}

fun <T> Observable<T>.toResult(): Observable<Result<T>>{
    return map{
        Result.fromData(it)
    }.onErrorResumeNext( Function {
     Observable.just(Result.fromError(it))
    })
}

fun Observable<AuthorizationResponce>.checkResponceResult()= this.compose(ObservableTransformer<AuthorizationResponce, String>(){
        it.flatMap {response ->
            if (response.token.isNullOrEmpty()){
                 Observable.error<String>(ErrorResponse( response.message, response.fields))
            }else
                Observable.just<String>(response.token)
        }
            .onErrorResumeNext { t: Throwable ->
                if (t is HttpException) {
                    val errorString = t.response()?.errorBody()?.string()
                    val errorModel = Gson().fromJson<ErrorResponse>(errorString, ErrorResponse::class.java)
                    return@onErrorResumeNext Observable.error<String>(errorModel)
                }
                return@onErrorResumeNext Observable.error(t)
            }
})



