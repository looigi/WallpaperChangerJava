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

            /* if (VariabiliGlobali.getInstance().isImmagineDaCambiare()) {
                Log.getInstance().ScriveLog("---Cambio immagine dopo unlock schermo---");
                ChangeWallpaper c = new ChangeWallpaper();
                if (!VariabiliGlobali.getInstance().isOffline()) {
                    boolean fatto = c.setWallpaper(null);
                    Log.getInstance().ScriveLog("---Immagine cambiata online dopo unlock schermo: " + fatto + "---");
                } else {
                    int numeroRandom = Utility.getInstance().GeneraNumeroRandom(VariabiliGlobali.getInstance().getListaImmagini().size() - 1);
                    if (numeroRandom > -1) {
                        boolean fatto = c.setWallpaper(VariabiliGlobali.getInstance().getListaImmagini().get(numeroRandom));
                        Log.getInstance().ScriveLog("---Immagine cambiata dopo un unlock schermo: " + fatto + "---");
                    } else {
                        Log.getInstance().ScriveLog("---Immagine NON cambiata dopo un unlock schermo: Caricamento immagini in corso---");
                    }
                }

                VariabiliGlobali.getInstance().setImmagineDaCambiare(false);
            } */
            VariabiliGlobali.getInstance().setImmagineCambiataConSchermoSpento(false);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.getInstance().ScriveLog("Phone locked");
            VariabiliGlobali.getInstance().setScreenOn(false);
        }
    }
}