package com.aait.getak.utils

import android.content.Context
import android.content.SharedPreferences
import com.aait.getak.models.register_model.UserModel
import com.google.gson.Gson

class GlobalPreferences(private val context: Context) {
    private val PAYMENT: String? = "payment"
    private val CASH: String? = "cash"
    private val VISA: String? = "visa"
    private val PrefsEditor: SharedPreferences.Editor



    var isNew: Boolean
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getBoolean(STAT_NEW, true)
        }
        set(isNew) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putBoolean(STAT_NEW, isNew)
            editor.apply()
        }
    val _user_id: Int
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getInt(U_ID, 0)
        }
    val notifNum: Int
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getInt(NOTIF_COUNT, 0)
        }

    val user: UserModel?
        get() {
            val prefs = context.getSharedPreferences(
                PREFS_NAME, 0
            )
            return Gson().fromJson(prefs.getString(USER_DATA, null), UserModel::class.java)
        }
    val lang: String?
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getString(APP_lANGUAGE, "ar")
        }

    var token: String?
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getString(TOKEN, null)
        }
        set(token) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putString(TOKEN, token)
            editor.apply()
        }

    var selectedPayment: String?
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getString(PAYMENT, null)
        }
        set(selectedPayment) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putString(PAYMENT, selectedPayment)
            editor.apply()
        }

    var isActive: Boolean
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getBoolean(ACTIVE, true)
        }
        set(active) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putBoolean(ACTIVE, active)
            editor.apply()
        }
    var isPaidVisa: Boolean
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getBoolean(VISA, false)
        }
        set(active) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putBoolean(VISA, active)
            editor.apply()
        }
    var isPaidCash: Boolean
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getBoolean(CASH, true)
        }
        set(active) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putBoolean(CASH, active)
            editor.apply()
        }




    var isFirst: Boolean
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getBoolean(IS_FIRST, false)
        }

        set(isFirst) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putBoolean(IS_FIRST,isFirst)
            editor.apply()
        }


    init {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        PrefsEditor = prefs.edit()
        PrefsEditor.apply()
    }


    fun logout() {
        PrefsEditor.clear()
        PrefsEditor.commit()
    }

    fun storeSession(isLogin: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putBoolean(STAT, isLogin)
        editor.apply()
    }

    val isLogin: Boolean
        get() {
            val preferences = context.getSharedPreferences(PREFS_NAME, 0)
            return preferences.getBoolean(STAT, false)
        }

    fun store_user_id(user_id: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putInt(U_ID, user_id)
        editor.apply()
    }
    fun notif_count(count: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putInt(NOTIF_COUNT, count)
        editor.apply()
    }


    fun storeUser(user: UserModel) {
        val gson = Gson()
        val settings = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = settings.edit()
        editor.putString(USER_DATA, gson.toJson(user))
        editor.apply()
    }

    fun storeLang(lang: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putString(APP_lANGUAGE, lang)
        editor.apply()
    }

    companion object {
        internal val APP_lANGUAGE = "language"
        internal val PREFS_NAME = "settings"
        internal val SETTINGS_LOGIN = "login"
        private val POS = "pos"
        private val USER_DATA = "user"
        private val DEV = "device"
        private val STAT = "status"
        private val STAT_NEW = "new"
        private val MSG = "msg"
        private val PROFILE = "profile"
        private val STAT_ACT = "stat_Act"
        private val POSITION = "position"
        private val ORDER_COUNT = "order"
        private val NOTIF_COUNT = "notif_model"
        private val USER_ID = "id"
        private val AVATAR = "avatar"
        private val COVER = "cover"
        private val HISTORY = "history"
        private val REG_STAT = "reg_stat"
        private val U_ID = "u_id"
        private val SHOP_ID = "shop_id"
        private val SHOP_NAME = "shop_name"
        private val ACTIVE = "active"
        private val TOKEN = "token"
        private val IS_FIRST = "isFirst"
    }

}

