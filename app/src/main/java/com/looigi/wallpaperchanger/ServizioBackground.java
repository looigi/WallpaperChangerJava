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
    private int tempoTimer = 10000;
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

        VariabiliGlobali.getInstance().setImmagineDaCambiare(false);
        secondiPassati = 0;
        int quantiGiri = VariabiliGlobali.getInstance().getSecondiAlCambio() / tempoTimer;

        handlerTimer = new Handler(Looper.getMainLooper());
        handlerTimer.postDelayed(runTimer = new Runnable() {
            @Override
            public void run() {
                secondiPassati++;
                if (secondiPassati >= quantiGiri) {
                    secondiPassati = 0;

                    if (VariabiliGlobali.getInstance().isScreenOn()) {
                        Log.getInstance().ScriveLog("---Cambio Immagine---");
                        int numeroRandom = Utility.getInstance().GeneraNumeroRandom(VariabiliGlobali.getInstance().getListaImmagini().size() - 1);
                        ChangeWallpaper c = new ChangeWallpaper();
                        boolean fatto = c.setWallpaper(VariabiliGlobali.getInstance().getListaImmagini().get(numeroRandom));
                        Log.getInstance().ScriveLog("---Immagine cambiata: " + fatto + "---");
                    } else {
                        Log.getInstance().ScriveLog("---Cambio Immagine posticipato per schermo spento---");
                        VariabiliGlobali.getInstance().setImmagineDaCambiare(true);
                    }
                }

                Log.getInstance().ScriveLog("Secondi passati dall'avvio: " + Integer.toString(secondiPassati) + "/" + Integer.toString(quantiGiri) +
                        ". Schermo acceso: " + VariabiliGlobali.getInstance().isScreenOn());

                handlerTimer.postDelayed(this, tempoTimer);
            }
        }, tempoTimer);
        // VariabiliGlobali.getInstance().getFragmentActivityPrincipale().moveTaskToBack(true);
        VariabiliGlobali.getInstance().setePartito(true);

        return START_STICKY;
    }

    private void InstanziaNotifica() {
        Log.getInstance().ScriveLog("Instanzia notifica");

        Notifica.getInstance().setContext(VariabiliGlobali.getInstance().getContext());
        if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
            Notifica.getInstance().setTitolo(VariabiliGlobali.getInstance().getUltimaImmagine().getImmagine());
            Notifica.getInstance().setImmagine(VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine());
        } else {
            Notifica.getInstance().setTitolo("");
            Notifica.getInstance().setImmagine("");
        }

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
    }
}
