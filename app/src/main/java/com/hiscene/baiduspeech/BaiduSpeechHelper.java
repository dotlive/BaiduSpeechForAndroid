package com.hiscene.baiduspeech;

import com.unity3d.player.UnityPlayer;

import org.json.JSONObject;

public class BaiduSpeechHelper {

    private static MainActivity m_Activity = null;
    private static String m_UnityObjectName = "";
    private static String m_Methodname = "OnMessage";

    //初始化
    public static void Init(MainActivity activity, String unityObjectName) {
        m_Activity = activity;
        m_UnityObjectName = unityObjectName;
    }

    //平台发送消息给Unity
    public static void SendPlatformMessageToUnity(MessageCode msgCode, String param) {
        int msgCodeInt = msgCode.ordinal();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgCode", msgCodeInt);
            jsonObject.put("Content", param);
            UnityPlayer.UnitySendMessage(m_UnityObjectName, m_Methodname, jsonObject.toString());
        } catch (Exception e) {
            Debug.LogWarning("BaiduSpeechHelper/SendPlatformMessageToUnity()/Exception:" + e);
        }
    }

    //平台发送消息给Unity
    public static void SendPlatformMessageToUnity(MessageCode msgCode, String... params)
    {
        int msgCodeInt = msgCode.ordinal();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgCode", msgCodeInt);
            jsonObject.put("ContentArr", params);
            UnityPlayer.UnitySendMessage(m_UnityObjectName, m_Methodname, jsonObject.toString());
        } catch (Exception e) {
            Debug.LogWarning("BaiduSpeechHelper/SendPlatformMessageToUnity()/Exception:" + e);
        }
    }

    //Unity发给平台的消息
    public static void SendUnityMessageToPlatform(int iMsgCode, String... params) {
        MessageCode msgCode = MessageCode.values()[iMsgCode];

        switch (msgCode) {
            //todo
        }
    }

}
