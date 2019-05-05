package com.example.managingtask.data.manager

import android.content.Context
import android.util.Log
import com.example.managingtask.data.ITokenManager
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val context: Context
): ITokenManager {

    companion object {
        const val KEY_TOKEN = "key_token"
        const val PREF_TOKEN_MANAGER = "pref_token_manager"
    }

    private var buffer: String? = null

    override var token: String?
        get() {
            if (buffer == null) buffer = getFromPreference() ?: ""
            Log.d("TAG","Current Token: $buffer")
            return buffer
        }
        set(value) {
            buffer = if (value?.isNotEmpty() != true) "" else value
            setToPreferences(buffer)
            Log.d("TAG", "Token was updated\nCurrent Token: $buffer")
        }

    override fun clear() {
        Log.d("TAG", "Token was updated\nempty")
        clearPreferences()
    }

    private fun setToPreferences(token: String?){
        val editor = context.getSharedPreferences(PREF_TOKEN_MANAGER, Context.MODE_PRIVATE).edit()
        if (token?.isNotEmpty() != true) editor.remove(KEY_TOKEN)
        else editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    private fun getFromPreference(): String? = context.getSharedPreferences(PREF_TOKEN_MANAGER, Context.MODE_PRIVATE).getString(KEY_TOKEN, null)

    private fun clearPreferences() = context.getSharedPreferences(PREF_TOKEN_MANAGER, Context.MODE_PRIVATE).edit().clear().apply()
}