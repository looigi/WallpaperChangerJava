package com.looigi.newlooplayer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConnectionQuality {
    public String RitornaQualitaConnessione() {
        NetworkInfo info = getInfo();
        if (info == null || !info.isConnected()) {
            return "-1;UNKNOWN;UNKNOWN";
        }

        if(info.getType() == ConnectivityManager.TYPE_WIFI) {
            VariabiliGlobali.getInstance().setTipoRete("");
            WifiManager wifiManager = (WifiManager) VariabiliGlobali.getInstance().getFragmentActivityPrincipale().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            if(level == 2 )
                return level + ";WIFI;POOR";
            else if(level == 3 )
                return level + ";WIFI;MODERATE";
            else if(level == 4 )
                return level + ";WIFI;GOOD";
            else if(level == 5 )
                return level + ";WIFI;EXCELLENT";
            else
                return "-2;WIFI;UNKNOWN";
        } else if(info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkClass = getNetworkClass(getNetworkType(VariabiliGlobali.getInstance().getContext()));
            if(networkClass == 1)
                return networkClass + ";MOBILE;POOR";
            else if(networkClass == 2 )
                return networkClass + ";MOBILE;GOOD";
            else if(networkClass == 3 )
                return networkClass + ";MOBILE;EXCELLENT";
            else
                return "-3;MOBILE;UNKNOWN";
        } else
            return "-4;UNKNOWN;UNKNOWN";
    }

    public NetworkInfo getInfo() {
        return ((ConnectivityManager) VariabiliGlobali.getInstance().getFragmentActivityPrincipale().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    public int getNetworkClass(int networkType) {
        try {
            return getNetworkClassReflect(networkType);
        }catch (Exception ignored) {
        }

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                VariabiliGlobali.getInstance().setTipoRete("GPRS");
            case 16: // TelephonyManager.NETWORK_TYPE_GSM:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                VariabiliGlobali.getInstance().setTipoRete("GSM");
                return 1;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case 17: // TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                VariabiliGlobali.getInstance().setTipoRete("UMTS");
                return 2;
            case TelephonyManager.NETWORK_TYPE_LTE:
            case 18: // TelephonyManager.NETWORK_TYPE_IWLAN:
                VariabiliGlobali.getInstance().setTipoRete("LTE");
                return 3;
            default:
                VariabiliGlobali.getInstance().setTipoRete("");
                return 0;
        }
    }

    private int getNetworkClassReflect(int networkType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getNetworkClass = TelephonyManager.class.getDeclaredMethod("getNetworkClass", int.class);
        if (!getNetworkClass.isAccessible()) {
            getNetworkClass.setAccessible(true);
        }
        return (Integer) getNetworkClass.invoke(null, networkType);
    }

    public static int getNetworkType(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType();
        } catch(SecurityException ignored) {
        }
        return -1;
    }
}
