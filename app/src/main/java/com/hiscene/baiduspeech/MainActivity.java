package com.hiscene.baiduspeech;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.speech.aidl.EventManager;
import com.unity3d.player.UnityPlayerActivity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

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
    public void CheckPermissions(String[] permissons) {
        Debug.Log("MainActivity/CheckPermissions()/permissons:"+permissons.toString());
        PermissionsHelper.CheckPermissions(permissons);
    }

    //构造语音识别类
    public Speech NewSpeech() {
        Debug.Log("MainActivity/Speech()/构造语音识别类!");
        return new Speech(m_Activity);
    }

    //构造唤醒词类
    public Wakeup NewWakeup() {
        Debug.Log("MainActivity/NewWakeup()/构造唤醒词类!");
        return new Wakeup(m_Activity);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Debug.Log("MainActivity/onRequestPermissionsResult()/permissions:"+permissions.toString());
        PermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}