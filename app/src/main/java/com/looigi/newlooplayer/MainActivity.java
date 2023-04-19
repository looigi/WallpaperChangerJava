package com.looigi.newlooplayer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.looigi.newlooplayer.WebServices.ChiamateWs;
import com.looigi.newlooplayer.WebServices.ChiamateWsAmministrazione;
import com.looigi.newlooplayer.adapters.AdapterListenerGestioneTags;
import com.looigi.newlooplayer.adapters.AdapterListenerListaPreferiti;
import com.looigi.newlooplayer.adapters.AdapterListenerTags;
import com.looigi.newlooplayer.adapters.AdapterListenerTagsBrano;
import com.looigi.newlooplayer.adapters.AdapterListenerTagsTutti;
import com.looigi.newlooplayer.cuffie.GestioneTastoCuffie;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.download.DownloadFileTesto;
import com.looigi.newlooplayer.strutture.StrutturaTags;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private boolean ciSonoPermessi;
    private AudioManager mAudioManagerInterno;
    private ComponentName mReceiverComponentInterno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (Utility.getInstance().EsisteFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/" + "EliminaDebug.txt")) {
            Log.getInstance().EliminaFileLog();
        }

        // myReceiver = new GestioneTastoCuffie();

        VariabiliGlobali.getInstance().setContext(this);
        VariabiliGlobali.getInstance().setFragmentActivityPrincipale(this);

        /* myReceiver = new GestioneTastoCuffie();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter); */

        Log.getInstance().ScriveLog("");
        Log.getInstance().ScriveLog(">>>>>>>>>>>>>>>>>>>>>>>>NUOVA SESSIONE<<<<<<<<<<<<<<<<<<<<<<<<");
        Log.getInstance().ScriveLog("Applicazione partita: " + VariabiliGlobali.getInstance().isePartito());

        ControllaAmministratore();

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

    private void ControllaAmministratore() {
        String filetto = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Amministratore.txt";
        if (Utility.getInstance().EsisteFile(filetto)) {
            VariabiliGlobali.getInstance().setAmministratore(true);
        } else {
            VariabiliGlobali.getInstance().setAmministratore(false);
        }
        Log.getInstance().ScriveLog("Controllo amministratore: " + VariabiliGlobali.getInstance().isAmministratore());
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
        PuliziaGenerale pu = new PuliziaGenerale();
        pu.PuliziaCartelleInutili();
        pu.PulisceTemporanei();
        pu.AttesaFinePulizia(true);

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

        ChiamateWs c = new ChiamateWs();
        c.RitornaVersioneApplicazione();
        // startService(new Intent(MainActivity.this, ServizioBackground.class));
    }

    private void ImpostaOggettiAVideo() {
        VariabiliGlobali.getInstance().setMediaPlayer(new MediaPlayer());

        TextView txtInizio = (TextView) findViewById(R.id.txtInizio);
        TextView txtFine = (TextView) findViewById(R.id.txtFine);
        TextView txtNomeBrano = (TextView) findViewById(R.id.txtNomeBrano);
        ImmagineZoomabile imgSfondo = (ImmagineZoomabile) findViewById(R.id.imgImmagineSfondo);
        ImageView imgSfondoLogo = (ImageView) findViewById(R.id.imgImmagineSfondoLogo);
        imgSfondoLogo.setVisibility(LinearLayout.GONE);
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
        ImageView imgBellezza10 = (ImageView) findViewById(R.id.imgBellezza10);
        ImageView imgNoNet = (ImageView) findViewById(R.id.imgNoInternet);
        ListView lstArtisti = (ListView) findViewById(R.id.lstArtisti);
        ListView lstTags = (ListView) findViewById(R.id.lstTags);
        TextView txtPreferiti = (TextView) findViewById(R.id.txtPreferiti);
        TextView txtPreferitiElimina = (TextView) findViewById(R.id.txtPreferitiElimina);
        TextView txtTags = (TextView) findViewById(R.id.txtTags);
        TextView txtEliminaTags = (TextView) findViewById(R.id.txtEliminaTags);
        ImageView imgChiudeLista = (ImageView) findViewById(R.id.imgChiudeLista);
        ImageView imgChiudeListaTags = (ImageView) findViewById(R.id.imgChiudeListaTags);
        OggettiAVideo.getInstance().setImgChiudeListaTags(imgChiudeListaTags);
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
        LinearLayout layShareDebug = (LinearLayout) findViewById(R.id.layShareDebug);
        Button btnShareDebug = (Button) findViewById(R.id.btnShareDebug);
        Button btnShareDB = (Button) findViewById(R.id.btnShareDB);
        btnShareDB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String FileLog = VariabiliGlobali.getInstance().getPercorsoDIR() + "/DB/dati.db";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateAndTime = sdf.format(new Date());
                String FileLogDest = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Versioni/dati_" + currentDateAndTime + ".db";
                Utility.getInstance().EliminaFileUnico(FileLogDest);

                try {
                    Utility.getInstance().CopiaFile(FileLog, FileLogDest);

                    File fileImagePath = new File(FileLogDest);
                    Uri apkUri = FileProvider.getUriForFile(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                            BuildConfig.APPLICATION_ID + ".fileprovider", fileImagePath);

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_STREAM, apkUri);
                    share.setType("text/*");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startActivity(Intent.createChooser(share, "Share log File"));
                    // Utility.getInstance().EliminaFileUnico(FileLogDest);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
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
        Switch switchStelleSuperiori = (Switch) findViewById(R.id.switchStelleSuperiori);
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
        Button btnDataSuperiore = (Button) findViewById(R.id.btnDataSuperiore);
        Button btnDataInferiore = (Button) findViewById(R.id.btnDataInferiore);
        ImageView imgSuoneria = (ImageView) findViewById(R.id.imgSuoneria);
        imgSuoneria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Brano = VariabiliGlobali.getInstance().getStrutturaDelBrano().getPathBrano();

                if (Brano != null && !Brano.isEmpty()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Set Suoneria");
                    alert.setMessage("Vuoi impostare il brano come suoneria di default ?");
                    alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File k = new File(Brano);

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
                            values.put(MediaStore.MediaColumns.TITLE, VariabiliGlobali.getInstance().getStrutturaDelBrano().getBrano());
                            values.put(MediaStore.MediaColumns.SIZE, VariabiliGlobali.getInstance().getStrutturaDelBrano().getDimensione());
                            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                            values.put(MediaStore.Audio.Media.ARTIST, VariabiliGlobali.getInstance().getStrutturaDelBrano().getArtista());
                            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                            values.put(MediaStore.Audio.Media.IS_ALARM, false);
                            values.put(MediaStore.Audio.Media.IS_MUSIC, false);

                            Uri uri = MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath());
                            Uri newUri = VariabiliGlobali.getInstance().getFragmentActivityPrincipale().getContentResolver().insert(uri, values);

                            RingtoneManager.setActualDefaultRingtoneUri(
                                    VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                                    RingtoneManager.TYPE_RINGTONE,
                                    newUri
                            );

                            dialog.dismiss();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                } else {
                    Toast.makeText(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                            "Nessun brano caricato", Toast.LENGTH_LONG).show();
                }
            }
        });
        OggettiAVideo.getInstance().setImgSuoneria(imgSuoneria);
        OggettiAVideo.getInstance().getImgSuoneria().setVisibility(LinearLayout.GONE);

        LinearLayout layAmministrazione = (LinearLayout) findViewById(R.id.layAmministrazione);
        View viewAmministrazione = (View) findViewById(R.id.viewAmministrazione);
        LinearLayout layMascheraAmministrazione = (LinearLayout) findViewById(R.id.layMascheraAmministrazione);
        // layMascheraAmministrazione.setVisibility(LinearLayout.GONE);
        if (!VariabiliGlobali.getInstance().isAmministratore()) {
            layAmministrazione.setVisibility(LinearLayout.GONE);
            viewAmministrazione.setVisibility(LinearLayout.GONE);
        }
        Button btnAmministrazione = (Button) findViewById(R.id.btnAmministrazione);
        btnAmministrazione.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layMascheraAmministrazione.setVisibility(LinearLayout.VISIBLE);
            }
        });
        ImageView imgChiudAmministrazione = (ImageView) findViewById(R.id.imgAnnullaAmministrazione);
        imgChiudAmministrazione.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layMascheraAmministrazione.setVisibility(LinearLayout.GONE);
            }
        });
        Amministrazione.getInstance().GestioneTastiAmministrazione(this);

        TextView txtArtistaGAR = (TextView) findViewById(R.id.txtArtistaGAR);
        ListView lstTagsGAR = (ListView) findViewById(R.id.lstTagsGAR);
        ListView lstTagsTuttiGAR = (ListView) findViewById(R.id.lstTagsTuttiGAR);
        ImageView imgSalvaTagsGAR = (ImageView) findViewById(R.id.imgSalvaTagsGAR);
        imgSalvaTagsGAR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Artista = OggettiAVideo.getInstance().getTxtArtistaGAR().getText().toString();

                ChiamateWs ws = new ChiamateWs();
                ws.AggiornaTagsArtista(Artista);
            }
        });

        OggettiAVideo.getInstance().setLstTagsGAR(lstTagsGAR);
        OggettiAVideo.getInstance().setLstTagsTuttiGAR(lstTagsTuttiGAR);
        OggettiAVideo.getInstance().setImgSalvaTagsGAR(imgSalvaTagsGAR);
        OggettiAVideo.getInstance().setTxtArtistaGAR(txtArtistaGAR);

        LinearLayout layCambioImmagineGA = (LinearLayout) findViewById(R.id.layCambioImmagineGA);
        layCambioImmagineGA.setVisibility(LinearLayout.GONE);
        ImageView imgImmagineSceltaGA = (ImageView) findViewById(R.id.imgImmagineSceltaGA);
        ImageView imgCambioImmagineAlbum = (ImageView) findViewById(R.id.imgCambiaAlbumGA);
        ImageView imgAvantiGA = (ImageView) findViewById(R.id.imgAvantiGA);
        ImageView imgIndietroGA = (ImageView) findViewById(R.id.imgIndietroGA);
        TextView txtInfoGA = (TextView) findViewById(R.id.txtInfoImmagineGA);
        ImageView imgAlbumGA = (ImageView) findViewById(R.id.imgAlbumGA);
        EditText edtQuanteImmaginiGA = (EditText) findViewById(R.id.edtQuanteImmagini);
        edtQuanteImmaginiGA.setText(VariabiliGlobali.getInstance().getQuanteImmaginiDaScaricareGA().toString());
        edtQuanteImmaginiGA.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if (!edtQuanteImmaginiGA.getText().toString().isEmpty()) {
                    VariabiliGlobali.getInstance().setQuanteImmaginiDaScaricareGA(Integer.parseInt(edtQuanteImmaginiGA.getText().toString()));

                    db_dati db = new db_dati();
                    db.ScriveImpostazioni();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        LinearLayout layEditGA = (LinearLayout) findViewById(R.id.layEditGA);
        layEditGA.setVisibility(LinearLayout.GONE);
        TextView txtNomeAlbumGA = (TextView) findViewById(R.id.txtNomeAlbumGA);
        EditText edtNuovoNomeAlbumGA = (EditText) findViewById(R.id.edtNomeAlbumGA);
        EditText edtNuovoAnnoAlbumGA = (EditText) findViewById(R.id.edtAnnoAlbumGA);
        ImageView imgRinominaAlbumGA = (ImageView) findViewById(R.id.imgRinominaAlbumGA);
        ImageView imgAnnullaRinominaAlbumGA = (ImageView) findViewById(R.id.imgAnnullaRinominaAlbumGA);
        imgAnnullaRinominaAlbumGA.setVisibility(LinearLayout.GONE);
        imgAnnullaRinominaAlbumGA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtNomeAlbumGA.setVisibility(LinearLayout.VISIBLE);
                layEditGA.setVisibility(LinearLayout.GONE);
                imgAnnullaRinominaAlbumGA.setVisibility(LinearLayout.GONE);
                VariabiliGlobali.getInstance().setStaEditandoAlbum(false);
            }
        });
        VariabiliGlobali.getInstance().setStaEditandoAlbum(false);
        imgRinominaAlbumGA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!VariabiliGlobali.getInstance().isStaEditandoAlbum()) {
                    edtNuovoNomeAlbumGA.setText(VariabiliGlobali.getInstance().getNomeAlbumGA());
                    edtNuovoAnnoAlbumGA.setText(VariabiliGlobali.getInstance().getAnnoAlbumGA());
                    txtNomeAlbumGA.setVisibility(LinearLayout.GONE);
                    layEditGA.setVisibility(LinearLayout.VISIBLE);
                    imgAnnullaRinominaAlbumGA.setVisibility(LinearLayout.VISIBLE);
                    VariabiliGlobali.getInstance().setStaEditandoAlbum(true);
                } else {
                    // TextView txtAlbum = (TextView) VariabiliGlobali.getInstance().getFragmentActivityPrincipale().findViewById(R.id.txtNomeAlbumGA);
                    OggettiAVideo.getInstance().getTxtNomeAlbumGA().setText(edtNuovoNomeAlbumGA.getText().toString() +
                            " (" + VariabiliGlobali.getInstance().getNomeArtistaGA() + "). Anno " + edtNuovoAnnoAlbumGA.getText().toString());

                    ChiamateWsAmministrazione ws = new ChiamateWsAmministrazione();
                    ws.RinominaAlbum(VariabiliGlobali.getInstance().getNomeArtistaGA(),
                            VariabiliGlobali.getInstance().getNomeAlbumGA(),
                            VariabiliGlobali.getInstance().getAnnoAlbumGA(),
                            edtNuovoNomeAlbumGA.getText().toString(),
                            edtNuovoAnnoAlbumGA.getText().toString());
                }
            }
        });
        edtNuovoNomeAlbumGA.setText(VariabiliGlobali.getInstance().getQuanteImmaginiDaScaricareGA().toString());
        txtNomeAlbumGA.setVisibility(LinearLayout.VISIBLE);
        ListView lstTagsGA = (ListView) findViewById(R.id.lstTagsGA);
        ListView lstTagsTuttiGA = (ListView) findViewById(R.id.lstTagsTuttiGA);
        ImageView imgSalvaTagsGA = (ImageView) findViewById(R.id.imgSalvaTagsGA);
        imgSalvaTagsGA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWs ws = new ChiamateWs();
                ws.AggiornaTagsAlbum();
            }
        });
        OggettiAVideo.getInstance().setLstTagsGA(lstTagsGA);
        OggettiAVideo.getInstance().setLstTagsTuttiGA(lstTagsTuttiGA);
        OggettiAVideo.getInstance().setImgSalvaTagsGA(imgSalvaTagsGA);

        OggettiAVideo.getInstance().setTxtNomeAlbumGA(txtNomeAlbumGA);
        OggettiAVideo.getInstance().setImgRinominaAlbumGA(imgRinominaAlbumGA);
        OggettiAVideo.getInstance().setEdtNomeAlbumGA(edtNuovoNomeAlbumGA);

        OggettiAVideo.getInstance().setLayCambioImmagineGA(layCambioImmagineGA);
        OggettiAVideo.getInstance().setImgSceltaGA(imgImmagineSceltaGA);
        OggettiAVideo.getInstance().setImgCambiaAlbumGA(imgCambioImmagineAlbum);
        OggettiAVideo.getInstance().setImgIndietroGA(imgIndietroGA);
        OggettiAVideo.getInstance().setImgAvantiGA(imgAvantiGA);
        OggettiAVideo.getInstance().setTxtInfoGA(txtInfoGA);
        OggettiAVideo.getInstance().setImgAlbumGA(imgAlbumGA);
        OggettiAVideo.getInstance().setEdtQuanteImmaginiGA(edtQuanteImmaginiGA);

        Button btnPulisce = (Button) findViewById(R.id.btnPulisceBrani);
        btnPulisce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EliminaBraniDaDisco bckElimina = new EliminaBraniDaDisco(true);
                bckElimina.execute();
            }
        });

        ImageView imgShare = (ImageView) findViewById(R.id.imgCondividi);
        imgShare.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               String Brano = VariabiliGlobali.getInstance().getStrutturaDelBrano().getPathBrano();

               if (Brano != null && !Brano.isEmpty()) {
                   String BranoDest = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Versioni/" + VariabiliGlobali.getInstance().getStrutturaDelBrano().getBrano() +
                           VariabiliGlobali.getInstance().getStrutturaDelBrano().getEstensione();

                   try {
                       Utility.getInstance().CopiaFile(Brano, BranoDest);
                       // File f = new File(Brano);
                       // Uri uri = Uri.parse("file://" + f.getAbsolutePath());

                       File fileImagePath = new File(BranoDest);
                       Uri apkUri = FileProvider.getUriForFile(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                               BuildConfig.APPLICATION_ID + ".fileprovider", fileImagePath);

                       Intent share = new Intent(Intent.ACTION_SEND);
                       share.putExtra(Intent.EXTRA_STREAM, apkUri);
                       share.setType("audio/*");
                       share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                       VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startActivity(Intent.createChooser(share, "Share audio File"));
                   } catch (IOException e) {
                       Log.getInstance().ScriveLog("Share brano " + BranoDest + "\n\n" + Utility.getInstance().PrendeErroreDaException(e));
                       Utility.getInstance().VisualizzaErrore("Share brano " + BranoDest + "\n\n" + Utility.getInstance().PrendeErroreDaException(e));
                   }
               } else {
                   Toast.makeText(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                           "Nessun brano caricato", Toast.LENGTH_LONG).show();
               }
           }
       });

        OggettiAVideo.getInstance().setBtnDataSuperiore(btnDataSuperiore);
        OggettiAVideo.getInstance().setBtnDataInferiore(btnDataInferiore);
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
        OggettiAVideo.getInstance().setLayShareDebug(layShareDebug);
        OggettiAVideo.getInstance().setBtnShareDebug(btnShareDebug);
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
        OggettiAVideo.getInstance().setSwitchStelleSuperiori(switchStelleSuperiori);
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
        OggettiAVideo.getInstance().setImgSfondoLogo(imgSfondoLogo);
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
        OggettiAVideo.getInstance().setImgBellezzza10(imgBellezza10);
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
        VariabiliGlobali.getInstance().setDimeSchermoX(displayMetrics.widthPixels);
        VariabiliGlobali.getInstance().setDimeSchermoY(displayMetrics.heightPixels);
        ViewGroup.LayoutParams params = layTesto.getLayoutParams();
        params.width = (width * 3) / 4;
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
                    int doveX = (width * 3) / 4;
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
        params2.width = (width * 75) / 100;
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

                    int doveX = (width * 75) / 100;
                    imgLinguetta2.setX(doveX);
                }
            }
        });

        Button imgRicaricaBrani = (Button) findViewById(R.id.btnRicaricaBrani);
        imgRicaricaBrani.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWs ws = new ChiamateWs();
                ws.RicaricaBrani();
            }
        });

        Switch swcSimulazione = (Switch) findViewById(R.id.swcSimulazione);
        swcSimulazione.setChecked(true);
        VariabiliGlobali.getInstance().setSimulazione(true);
        swcSimulazione.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  VariabiliGlobali.getInstance().setSimulazione(isChecked);
              }
        });
        Switch swcSoloLocale = (Switch) findViewById(R.id.swcSoloLocale);
        swcSoloLocale.setChecked(false);
        VariabiliGlobali.getInstance().setSoloLocale(true);
        swcSoloLocale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setSoloLocale(isChecked);
            }
        });
        Button imgPuliziaCompleta = (Button) findViewById(R.id.btnPuliziaCompleta);
        imgPuliziaCompleta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LogPulizia.getInstance().EliminaFileLog();

                PuliziaGenerale p = new PuliziaGenerale();
                p.PuliziaCartelleInutili();

                if (!VariabiliGlobali.getInstance().isSoloLocale()) {
                    ChiamateWs ws = new ChiamateWs();
                    ws.PuliziaCompleta();
                }

                p.PulizieProfonde();
            }
        });

        LinearLayout layGestioneAlbum = (LinearLayout) findViewById(R.id.layGestioneAlbum);
        layGestioneAlbum.setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().setLayGestioneAlbum(layGestioneAlbum);
        ImageView imgChiudeAlbum = (ImageView) findViewById(R.id.imgChiudeGestioneAlbum);
        imgChiudeAlbum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layGestioneAlbum.setVisibility(LinearLayout.GONE);
            }
        });

        LinearLayout layGestioneArtista = (LinearLayout) findViewById(R.id.layGestioneArtista);
        layGestioneArtista.setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().setLayGestioneArtista(layGestioneArtista);
        ImageView imgChiudeArtista = (ImageView) findViewById(R.id.imgChiudeGestioneArtista);
        imgChiudeArtista.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layGestioneArtista.setVisibility(LinearLayout.GONE);
            }
        });

        imgChiudeESalvaLista.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgLinguetta1.setVisibility(LinearLayout.VISIBLE);
                OggettiAVideo.getInstance().getLayGestionePreferiti().setVisibility(LinearLayout.GONE);
                layLista.setVisibility(LinearLayout.GONE);

                db_dati db = new db_dati();
                db.ScrivePreferiti();

                // FiltraBrani f = new FiltraBrani();
                // f.FiltraLista(true);
            }
        });

        imgChiudeLista.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getLayGestionePreferiti().setVisibility(LinearLayout.GONE);
                imgLinguetta1.setVisibility(LinearLayout.VISIBLE);
                layLista.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgTagsBrano = (ImageView) findViewById(R.id.imgTagsBrano);
        OggettiAVideo.getInstance().setImgTags(imgTagsBrano);

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
        if (!VariabiliGlobali.getInstance().isAmministratore()) {
            imgRefreshTesto.setVisibility(LinearLayout.GONE);
        }
        imgRefreshTesto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWs ws = new ChiamateWs();
                ws.AggiornaTesto(true);
            }
        });

        // GESTIONE PREFERITI
        LinearLayout layGestionePreferiti = (LinearLayout) findViewById(R.id.layGestionePreferiti);
        layGestionePreferiti.setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().setLayGestionePreferiti(layGestionePreferiti);

        EditText edtPreferito = (EditText) findViewById(R.id.edtPreferito);
        OggettiAVideo.getInstance().setEdtNomePreferito(edtPreferito);
        ImageView imgSalvaPreferito = (ImageView) findViewById(R.id.imgSalvaPreferito);
        imgSalvaPreferito.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String preferito = edtPreferito.getText().toString();
                if (preferito.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Inserire un nome di preferito");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    db_dati db = new db_dati();
                    if (db.SalvaListaPreferiti(preferito)) {
                        AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Salvataggio effettuato");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            }
        });

        ImageView imgPuliscePreferiti = (ImageView) findViewById(R.id.imgPuliscePreferiti);
        imgPuliscePreferiti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliGlobali.getInstance().setPreferiti(";");
                VariabiliGlobali.getInstance().setPreferitiElimina(";");

                AdapterListenerListaPreferiti customAdapter = new AdapterListenerListaPreferiti(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        VariabiliGlobali.getInstance().getListaPreferiti());
                OggettiAVideo.getInstance().getLstListaPreferiti().setAdapter(customAdapter);
            }
        });

        Button btnScatenaAggiornamento = (Button) findViewById(R.id.btnScatenaAggiornamento);
        btnScatenaAggiornamento.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWs c = new ChiamateWs();
                c.ScatenaAggiornamento();
            }
        });

        ImageView imgPulisceTags = (ImageView) findViewById(R.id.imgPulisceTags);
        imgPulisceTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliGlobali.getInstance().setPreferitiTags(";");
                VariabiliGlobali.getInstance().setPreferitiEliminaTags(";");

                AdapterListenerTags customAdapterTags = new AdapterListenerTags(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        VariabiliGlobali.getInstance().getListaTags());
                OggettiAVideo.getInstance().getLstTags().setAdapter(customAdapterTags);
            }
        });

        ListView lstListaPreferiti = (ListView) findViewById(R.id.lstListaPreferiti);
        OggettiAVideo.getInstance().setLstListaPreferiti(lstListaPreferiti);
        ImageView imgCaricaPreferito = (ImageView) findViewById(R.id.imgCaricaPreferito);
        imgCaricaPreferito.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati db = new db_dati();
                db.RitornaListaPreferiti();

                AdapterListenerListaPreferiti customAdapter = new AdapterListenerListaPreferiti(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        VariabiliGlobali.getInstance().getListaPreferiti());
                OggettiAVideo.getInstance().getLstListaPreferiti().setAdapter(customAdapter);

                layGestionePreferiti.setVisibility(LinearLayout.VISIBLE);
            }
        });
        ImageView imgChiudeListaPreferiti = (ImageView) findViewById(R.id.imgChiudeListaPreferiti);
        imgChiudeListaPreferiti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layGestionePreferiti.setVisibility(LinearLayout.GONE);
            }
        });
        // GESTIONE PREFERITI

        // GESTIONE TAGS
        LinearLayout layGestioneTags = (LinearLayout) findViewById(R.id.layGestioneTags);
        layGestioneTags.setVisibility(LinearLayout.GONE);
        ListView lstGestioneTags = (ListView) findViewById(R.id.lstGestioneTags);
        OggettiAVideo.getInstance().setLstGestioneTags(lstGestioneTags);
        Button btnGestioneTags = (Button) findViewById(R.id.btnGestioneTags);
        btnGestioneTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AdapterListenerGestioneTags customAdapterT = new AdapterListenerGestioneTags(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        VariabiliGlobali.getInstance().getListaTags());
                lstGestioneTags.setAdapter(customAdapterT);

                layGestioneTags.setVisibility(LinearLayout.VISIBLE);
            }
        });
        ImageView imgChiudeGestioneTags = (ImageView) findViewById(R.id.imgChiudeGestioneTags);
        imgChiudeGestioneTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layGestioneTags.setVisibility(LinearLayout.GONE);
            }
        });
        ImageView imgAggiungeTag = (ImageView) findViewById(R.id.imgAggiungeTag);
        imgAggiungeTag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale());
                builder.setTitle("Aggiunge Tag");

                final EditText input = new EditText(VariabiliGlobali.getInstance().getFragmentActivityPrincipale());
                builder.setView(input);
                input.setText("");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWsAmministrazione c = new ChiamateWsAmministrazione();
                        c.AggiungeTag(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        // GESTIONE TAGS

        mAudioManagerInterno = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mReceiverComponentInterno = new ComponentName(this, GestioneTastoCuffie.class);
        mAudioManagerInterno.registerMediaButtonEventReceiver(mReceiverComponentInterno);
        VariabiliGlobali.getInstance().setmAudioManager(mAudioManagerInterno);
        VariabiliGlobali.getInstance().setmReceiverComponent(mReceiverComponentInterno);

        IntentFilter filterHeadset;
        filterHeadset = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(VariabiliGlobali.getInstance().mNoisyReceiver, filterHeadset);
    }

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
        Utility.getInstance().Uscita();
    }
}