package com.hiscene.baiduspeech;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PermissionsHelper {

    private static MainActivity m_Activity = null;
    private static final int PERMISSIONS_CODE = 656;

    public static void Init(MainActivity activity) {
        m_Activity = activity;
    }

    //检查用户权限
    public static void CheckPermissions(String[] permissons) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (m_Activity != null) {
                boolean needPermission = false;
                for (String permisson : permissons) {
                    if (m_Activity.checkSelfPermission(permisson) != PackageManager.PERMISSION_GRANTED) {
                        needPermission = true;
                        BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnNoPermissions, permisson);
                    } else {
                        //用户已经有了的权限
                        BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnHavePermissions, permisson);
                    }
                }
                //用户没有权限
                if (needPermission) {
                    BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnNoPermissions, permissons);
                    m_Activity.requestPermissions(permissons, PERMISSIONS_CODE);
                } else {
                    //用户已经有了所有权限
                    BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnHaveAllPermissions, permissons);
                }
            } else {
                //授权发生错误
                BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.Error, "checkPermissions: activity is null");
            }
        }
    }

    //权限管理回调
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String s = permissions[i];
                if (grantResults[i] != PERMISSION_GRANTED) {
                    BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnPermissionFail, permissions[i]);
                } else {
                    BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnPermissionSuccess, permissions[i]);
                }
            }
        }
    }

}
