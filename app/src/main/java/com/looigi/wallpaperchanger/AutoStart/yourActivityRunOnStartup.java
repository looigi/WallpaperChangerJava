package com.looigi.wallpaperchanger.AutoStart;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger.Log;
import com.looigi.wallpaperchanger.MainActivity;
import com.looigi.wallpaperchanger.Notifiche.PartenzaServizio;
import com.looigi.wallpaperchanger.R;
import com.looigi.wallpaperchanger.Utility;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.telephony.AvailableNetworkInfo.PRIORITY_HIGH;

public class yourActivityRunOnStartup extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Log.getInstance().EliminaFileLog();
        Log.getInstance().ScriveLog("*************ON RECEIVE BOOT*************");
        Log.getInstance().ScriveLog("Action: " + intent.getAction());
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context,"Faccio partire Wallpaper Changer", Toast.LENGTH_SHORT).show();

            Log.getInstance().ScriveLog("Caricamento activity");

            /* try {
                if (context != null) {
                    Log.getInstance().ScriveLog("Caricamento activity. Context valido");
                    long restartTime = 1000 * 60;
                    Intent intents = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                    Log.getInstance().ScriveLog("Caricamento activity. Nome pacchetto: " + context.getPackageName());
                    PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intents, PendingIntent.FLAG_ONE_SHOT);
                    Log.getInstance().ScriveLog("Caricamento activity. Restart Intent: " + restartIntent.getCreatorPackage());
                    AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.getInstance().ScriveLog("Caricamento activity per so 1");
                        mgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + restartTime, restartIntent);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Log.getInstance().ScriveLog("Caricamento activity per so 2");
                            mgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + restartTime, restartIntent);
                        } else {
                            Log.getInstance().ScriveLog("Caricamento activity. So non riconosciuto: " + Build.VERSION.SDK_INT);
                        }
                    }
                    Log.getInstance().ScriveLog("Caricamento activity. Mgr: " + mgr.getNextAlarmClock());
                } else {
                    Log.getInstance().ScriveLog("Caricamento activity. Context NULLO");
                }
            } catch (Exception e) {
                Log.getInstance().ScriveLog("Errore nell'avvio: " + e.getMessage());
            } */

            Log.getInstance().ScriveLog("Chiamo servizio");
           /*  MainActivity.getAppActivity().startService(new Intent(
                    MainActivity.getAppActivity(),
                    ServizioBackground.class)); */

            // // RICHIAMO SERVIZIO PartenzaServizio.getInstance()StartApplicazione(this);
            try {
                /* Intent intent2 = new Intent(context, MainActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent2);
                } else {
                    context.startService(intent2);
                } */

                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (Exception e) {
                Log.getInstance().ScriveLog("Errore: " + e.getMessage());
            }

            /* Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i); */

            Log.getInstance().ScriveLog("Activity caricata");

            /* Log.getInstance().ScriveLog("Caricamento activity metodo 2");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, MainActivity.class));
            } else {
                context.startService(new Intent(context, MainActivity.class));
            }

            Log.getInstance().ScriveLog("Activity caricata metodo 2");

            try {
                Intent i= new Intent(context.getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (Exception e) {
                Log.getInstance().ScriveLog("Errore nell'avvio");
                Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            } */


                /*int interval = 5000;
                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pi = PendingIntent.getService(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pi); */
        }
        Log.getInstance().ScriveLog("*************ON RECEIVE BOOT*************");
    }
}