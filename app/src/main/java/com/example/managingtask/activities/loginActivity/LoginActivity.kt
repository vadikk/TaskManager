package com.example.managingtask.activities.loginActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.example.managingtask.R
import com.example.managingtask.activities.mainActivity.MainActivity
import com.example.managingtask.dagger.App
import com.example.managingtask.data.model.ErrorResponse
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginActivityContract.View {

    companion object {
        const val REGISTARTION = "registartion"
        const val SIGN_IN = "signIn"
    }

    private val regSharedPreference by lazy { getSharedPreferences(REGISTARTION, Context.MODE_PRIVATE) }
    private var isSignIn = true

    @Inject
    lateinit var presenter: LoginActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        App.appComponent.inject(this)
        presenter.init(this)

        emailTextInputLayout.isErrorEnabled = false

        isSignIn = regSharedPreference.getBoolean(SIGN_IN, true)

        if (isSignIn) init(isSignIn, getString(R.string.sign_up))
        else init(isSignIn, getString(R.string.sign_in))

        loginSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) init(isChecked, getString(R.string.sign_up))
            else init(isChecked, getString(R.string.sign_in))
        }

        loginBtn.setOnClickListener {
            if (loginSwitch.isChecked)
                presenter.signUp(emailEditText.text.trim().toString(), passwordEditText.text.trim().toString())
            else
                presenter.signIn(emailEditText.text.trim().toString(), passwordEditText.text.trim().toString())
        }

        emailEditText.doOnTextChanged { text, start, count, after ->
            emailTextInputLayout.error = ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun goToMainView() {

        isSignIn = false

        val editor = regSharedPreference.edit().apply{
            putBoolean(SIGN_IN, false)
            apply()
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showError(errorResponce: ErrorResponse) {
        emailTextInputLayout.isErrorEnabled = true

        val stringBuilder = StringBuilder().apply {
            append(errorResponce.message + ". " )

            if (errorResponce.fields != null) append(errorResponce.fields.emailError.first())
        }
        emailTextInputLayout.error = stringBuilder.toString()
    }

    private fun init(isSign: Boolean, signInText: String){
        signInTextView.text = signInText
        loginBtn.text = signInText
        loginSwitch.setChecked(isSign)
    }
}
