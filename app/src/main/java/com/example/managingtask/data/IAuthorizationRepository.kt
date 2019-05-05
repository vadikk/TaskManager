package com.example.managingtask.data

import io.reactivex.Observable

interface IAuthorizationRepository {

    fun signUp(email: String, password: String): Observable<String>
    fun signIn(email: String, password: String): Observable<String>
}