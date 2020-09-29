package com.hiscene.baiduspeech;

import android.util.Log;

public class Debug {

    public static String TAG = "baiduspeech";

    //普通日记
    public static void Log(String msg) {
        Log.d(TAG, msg);
    }

    //警告日记
    public static void LogWarning(String msg) {
        Log.d(TAG, msg);
    }

    //错误日记
    public static void LogError(String msg) {
        Log.d(TAG, msg);
    }

}
