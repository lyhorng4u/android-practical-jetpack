package com.lyhorng.sampleapp.viewmodel.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        Constants.PREF_NAME,
        Context.MODE_PRIVATE
    )

    // Token - Use commit() for login to ensure it's written before Activity navigation
    fun saveToken(token: String) {
        preferences.edit().putString(Constants.KEY_TOKEN, token).commit()
    }
    
    fun getToken(): String? {
        return preferences.getString(Constants.KEY_TOKEN, null)
    }

    // User Data
    fun saveUserData(userId: Int, email: String, fullName: String) {
        preferences.edit().apply {
            putInt(Constants.KEY_USER_ID, userId)
            putString(Constants.KEY_USER_EMAIL, email)
            putString(Constants.KEY_USER_NAME, fullName)
            apply()
        }
    }
    
    fun getUserId(): Int {
        return preferences.getInt(Constants.KEY_USER_ID, -1)
    }
    
    fun getUserEmail(): String? {
        return preferences.getString(Constants.KEY_USER_EMAIL, null)
    }
    
    fun getUserName(): String? {
        return preferences.getString(Constants.KEY_USER_NAME, null)
    }
    
    fun isLoginIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }
    
    fun clearAll() {
        preferences.edit().clear().commit()
    }
}