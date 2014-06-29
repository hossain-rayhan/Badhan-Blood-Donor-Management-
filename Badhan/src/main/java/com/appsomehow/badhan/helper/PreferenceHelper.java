package com.appsomehow.badhan.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rayhan on 6/29/2014.
 */
public class PreferenceHelper {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceHelper(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    public void setBoolean(String key, boolean value) {
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
