package com.hiscene.baiduspeech;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.util.Arrays;

public class PermissionsHelper {

    private static MainActivity m_Activity = null;

    public static void Init(MainActivity activity) {
        m_Activity = activity;
    }

    //检查用户权限
    public static boolean CheckPermissions(String[] permissions) {

        boolean havePermission = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permisson : permissions) {
                if (m_Activity.checkSelfPermission(permisson) != PackageManager.PERMISSION_GRANTED) {
                    havePermission = false;
                    break;
                }
            }
        }
        return havePermission;
    }

    //请求权限
    public static void RequestPermissions(int requestCode, String[] permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean needPermission = false;
            for (String permisson : permissions) {
                if (m_Activity.checkSelfPermission(permisson) != PackageManager.PERMISSION_GRANTED) {
                    needPermission = true;
                    break;
                }
            }
            if (needPermission) {
                //用户没有权限
                m_Activity.requestPermissions(permissions, requestCode);
            } else {
                int[] grantResults = new int[permissions.length];
                Arrays.fill(grantResults, 1);
                onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            int[] grantResults = new int[permissions.length];
            Arrays.fill(grantResults, 1);
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //权限管理回调
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestCode", requestCode);
            jsonObject.put("permissions", permissions);
            jsonObject.put("grantResults", grantResults);
            BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.onRequestPermissionsResult, jsonObject.toString());

        } catch (Exception e) {
            Debug.LogWarning("PermissionsHelper/onRequestPermissionsResult()/Exception:" + e);
        }
    }
}
