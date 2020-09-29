package com.hiscene.baiduspeech;

import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONObject;

import java.util.HashMap;

//唤醒词
public class Wakeup {

    private static MainActivity m_Activity = null;
    private static EventManager wp = null;//唤醒词对象

    public Wakeup() { }

    public Wakeup(MainActivity activity) {
        m_Activity = activity;
        Init();
    }

    //初始化唤醒词
    private static void Init() {
        Debug.Log("Wakeup/Init()/初始化唤醒词！");
        wp = EventManagerFactory.create(m_Activity, "wp");
        EventListener yourListener = new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                OnWakeupCallback(name, params);
            }
        };

        wp.registerListener(yourListener);
    }

    //开始唤醒词
    public static void StartWakeup(String wakeUpPath) {
        Debug.Log("Wakeup/StartWakeup()/开始唤醒词！");
        //map.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        HashMap map = new HashMap();
        map.put(SpeechConstant.WP_WORDS_FILE, wakeUpPath);
        String json = new JSONObject(map).toString();
        wp.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    //停止唤醒词
    public static void StopWakeup() {
        Debug.Log("Wakeup/StopWakeup()/停止唤醒词！");
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
    }

    //唤醒词回调
    private static void OnWakeupCallback(String name, String params) {
        Debug.Log("Wakeup/OnWakeupCallback()/唤醒词回调！" + name + "|" + params);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", name);
            jsonObject.put("paramsData", params);

            BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnWakeupCallback, jsonObject.toString());

        } catch (Exception e) {

            Debug.LogWarning("Wakeup/OnWakeupCallback()/唤醒词回调错误！Exception:" + e);
        }
    }
}
