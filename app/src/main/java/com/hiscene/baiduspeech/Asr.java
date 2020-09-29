package com.hiscene.baiduspeech;

import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONObject;

//语音识别
public class Asr {

    private static MainActivity m_Activity = null;
    private static EventManager asr = null;//语音识别对象

    public Asr(){}

    public Asr(MainActivity activity) {
        m_Activity = activity;
        Init();
    }

    //初始化语音识别
    private static void Init() {
        asr = EventManagerFactory.create(m_Activity, "asr");
        EventListener myEventListener = new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] bytes, int i, int i1) {
                OnAsrCallback(name, params);
            }
        };

        asr.registerListener(myEventListener);
    }

    //开始录音方法
    public static void VoiceStart(String json) {
        Debug.Log("Speech/StartVoice()/开始录音方法! json:"+json.toString());
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }

    //取消本次识别，取消后将立即停止不会返回识别结果
    public static void VoiceCancel() {
        Debug.Log("Speech/CancelVoice()/取消本次识别!");
        asr.send(SpeechConstant.ASR_CANCEL, null, null, 0, 0);
    }

    //停止录音方法
    public static void VoiceStop() {
        Debug.Log("Speech/StopVoice()/停止录音方法!");
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
    }

    //语音识别回调
    private static void OnAsrCallback(String name, String params) {
        Debug.Log("Speech/OnSpeechCallback()/语音识别回调！" + name + "|" + params);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", name);
            jsonObject.put("paramsData", params);

            BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnSpeechCallback, jsonObject.toString());
        } catch (Exception e) {
            Debug.LogWarning("Speech/OnSpeechCallback()/语音识别回调错误！Exception：" + e);
        }

    }
}
