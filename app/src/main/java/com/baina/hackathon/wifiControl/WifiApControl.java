package com.baina.hackathon.wifiControl;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by jxyi on 2016/10/23.
 */

public class WifiApControl {

    private static WifiApAdmin s_WifiApAdmin = null;

    public static void openWifiAp(Context context, final String apName, final String pwd)
    {
        WifiApAdmin wifiApAdmin = new WifiApAdmin(context);
        s_WifiApAdmin = wifiApAdmin;
        wifiApAdmin.startWifiAp(apName, pwd);
    }

    public static void closeWifiAp()
    {
        if (s_WifiApAdmin != null)
        {

        }

    }

    public static String getWiFiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID",wifiInfo.getSSID());
        return wifiInfo.getSSID();
    }
}
