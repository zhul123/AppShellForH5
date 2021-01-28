package com.capinfo.fysystem.event;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于Activity Fragment Service 传递数据
 * 和Activity之间返回数据，等情况
 * Created by jiahongfei on 2017/2/17.
 */

public class TransmitData {

    public ConcurrentHashMap<String, Object> mDataMap = new ConcurrentHashMap<>();

    public TransmitData(String eventKey, Object eventData) {
        this(new String[]{eventKey}, new Object[]{eventData});
    }

    public TransmitData(String[] eventKeys, Object[] eventDatas) {
        for (int i = 0; i < eventKeys.length; i++) {
            mDataMap.put(eventKeys[i], eventDatas[i]);
        }

    }

    public Object getDataMap(String eventKey) {
        return mDataMap.get(eventKey);
    }

}
