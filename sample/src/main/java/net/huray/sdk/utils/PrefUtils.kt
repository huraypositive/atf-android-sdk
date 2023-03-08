package net.huray.sdk.utils

import android.annotation.SuppressLint
import android.content.Context
import net.huray.sdk.App.Companion.instance
import net.huray.sdk.model.AtfleeDeviceType

@SuppressLint("CommitPrefEdits")
object PrefUtils {
    private val SHARED_PREFERENCES =
        instance.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE)

    fun saveDeviceAddress(address: String, deviceType: AtfleeDeviceType) {
        SHARED_PREFERENCES
            .edit()
            .putString(deviceType.scanName, address)
            .apply()
    }

    fun getDeviceAddress(deviceType: AtfleeDeviceType): String? {
        return SHARED_PREFERENCES.getString(deviceType.scanName, null)
    }

    fun removeDeviceAddress(deviceType: AtfleeDeviceType) {
        SHARED_PREFERENCES
            .edit()
            .putString(deviceType.scanName, null)
            .apply()
    }
}