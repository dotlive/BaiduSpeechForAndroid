package com.hiscene.baiduspeech;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.os.Build;
import android.os.Bundle;

import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends UnityPlayerActivity {

    private static MainActivity m_Activity = null;
    private static String m_UnityObjectName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //初始化
    private void Init(MainActivity activity, String unityObjectName) {
        m_Activity = activity;
        m_UnityObjectName = unityObjectName;

        BaiduSpeechHelper.Init(m_Activity, m_UnityObjectName);
        PermissionsHelper.Init(m_Activity);
    }

    //检查用户权限
    public int CheckPermissions(String permission) {
        Debug.Log("MainActivity/CheckPermissions()/permission:" + permission);
        return PermissionsHelper.CheckPermissions(permission);
    }

    //请求权限
    public void RequestPermissions(int requestCode, String[] permissions) {
        Debug.Log("MainActivity/RequestPermissions()/requestCode:"+requestCode+"--permissions:"+permissions.toString());
        PermissionsHelper.RequestPermissions(requestCode, permissions);
    }

    //构造语音识别类
    public Asr NewAsr() {
        Debug.Log("MainActivity/NewAsr()/构造语音识别类!");
        return new Asr(m_Activity);
    }

    //构造唤醒词类
    public Wakeup NewWakeup() {
        Debug.Log("MainActivity/NewWakeup()/构造唤醒词类!");
        return new Wakeup(m_Activity);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Debug.Log("MainActivity/onRequestPermissionsResult()/permissions:" + permissions.toString());
        PermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}