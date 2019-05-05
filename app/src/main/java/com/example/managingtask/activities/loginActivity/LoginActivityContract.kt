package com.example.managingtask.activities.loginActivity

import com.example.managingtask.data.model.ErrorModel
import com.example.managingtask.data.model.ErrorResponse
import com.example.managingtask.data.model.responce.AuthorizationResponce

interface LoginActivityContract {

    interface View{
        fun goToMainView()
        fun showError(errorResponce: ErrorResponse)
    }

    interface Presenter{
        fun init(view: View)
        fun destroy()
        fun signUp(email: String, password: String)
        fun signIn(email: String, password: String)
    }
}