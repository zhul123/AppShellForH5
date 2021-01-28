package com.capinfo.fysystem.utils;

import com.capinfo.fysystem.event.IntentEvent;
import com.capinfo.fysystem.event.ResultEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by hetao.
 */
public class EventBusUtils {

    public static void postEventBus(Object object) {
        EventBus.getDefault().post(object);
    }

    //以下方法用于传递数据和获取数据
    public static void putIntentEventData(String[] eventKeys, Object[] eventDatas){
        postEventSticky(new IntentEvent(eventKeys, eventDatas));
    }
    public static void putIntentEventData(String eventKey, Object eventData){
        putIntentEventData(new String[]{eventKey}, new Object[]{eventData});
    }
    public static void putResultEventData(String[] eventKeys, Object[] eventDatas){
        postEventSticky(new ResultEvent(eventKeys, eventDatas));
    }
    public static void putResultEventData(String eventKey, Object eventData){
        putResultEventData(new String[]{eventKey}, new Object[]{eventData});
    }
    //以下方法用于获取数据
    public static Object getIntentEventData(String eventKey){
        IntentEvent intentEvent = getStickyEvent(IntentEvent.class);
        if(null != intentEvent){
            return intentEvent.getDataMap(eventKey);
        }
        return null;
    }
    public static Object getResultEventData(String eventKey){
        ResultEvent resultEvent = getStickyEvent(ResultEvent.class);
        if(null != resultEvent){
            return resultEvent.getDataMap(eventKey);
        }
        return null;
    }
    //删除Sticky Event
    public static IntentEvent removeIntentEventData(){
        return removeStickyEvent(IntentEvent.class);
    }
    public static ResultEvent removeResultEventData(){
        return removeStickyEvent(ResultEvent.class);
    }

    //内部方法
    /**
     * EventBus发送粘性Event主要用于Activity 之间传递数据
     *
     * @param object
     */
    private static void postEventSticky(Object object) {
        EventBus.getDefault().postSticky(object);
    }

    /**
     * 获取粘性Event
     *
     * @param eventType
     * @param <T>
     * @return
     */
    private static <T> T getStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().getStickyEvent(eventType);
    }

    private static <T> T removeStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().removeStickyEvent(eventType);
    }
}
