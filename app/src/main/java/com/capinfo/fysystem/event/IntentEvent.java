package com.capinfo.fysystem.event;


/**
 * 用于Activity Fragment Service 传递数据
 */
public class IntentEvent extends TransmitData {

    public IntentEvent(String eventKey, Object eventData) {
        super(new String[]{eventKey}, new Object[]{eventData});
    }

    public IntentEvent(String[] eventKeys, Object[] eventDatas) {
       super(eventKeys,eventDatas);
    }
}
