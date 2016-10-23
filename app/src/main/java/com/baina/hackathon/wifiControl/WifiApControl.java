package com.baina.hackathon.wifiControl;

import android.content.Context;

/**
 * Created by jxyi on 2016/10/23.
 */

public class WifiApControl {

    public static void openWifiAp(Context context, final String apName, final String pwd)
    {
        WifiApAdmin wifiApAdmin = new WifiApAdmin(context);
        wifiApAdmin.startWifiAp(apName, pwd);
    }

}
