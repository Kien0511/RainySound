package com.example.rainysound.MySharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {
    public static String MY_SHAREDPREFERENCE = "com.example.rainysound.MY_SHAREDPREFERENCE";

    public static String ITEM_POSITION = "com.example.rainysound.ITEM_POSITION";
    public static String ITEM_TITLE = "com.example.rainysound.ITEM_TITLE";
    public static String STATE_MEDIA = "com.example.rainysound.STATE_MEDIA";
    public static String STATE_DESTROY = "com.example.rainysound.STATE_DESTROY";

    public static void putData (Context context, String key, int value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHAREDPREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public static void putData (Context context, String key, String value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHAREDPREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static void putData (Context context, String key, boolean value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHAREDPREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static int getDataInt(Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHAREDPREFERENCE,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,-1);
    }

    public static String getDataString(Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHAREDPREFERENCE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public static boolean getDataBolean(Context context, String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHAREDPREFERENCE,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
}
