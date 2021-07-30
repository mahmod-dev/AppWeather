package com.mahmouddev.appweather.util

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object MyPreferences {
    var context: Context? = null
    const val PREFS_NAME = "prefs"

    fun setInt(key: String?, value: Int) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String?,defValue:Int = 0): Int {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(key, defValue)
    }

    fun setStr(key: String?, value: String?) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun String.prefSave(key: String?) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putString(key, this)
        editor.apply()
    }


    fun getStr(key: String?): String? {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString(key, null)
    }

    fun getStr(key: String?, defValue: String?): String? {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString(key, defValue)
    }

    fun setBool(key: String?, value: Boolean) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBool(key: String?,defValue:Boolean =false): Boolean {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean(key, defValue)
    }

    fun setLong(key: String?, value: Long) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String?): Long {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getLong(key, 0)
    }

    fun setFloat(key: String?, value: Float) {
        val sharedPref = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getFloat(key: String?): Float {
        val prefs = context!!.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefs.getFloat(key, 0.0f)
    }

}