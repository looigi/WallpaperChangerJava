package com.looigi.wallpaperchanger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import org.kobjects.util.Util;

public class ServizioBackground extends android.app.Service {
    private Runnable runTimer;
    private Handler handlerTimer;
    // private int secondiPassati = 0;
    // private int tempoTimer = 10000;

    private PhoneUnlockedReceiver receiverAccesoSpento;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.getInstance().ScriveLog("Partenza servizio");

        AzionaControlloSchermo();
        Utility.getInstance().InstanziaNotifica();

        VariabiliGlobali.getInstance().setImmagineDaCambiare(false);
        VariabiliGlobali.getInstance().setSecondiPassati(0);
        VariabiliGlobali.getInstance().setQuantiGiri(VariabiliGlobali.getInstance().getSecondiAlCambio() / VariabiliGlobali.getInstance().getTempoTimer());

        handlerTimer = new Handler(Looper.getMainLooper());
        handlerTimer.postDelayed(runTimer = new Runnable() {
            @Override
            public void run() {
                if (MainActivity.getAppContext() != null) {
                    VariabiliGlobali.getInstance().setSecondiPassati(VariabiliGlobali.getInstance().getSecondiPassati() + 1);
                    VariabiliGlobali.getInstance().getTxtTempoAlCambio().setText("Prossimo cambio: " +
                            VariabiliGlobali.getInstance().getSecondiPassati() + "/" + VariabiliGlobali.getInstance().getQuantiGiri());

                    Log.getInstance().ScriveLog("Prossimo cambio: " +
                            VariabiliGlobali.getInstance().getSecondiPassati() + "/" + VariabiliGlobali.getInstance().getQuantiGiri() +
                            ". Schermo acceso: " + VariabiliGlobali.getInstance().isScreenOn());
                    if (VariabiliGlobali.getInstance().getSecondiPassati() >= VariabiliGlobali.getInstance().getQuantiGiri()) {
                        VariabiliGlobali.getInstance().setSecondiPassati(0);
                        if (VariabiliGlobali.getInstance().isScreenOn()) {
                            ChangeWallpaper c = new ChangeWallpaper();
                            if (!VariabiliGlobali.getInstance().isOffline()) {
                                boolean fatto = c.setWallpaper(null);
                                Log.getInstance().ScriveLog("---Immagine cambiata manualmente: " + fatto + "---");
                            } else {
                                Log.getInstance().ScriveLog("---Cambio Immagine---");
                                int numeroRandom = Utility.getInstance().GeneraNumeroRandom(VariabiliGlobali.getInstance().getListaImmagini().size() - 1);
                                if (numeroRandom > -1) {
                                    boolean fatto = c.setWallpaper(VariabiliGlobali.getInstance().getListaImmagini().get(numeroRandom));
                                    Log.getInstance().ScriveLog("---Immagine cambiata: " + fatto + "---");
                                } else {
                                    Log.getInstance().ScriveLog("---Immagine NON cambiata: Caricamento immagini in corso---");
                                }
                            }
                        } else {
                            Log.getInstance().ScriveLog("---Cambio Immagine posticipato per schermo spento---");
                            VariabiliGlobali.getInstance().setImmagineDaCambiare(true);
                        }
                    }

                    // Log.getInstance().ScriveLog("Secondi passati dall'avvio: " + Integer.toString(VariabiliGlobali.getInstance().getSecondiPassati()) + "/" + Integer.toString(VariabiliGlobali.getInstance().getQuantiGiri()) +
                    //         ". Schermo acceso: " + VariabiliGlobali.getInstance().isScreenOn());

                    handlerTimer.postDelayed(this, VariabiliGlobali.getInstance().getTempoTimer());
                } else {
                    Log.getInstance().ScriveLog(">>>Blocco timer ed esco in quanto context non esistente<<<");
                    Notifica.getInstance().RimuoviNotifica();

                    System.exit(0);
                }
            }
        }, VariabiliGlobali.getInstance().getTempoTimer());

        if (MainActivity.getAppActivity() != null) {
            VariabiliGlobali.getInstance().setMascheraAperta(false);
            MainActivity.getAppActivity().moveTaskToBack(true);
        }
        VariabiliGlobali.getInstance().setePartito(true);

        return START_STICKY;
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
        Log.getInstance().ScriveLog(">>>DESTROY SERVIZIO<<<");
    }
}
