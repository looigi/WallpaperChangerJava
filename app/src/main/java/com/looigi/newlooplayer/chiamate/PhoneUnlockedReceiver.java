package com.looigi.newlooplayer.chiamate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.VariabiliGlobali;

public class PhoneUnlockedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Log.getInstance().ScriveLog("Phone unlocked");
            // NetThreadNuovo.getInstance().setScreenOn(true);
            VariabiliGlobali.getInstance().setScreenOn(true);
        }else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.getInstance().ScriveLog("Phone locked");
            // NetThreadNuovo.getInstance().setScreenOn(false);
            VariabiliGlobali.getInstance().setScreenOn(false);
        }
    }
}