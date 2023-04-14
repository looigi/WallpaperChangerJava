package com.looigi.wallpaperchanger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

public class ServizioBackground extends android.app.Service {
    private Runnable runTimer;
    private Handler handlerTimer;
    private int secondiPassati = 0;
    private PhoneUnlockedReceiver receiverAccesoSpento;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.getInstance().ScriveLog("Partenza servizio");

        AzionaControlloSchermo();
        InstanziaNotifica();

        secondiPassati = 0;

        handlerTimer = new Handler(Looper.getMainLooper());
        handlerTimer.postDelayed(runTimer = new Runnable() {
            @Override
            public void run() {
                secondiPassati++;

                Log.getInstance().ScriveLog("Secondi passati dall'avvio: " + Integer.toString(secondiPassati) + ". Schermo acceso: " +
                        VariabiliGlobali.getInstance().isScreenOn());

                handlerTimer.postDelayed(this, 10000);
            }
        }, 10000);
        VariabiliGlobali.getInstance().getFragmentActivityPrincipale().moveTaskToBack(true);

        return START_NOT_STICKY;
    }

    private void InstanziaNotifica() {
        Log.getInstance().ScriveLog("Instanzia notifica");

        Notifica.getInstance().setContext(VariabiliGlobali.getInstance().getContext());
        Notifica.getInstance().setTitolo("");
        Notifica.getInstance().setImmagine("");

        Notifica.getInstance().CreaNotifica();
    }

    private void AzionaControlloSchermo() {
        if (receiverAccesoSpento != null) {
            unregisterReceiver(receiverAccesoSpento);
            receiverAccesoSpento = null;
        }
        receiverAccesoSpento = new PhoneUnlockedReceiver();

        IntentFilter fRecv = new IntentFilter();
        fRecv.addAction(Intent.ACTION_USER_PRESENT);
        fRecv.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiverAccesoSpento, fRecv);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiverAccesoSpento);
    }
}
