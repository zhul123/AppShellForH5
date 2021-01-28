
package com.capinfo.fysystem.event;

/**
 * 网络监听event
 */

public class NetWorkChangedEvent {
    public Boolean mNetWorkConnected;

    public NetWorkChangedEvent(Boolean netWorkConnected) {
        mNetWorkConnected = netWorkConnected;
    }
}
