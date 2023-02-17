package com.looigi.newlooplayer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetDetector {

    private InternetDetector() {
    }

    public static InternetDetector getInstance() {
        return new InternetDetector();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) VariabiliGlobali.getInstance().getFragmentActivityPrincipale().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        int netType = 0;
        if (netInfo != null) {
            netType = netInfo.getType();
            if (netInfo.isAvailable() && netInfo.isConnected()) {
                if (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
        }
        return false;

    }
}