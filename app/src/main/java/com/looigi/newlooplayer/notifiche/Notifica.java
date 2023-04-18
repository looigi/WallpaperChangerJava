package com.looigi.newlooplayer.notifiche;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.looigi.newlooplayer.ImmagineZoomabile;
import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.R;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;

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
    private int Icona;
    private String Titolo;
    private String Contenuto;
    private String Immagine;
    private String Artista;
    private String Album;
    private boolean inDownload=false;
    private int NOTIF_ID = 272727;
    private boolean staSuonando = false;

    public void setStaSuonando(boolean staSuonando) {
        this.staSuonando = staSuonando;
    }

    public void setInDownload(boolean inDownload) {
        this.inDownload = inDownload;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setIcona(int icona) {
        Icona = icona;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public void setContenuto(String contenuto) {
        Contenuto = contenuto;
    }

    public void setImmagine(String immagine) {
        // Log.getInstance().ScriveLog("Imposto Immagine su notifica: " + immagine);
        Immagine = immagine;
    }

    public void setArtista(String artista) {
        Artista = artista;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    /* public void CreaNotificaVecchio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager = (NotificationManager) VariabiliStaticheGlobali.getInstance()
                    .getFragmentActivityPrincipale().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationBuilder = new NotificationCompat.Builder(context);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
            notificationBuilder.setOngoing(true);

            contentView = new RemoteViews(VariabiliStaticheGlobali.getInstance()
                    .getFragmentActivityPrincipale().getPackageName(), R.layout.barra_notifica);
            setListenersTasti(contentView);

            notificationBuilder.setContent(contentView);
            notificationBuilder.setAutoCancel(false);

            notificationManager.notify(1, notificationBuilder.build());
        } else {
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);

            Notification notification =  new NotificationCompat.Builder(context).setAutoCancel(false)
                    .setContentTitle(Titolo)
                    .setContentText(Contenuto)
                    .setContentIntent(pi)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setTicker(Titolo)
                    .build();
            NotificationManager notificationManager =
                    (NotificationManager) VariabiliStaticheGlobali.getInstance()
                            .getFragmentActivityPrincipale().getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }
    }

    public void AggiornaNotifica() {
        Log.getInstance().ScriveLog("Aggiorna notifica. Artista: " + Artista);
        Log.getInstance().ScriveLog("Aggiorna notifica. Album: " + Album);
        Log.getInstance().ScriveLog("Aggiorna notifica. Brano: " + Titolo);
        Log.getInstance().ScriveLog("Aggiorna notifica. Immagine: " + Immagine);
        Log.getInstance().ScriveLog("Aggiorna notifica. Sta Suonando: " + staSuonando);

        setListenersTasti(contentView);
        setListeners(contentView);
        if (notificationManager!=null && notificationBuilder != null) {
            Log.getInstance().ScriveLog("Aggiorna notifica. Build");
            notificationManager.notify(NOTIF_ID, notificationBuilder.build());
        }
    } */

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
            if (Artista != null && !Artista.isEmpty()) {
                view.setTextViewText(R.id.txtArtista, Artista);
            } else {
                view.setTextViewText(R.id.txtArtista, "---");
            }

            String Traccia = "";

            if (Titolo != null && !Titolo.isEmpty()) {
                String sTitolo = Titolo;
                if (sTitolo.contains("-")) {
                    String A[] = sTitolo.split("-");
                    if (!A[0].isEmpty() && !A[0].equals("00")) {
                        Traccia = "        Traccia " + A[0];
                    }
                    sTitolo = A[1].trim();
                }
                if (sTitolo.contains(".")) {
                    sTitolo = sTitolo.substring(0, sTitolo.indexOf("."));
                }
                view.setTextViewText(R.id.txtTitoloBrano, sTitolo);
            } else {
                view.setTextViewText(R.id.txtTitoloBrano, "---");
            }

            if (Album != null && !Album.isEmpty()) {
                String sAlbum = Album;
                if (sAlbum.contains("-")) {
                    String A[] = sAlbum.split("-");
                    if (!A[0].isEmpty() && !A[0].equals("0000")) {
                        sAlbum = A[1] + " (Anno " + A[0] + ")";
                    } else {
                        sAlbum = A[1];
                    }
                }
                sAlbum = "Album: " + sAlbum + Traccia;
                view.setTextViewText(R.id.txtAlbum, sAlbum);
            } else {
                view.setTextViewText(R.id.txtAlbum, "---");
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

            if (staSuonando) {
                Log.getInstance().ScriveLog("Set Listeners. Icona pausa");
                view.setImageViewResource(R.id.ImgPlay, R.drawable.icona_pausa);
            } else {
                Log.getInstance().ScriveLog("Set Listeners. Icona suona");
                view.setImageViewResource(R.id.ImgPlay, R.drawable.icona_suona);
            }

            if (inDownload) {
                view.setViewVisibility(R.id.imgDownload, LinearLayout.VISIBLE);
            } else {
                view.setViewVisibility(R.id.imgDownload, LinearLayout.GONE);
            }
        } else {
            Log.getInstance().ScriveLog("Set Listeners. View NON corretta");
        }
    }

    private void setListenersTasti(RemoteViews view){
        if (view != null) {
            Log.getInstance().ScriveLog("Set Listeners tasti. View corretta" );

            Intent play=new Intent(context, PassaggioNotifica.class);
            play.putExtra("DO", "play");
            // play.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            PendingIntent pplay = PendingIntent.getActivity(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.imgPlay, pplay);
            // view.setImageViewResource(R.id.imgPlay, R.drawable.play);

            Intent indietro=new Intent(context, PassaggioNotifica.class);
            indietro.putExtra("DO", "indietro");
            PendingIntent pCambia = PendingIntent.getActivity(context, 1, indietro, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.imgIndietro, pCambia);
            // view.setImageViewResource(R.id.imgIndietro, R.drawable.indietro);

            Intent avanti=new Intent(context, PassaggioNotifica.class);
            avanti.putExtra("DO", "avanti");
            PendingIntent pAvanti = PendingIntent.getActivity(context, 2, avanti, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.imgAvanti, pAvanti);
            // view.setImageViewResource(R.id.imgAvanti, R.drawable.avanti);

            Intent apre=new Intent(context, PassaggioNotifica.class);
            apre.putExtra("DO", "apre");
            PendingIntent pApre = PendingIntent.getActivity(context, 3, apre, PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.layBarraNotifica, pApre);

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
                // NOTIF_ID++;
            } catch (Exception e) {
                Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }
        }
    }
}
