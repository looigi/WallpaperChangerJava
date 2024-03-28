package com.looigi.wallpaperchanger.Notifiche;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.appsearch.GetSchemaResponse;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.looigi.wallpaperchanger.ChangeWallpaper;
import com.looigi.wallpaperchanger.Log;
import com.looigi.wallpaperchanger.MainActivity;
import com.looigi.wallpaperchanger.Permessi;
import com.looigi.wallpaperchanger.PhoneUnlockedReceiver;
import com.looigi.wallpaperchanger.R;
import com.looigi.wallpaperchanger.Utility;
import com.looigi.wallpaperchanger.VariabiliGlobali;

public class PartenzaServizio extends Service {
    private Runnable runTimer;
    private Handler handlerTimer;
    private PhoneUnlockedReceiver receiverAccesoSpento;
    private View viewActivity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        viewActivity = LayoutInflater.from(getApplication()).inflate(R.layout.activity_main, null);

        if (MainActivity.getAppContext() == null) {
            Log.getInstance().ScriveLog(">>>>>>>>>>>>>>>>>>>>>>>>NUOVA SESSIONE DA BOOT<<<<<<<<<<<<<<<<<<<<<<<<");
            Log.getInstance().ScriveLog("Partito: " + VariabiliGlobali.getInstance().isePartito());

            MainActivity.setAppContext(getApplicationContext());

        }
        // VariabiliGlobali.getInstance().setContext(this);
        // VariabiliGlobali.getInstance().setFragmentActivityPrincipale(this);


        String Titolo = "";
        String Immagine = "";

        if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
                Titolo = VariabiliGlobali.getInstance().getUltimaImmagine().getImmagine();
                Immagine = VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine();
        }

        Notification notification = GestioneNotifiche.getInstance().StartApplicazione(this, Titolo, Immagine);

        startForeground(GestioneNotifiche.getInstance().getIdNotifica(), notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!VariabiliGlobali.getInstance().isePartito()) {
            Context ctxEsterno = this;
            Log.getInstance().ScriveLog("Partenza servizio");

            AzionaControlloSchermo();
            // // RICHIAMO SERVIZIO PartenzaServizio.getInstance()InstanziaNotifica();

            // VariabiliGlobali.getInstance().setImmagineDaCambiare(false);
            VariabiliGlobali.getInstance().setSecondiPassati(0);
            VariabiliGlobali.getInstance().setQuantiGiri(VariabiliGlobali.getInstance().getSecondiAlCambio() / VariabiliGlobali.getInstance().getTempoTimer());

            handlerTimer = new Handler(Looper.getMainLooper());
            handlerTimer.postDelayed(runTimer = new Runnable() {
                @Override
                public void run() {
                    // Context ctx = MainActivity.getAppContext();
                    /* if (ctx == null) {
                        Log.getInstance().ScriveLog("Contxt main nullo. Provo a prendere quello locale");
                        ctx = ctxEsterno; // ServizioBackground.this; // getApplicationContext();
                    } */
                    if (ctxEsterno != null) {
                        VariabiliGlobali.getInstance().setSecondiPassati(VariabiliGlobali.getInstance().getSecondiPassati() + 1);
                        if (VariabiliGlobali.getInstance().getTxtTempoAlCambio() != null) {
                            VariabiliGlobali.getInstance().getTxtTempoAlCambio().setText("Prossimo cambio: " +
                                    VariabiliGlobali.getInstance().getSecondiPassati() + "/" + VariabiliGlobali.getInstance().getQuantiGiri());
                        }
                        /* Log.getInstance().ScriveLog("Prossimo cambio: " +
                                VariabiliGlobali.getInstance().getSecondiPassati() + "/" + VariabiliGlobali.getInstance().getQuantiGiri() +
                                ". Schermo acceso: " + VariabiliGlobali.getInstance().isScreenOn()); */
                        if (VariabiliGlobali.getInstance().getSecondiPassati() >= VariabiliGlobali.getInstance().getQuantiGiri()) {
                            VariabiliGlobali.getInstance().setSecondiPassati(0);
                            if (VariabiliGlobali.getInstance().isScreenOn()) {
                                if (VariabiliGlobali.getInstance().isOnOff()) {
                                    VariabiliGlobali.getInstance().setImmagineCambiataConSchermoSpento(false);
                                    CambiaImmagine();
                                }
                            } else {
                                if (!VariabiliGlobali.getInstance().isImmagineCambiataConSchermoSpento()) {
                                    if (VariabiliGlobali.getInstance().isOnOff()) {
                                        Log.getInstance().ScriveLog("---Cambio Immagine per schermo spento---");
                                        VariabiliGlobali.getInstance().setImmagineCambiataConSchermoSpento(true);
                                        CambiaImmagine();
                                    }
                                }
                                // VariabiliGlobali.getInstance().setImmagineDaCambiare(true);
                            }
                        }

                        // Log.getInstance().ScriveLog("Secondi passati dall'avvio: " + Integer.toString(VariabiliGlobali.getInstance().getSecondiPassati()) + "/" + Integer.toString(VariabiliGlobali.getInstance().getQuantiGiri()) +
                        //         ". Schermo acceso: " + VariabiliGlobali.getInstance().isScreenOn());

                        handlerTimer.postDelayed(this, VariabiliGlobali.getInstance().getTempoTimer());
                    } else {
                        Log.getInstance().ScriveLog(">>>Blocco timer e riavvio in quanto context non esistente<<<");
                        GestioneNotifiche.getInstance().RimuoviNotifica();

                        VariabiliGlobali.getInstance().setePartito(false);

                        Log.getInstance().ScriveLog(">>>Context per riavvio: " + ctxEsterno + "<<<");
                        PackageManager packageManager = ctxEsterno.getPackageManager();
                        Intent intent = packageManager.getLaunchIntentForPackage(ctxEsterno.getPackageName());
                        ComponentName componentName = intent.getComponent();
                        Log.getInstance().ScriveLog(">>>Component name: " + componentName.getClassName() + "<<<");
                        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                        ctxEsterno.startActivity(mainIntent);
                        Runtime.getRuntime().exit(0);

                        System.exit(0);
                    }
                }
            }, VariabiliGlobali.getInstance().getTempoTimer());

            if (MainActivity.getAppActivity() != null) {
                VariabiliGlobali.getInstance().setMascheraAperta(false);
                MainActivity.getAppActivity().moveTaskToBack(true);
            }
            VariabiliGlobali.getInstance().setePartito(true);
        } else {
            Log.getInstance().ScriveLog(">>>Servizio già partito<<<");
        }

        return START_STICKY;
    }

    private void CambiaImmagine() {
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
    public void onTrimMemory(int level) {
        Log.getInstance().ScriveLog(">>>ON TRIM MEMORY ON SERVIZIO<<<");

        super.onTrimMemory(level);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.getInstance().ScriveLog(">>>ON TASK REMOVED ON SERVIZIO<<<");

        super.onTaskRemoved(rootIntent);

        stopForeground(true);
        this.stopSelf();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.getInstance().ScriveLog(">>>UNBIND ON SERVIZIO<<<");

        return super.onUnbind(intent);
    }

    @Override
    public void onLowMemory() {
        Log.getInstance().ScriveLog(">>>LOW MEMORY ON SERVIZIO<<<");

        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiverAccesoSpento);
        Log.getInstance().ScriveLog(">>>DESTROY SERVIZIO<<<");

        super.onDestroy();
    }

}
