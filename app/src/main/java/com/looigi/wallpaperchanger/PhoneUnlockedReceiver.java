package com.looigi.wallpaperchanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneUnlockedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Log.getInstance().ScriveLog("Phone unlocked");
            VariabiliGlobali.getInstance().setScreenOn(true);

            if (VariabiliGlobali.getInstance().isImmagineDaCambiare()) {
                Log.getInstance().ScriveLog("---Cambio immagine dopo unlock schermo---");
                int numeroRandom = Utility.getInstance().GeneraNumeroRandom(VariabiliGlobali.getInstance().getListaImmagini().size() - 1);
                ChangeWallpaper c = new ChangeWallpaper();
                boolean fatto = c.setWallpaper(VariabiliGlobali.getInstance().getListaImmagini().get(numeroRandom));
                VariabiliGlobali.getInstance().setImmagineDaCambiare(false);
                Log.getInstance().ScriveLog("---Immagine cambiata: " + fatto + "---");
            }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.getInstance().ScriveLog("Phone locked");
            VariabiliGlobali.getInstance().setScreenOn(false);
        }
    }
}