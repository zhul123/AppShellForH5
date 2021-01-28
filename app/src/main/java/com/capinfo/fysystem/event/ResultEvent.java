package com.capinfo.fysystem.event;

/**
 * 用于Activity返回数据
 * Created by jiahongfei on 2017/2/17.
 */

public class ResultEvent extends TransmitData {

    public ResultEvent(String eventKey, Object eventData) {
        this(new String[]{eventKey}, new Object[]{eventData});
    }

    public ResultEvent(String[] eventKeys, Object[] eventDatas) {
        super(eventKeys,eventDatas);
    }
}
