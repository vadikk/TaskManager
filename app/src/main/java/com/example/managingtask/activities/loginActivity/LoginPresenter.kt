package com.example.managingtask.activities.loginActivity

import android.util.Log
import com.example.managingtask.data.IAuthorizationRepository
import com.example.managingtask.data.ITokenManager
import com.example.managingtask.data.model.ErrorResponse
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val authorizationRepository: IAuthorizationRepository,
    private val tokenManger: ITokenManager
): LoginActivityContract.Presenter {

    private var view: LoginActivityContract.View? = null
    private var signUpDisposable: Disposable? = null
    private var signInDisposable: Disposable? = null

    override fun init(view: LoginActivityContract.View) {
        this.view = view
    }

    override fun destroy() {
        signUpDisposable?.dispose()
        signInDisposable?.dispose()
        view = null
    }

    override fun signUp(email: String, password: String) {
        if (signUpDisposable != null && signUpDisposable?.isDisposed != true) return

        signUpDisposable = authorizationRepository.signUp(email, password)
            .subscribe({
                tokenManger.token = it
                view?.goToMainView()
            },{
                view?.showError(it as ErrorResponse)
            })
    }

    override fun signIn(email: String, password: String) {
        if (signInDisposable != null && signInDisposable?.isDisposed != true) return

        signInDisposable = authorizationRepository.signIn(email, password)
            .subscribe({
                tokenManger.token = it
                view?.goToMainView()
            },{
                view?.showError(it as ErrorResponse)
//                Log.d("TAG","SignIn ErrorResponse - ${it.localizedMessage}")
            })
    }
}