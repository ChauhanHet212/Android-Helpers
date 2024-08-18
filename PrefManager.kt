package com.example.helper.extras

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class PrefManager {
    val sharedPreferences: SharedPreferences
    val sharedPreferencesEditor: SharedPreferences.Editor

    constructor(context: Context, name: String) {
        sharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences.edit()
    }

    fun put(key: String, value: Any) = sharedPreferencesEditor.putString(key, value.toJson()).commit()

    fun putInt(key: String, value: Int) = sharedPreferencesEditor.putInt(key, value).commit()

    fun putFloat(key: String, value: Float) = sharedPreferencesEditor.putFloat(key, value).commit()

    fun putLong(key: String, value: Long) = sharedPreferencesEditor.putLong(key, value).commit()

    fun putBoolean(key: String, value: Boolean) = sharedPreferencesEditor.putBoolean(key, value).commit()

    fun putString(key: String, value: String) = sharedPreferencesEditor.putString(key, value).commit()


    inline fun <reified T : Any> get(key: String) = (sharedPreferences.getString(key, "") ?: "").takeIf { it.isNotBlank() }?.toData<T>()

    fun getInt(key: String, defaultValue: Int = 0) = sharedPreferences.getInt(key, defaultValue)

    fun getFloat(key: String, defaultValue: Float = 0f) = sharedPreferences.getFloat(key, defaultValue)

    fun getLong(key: String, defaultValue: Long = 0L) = sharedPreferences.getLong(key, defaultValue)

    fun getBoolean(key: String, defaultValue: Boolean = false) = sharedPreferences.getBoolean(key, defaultValue)

    fun getString(key: String, defaultValue: String = "") = sharedPreferences.getString(key, defaultValue) ?: defaultValue
}