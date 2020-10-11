package com.hiscene.baiduspeech;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONObject;

import java.util.HashMap;

//唤醒词
public class Wakeup {

    private static boolean isInited = false;
    private static MainActivity m_Activity = null;
    private static EventManager wp = null;//唤醒词对象
    private static EventListener eventListener;

    public Wakeup() {
    }

    public Wakeup(MainActivity activity) {
        m_Activity = activity;
        Init();
    }

    //初始化唤醒词
    private static void Init() {

        if (isInited) {
            Debug.LogWarning("Wakeup/Init()/还未调用release()，请勿新建一个新类！");
            return;
        } else {
            Debug.Log("Wakeup/Init()/初始化唤醒词！");
        }

        isInited = true;

        wp = EventManagerFactory.create(m_Activity, "wp");
        eventListener = new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                OnEventListener(name, params, data, offset, length);
            }
        };

        wp.registerListener(eventListener);
    }

    //开始唤醒词
    public static void Start(String wakeUpPath) {
        Debug.Log("Wakeup/Start()/开始唤醒词！");
        //map.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        HashMap map = new HashMap();
        map.put(SpeechConstant.WP_WORDS_FILE, wakeUpPath);
        String json = new JSONObject(map).toString();
        wp.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    //停止唤醒词
    public static void Stop() {
        Debug.Log("Wakeup/Stop()/停止唤醒词！");
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
    }

    //释放唤醒词
    public static void Release() {
        Debug.Log("Wakeup/Release()/释放唤醒词！");
        Stop();
        wp.unregisterListener(eventListener);
        wp = null;
        isInited = false;
    }

    //唤醒词回调
    private static void OnEventListener(String name, String params, byte[] data, int offset, int length) {
        Debug.Log("Wakeup/OnEventListener()/唤醒词回调！" + name + "|" + params);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", name);
            jsonObject.put("param", params);
            jsonObject.put("data",data);
            jsonObject.put("offset",offset);
            jsonObject.put("length",length);

            BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnWakeupCallback, jsonObject.toString());

        } catch (Exception e) {

            Debug.LogWarning("Wakeup/OnWakeupCallback()/唤醒词回调错误！Exception:" + e);
        }
    }
}
