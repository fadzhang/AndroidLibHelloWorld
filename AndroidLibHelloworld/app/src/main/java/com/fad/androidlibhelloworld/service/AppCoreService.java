package com.fad.androidlibhelloworld.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fad.androidlib.coreservice.CoreService;
import com.fad.androidlibhelloworld.constants.MessageType;

/**
 * 实现的主要功能:
 * 核心服务类
 *
 * @author zf 创建日期：2016/6/6
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class AppCoreService extends CoreService {

    @Override
    public void onCreate() {
        super.onCreate();

        // 监听网络状态变化，并向UI发送消息
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netStateReceiver);

    }

    private BroadcastReceiver netStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent arg1) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (gprs == null) { // gprs是空，说明该设备没有sim卡插槽，可能是个带wifi的pad
                if (wifi == null) { // 如果连wifi都是空的，那就没法上网了擦
                    // 网络连接断开
                    callbackUiDirect(MessageType.NET_UNCONNECT, null);
                    return;
                } else if (!wifi.isConnected()) { // 如果wifi没有连接，木有网呀
                    // 网络连接断开
                    callbackUiDirect(MessageType.NET_UNCONNECT, null);
                    return;
                } else { // wifi是连接上的
                    // 网络连接成功
                    callbackUiDirect(MessageType.NET_CONNECT_WIFI, null);
                    return;
                }

            }


            if (!gprs.isConnected()) { // 移动网络没有连接
                // 那就判断一下wifi
                if (wifi == null){
                    // wifi没有！！
                    // 网络连接断开
                    callbackUiDirect(MessageType.NET_UNCONNECT, null);
                } else{
                    // 有wifi，判断wifi
                    if(!wifi.isConnected()){
                        // wifi也没连，那就没网了
                        // 网络连接断开
                        callbackUiDirect(MessageType.NET_UNCONNECT, null);
                    } else {
                        // 还是有wifi的
                        callbackUiDirect(MessageType.NET_CONNECT_WIFI, null);
                    }
                }
            } else { // 移动网络有连接
                // 先要判断一下wifi,因为wifi是优先的
                if (wifi == null){
                    // 没有wifi，那就只有移动网络了
                    callbackUiDirect(MessageType.NET_CONNECTED_MOBILE, null);
                } else {
                    // 有wifi，那看它连上没
                    if(!wifi.isConnected()){
                        // 没连上，那就是移动网络
                        callbackUiDirect(MessageType.NET_CONNECTED_MOBILE, null);
                    } else {
                        // 连上了，wifi优先
                        callbackUiDirect(MessageType.NET_CONNECT_WIFI, null);
                    }
                }
            }
        }
    };
}
