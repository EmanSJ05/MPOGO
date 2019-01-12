package com.emansj.mpogo.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class AppSharedPref {
    private static SharedPreferences mSharedPref;
    private static  String PREFS_NAME = "MPOGO.SETTINGS";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASS = "user_pass";
    public static final String USER_ANDROID_ID = "user_android_id";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_TOPIC = "user_topic";
    public static final String USER_REMEMBER_LOGIN = "user_remember_login";
    public static final String APP_TAHUN_RKA = "app_tahun_rka";
    public static final String APP_FILTER_SEMUA_SATKER = "app_filter_semua_satker";
    public static final String APP_FILTER_SELECTED_IDSATKERS = "app_filter_selected_idsatkers";
    public static final String APP_FILTER_SELECTED_IDSATKERS_LIST = "app_filter_selected_idsatkers_list";
    public static final String APP_NOTIIFICATIONS = "app_notifications";


    private AppSharedPref() {}

    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).commit();
    }

    public static Set<String> readSet(String key, Set<String> defValue) {
        return mSharedPref.getStringSet(key, defValue);
    }

    public static void writeSet(String key, Set<String> value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putStringSet(key, value).commit();
    }
}
