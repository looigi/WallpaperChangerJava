package com.looigi.wallpaperchanger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class Notifica {
    private static Notifica instance = null;

    private Notifica() {
    }

    public static Notifica getInstance() {
        if (instance == null) {
            instance = new Notifica();
        }

        return instance;
    }

    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private Context context;
    private String Titolo;
    private String Immagine;
    private boolean inDownload=false;
    private static final int NOTIF_ID = 272727;

    public void setInDownload(boolean inDownload) {
        this.inDownload = inDownload;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;

        if (contentView != null) {
            if (Titolo != null && !Titolo.isEmpty()) {
                contentView.setTextViewText(R.id.txtTitoloImmagine, Titolo);
            } else {
                contentView.setTextViewText(R.id.txtTitoloImmagine, "---");
            }
        }
    }

    public void setImmagine(String immagine) {
        // Log.getInstance().ScriveLog("Imposto Immagine su notifica: " + immagine);
        Immagine = immagine;

        if (contentView != null) {
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

            if (notificationManager!=null && notificationBuilder != null) {
                Log.getInstance().ScriveLog("Aggiorna notifica. Build");
                notificationManager.notify(NOTIF_ID, notificationBuilder.build());
            }
        }
    }


    public void AggiornaNotifica() {
        Log.getInstance().ScriveLog("Aggiorna notifica. Titolo: " + Titolo);
        Log.getInstance().ScriveLog("Aggiorna notifica. Immagine: " + Immagine);

        setListenersTasti(contentView);
        setListeners(contentView);
        if (notificationManager!=null && notificationBuilder != null) {
            Log.getInstance().ScriveLog("Aggiorna notifica. Build");
            notificationManager.notify(NOTIF_ID, notificationBuilder.build());
        }
    }

    public void CreaNotifica() {
        String id = "id_loowebplayer"; // default_channel_id
        String title = "title_lwp"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        // NotificationCompat.Builder builder;

        if (context == null) {
            context = VariabiliGlobali.getInstance().getContext();
            if (context == null) {
                context = VariabiliGlobali.getInstance().getFragmentActivityPrincipale();
            }
        }

        if (context != null) {
            if (notificationManager == null) {
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, NotificationManager.IMPORTANCE_LOW);
                    mChannel.enableVibration(false);
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationBuilder = new NotificationCompat.Builder(context, id);

                intent = new Intent(context, PassaggioNotifica.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                contentView = new RemoteViews(VariabiliGlobali.getInstance()
                        .getContext().getPackageName(),
                        R.layout.barra_notifica);
                setListenersTasti(contentView);
                setListeners(contentView);

                notificationBuilder
                        .setContentTitle(Titolo)                            // required
                        .setSmallIcon(android.R.drawable.ic_media_play)   // required
                        .setContentText(context.getString(R.string.app_name)) // required
                        // .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        // .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                        // .setGroup("LOO'S WEB PLAYER")
                        // .setGroupSummary(true)
                        // .setDefaults(NotificationCompat.DEFAULT_ALL)
                        // .setPriority(NotificationManager.IMPORTANCE_LOW)
                        .setContentIntent(pendingIntent)
                        .setTicker("")
                        .setContent(contentView);
            }

            if (notificationBuilder != null) {
                Notification notification = notificationBuilder.build();
                notificationManager.notify(NOTIF_ID, notification);
            }
        } else {
            Log.getInstance().ScriveLog("Set Listeners. Context non valido");
        }
    }

    private void setListeners(RemoteViews view){
        if (view != null) {
            Log.getInstance().ScriveLog("Set Listeners. View corretta");

            // String Traccia = "";

            if (Titolo != null && !Titolo.isEmpty()) {
                view.setTextViewText(R.id.txtTitoloImmagine, Titolo);
            } else {
                view.setTextViewText(R.id.txtTitoloImmagine, "---");
            }

            // Log.getInstance().ScriveLog("Immagine notifica: " + Immagine);
            if (Immagine != null && !Immagine.isEmpty()) {
                try {
                    view.setImageViewBitmap(R.id.imgCopertina, BitmapFactory.decodeFile(Immagine));
                } catch (Exception ignored) {
                    // Log.getInstance().ScriveLog("Immagine notifica: Errore 1");
                    view.setImageViewResource(R.id.imgCopertina, R.drawable.logo);
                }
            } else {
                // Log.getInstance().ScriveLog("Immagine notifica non presente");
                view.setImageViewResource(R.id.imgCopertina, R.drawable.logo);
            }

            /* if (inDownload) {
                view.setViewVisibility(R.id.imgDownload, LinearLayout.VISIBLE);
            } else {
                view.setViewVisibility(R.id.imgDownload, LinearLayout.GONE);
            } */
        } else {
            Log.getInstance().ScriveLog("Set Listeners. View NON corretta");
        }
    }

    private void setListenersTasti(RemoteViews view){
        if (view != null) {
            Log.getInstance().ScriveLog("Set Listeners tasti. View corretta" );

            // Intent play=new Intent(context, PassaggioNotifica.class);
            // play.putExtra("DO", "play");
            // play.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            // PendingIntent pplay = PendingIntent.getActivity(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
            // view.setOnClickPendingIntent(R.id.imgPlay, pplay);
            // view.setImageViewResource(R.id.imgPlay, R.drawable.play);

            Intent avanti=new Intent(context, PassaggioNotifica.class);
            avanti.putExtra("DO", "prossima");
            PendingIntent pAvanti = PendingIntent.getActivity(context, 1, avanti, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.imgProssima, pAvanti);
            // view.setImageViewResource(R.id.imgAvanti, R.drawable.avanti);

            Intent apre=new Intent(context, PassaggioNotifica.class);
            apre.putExtra("DO", "apre");
            PendingIntent pApre = PendingIntent.getActivity(context, 0, apre, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.imgApre, pApre);

            /* Intent apre=new Intent(context, PassaggioNotifica.class);
            apre.putExtra("DO", "apre");
            PendingIntent pApre= PendingIntent.getActivity(context, 3, apre, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.imgCasa, pApre); */
        } else {
            Log.getInstance().ScriveLog("Set Listeners tasti. View NON corretta" );
        }
    }

    public void RimuoviNotifica() {
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        Log.getInstance().ScriveLog("Rimuovi notifica");
        if (notificationManager!=null) {
            try {
                notificationManager.cancel(NOTIF_ID);
                notificationManager=null;
                notificationBuilder=null;
            } catch (Exception e) {
                Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }
        }
    }
}
