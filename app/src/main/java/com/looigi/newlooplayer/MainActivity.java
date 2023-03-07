package com.looigi.newlooplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.AccessNetworkConstants;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.looigi.newlooplayer.WebServices.ChiamateWs;
import com.looigi.newlooplayer.adapters.AdapterListenerArtisti;
import com.looigi.newlooplayer.adapters.AdapterListenerTags;
import com.looigi.newlooplayer.adapters.AdapterListenerTagsBrano;
import com.looigi.newlooplayer.adapters.AdapterListenerTagsTutti;
import com.looigi.newlooplayer.chiamate.PhoneUnlockedReceiver;
import com.looigi.newlooplayer.cuffie.GestioneTastoCuffie;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.notifiche.Notifica;
import com.looigi.newlooplayer.strutture.StrutturaTags;

import org.kobjects.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private boolean ciSonoPermessi;
    private AudioManager mAudioManager;
    private ComponentName mReceiverComponent;

    // private AudioManager mAudioManager;
    // private ComponentName mReceiverComponent;
    // private GestioneTastoCuffie myReceiver;
    // private MusicIntentReceiver myReceiver;

    /* private static class MusicIntentReceiver extends BroadcastReceiver {
        public MusicIntentReceiver() {
            Log.getInstance().ScriveLog("---> AZIONATO RECEIVER CUFFIE <---");
        }

        @Override public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
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
            }
        }
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Utility.getInstance().EsisteFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/" + "EliminaDebug.txt")) {
            Log.getInstance().EliminaFileLog();
        }

        // myReceiver = new GestioneTastoCuffie();

        VariabiliGlobali.getInstance().setContext(this);
        VariabiliGlobali.getInstance().setFragmentActivityPrincipale(this);

        /* myReceiver = new GestioneTastoCuffie();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter); */

        Log.getInstance().ScriveLog("--------------------NUOVA SESSIONE--------------------");
        Log.getInstance().ScriveLog("Applicazione partita: " + VariabiliGlobali.getInstance().isePartito());

        if (!VariabiliGlobali.getInstance().isePartito()) {
            Permessi p = new Permessi();
            ciSonoPermessi = p.ControllaPermessi();

            if (ciSonoPermessi) {
                EsegueEntrata();
            }
        } else {
            Log.getInstance().ScriveLog("Applicazione ripartita: " + VariabiliGlobali.getInstance().isePartito());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (!ciSonoPermessi) {
            int index = 0;
            Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
            for (String permission : permissions) {
                PermissionsMap.put(permission, grantResults[index]);
                index++;
            }

            EsegueEntrata();
        }
    }

    private void EsegueEntrata() {
        ImpostaOggettiAVideo();

        /* Intent serviceIntent = new Intent(this, ServizioBackground.class);
        serviceIntent.putExtra("inputExtra2", "looWebPlayer");
        // ContextCompat.startForegroundService(this, serviceIntent);
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(new Intent(MainActivity.this, ServizioBackground.class));
        } */

        startService(new Intent(MainActivity.this, ServizioBackground.class));
    }

    private void ImpostaOggettiAVideo() {
        VariabiliGlobali.getInstance().setMediaPlayer(new MediaPlayer());

        TextView txtInizio = (TextView) findViewById(R.id.txtInizio);
        TextView txtFine = (TextView) findViewById(R.id.txtFine);
        TextView txtNomeBrano = (TextView) findViewById(R.id.txtNomeBrano);
        ImmagineZoomabile imgSfondo = (ImmagineZoomabile) findViewById(R.id.imgImmagineSfondo);
        ImageView imgIndietro = (ImageView) findViewById(R.id.imgIndietro);
        ImageView imgPlay = (ImageView) findViewById(R.id.imgPlay);
        ImageView imgAvanti = (ImageView) findViewById(R.id.imgAvanti);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        TextView txtInformazioni = (TextView) findViewById(R.id.txtInformazioni);
        ImageView imgBellezza0 = (ImageView) findViewById(R.id.imgBellezza0);
        ImageView imgBellezza1 = (ImageView) findViewById(R.id.imgBellezza1);
        ImageView imgBellezza2 = (ImageView) findViewById(R.id.imgBellezza2);
        ImageView imgBellezza3 = (ImageView) findViewById(R.id.imgBellezza3);
        ImageView imgBellezza4 = (ImageView) findViewById(R.id.imgBellezza4);
        ImageView imgBellezza5 = (ImageView) findViewById(R.id.imgBellezza5);
        ImageView imgBellezza6 = (ImageView) findViewById(R.id.imgBellezza6);
        ImageView imgBellezza7 = (ImageView) findViewById(R.id.imgBellezza7);
        ImageView imgBellezza8 = (ImageView) findViewById(R.id.imgBellezza8);
        ImageView imgBellezza9 = (ImageView) findViewById(R.id.imgBellezza9);
        ImageView imgNoNet = (ImageView) findViewById(R.id.imgNoInternet);
        ListView lstArtisti = (ListView) findViewById(R.id.lstArtisti);
        ListView lstTags = (ListView) findViewById(R.id.lstTags);
        TextView txtPreferiti = (TextView) findViewById(R.id.txtPreferiti);
        TextView txtPreferitiElimina = (TextView) findViewById(R.id.txtPreferitiElimina);
        TextView txtTags = (TextView) findViewById(R.id.txtTags);
        TextView txtEliminaTags = (TextView) findViewById(R.id.txtEliminaTags);
        // ImageView imgChiudeLista = (ImageView) findViewById(R.id.imgChiudeLista);
        ImageView imgChiudeESalvaLista = (ImageView) findViewById(R.id.imgChiudeESalvaLista);
        TextView txtArtista = (TextView) findViewById(R.id.txtNomeArtista);
        TextView txtAlbum = (TextView) findViewById(R.id.txtNomeAlbum);
        ImageView imgDownloadBrano = (ImageView) findViewById(R.id.imgDownloadBrano);
        ImageView imgEsce = (ImageView) findViewById(R.id.imgEsce);
        imgEsce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uscita();
            }
        });
        ImageView imgCuffie = (ImageView) findViewById(R.id.imgCuffie);
        ImageView imgRest = (ImageView) findViewById(R.id.imgRest);
        // ImageView imgPregresso = (ImageView) findViewById(R.id.imgCaricatoBranoPregresso);
        TextView txtTagsBrano = (TextView) findViewById(R.id.txtTagsBrano);
        ImageView imgErroreBrano = (ImageView) findViewById(R.id.imgErroreBrano);
        LinearLayout laySplash = (LinearLayout) findViewById(R.id.laySplash);
        OggettiAVideo.getInstance().setLaySplash(laySplash);
        /* laySplash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySplash.setVisibility(LinearLayout.GONE);
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                laySplash.setVisibility(LinearLayout.GONE);
            }
        }, 3000); */
        LinearLayout layDown = (LinearLayout) findViewById(R.id.layDownload);
        OggettiAVideo.getInstance().setLayDownload(layDown);
        TextView txtDownload = (TextView) findViewById(R.id.txtDownload);
        OggettiAVideo.getInstance().setTxtDownload(txtDownload);
        LinearLayout layCaricamento = (LinearLayout) findViewById(R.id.layCaricamento);
        TextView txtCaricamento = (TextView) findViewById(R.id.txtCaricamento);
        OggettiAVideo.getInstance().setTxtCaricamento(txtCaricamento);
        LinearLayout layTesto = (LinearLayout) findViewById(R.id.layTestoAperto);
        ImageView imgLinguetta1 = (ImageView) findViewById(R.id.imgLinguettaSinistra);
        OggettiAVideo.getInstance().setImgLinguetta1(imgLinguetta1);
        TextView txtTesto = (TextView) findViewById(R.id.txtTesto);
        OggettiAVideo.getInstance().setTxtTesto(txtTesto);
        LinearLayout layAlberoAperto = (LinearLayout) findViewById(R.id.layAlberoAperto);
        ImageView imgLinguetta2 = (ImageView) findViewById(R.id.imgLinguettaDestra);
        OggettiAVideo.getInstance().setImgLinguetta2(imgLinguetta2);
        LinearLayout layLista = (LinearLayout) findViewById(R.id.layListaArtisti);
        OggettiAVideo.getInstance().setLayLista(layLista);
        LinearLayout layContenitore = (LinearLayout) findViewById(R.id.layBottoniera);
        OggettiAVideo.getInstance().setLayContenitore(layContenitore);
        Button btnLista = (Button) findViewById(R.id.btnLista);
        OggettiAVideo.getInstance().setBtnLista(btnLista);
        LinearLayout layPregresso = (LinearLayout) findViewById(R.id.layPregresso);
        TextView txtPregresso = (TextView) findViewById(R.id.txtBranoPregresso);
        ImageView imgCambiaPregresso = (ImageView) findViewById(R.id.imgCambiaPregresso);
        OggettiAVideo.getInstance().setImgCambiaPregresso(imgCambiaPregresso);
        LinearLayout laySettaggi = (LinearLayout) findViewById(R.id.laySettaggi);
        OggettiAVideo.getInstance().setLaySettaggi(laySettaggi);
        ImageView imgMenu = (ImageView) findViewById(R.id.imgMenu);
        OggettiAVideo.getInstance().setImgMenu(imgMenu);
        LinearLayout layRicerche = (LinearLayout) findViewById(R.id.layRicerche);
        OggettiAVideo.getInstance().setLayRicerche(layRicerche);
        ImageView imgRicerche = (ImageView) findViewById(R.id.imgRicerche);
        Switch switchDebug = (Switch) findViewById(R.id.switchDebug);
        Switch switchEliminaDebug = (Switch) findViewById(R.id.switchEliminaDebug);
        LinearLayout layDebug = (LinearLayout) findViewById(R.id.layDebug);
        Button btnDebug = (Button) findViewById(R.id.btnDebug);
        Button btnListe = (Button) findViewById(R.id.btnListe);
        Switch switchOrologio = (Switch) findViewById(R.id.switchOrologio);
        DigitalClock clock = (DigitalClock) findViewById(R.id.fldOrologio);
        Switch switchSoloSelezionati = (Switch) findViewById(R.id.switchSoloSelezionati);
        ImageView imgCercaPreferiti = (ImageView) findViewById(R.id.imgCercaArtista);
        EditText edtRicercaArtisti = (EditText) findViewById(R.id.edtRicercaArtista);
        Switch switchPreferiti = (Switch) findViewById(R.id.switchPreferiti);
        Switch switchPresenteSuDisco = (Switch) findViewById(R.id.switchPresenteSuDisco);
        OggettiAVideo.getInstance().setSwitchPresenteSuDisco(switchPresenteSuDisco);
        Switch switchRandom = (Switch) findViewById(R.id.switchRandom);
        Switch switchScaricaBrano = (Switch) findViewById(R.id.switchScaricaBrano);
        Switch switchVisualizzaInfo = (Switch) findViewById(R.id.switchInformazioni);
        LinearLayout layCambioImmagine = (LinearLayout) findViewById(R.id.layCambioImmagine);
        Switch switchCambioImmagine = (Switch) findViewById(R.id.switchCambioImmagini);
        EditText edtSecondi = (EditText) findViewById(R.id.edtSecondiAlCambio);
        LinearLayout layOpacitaBottoni = (LinearLayout) findViewById(R.id.layOpacitaBottoni);
        Switch switchOpacitaBottoni = (Switch) findViewById(R.id.switchOpacitaBottoni);
        EditText edtOpacitaBottoni = (EditText) findViewById(R.id.edtOpacitaBottoni);
        LinearLayout layStelle = (LinearLayout) findViewById(R.id.layRicercaStelle);
        Switch switchStelle = (Switch) findViewById(R.id.switchStelle);
        EditText edtStelle = (EditText) findViewById(R.id.edtStelle);
        LinearLayout layRicercaTesto = (LinearLayout) findViewById(R.id.layRicercaTesto);
        Switch switchRicercaTesto = (Switch) findViewById(R.id.switchRicercaTesto);
        EditText edtTesto = (EditText) findViewById(R.id.edtRicercaTesto);
        Switch switchTags = (Switch) findViewById(R.id.switchTags);
        LinearLayout layListaTags = (LinearLayout) findViewById(R.id.layListaTags);
        layListaTags.setVisibility(LinearLayout.GONE);
        Button btnListaTags = (Button) findViewById(R.id.btnListaTags);
        Switch switchSoloSelezionatiTags = (Switch) findViewById(R.id.switchSoloSelezionatiTags);
        ImageView imgChiudeESalvaListaTags = (ImageView) findViewById(R.id.imgChiudeESalvaListaTags);
        Switch switchEliminaBrani = (Switch) findViewById(R.id.switchEliminaBrani);
        LinearLayout layElimina = (LinearLayout) findViewById(R.id.layEliminaBrani);
        EditText edtLimiteMB = (EditText) findViewById(R.id.edtMegaOccupati);
        ImageView imgAnnullaRicerche = (ImageView) findViewById(R.id.imgAnnullaRicerche);
        ImageView imgAnnullaSettaggi = (ImageView) findViewById(R.id.imgAnnullaSettaggi);
        Switch switchDataSuperiore = (Switch) findViewById(R.id.switchDataSuperiore);
        EditText edtDataSuperiore = (EditText) findViewById(R.id.editDataSuperiore);
        Switch switchDataInferiore = (Switch) findViewById(R.id.switchDataInferiore);
        EditText edtDataInferiore = (EditText) findViewById(R.id.editDataInferiore);
        Switch switchDate = (Switch) findViewById(R.id.switchDate);

        OggettiAVideo.getInstance().setSwitchDate(switchDate);
        OggettiAVideo.getInstance().setSwitchDataSuperiore(switchDataSuperiore);
        OggettiAVideo.getInstance().setEdtDataSuperiore(edtDataSuperiore);
        OggettiAVideo.getInstance().setSwitchDataInferiore(switchDataInferiore);
        OggettiAVideo.getInstance().setEdtDataInferiore(edtDataInferiore);
        OggettiAVideo.getInstance().setLayListaTags(layListaTags);
        OggettiAVideo.getInstance().setLayRicerche(layRicerche);
        OggettiAVideo.getInstance().setImgRicerche(imgRicerche);
        OggettiAVideo.getInstance().setSwitchDebug(switchDebug);
        OggettiAVideo.getInstance().setSwitchEliminaDebug(switchEliminaDebug);
        OggettiAVideo.getInstance().setLayDebug(layDebug);
        OggettiAVideo.getInstance().setBtnDebug(btnDebug);
        OggettiAVideo.getInstance().setBtnListe(btnListe);
        OggettiAVideo.getInstance().setSwitchOrologio(switchOrologio);
        OggettiAVideo.getInstance().setClock(clock);
        OggettiAVideo.getInstance().setSwitchSoloSelezionati(switchSoloSelezionati);
        OggettiAVideo.getInstance().setImgCercaPreferiti(imgCercaPreferiti);
        OggettiAVideo.getInstance().setEdtRicercaArtisti(edtRicercaArtisti);
        OggettiAVideo.getInstance().setSwitchPreferiti(switchPreferiti);
        OggettiAVideo.getInstance().setSwitchRandom(switchRandom);
        OggettiAVideo.getInstance().setSwitchScaricaBrano(switchScaricaBrano);
        OggettiAVideo.getInstance().setSwitchVisualizzaInfo(switchVisualizzaInfo);
        OggettiAVideo.getInstance().setLayCambioImmagine(layCambioImmagine);
        OggettiAVideo.getInstance().setSwitchCambioImmagine(switchCambioImmagine);
        OggettiAVideo.getInstance().setEdtSecondi(edtSecondi);
        OggettiAVideo.getInstance().setLayOpacitaBottoni(layOpacitaBottoni);
        OggettiAVideo.getInstance().setSwitchOpacitaBottoni(switchOpacitaBottoni);
        OggettiAVideo.getInstance().setEdtOpacitaBottoni(edtOpacitaBottoni);
        OggettiAVideo.getInstance().setLayStelle(layStelle);
        OggettiAVideo.getInstance().setSwitchStelle(switchStelle);
        OggettiAVideo.getInstance().setEdtStelle(edtStelle);
        OggettiAVideo.getInstance().setLayRicercaTesto(layRicercaTesto);
        OggettiAVideo.getInstance().setSwitchRicercaTesto(switchRicercaTesto);
        OggettiAVideo.getInstance().setEdtTesto(edtTesto);
        OggettiAVideo.getInstance().setSwitchTags(switchTags);
        OggettiAVideo.getInstance().setImgChiudeESalvaListaTags(imgChiudeESalvaListaTags);
        OggettiAVideo.getInstance().setSwitchSoloSelezionatiTags(switchSoloSelezionatiTags);
        OggettiAVideo.getInstance().setSwitchEliminaBrani(switchEliminaBrani);
        OggettiAVideo.getInstance().setLayElimina(layElimina);
        OggettiAVideo.getInstance().setEdtLimiteMB(edtLimiteMB);
        OggettiAVideo.getInstance().setImgAnnullaRicerche(imgAnnullaRicerche);
        OggettiAVideo.getInstance().setImgAnnullaSettaggi(imgAnnullaSettaggi);

        OggettiAVideo.getInstance().setTxtInizio(txtInizio);
        OggettiAVideo.getInstance().setTxtFine(txtFine);
        OggettiAVideo.getInstance().setTxtNomeBrano(txtNomeBrano);
        OggettiAVideo.getInstance().setTxtInformazioni(txtInformazioni);
        OggettiAVideo.getInstance().setImgSfondo(imgSfondo);
        OggettiAVideo.getInstance().setImgIndietro(imgIndietro);
        OggettiAVideo.getInstance().setImgPlay(imgPlay);
        OggettiAVideo.getInstance().setImgAvanti(imgAvanti);
        OggettiAVideo.getInstance().setSeekBar(seekBar);
        OggettiAVideo.getInstance().setImgBellezzza0(imgBellezza0);
        OggettiAVideo.getInstance().setImgBellezzza1(imgBellezza1);
        OggettiAVideo.getInstance().setImgBellezzza2(imgBellezza2);
        OggettiAVideo.getInstance().setImgBellezzza3(imgBellezza3);
        OggettiAVideo.getInstance().setImgBellezzza4(imgBellezza4);
        OggettiAVideo.getInstance().setImgBellezzza5(imgBellezza5);
        OggettiAVideo.getInstance().setImgBellezzza6(imgBellezza6);
        OggettiAVideo.getInstance().setImgBellezzza7(imgBellezza7);
        OggettiAVideo.getInstance().setImgBellezzza8(imgBellezza8);
        OggettiAVideo.getInstance().setImgBellezzza9(imgBellezza9);
        OggettiAVideo.getInstance().setImgNoNet(imgNoNet);
        OggettiAVideo.getInstance().setLstArtisti(lstArtisti);
        OggettiAVideo.getInstance().setLstTags(lstTags);
        OggettiAVideo.getInstance().setTxtPreferiti(txtPreferiti);
        OggettiAVideo.getInstance().setTxtPreferitiElimina(txtPreferitiElimina);
        OggettiAVideo.getInstance().setTxtTags(txtTags);
        OggettiAVideo.getInstance().setTxtEliminaTags(txtEliminaTags);
        OggettiAVideo.getInstance().setTxtArtista(txtArtista);
        OggettiAVideo.getInstance().setTxtAlbum(txtAlbum);
        OggettiAVideo.getInstance().setImgDownloadBrano(imgDownloadBrano);
        OggettiAVideo.getInstance().setTxtTagsBrano(txtTagsBrano);
        OggettiAVideo.getInstance().setLayAlbero(layAlberoAperto);
        imgErroreBrano.setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().setImgErroreBrano(imgErroreBrano);
        // OggettiAVideo.getInstance().setScrollViewAlbero(scrollView);
        OggettiAVideo.getInstance().getImgDownloadBrano().setVisibility(LinearLayout.GONE);
        // OggettiAVideo.getInstance().setImgPregresso(imgPregresso);
        OggettiAVideo.getInstance().setLayPregresso(layPregresso);
        OggettiAVideo.getInstance().setTxtBranoPregresso(txtPregresso);
        // PREGRESSO

        OggettiAVideo.getInstance().setImgRest(imgRest);
        OggettiAVideo.getInstance().getImgRest().setVisibility(LinearLayout.GONE);

        OggettiAVideo.getInstance().setLayTesto(layTesto);
        OggettiAVideo.getInstance().setLayDown(layDown);
        OggettiAVideo.getInstance().setLayCaricamento(layCaricamento);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels / 2;
        int width = displayMetrics.widthPixels;
        ViewGroup.LayoutParams params = layTesto.getLayoutParams();
        params.width = width / 2;
        OggettiAVideo.getInstance().getLayTesto().setLayoutParams(params);

        VariabiliGlobali.getInstance().setTestoAperto(false);
        OggettiAVideo.getInstance().getImgLinguetta1().setX(0);
        OggettiAVideo.getInstance().getImgLinguetta1().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliGlobali.getInstance().isTestoAperto()) {
                    VariabiliGlobali.getInstance().setTestoAperto(false);
                    OggettiAVideo.getInstance().getLayTesto().setVisibility(LinearLayout.GONE);

                    OggettiAVideo.getInstance().getImgLinguetta1().setX(0);
                } else {
                    VariabiliGlobali.getInstance().setTestoAperto(true);
                    OggettiAVideo.getInstance().getLayTesto().setVisibility(LinearLayout.VISIBLE);
                    int doveX = width / 2;
                    OggettiAVideo.getInstance().getImgLinguetta1().setX(doveX);
                }
            }
        });
        OggettiAVideo.getInstance().setTxtTesto(OggettiAVideo.getInstance().getTxtTesto());
        OggettiAVideo.getInstance().setLayContenitore(layContenitore);
        OggettiAVideo.getInstance().setImgCuffie(imgCuffie);
        OggettiAVideo.getInstance().setBtnListaTags(btnListaTags);
        OggettiAVideo.getInstance().getBtnLista().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getBtnListaTags().setVisibility(LinearLayout.GONE);

        layAlberoAperto.setVisibility(LinearLayout.GONE);

        ViewGroup.LayoutParams params2 = layAlberoAperto.getLayoutParams();
        params2.width = width / 2;
        layAlberoAperto.setLayoutParams(params2);

        VariabiliGlobali.getInstance().setAlberoAperto(false);
        // imgLinguetta2.setX(width - 80);
        imgLinguetta2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliGlobali.getInstance().isAlberoAperto()) {
                    VariabiliGlobali.getInstance().setAlberoAperto(false);
                    layAlberoAperto.setVisibility(LinearLayout.GONE);

                    imgLinguetta2.setX(0);
                } else {
                    VariabiliGlobali.getInstance().setAlberoAperto(true);
                    layAlberoAperto.setVisibility(LinearLayout.VISIBLE);

                    int doveX = width / 2;
                    imgLinguetta2.setX(doveX);
                }
            }
        });

        imgChiudeESalvaLista.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgLinguetta1.setVisibility(LinearLayout.VISIBLE);
                layLista.setVisibility(LinearLayout.GONE);

                db_dati db = new db_dati();
                db.ScrivePreferiti();

                // FiltraBrani f = new FiltraBrani();
                // f.FiltraLista(true);
            }
        });

        ImageView imgTagsBrano = (ImageView) findViewById(R.id.imgTagsBrano);
        LinearLayout layTagsBrano = (LinearLayout) findViewById(R.id.layTagsbrano);
        layTagsBrano.setVisibility(LinearLayout.GONE);
        ImageView imgChiudeTagsBrano = (ImageView) findViewById(R.id.imgChiudeTagsBrano);
        ListView lstTagsBrano = (ListView) findViewById(R.id.lstTagsBrano);
        ListView lstTagsTutti = (ListView) findViewById(R.id.lstTagsTutti);
        OggettiAVideo.getInstance().setLstTagsBrano(lstTagsBrano);
        OggettiAVideo.getInstance().setLstTagsTutti(lstTagsTutti);
        imgTagsBrano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliGlobali.getInstance().setModificheTags(false);

                String Tags = VariabiliGlobali.getInstance().getStrutturaDelBrano().getTags();
                String[] ListaTags = Tags.split(";");

                AdapterListenerTagsBrano customAdapter = new AdapterListenerTagsBrano(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        ListaTags);
                OggettiAVideo.getInstance().getLstTagsBrano().setAdapter(customAdapter);

                AdapterListenerTagsTutti customAdapterT = new AdapterListenerTagsTutti(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        Utility.getInstance().CreaVettoreTags());
                OggettiAVideo.getInstance().getLstTagsTutti().setAdapter(customAdapterT);

                layTagsBrano.setVisibility(LinearLayout.VISIBLE);
            }
        });
        OggettiAVideo.getInstance().setImgTagsBrano(imgTagsBrano);
        OggettiAVideo.getInstance().setLayTagsBrano(layTagsBrano);
        imgChiudeTagsBrano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliGlobali.getInstance().isModificheTags()) {
                    ChiamateWs ws = new ChiamateWs();
                    ws.AggiornaTagsBrano();
                } else {
                    OggettiAVideo.getInstance().getLayTagsBrano().setVisibility(LinearLayout.GONE);
                }
            }
        });

        ImageView imgRefreshTesto = (ImageView) findViewById(R.id.imgRefreshTesto);
        imgRefreshTesto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWs ws = new ChiamateWs();
                ws.AggiornaTesto();
            }
        });

        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mReceiverComponent = new ComponentName(this, GestioneTastoCuffie.class);
        mAudioManager.registerMediaButtonEventReceiver(mReceiverComponent);

        IntentFilter filterHeadset;
        filterHeadset = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mNoisyReceiver, filterHeadset);
    }

    private BroadcastReceiver mNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (VariabiliGlobali.getInstance().isStaSuonando()) {
                Utility.getInstance().premutoPlay(false);
            }
        }
    };

    @Override
    protected void onResume() {
        /* unregisterReceiver(myReceiver);
        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(myReceiver, filter); */

        Log.getInstance().ScriveLog("On Resume. Minimizzato: " + VariabiliGlobali.getInstance().isMinimizzato());
        VariabiliGlobali.getInstance().setMinimizzato(false);
        if (VariabiliGlobali.getInstance().isePartito()) {
            Utility.getInstance().CambioImmagine();
        }

        super.onResume();
    }

    /* private BroadcastReceiver mNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (VariabiliGlobali.getInstance().isStaSuonando()) {
                Utility.getInstance().premutoPlay(false);
            }
        }
    }; */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        Log.getInstance().ScriveLog("--->Tasto premuto: " + keyCode + "<---");

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.getInstance().ScriveLog("Minimizzato: " + VariabiliGlobali.getInstance().isMinimizzato());
                VariabiliGlobali.getInstance().setMinimizzato(true);
                moveTaskToBack(true);

                return false;
        }

        return false;
    }

    private void Uscita() {
        Log.getInstance().ScriveLog("Applicazione terminata");

        Notifica.getInstance().RimuoviNotifica();

        // GestioneCPU.getInstance().DisattivaCPU();

        /* unregisterReceiver(mNoisyReceiver);
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        try {
            mAudioManager.unregisterMediaButtonEventReceiver(mReceiverComponent);
        } catch (Exception e) {
            Log.getInstance().ScriveLog(e.getMessage());
        }
        if (VariabiliGlobali.getInstance().getMyReceiverCuffie() != null) {
            unregisterReceiver(VariabiliGlobali.getInstance().getMyReceiverCuffie());
            VariabiliGlobali.getInstance().setMyReceiverCuffie(null);
        }
        if (VariabiliGlobali.getInstance().getMyReceiverCuffie() != null) {
            try {
                unregisterReceiver(VariabiliGlobali.getInstance().getMyReceiverCuffie());
            } catch (Exception e) {
                Log.getInstance().ScriveLog(e.getMessage());
            }
        } */
        stopService(new Intent(MainActivity.this, ServizioBackground.class));
        mAudioManager.unregisterMediaButtonEventReceiver(mReceiverComponent);
        unregisterReceiver(mNoisyReceiver);

        System.exit(0);
    }
}