package com.looigi.newlooplayer;

import android.content.Context;
import android.os.PowerManager;

import static android.content.Context.POWER_SERVICE;

public class GestioneCPU {
    private Context ctx;
    private static final GestioneCPU ourInstance = new GestioneCPU();
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private boolean GiaAttivo;

    public static GestioneCPU getInstance() {
        return ourInstance;
    }

    private GestioneCPU() {
    }

    public void ImpostaValori(Context ctx) {
        this.ctx = ctx;
        powerManager = (PowerManager) ctx.getSystemService(POWER_SERVICE);
        GiaAttivo = false;
    }

    public void AttivaCPU() {
        Log.getInstance().ScriveLog("Attiva CPU");

        if (!GiaAttivo) {
            if (ctx == null) {
                ctx = VariabiliGlobali.getInstance().getContext();
            }
            if (powerManager == null && ctx != null) {
                powerManager = (PowerManager) ctx.getSystemService(POWER_SERVICE);
            }
            if (powerManager != null) {
                wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        "MyApp::MyWakelockTag");
                wakeLock.acquire(60000);
                GiaAttivo = true;

                Log.getInstance().ScriveLog("Attivata");
            } else {
                Log.getInstance().ScriveLog("NON Attivata");
            }
        } else {
            Log.getInstance().ScriveLog("Già Attivata");
        }
    }

    public void DisattivaCPU() {
        Log.getInstance().ScriveLog("Interrompo CPU");

        if (wakeLock != null) {
            try {
                if (wakeLock.isHeld()) {
                    wakeLock.release();

                    Log.getInstance().ScriveLog("Interrotta");
                } else {
                    Log.getInstance().ScriveLog("NON Held");
                }
            } catch (Exception ignored) {
                String e = Utility.getInstance().PrendeErroreDaException(ignored);

                Log.getInstance().ScriveLog("ERRORE: " + e);
            }
        }
        GiaAttivo = false;
    }
}
