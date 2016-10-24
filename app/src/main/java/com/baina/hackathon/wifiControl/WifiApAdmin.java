package com.baina.hackathon.wifiControl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import java.net.*;
import java.util.Enumeration;

/**
 * 创建热点
 */
public class WifiApAdmin {
    public static final String TAG = "WifiApAdmin";

    private static WifiApAdmin s_wifiApAdmin = null;

    private String mSSID = "";
    private String mPasswd = "";

    public static WifiApAdmin getInstance() {
        if (s_wifiApAdmin == null) {
            s_wifiApAdmin = new WifiApAdmin();
        }
        return s_wifiApAdmin;
    }

    private WifiApAdmin() {

    }

    public String getSSID(){
        return mSSID;
    }

    public String getPwd() {
        return mPasswd;
    }

    @SuppressWarnings("deprecation") // Deprecated because it doesn't handle IPv6 but wifimanager only supports IPV4 anyway
    public String getGateway(Context context)
    {
//        if (context == null)
//        {
//            return "";
//        }
//        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
//        return Formatter.formatIpAddress(dhcpInfo.gateway);
        return "192.168.43.1";
    }

    public enum WifiResult {
        WIFI_START,
        WIFI_FAIL
    }

    public interface StartWifiApListener {
        void onEvent(WifiResult status);
    }

    public void startWifiAp(final Context context, String ssid, String passwd, final StartWifiApListener listener) {
        if (context == null){
            return;
        }

        mSSID = ssid;
        mPasswd = passwd;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        startWifiAp(context);

        MyTimerCheck timerCheck = new MyTimerCheck() {

            @Override
            public void doTimerCheckWork() {
                if (isWifiApEnabled(context)) {
                    Log.v(TAG, "Wifi enabled success!");
                    if (listener != null) {
                        listener.onEvent(WifiResult.WIFI_START);
                    }
                    this.exit();
                } else {
                    Log.v(TAG, "Wifi enabled failed!");
                }
            }

            @Override
            public void doTimeOutWork() {
                if (listener != null) {
                    listener.onEvent(WifiResult.WIFI_FAIL);
                }
                this.exit();
            }
        };
        timerCheck.start(15, 1000);
    }

    private void startWifiAp(Context context) {
        if (context == null) {
            return ;
        }
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method1 = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;

            netConfig.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.TKIP);

            int networkId = wifiManager.addNetwork(netConfig);
            wifiManager.enableNetwork(networkId, true);

            method1.invoke(wifiManager, netConfig, true);

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void closeWifiAp(Context context) {
        if (context == null) {
            return;
        }
        mSSID = "";
        mPasswd = "";

        if (isWifiApEnabled(context)) {
            try {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);

                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);

                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public boolean isWifiApEnabled(Context context) {
        try {
            if (context == null) {
                mSSID = "";
                mPasswd = "";
                return false;
            }

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSSID = "";
        mPasswd = "";
        return false;
    }
}
