package com.looigi.wallpaperchanger.Notifiche;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger.ChangeWallpaper;
import com.looigi.wallpaperchanger.Log;
import com.looigi.wallpaperchanger.MainActivity;
import com.looigi.wallpaperchanger.R;
import com.looigi.wallpaperchanger.Utility;
import com.looigi.wallpaperchanger.VariabiliGlobali;

import java.util.Date;

public class GestioneNotifiche {
    private NotificationManager manager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private final int idNotifica = 345678;
    private String channelName = "walppaperchanger";
    private String NOTIFICATION_CHANNEL_ID = "com.looigi.wallpaperchanger";
    private Context ctx;

    private static final GestioneNotifiche ourInstance = new GestioneNotifiche();

    public static GestioneNotifiche getInstance() {
        return ourInstance;
    }

    private GestioneNotifiche() {
    }

    public int getIdNotifica() {
        return idNotifica;
    }

    public Notification StartApplicazione(Context ctxP, String Titolo, String Immagine) {
        ctx = ctxP;
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_LOW);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        chan.setSound(null, null);
        chan.setImportance(NotificationManager.IMPORTANCE_LOW);

        manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        /* Intent intent = new Intent(ctx, PassaggioNotifica.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, idNotifica, intent, 0); */

        contentView = new RemoteViews(ctx.getPackageName(), R.layout.barra_notifica);
        setListenersTasti(contentView, ctx);
        setListeners(contentView);

        if (Titolo != null && !Titolo.isEmpty()) {
            contentView.setTextViewText(R.id.txtTitoloImmagine, Titolo);
        } else {
            contentView.setTextViewText(R.id.txtTitoloImmagine, "---");
        }

        if (Immagine != null && !Immagine.isEmpty()) {
            try {
                contentView.setImageViewBitmap(R.id.imgCopertina, BitmapFactory.decodeFile(Immagine));
            } catch (Exception ignored) {
                // Log.getInstance().ScriveLog("Immagine notifica: Errore 1");
                contentView.setImageViewResource(R.id.imgCopertina, R.drawable.logo);
            }
        } else {
            // Log.getInstance().ScriveLog("Immagine notifica non presente");
            contentView.setImageViewResource(R.id.imgCopertina, R.drawable.logo);
        }

        notificationBuilder = new NotificationCompat.Builder(ctx, NOTIFICATION_CHANNEL_ID);
        return notificationBuilder
                .setContentTitle("WallpaperChanger")                            // required
                .setSmallIcon(R.drawable.logo)   // required android.R.drawable.ic_menu_slideshow
                .setContentText("WallpaperChanger") // required
                // .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(false)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setAutoCancel(false)
                .setOngoing(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                // .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                // .setGroup("LOO'S WEB PLAYER")
                // .setGroupSummary(true)
                // .setDefaults(NotificationCompat.DEFAULT_ALL)
                // .setPriority(NotificationManager.IMPORTANCE_LOW)
                // .setContentIntent(pendingIntent)
                .setTicker("")
                .setContent(contentView)
                .build();

        // GestioneNotifiche.getInstance().ImpostaValori(manager, idNotifica, notificationBuilder, contentView);
    }

    private void setListeners(RemoteViews view) {
        if (view != null) {
            Log.getInstance().ScriveLog("Set Listeners. View corretta");

            // String Traccia = "";

            /* if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
                AggiornaNotifica(VariabiliGlobali.getInstance().getUltimaImmagine().getImmagine(),
                        VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine());
            } else {
                view.setTextViewText(R.id.txtTitoloImmagine, "---");
                view.setImageViewResource(R.id.imgCopertina, R.drawable.logo);
            } */
        } else {
            Log.getInstance().ScriveLog("Set Listeners. View NON corretta");
        }
    }

    private void setListenersTasti(RemoteViews view, Context ctx) {
        if (view != null) {
            Log.getInstance().ScriveLog("Set Listeners tasti. View corretta" );

            // Intent play=new Intent(context, PassaggioNotifica.class);
            // play.putExtra("DO", "play");
            // play.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            // PendingIntent pplay = PendingIntent.getActivity(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
            // view.setOnClickPendingIntent(R.id.imgPlay, pplay);
            // view.setImageViewResource(R.id.imgPlay, R.drawable.play);

            Intent avanti = new Intent(ctx, NotificationActionService.class);
            avanti.putExtra("DO", "prossima");
            PendingIntent pAvanti = PendingIntent.getService(ctx, 51, avanti, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.imgProssima, pAvanti);
            // view.setImageViewResource(R.id.imgAvanti, R.drawable.avanti);

            Intent apre = new Intent(ctx, PassaggioNotifica.class);
            apre.putExtra("DO", "apre");
            PendingIntent pApre = PendingIntent.getActivity(ctx, 52, apre, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.imgCopertina, pApre);
        } else {
            Log.getInstance().ScriveLog("Set Listeners tasti. View NON corretta" );
        }
    }

    public void AggiornaNotifica(String Titolo, String Immagine) {
        Notification notification = StartApplicazione(ctx, Titolo, Immagine);
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(idNotifica, notification);
    }

    public void RimuoviNotifica() {
        Log.getInstance().ScriveLog("Rimuovi notifica");
        if (manager != null) {
            try {
                manager.cancel(idNotifica);
                manager = null;
                notificationBuilder = null;
                // NOTIF_ID++;
            } catch (Exception e) {
                Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public static class NotificationActionService extends Service {
        private Context context;

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            context = MainActivity.getAppContext();
            String action="";

            Log.getInstance().ScriveLog("Notifica: onCreate PassaggioNotifica");

            try {
                action = (String) intent.getExtras().get("DO");
                Log.getInstance().ScriveLog("Notifica: Action: " + action);
            } catch (Exception e) {
                Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }

            if (action!=null) {
                boolean Chiude = true;

                switch (action) {
                    case "prossima":
                        VariabiliGlobali.getInstance().setImmagineCambiataConSchermoSpento(false);
                        ChangeWallpaper c = new ChangeWallpaper();
                        if (!VariabiliGlobali.getInstance().isOffline()) {
                            Log.getInstance().ScriveLog("---Cambio Immagine Manuale da notifica OnLine---");
                            boolean fatto = c.setWallpaper(null);
                            Log.getInstance().ScriveLog("---Immagine cambiata manualmente Online da notifica: " + fatto + "---");
                        } else {
                            if (VariabiliGlobali.getInstance().getListaImmagini() == null) {
                                Log.getInstance().ScriveLog("---Immagini in array nulle---");
                            } else {
                                Log.getInstance().ScriveLog("---Immagini in array: " + VariabiliGlobali.getInstance().getListaImmagini().size() + "---");
                            }
                            if (VariabiliGlobali.getInstance().getListaImmagini() != null && VariabiliGlobali.getInstance().getListaImmagini().size() > 0) {
                                Log.getInstance().ScriveLog("---Cambio Immagine Manuale da notifica Offline---");
                                int numeroRandom = Utility.getInstance().GeneraNumeroRandom(VariabiliGlobali.getInstance().getListaImmagini().size() - 1);
                                if (numeroRandom > -1) {
                                    Log.getInstance().ScriveLog("---Numero Random: " + numeroRandom + "/" + (VariabiliGlobali.getInstance().getListaImmagini().size() - 1) + "---");
                                    boolean fatto = c.setWallpaper(VariabiliGlobali.getInstance().getListaImmagini().get(numeroRandom));
                                    Log.getInstance().ScriveLog("---Immagine cambiata manualmente da notifica: " + fatto + "---");
                                } else {
                                    Log.getInstance().ScriveLog("---Immagine NON cambiata manualmente da notifica: Caricamento immagini non terminato---");
                                }
                            } else {
                                Log.getInstance().ScriveLog("---Non riesco a cambiare l'immagine manuale da notifica in quanto l'array è vuoto---");
                            }
                        }
                        VariabiliGlobali.getInstance().setMascheraAperta(false);
                        Activity a = MainActivity.getAppActivity();
                        if (a != null) {
                            a.moveTaskToBack(true);
                        } else {
                            Log.getInstance().ScriveLog("Chiusura maschera non possibile in quanto l'activity è nulla");
                        }
                        break;
                }
            }

            return START_NOT_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
