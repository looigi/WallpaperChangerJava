package com.looigi.newlooplayer.cuffie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;

public class GestioneTastoCuffie extends BroadcastReceiver {
    private Runnable runRiga;
    private Handler hSelezionaRiga;

    // Constructor is mandatory
    public GestioneTastoCuffie ()
    {
        super ();

        Log.getInstance().ScriveLog("CUFFIE: Costruttore gestione tasto cuffie");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.getInstance().ScriveLog("CUFFIE: ONReceive cuffie");

        if (intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            int state = intent.getIntExtra("state", -1);
            Log.getInstance().ScriveLog("CUFFIE: ACTION_AUDIO_BECOMING_NOISY -> Stato " + state);
            switch (state) {
                case 0:
                    Log.getInstance().ScriveLog("---> CUFFIE DISINSERITE <---");
                    VariabiliGlobali.getInstance().setCuffieInserite(false);
                    OggettiAVideo.getInstance().getImgCuffie().setVisibility(LinearLayout.GONE);
                    if (VariabiliGlobali.getInstance().isStaSuonando()) {
                        Utility.getInstance().premutoPlay(false);
                    }
                    break;
                case 1:
                    Log.getInstance().ScriveLog("---> CUFFIE INSERITE <---");
                    VariabiliGlobali.getInstance().setCuffieInserite(true);
                    OggettiAVideo.getInstance().getImgCuffie().setVisibility(LinearLayout.VISIBLE);
                    break;
                default:
                    // Log.d(TAG, "I have no idea what the headset state is");
            }
        } else {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_MEDIA_BUTTON)) {
                String data = intent.getDataString();
                Bundle extraData = intent.getExtras();

                String st = intent.getStringExtra("state");
                String nm = intent.getStringExtra("name");
                String mic = intent.getStringExtra("microphone");

                Log.getInstance().ScriveLog("CUFFIE: Action Media Button 1: " + data);
                Log.getInstance().ScriveLog("CUFFIE: Action Media Button 2: " + extraData.toString());

                Log.getInstance().ScriveLog("CUFFIE: Action Media Button. ST:  " + st);
                Log.getInstance().ScriveLog("CUFFIE: Action Media Button. Name:  " + nm);
                Log.getInstance().ScriveLog("CUFFIE: Action Media Button. Mic:  " + mic);

                KeyEvent event1 = (KeyEvent) intent.getParcelableExtra(Intent.ACTION_MEDIA_BUTTON);
                if (event1 != null) {
                    Log.getInstance().ScriveLog("CUFFIE: Action Media Button. Tasto:  " + event1.getKeyCode());
                } else {
                    Log.getInstance().ScriveLog("CUFFIE: Action Media Button. Tasto nullo");

                    KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                    if (event == null) {
                        Log.getInstance().ScriveLog("CUFFIE: Action Media Button. Tasto nullo 2");
                        return;
                    }

                    int action = event.getAction();
                    Log.getInstance().ScriveLog("CUFFIE: Action Media Button. Azione: " + action);
                    if (action == KeyEvent.ACTION_DOWN) {
                        Log.getInstance().ScriveLog("CUFFIE: Avanti brano da cuffia");
                        Utility.getInstance().AvantiBrano();
                    }
                    if (action == KeyEvent.ACTION_UP) {
                        Log.getInstance().ScriveLog("CUFFIE: Indietro brano da cuffia");
                        Utility.getInstance().IndietroBrano();
                    }
                // }
            /* } else {
                String intentAction = intent.getAction();

                Log.getInstance().ScriveLog("CUFFIE: " + intentAction.toString() + " happended");
                Log.getInstance().ScriveLog("CUFFIE: Data String: " + intent.getDataString() + " ");
                KeyEvent event1 = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                KeyEvent event2 = (KeyEvent) intent.getParcelableExtra(Intent.ACTION_MEDIA_BUTTON);
                if (event1 != null) {
                    Log.getInstance().ScriveLog("CUFFIE: event1: " + event1.toString());
                    Log.getInstance().ScriveLog("CUFFIE: event1 code: " + event1.getKeyCode());
                }
                if (event2 != null) {
                    Log.getInstance().ScriveLog("CUFFIE: event2: " + event2.toString());
                    Log.getInstance().ScriveLog("CUFFIE: event2 code: " + event2.getKeyCode());
                }
                KeyEvent event;

                if (event1 != null) {
                    Log.getInstance().ScriveLog("CUFFIE: Extra Key Event");
                    event = event1;
                } else {
                    if (event2 != null) {
                        Log.getInstance().ScriveLog("CUFFIE: Action media button");
                        event = event2;
                    } else {
                        Log.getInstance().ScriveLog("CUFFIE: no keypress");
                        return;
                    }
                }
                // }
                // other stuff you want to do

                // if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                // KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

                // if (event == null) {
                //     return;
                // }

                Log.getInstance().ScriveLog("CUFFIE: ONReceive cuffie. Evento: " + Integer.toString(event.getKeyCode()));

                if (event.getKeyCode() == 88 || event.getKeyCode() == 126) {
                    // Calendar c = Calendar.getInstance();
                    // int seconds = c.get(Calendar.SECOND);
                    // int diffe=PlayerOne.SecondoUltimoCambio-seconds;
                    // if (diffe<0) {
                    // 	diffe=-diffe;
                    // }
                    //
                    // if (diffe<3) {
                    //
                    // } else {
                    Log.getInstance().ScriveLog("CUFFIE: Avanti brano da cuffia");
                    Utility.getInstance().AvantiBrano();

                    // 	PlayerOne.SecondoUltimoCambio=seconds;
                    // }
                }

                if (event.getKeyCode() == 87) {
                    // Calendar c = Calendar.getInstance();
                    // int seconds = c.get(Calendar.SECOND);
                    // int diffe=PlayerOne.SecondoUltimoCambio-seconds;
                    // if (diffe<0) {
                    // 	diffe=-diffe;
                    // }
                    //
                    // if (diffe<3) {
                    //
                    // } else {
                    Log.getInstance().ScriveLog("CUFFIE: Indietro brano da cuffia");
                    Utility.getInstance().IndietroBrano();

                    // 	PlayerOne.SecondoUltimoCambio=seconds;
                    // }
                } */
                }
            }
        }
    }

}