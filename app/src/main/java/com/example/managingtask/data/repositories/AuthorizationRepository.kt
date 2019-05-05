package com.example.managingtask.data.repositories

import com.example.managingtask.data.IApiClient
import com.example.managingtask.data.IAuthorizationRepository
import com.example.managingtask.data.model.checkResponceResult
import com.example.managingtask.data.model.request.AuthorizationRequest
import com.example.managingtask.data.model.responce.AuthorizationResponce
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val apiClient: IApiClient
): IAuthorizationRepository {

    override fun signUp(email: String, password: String): Observable<String> {
        return apiClient.service.signUp(AuthorizationRequest(email, password))
            .subscribeOn(Schedulers.io())
            .checkResponceResult()
            .observeOn(AndroidSchedulers.mainThread())
    }


    override fun signIn(email: String, password: String): Observable<String> {
        return apiClient.service.signIn(AuthorizationRequest(email, password))
            .subscribeOn(Schedulers.io())
            .checkResponceResult()
            .observeOn(AndroidSchedulers.mainThread())
    }
}