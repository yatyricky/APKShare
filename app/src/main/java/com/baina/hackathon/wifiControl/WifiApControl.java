package com.baina.hackathon.wifiControl;

import android.content.Context;

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
}
