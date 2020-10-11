package com.hiscene.baiduspeech;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONObject;

//语音识别
public class Asr {

    private static MainActivity m_Activity = null;
    private static volatile boolean isInited = false;// 未release前，只能new一个
    private static boolean isOfflineEngineLoaded = false;// 是否加载离线资源
    private static EventManager asr = null;//语音识别对象
    private static EventListener eventListener;//回调事件

    public Asr() {
    }

    public Asr(MainActivity activity) {
        m_Activity = activity;
        Init();
    }

    //初始化语音识别
    private static void Init() {

        if (isInited) {
            Debug.LogWarning("Asr/Init()/还未调用release()，请勿新建一个新类!");
            return;
        } else {
            Debug.Log("Asr/Init()/初始化语音识别!");
        }

        isInited = true;
        asr = EventManagerFactory.create(m_Activity, "asr");
        eventListener = new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                OnEventListener(name, params, data, offset, length);
            }
        };

        asr.registerListener(eventListener);
    }

    //离线命令词，在线不需要调用
    public static void LoadOfflineEngine(String json) {
        Debug.Log("Speech/LoadOfflineEngine()/离线命令词! json:" + json.toString());
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, json, null, 0, 0);
        isOfflineEngineLoaded = true;
    }

    //开始录音方法
    public static void Start(String json) {
        if (!isInited) {
            Debug.LogWarning("Speech/Start()/语音识别没有初始化!");
            return;
        }

        Debug.Log("Speech/Start()/开始录音方法! json:" + json.toString());
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }

    //取消本次识别，取消后将立即停止不会返回识别结果
    public static void Cancel() {
        if (!isInited) {
            Debug.LogWarning("Speech/Cancel()/语音识别没有初始化!");
            return;
        }

        Debug.Log("Speech/Cancel()/取消本次识别!");
        asr.send(SpeechConstant.ASR_CANCEL, null, null, 0, 0);
    }

    //停止录音方法
    public static void Stop() {
        if (!isInited) {
            Debug.LogWarning("Speech/Stop()/语音识别没有初始化!");
            return;
        }

        Debug.Log("Speech/Stop()/停止录音方法!");
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
    }

    //释放算法
    public static void Release() {
        if (asr == null) {
            return;
        }
        Cancel();
        if (isOfflineEngineLoaded) {
            // SDK集成步骤 如果之前有调用过 加载离线命令词，这里要对应释放
            asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0);
            isOfflineEngineLoaded = false;
        }
        // SDK 集成步骤（可选），卸载listener
        asr.unregisterListener(eventListener);
        asr = null;
        isInited = false;
    }

    //语音识别回调
    private static void OnEventListener(String name, String params, byte[] data, int offset, int length) {
        Debug.Log("Speech/OnEventListener()/语音识别回调！" + name + "|" + params);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", name);
            jsonObject.put("param", params);
            jsonObject.put("data", data);
            jsonObject.put("offset", offset);
            jsonObject.put("length", length);

            BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnAsrCallback, jsonObject.toString());
        } catch (Exception e) {
            Debug.LogWarning("Speech/OnSpeechCallback()/语音识别回调错误！Exception：" + e);
        }

    }
}
