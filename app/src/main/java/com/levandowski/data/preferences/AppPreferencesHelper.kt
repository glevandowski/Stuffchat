package com.levandowski.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.levandowski.utils.AppConstants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesHelper @Inject constructor(val context: Context) {

    var mPrefs: SharedPreferences? = null

    init {
        mPrefs = context.getSharedPreferences("", Context.MODE_PRIVATE)
    }

    var id:Long? set(value) {
        val id = value ?: AppConstants.NULL_INDEX
        mPrefs?.edit()?.putLong(PREF_KEY_CURRENT_ID, id)?.apply()
    }
    get() {
        val userId = mPrefs?.getLong (PREF_KEY_CURRENT_ID, AppConstants.NULL_INDEX)
        return if (userId == AppConstants.NULL_INDEX)  null else userId
    }

    var token:String? set(value) {
        mPrefs?.edit()?.putString(PREF_KEY_CURRENT_ID, value)?.apply()
    }
    get() {
        return mPrefs?.getString(PREF_KEY_CURRENT_TOKEN,null)
    }

    var authTimeStamp:Long? set(value) {
        val timeStamp = value ?: AppConstants.NULL_INDEX
        mPrefs?.edit()?.putLong(PREF_KEY_CURRENT_AUTH_TIMESTAMP, timeStamp)?.apply()
    }
    get() {
        val timeStamp = mPrefs?.getLong (PREF_KEY_CURRENT_AUTH_TIMESTAMP, AppConstants.NULL_INDEX)
        return if (timeStamp == AppConstants.NULL_INDEX)  null else timeStamp
    }

    companion object {
        const val PREF_KEY_CURRENT_TOKEN = "PREF_KEY_CURRENT_TOKEN"
        const val PREF_KEY_CURRENT_ID = "PREF_KEY_CURRENT_ID"
        const val PREF_KEY_CURRENT_AUTH_TIMESTAMP = "PREF_KEY_CURRENT_AUTH_TIMESTAMP"
    }
}
