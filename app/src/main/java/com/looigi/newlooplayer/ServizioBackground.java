package com.looigi.newlooplayer;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;

import com.looigi.newlooplayer.WebServices.ChiamateWs;
import com.looigi.newlooplayer.adapters.AdapterListenerArtisti;
import com.looigi.newlooplayer.adapters.AdapterListenerTags;
import com.looigi.newlooplayer.chiamate.PhoneUnlockedReceiver;
import com.looigi.newlooplayer.cuffie.GestioneTastoCuffie;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.notifiche.Notifica;

public class ServizioBackground extends Service {
    private GestioneTastoCuffie myReceiver = new GestioneTastoCuffie();
    private PhoneUnlockedReceiver receiverAccesoSpento;

    private AudioManager mAudioManager;
    private ComponentName mReceiverComponent;

    private Runnable rAgg;
    private Handler handlerAgg;
    private ProgressDialog progressDialog;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Toast.makeText(this, "Servizio LooPlayer partito.", Toast.LENGTH_LONG).show();

        VariabiliGlobali.getInstance().setControllatoUpdate("");
        ChiamateWs ws = new ChiamateWs();
        ws.RitornaUltimoAggiornamento();

        try {
            progressDialog = new ProgressDialog(VariabiliGlobali.getInstance().getContext());
            progressDialog.setMessage("Attendere Prego...\n\nControllo aggiornamento brani");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        } catch (Exception ignored) {

        }

        handlerAgg = new Handler();
        rAgg = new Runnable() {
            public void run() {
                String ritorno = VariabiliGlobali.getInstance().isControllatoUpdate();
                if (!ritorno.isEmpty()) {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception ignored) {
                    }

                    /* if (ritorno.equals("AGGIORNARE")) {

                    } else { */
                        OggettiAVideo.getInstance().getLaySplash().setVisibility(LinearLayout.GONE);

                        ProsegueInizio();
                    // }
                } else {
                    handlerAgg.postDelayed(rAgg, 1000);
                }
            }
        };
        handlerAgg.postDelayed(rAgg, 1000);

        return START_NOT_STICKY;
    }

    private void ProsegueInizio() {
        Log.getInstance().ScriveLog("Instanzio receiver cuffie");
        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(myReceiver, filter);

        Log.getInstance().ScriveLog("Apertura DB");
        db_dati db = new db_dati();
        // db.PulisceDati();
        Log.getInstance().ScriveLog("Creazione Tabelle");
        db.CreazioneTabelle();
        boolean ok = db.LeggeIdUtente();
        if (!ok) {
            // Gestione richiesta utente
            VariabiliGlobali.getInstance().setIdUtente(1); // TOGLIERE E GESTIRE ID UTENTE IN FASE FINALE
            Log.getInstance().ScriveLog("Utente sconosciuto. Imposto 1");
        }
        VariabiliGlobali.getInstance().setSpazioOccupatoSuDisco(db.PrendeDimensioniBrani());

        int ultimo = db.LeggeUltimoBranoAscoltato();
        Log.getInstance().ScriveLog("Ultimo brano ascoltato: " + ultimo);
        if (ultimo == -1) {
            VariabiliGlobali.getInstance().setIdUltimoBranoAscoltato(1);
        }
        if (VariabiliGlobali.getInstance().getIdUtente() != null & VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato() != null) {
            Log.getInstance().ScriveLog("Inizio Sessione. idUtente: " + VariabiliGlobali.getInstance().getIdUtente().toString() +
                    " ID Ultimo Brano Ascoltato: " + VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato().toString());
        }

        boolean Impostazioni = db.LeggeImpostazioni();
        if (Impostazioni) {
            if (VariabiliGlobali.getInstance().getStelleDaRicercare() == -1) {
                VariabiliGlobali.getInstance().setStelleDaRicercare(7);
            }
            Log.getInstance().ScriveLog("Ricerca testo: " + VariabiliGlobali.getInstance().isRicercaTesto());
            Log.getInstance().ScriveLog("Testo da ricercare: " + VariabiliGlobali.getInstance().getTestoDaRicercare());
            Log.getInstance().ScriveLog("Ricerca Esclusione: " + VariabiliGlobali.getInstance().isRicercaEsclusione());
            Log.getInstance().ScriveLog("Testo da escludere: " + VariabiliGlobali.getInstance().getTestoDaEscludere());
            Log.getInstance().ScriveLog("Ricerca Stelle: " + VariabiliGlobali.getInstance().isRicercaStelle());
            Log.getInstance().ScriveLog("Stelle da ricercare: " + VariabiliGlobali.getInstance().getStelleDaRicercare().toString());
            Log.getInstance().ScriveLog("Ricerca Mai Ascoltata: " + VariabiliGlobali.getInstance().isRicercaMaiAscoltata());
            Log.getInstance().ScriveLog("Ricerca Tags: " + VariabiliGlobali.getInstance().isRicercaTags());
            Log.getInstance().ScriveLog("Tags da ricercare: " + VariabiliGlobali.getInstance().getTagsDaRicercare());
            Log.getInstance().ScriveLog("Visualizza Info: " + VariabiliGlobali.getInstance().isVisualizzaInformazioni());
            Log.getInstance().ScriveLog("Random: " + VariabiliGlobali.getInstance().isRandom());
        } else {
            Log.getInstance().ScriveLog("Impostazioni di default");
        }

        db.LeggePreferiti();
        db.LeggeTags();
        // Utility.getInstance().LeggeListaBrani();

        VariabiliGlobali.getInstance().setFermaTimer(true);
        Utility.getInstance().FaiPartireTimer();

        /* IntentFilter filterHeadset;
        filterHeadset = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mNoisyReceiver, filterHeadset); */

        // VariabiliGlobali.getInstance().setMediaPlayer(new MediaPlayer());

        AzionaControlloSchermo();
        AzionaCuffie();

        ControllaAmministratore();

        // GestioneCPU.getInstance().ImpostaValori(this);
        // GestioneCPU.getInstance().AttivaCPU();

        InstanziaNotifica();

        Log.getInstance().ScriveLog("Controllo presenza rete");
        int statoRete = Utility.getInstance().ControllaRete();
        Log.getInstance().ScriveLog("Valore rete: " + statoRete);

        /* if (VariabiliGlobali.getInstance().getUltimoStatoRete() >= 2) {
            // RETE OK
            if (VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato() != null) {
                Log.getInstance().ScriveLog("Caricamento ultimo brano: " + Integer.toString(VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato()));
                Utility.getInstance().CaricamentoBrano(Integer.toString(VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato()), false);
            }

            ChiamateWs ws = new ChiamateWs();
            ws.RitornaListaArtisti();

            ChiamateWs ws2 = new ChiamateWs();
            ws2.RitornaListaTags();
        } else { */
        // RETE SBRAGATA. Carico i files di appoggio e carico il primo brano disponibile sulla sd

        Utility.getInstance().ControllaRete();

        String NomeFile = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste/ListaArtisti.txt";
        if (Utility.getInstance().EsisteFile(NomeFile)) {
            Log.getInstance().ScriveLog("Lettura file artisti");
            String result = Utility.getInstance().LeggeFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaArtisti.txt");
            ChiamateWs ws = new ChiamateWs();
            ws.RitornaArtisti2(result);
        } else {
            Log.getInstance().ScriveLog("Chiamata lista artisti");
            ChiamateWs ws = new ChiamateWs();
            ws.RitornaListaArtisti();
            // Log.getInstance().ScriveLog("Impossibile proseguire... File artisti non presente");
        }

        NomeFile = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste/ListaTags.txt";
        if (Utility.getInstance().EsisteFile(NomeFile)) {
            Log.getInstance().ScriveLog("Lettura file tags");
            String result = Utility.getInstance().LeggeFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaTags.txt");
            ChiamateWs ws = new ChiamateWs();
            ws.RitornaListaTags22(result);
        } else {
            Log.getInstance().ScriveLog("Chiamata lista tags");
            ChiamateWs ws2 = new ChiamateWs();
            ws2.RitornaListaTags();
        }

        NomeFile = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste/BraniInLocale.txt";
        if (Utility.getInstance().EsisteFile(NomeFile)) {
            if (VariabiliGlobali.getInstance().isEliminaBrani()) {
                EliminaBraniDaDisco bckElimina = new EliminaBraniDaDisco();
                bckElimina.execute();
            } else {
                Utility.getInstance().ProsegueAvvio();
            }
        } else {
            db.EliminaTuttiIBrani();
            Log.getInstance().ScriveLog("Chiamata lista brani");
            ScannaDiscoPerBraniLocali bckLeggeBraniLocali = new ScannaDiscoPerBraniLocali();
            bckLeggeBraniLocali.execute();

            if (VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato() != null) {
                Log.getInstance().ScriveLog("Caricamento ultimo brano: " + Integer.toString(VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato()));
                Utility.getInstance().CaricamentoBrano(Integer.toString(VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato()), false);
            } else {
                Log.getInstance().ScriveLog("Ultimo brano non impostato... Prendo il successivo");
                Utility.getInstance().AvantiBrano();
            }
        }

        ImpostaFunzionalita();
    }

    private void ImpostaFunzionalita() {
        OggettiAVideo.getInstance().getLayTesto().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getLayDown().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getLayCaricamento().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().setLayCaricamento(OggettiAVideo.getInstance().getLayCaricamento());
        OggettiAVideo.getInstance().setTxtCaricamento(OggettiAVideo.getInstance().getTxtCaricamento());
        OggettiAVideo.getInstance().getTxtCaricamento().setText("");
        OggettiAVideo.getInstance().getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

                if (fromUser) {
                    OggettiAVideo.getInstance().getLayContenitore().setAlpha((float) 0.85);
                    Utility.getInstance().AzionaTimerOpacitaBottoniera();

                    Utility.getInstance().ImpostaPosizioneBrano(progress);
                }

                // t1.setTextSize(progress);
                // Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();
            }
        });
        OggettiAVideo.getInstance().getImgIndietro().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getLayContenitore().setAlpha((float) 0.85);
                Utility.getInstance().AzionaTimerOpacitaBottoniera();

                Utility.getInstance().IndietroBrano();
            }
        });
        OggettiAVideo.getInstance().getImgPlay().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getLayContenitore().setAlpha((float) 0.85);
                Utility.getInstance().AzionaTimerOpacitaBottoniera();

                boolean Accende = true;
                if (VariabiliGlobali.getInstance().isStaSuonando()) {
                    Accende = false;
                }
                Utility.getInstance().premutoPlay(Accende);
            }
        });
        OggettiAVideo.getInstance().getImgAvanti().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getLayContenitore().setAlpha((float) 0.85);
                Utility.getInstance().AzionaTimerOpacitaBottoniera();

                Utility.getInstance().AvantiBrano();
            }
        });

        OggettiAVideo.getInstance().getImgBellezzza0().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(0);
            }
        });
        OggettiAVideo.getInstance().getImgBellezzza1().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(1);
            }
        });
        OggettiAVideo.getInstance().getImgBellezzza2().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(2);
            }
        });
        OggettiAVideo.getInstance().getImgBellezzza3().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(3);
            }
        });
        OggettiAVideo.getInstance().getImgBellezzza4().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(4);
            }
        });
        OggettiAVideo.getInstance().getImgBellezzza5().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(5);
            }
        });
        OggettiAVideo.getInstance().getImgBellezzza6().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(6);
            }
        });
        OggettiAVideo.getInstance().getImgBellezzza7().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(7);
            }
        });
        OggettiAVideo.getInstance().getImgBellezzza8().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(8);
            }
        });
        OggettiAVideo.getInstance().getImgBellezzza9().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().SettaBellezza(9);
            }
        });
        /* ScrollView scrollView = (ScrollView) findViewById(R.id.horizontalScroll);
        MyHorizontalScrollView.OnScrollChangedListener o = new MyHorizontalScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                // Toast.makeText(VariabiliGlobali.getInstance().getContext(), Integer.toString(l),
                //         Toast.LENGTH_LONG).show();
                Log.getInstance().ScriveLog(Integer.toString(l) + " - " + Integer.toString(t));
            }
        };
        scrollView.setOnScrollChangedListener(o); */

        OggettiAVideo.getInstance().getLayLista().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getBtnLista().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getLayLista().setVisibility(LinearLayout.VISIBLE);
                OggettiAVideo.getInstance().getImgLinguetta1().setVisibility(LinearLayout.GONE);
            }
        });
        OggettiAVideo.getInstance().setBtnLista(OggettiAVideo.getInstance().getBtnLista());

        /* imgChiudeLista.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgLinguetta1.setVisibility(LinearLayout.VISIBLE);
                layLista.setVisibility(LinearLayout.GONE);
            }
        }); */

        boolean statoRete = InternetDetector.getInstance().isOnline();
        VariabiliGlobali.getInstance().setRetePresente(statoRete);
        if (statoRete) {
            OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.GONE);
            if (!VariabiliGlobali.getInstance().isBranosSuSDOriginale()) {
                VariabiliGlobali.getInstance().setBranoSuSD(false);
            }
        } else {
            OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.VISIBLE);
            VariabiliGlobali.getInstance().setBranoSuSD(true);
        }

        OggettiAVideo.getInstance().getLayContenitore().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getLayContenitore().setAlpha((float) 0.75);
                Utility.getInstance().AzionaTimerOpacitaBottoniera();
            }
        });
        OggettiAVideo.getInstance().getLayContenitore().setAlpha((float) 0.75);
        Utility.getInstance().AzionaTimerOpacitaBottoniera();

        OggettiAVideo.getInstance().getImgCuffie().setVisibility(LinearLayout.GONE);

        // PREGRESSO
        OggettiAVideo.getInstance().getLayPregresso().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getImgCambiaPregresso().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.GONE);

                Utility.getInstance().CambiaBranoPregresso();
            }
        });
        // OggettiAVideo.getInstance().getImgPregresso().setVisibility(LinearLayout.GONE);

        OggettiAVideo.getInstance().getLaySettaggi().setVisibility(LinearLayout.GONE);

        OggettiAVideo.getInstance().getImgMenu().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (OggettiAVideo.getInstance().getLaySettaggi().getVisibility() == LinearLayout.VISIBLE) {
                    OggettiAVideo.getInstance().getLaySettaggi().setVisibility(LinearLayout.GONE);
                } else {
                    OggettiAVideo.getInstance().getLaySettaggi().setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        OggettiAVideo.getInstance().getLayRicerche().setVisibility(LinearLayout.GONE);

        OggettiAVideo.getInstance().getImgRicerche().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (OggettiAVideo.getInstance().getLayRicerche().getVisibility() == LinearLayout.VISIBLE) {
                    OggettiAVideo.getInstance().getLayRicerche().setVisibility(LinearLayout.GONE);
                } else {
                    OggettiAVideo.getInstance().getLayRicerche().setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        boolean dataSuperiore = VariabiliGlobali.getInstance().isDataSuperiore();
        OggettiAVideo.getInstance().getSwitchDataSuperiore().setChecked(dataSuperiore);
        if (dataSuperiore) {
            OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchDataSuperiore().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setDataSuperiore(isChecked);

                if (!VariabiliGlobali.getInstance().isDate()) {
                    OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.GONE);
                    OggettiAVideo.getInstance().getSwitchDataInferiore().setChecked(false);
                    VariabiliGlobali.getInstance().setDataInferiore(false);
                    OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.GONE);
                    OggettiAVideo.getInstance().getSwitchDataSuperiore().setChecked(false);
                    VariabiliGlobali.getInstance().setDataSuperiore(false);
                } else {
                    if (isChecked) {
                        OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.VISIBLE);

                        OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.GONE);
                        OggettiAVideo.getInstance().getSwitchDataInferiore().setChecked(false);
                        VariabiliGlobali.getInstance().setDataInferiore(false);
                    } else {
                        OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.GONE);

                        OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.VISIBLE);
                        OggettiAVideo.getInstance().getSwitchDataInferiore().setChecked(true);
                        VariabiliGlobali.getInstance().setDataInferiore(true);
                    }
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });
        OggettiAVideo.getInstance().getEdtDataSuperiore().setText(VariabiliGlobali.getInstance().getTxtDataSuperiore());
        OggettiAVideo.getInstance().getEdtDataSuperiore().addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                String dataora = OggettiAVideo.getInstance().getEdtDataSuperiore().getText().toString();
                // if (dataora.length() == 10) {
                    if (!dataora.contains("/")) {
                        // Controllo formale sulle barre
                    } else {
                        String c[] = dataora.split("/");
                        if (c.length < 3) {
                            // Controllo sul numero delle barre
                        } else {
                            try {
                                int giorno = Integer.parseInt(c[0]);
                                int mese = Integer.parseInt(c[1]);
                                String anno = c[2];

                                if (giorno < 1 || giorno > 31) {
                                    // Controllo formale sul giorno
                                } else {
                                    if (giorno > 30 && (mese == 2 || mese == 4 || mese == 6 || mese == 9 || mese == 11)) {
                                        // Controllo sul giorno maggiore di 30 per i mesi di 30
                                    } else {
                                        if (mese < 1 || mese > 12) {
                                            // Controllo formale sul mese
                                        } else {
                                            if (anno.length() != 4) {
                                                // Controllo lunghezza anno
                                            } else {
                                                String g = Integer.toString(giorno);
                                                String m = Integer.toString(mese);
                                                if (g.length() == 1) {
                                                    g = "0" + g;
                                                }
                                                if (m.length() == 1) {
                                                    m = "0" + m;
                                                }

                                                dataora = g + "/" + m + "/" + anno;
                                                VariabiliGlobali.getInstance().setTxtDataSuperiore(dataora);

                                                db_dati db = new db_dati();
                                                db.ScriveImpostazioni();
                                            }
                                        }
                                    }
                                }
                            } catch (Exception ignored) {

                            }
                        }
                    }
                // }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        boolean dataInferiore = VariabiliGlobali.getInstance().isDataInferiore();
        OggettiAVideo.getInstance().getSwitchDataInferiore().setChecked(dataInferiore);
        if (dataInferiore) {
            OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchDataInferiore().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setDataInferiore(isChecked);

                if (!VariabiliGlobali.getInstance().isDate()) {
                    OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.GONE);
                    OggettiAVideo.getInstance().getSwitchDataInferiore().setChecked(false);
                    VariabiliGlobali.getInstance().setDataInferiore(false);
                    OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.GONE);
                    OggettiAVideo.getInstance().getSwitchDataSuperiore().setChecked(false);
                    VariabiliGlobali.getInstance().setDataSuperiore(false);
                } else {
                    if (isChecked) {
                        OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.VISIBLE);

                        OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.GONE);
                        OggettiAVideo.getInstance().getSwitchDataSuperiore().setChecked(false);
                        VariabiliGlobali.getInstance().setDataSuperiore(false);
                    } else {
                        OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.GONE);

                        OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.VISIBLE);
                        OggettiAVideo.getInstance().getSwitchDataSuperiore().setChecked(true);
                        VariabiliGlobali.getInstance().setDataSuperiore(true);
                    }
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });
        OggettiAVideo.getInstance().getEdtDataInferiore().setText(VariabiliGlobali.getInstance().getTxtDataInferiore());
        OggettiAVideo.getInstance().getEdtDataInferiore().addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                String dataora = OggettiAVideo.getInstance().getEdtDataInferiore().getText().toString();
                // if (dataora.length() == 10) {
                if (!dataora.contains("/")) {
                    // Controllo formale sulle barre
                } else {
                    String c[] = dataora.split("/");
                    if (c.length < 3) {
                        // Controllo sul numero delle barre
                    } else {
                        try {
                            int giorno = Integer.parseInt(c[0]);
                            int mese = Integer.parseInt(c[1]);
                            String anno = c[2];

                            if (giorno < 1 || giorno > 31) {
                                // Controllo formale sul giorno
                            } else {
                                if (giorno > 30 && (mese == 2 || mese == 4 || mese == 6 || mese == 9 || mese == 11)) {
                                    // Controllo sul giorno maggiore di 30 per i mesi di 30
                                } else {
                                    if (mese < 1 || mese > 12) {
                                        // Controllo formale sul mese
                                    } else {
                                        if (anno.length() != 4) {
                                            // Controllo lunghezza anno
                                        } else {
                                            String g = Integer.toString(giorno);
                                            String m = Integer.toString(mese);
                                            if (g.length() == 1) {
                                                g = "0" + g;
                                            }
                                            if (m.length() == 1) {
                                                m = "0" + m;
                                            }

                                            dataora = g + "/" + m + "/" + anno;
                                            VariabiliGlobali.getInstance().setTxtDataInferiore(dataora);

                                            db_dati db = new db_dati();
                                            db.ScriveImpostazioni();
                                        }
                                    }
                                }
                            }
                        } catch (Exception ignored) {

                        }
                    }
                }
                // }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        boolean date = VariabiliGlobali.getInstance().isDate();
        OggettiAVideo.getInstance().getSwitchDate().setChecked(date);
        if (date) {
            OggettiAVideo.getInstance().getSwitchDataSuperiore().setChecked(false);
            OggettiAVideo.getInstance().getSwitchDataInferiore().setChecked(false);

            // OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.GONE);
            // OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.GONE);

            VariabiliGlobali.getInstance().setDataSuperiore(false);
            VariabiliGlobali.getInstance().setDataInferiore(false);

            db_dati db = new db_dati();
            db.ScriveImpostazioni();
        }
        OggettiAVideo.getInstance().getSwitchDate().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setDate(isChecked);

                if (!isChecked) {
                    OggettiAVideo.getInstance().getSwitchDataSuperiore().setChecked(false);
                    OggettiAVideo.getInstance().getSwitchDataInferiore().setChecked(false);

                    OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.GONE);
                    OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.GONE);

                    VariabiliGlobali.getInstance().setDataSuperiore(false);
                    VariabiliGlobali.getInstance().setDataInferiore(false);
                } else {
                    OggettiAVideo.getInstance().getSwitchDataSuperiore().setChecked(true);
                    OggettiAVideo.getInstance().getSwitchDataInferiore().setChecked(false);

                    OggettiAVideo.getInstance().getEdtDataSuperiore().setVisibility(LinearLayout.VISIBLE);
                    OggettiAVideo.getInstance().getEdtDataInferiore().setVisibility(LinearLayout.GONE);

                    VariabiliGlobali.getInstance().setDataSuperiore(true);
                    VariabiliGlobali.getInstance().setDataInferiore(false);
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        // Switch switchEliminaDebug = (Switch) findViewById(R.id.switchEliminaDebug);
        boolean debug = VariabiliGlobali.getInstance().isAzionaDebug();
        OggettiAVideo.getInstance().getSwitchDebug().setChecked(debug);
        if (debug) {
            OggettiAVideo.getInstance().getLayDebug().setVisibility(LinearLayout.VISIBLE);
            OggettiAVideo.getInstance().getSwitchEliminaDebug().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getLayDebug().setVisibility(LinearLayout.GONE);
            OggettiAVideo.getInstance().getSwitchEliminaDebug().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchDebug().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setAzionaDebug(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
                if (isChecked) {
                    OggettiAVideo.getInstance().getLayDebug().setVisibility(LinearLayout.VISIBLE);
                    OggettiAVideo.getInstance().getSwitchEliminaDebug().setVisibility(LinearLayout.VISIBLE);
                } else {
                    OggettiAVideo.getInstance().getLayDebug().setVisibility(LinearLayout.GONE);
                    OggettiAVideo.getInstance().getSwitchEliminaDebug().setVisibility(LinearLayout.GONE);
                }
            }
        });

        if (Utility.getInstance().EsisteFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/" + "EliminaDebug.txt")) {
            OggettiAVideo.getInstance().getSwitchEliminaDebug().setChecked(true);
        } else {
            OggettiAVideo.getInstance().getSwitchEliminaDebug().setChecked(false);
        }
        OggettiAVideo.getInstance().getSwitchEliminaDebug().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "EliminaDebug.txt", "*");
                } else {
                    Utility.getInstance().EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "EliminaDebug.txt");
                }
            }
        });
        /* switchEliminaDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setEliminaDebug(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        }); */

        OggettiAVideo.getInstance().getBtnDebug().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.getInstance().EliminaFileLog();

                AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("File di log eliminato");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        OggettiAVideo.getInstance().getBtnListe().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().PulisceListe(true);
            }
        });

        boolean orologio = VariabiliGlobali.getInstance().isMostraOrologio();
        if (orologio) {
            OggettiAVideo.getInstance().getClock().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getClock().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchOrologio().setChecked(orologio);
        OggettiAVideo.getInstance().getSwitchOrologio().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setMostraOrologio(isChecked);
                if (isChecked) {
                    OggettiAVideo.getInstance().getClock().setVisibility(LinearLayout.VISIBLE);
                } else {
                    OggettiAVideo.getInstance().getClock().setVisibility(LinearLayout.GONE);
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        OggettiAVideo.getInstance().getSwitchSoloSelezionati().setChecked(false);
        OggettiAVideo.getInstance().getSwitchSoloSelezionati().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setSoloSelezionati(isChecked);

                AdapterListenerArtisti customAdapter = new AdapterListenerArtisti(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        VariabiliGlobali.getInstance().getArtisti());
                OggettiAVideo.getInstance().getLstArtisti().setAdapter(customAdapter);
            }
        });

        OggettiAVideo.getInstance().getImgCercaPreferiti().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ricerca = OggettiAVideo.getInstance().getEdtRicercaArtisti().getText().toString().toUpperCase();
                VariabiliGlobali.getInstance().setRicercaArtisti(ricerca);

                AdapterListenerArtisti customAdapter = new AdapterListenerArtisti(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        VariabiliGlobali.getInstance().getArtisti());
                OggettiAVideo.getInstance().getLstArtisti().setAdapter(customAdapter);
            }
        });

        boolean pref = VariabiliGlobali.getInstance().isRicercaPreferiti();
        OggettiAVideo.getInstance().getSwitchPreferiti().setChecked(pref);
        OggettiAVideo.getInstance().getSwitchPreferiti().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setRicercaPreferiti(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        boolean psd = VariabiliGlobali.getInstance().isBranoSuSD();
        OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setChecked(psd);
        OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setBranoSuSD(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });
        OggettiAVideo.getInstance().setSwitchPresenteSuDisco(OggettiAVideo.getInstance().getSwitchPresenteSuDisco());

        boolean random = VariabiliGlobali.getInstance().isRandom();
        OggettiAVideo.getInstance().getSwitchRandom().setChecked(random);
        OggettiAVideo.getInstance().getSwitchRandom().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setRandom(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        boolean scarica = VariabiliGlobali.getInstance().isScaricaBrano();
        OggettiAVideo.getInstance().getSwitchScaricaBrano().setChecked(scarica);
        OggettiAVideo.getInstance().getSwitchScaricaBrano().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setScaricaBrano(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        boolean info = VariabiliGlobali.getInstance().isVisualizzaInformazioni();
        OggettiAVideo.getInstance().getSwitchVisualizzaInfo().setChecked(info);
        if (info) {
            OggettiAVideo.getInstance().getTxtInformazioni().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getTxtInformazioni().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchVisualizzaInfo().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setVisualizzaInformazioni(isChecked);
                if (isChecked) {
                    OggettiAVideo.getInstance().getTxtInformazioni().setVisibility(LinearLayout.VISIBLE);
                } else {
                    OggettiAVideo.getInstance().getTxtInformazioni().setVisibility(LinearLayout.GONE);
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        // CAMBIO IMMAGINE
        boolean CambioImmagine = VariabiliGlobali.getInstance().isCambioImmagine();
        OggettiAVideo.getInstance().getSwitchCambioImmagine().setChecked(CambioImmagine);
        if (CambioImmagine) {
            OggettiAVideo.getInstance().getLayCambioImmagine().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getLayCambioImmagine().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchCambioImmagine().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setCambioImmagine(isChecked);
                if (isChecked) {
                    OggettiAVideo.getInstance().getLayCambioImmagine().setVisibility(LinearLayout.VISIBLE);
                } else {
                    OggettiAVideo.getInstance().getLayCambioImmagine().setVisibility(LinearLayout.GONE);
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        OggettiAVideo.getInstance().getEdtSecondi().setText(Integer.toString(VariabiliGlobali.getInstance().getSecondiCambioImmagine()));
        OggettiAVideo.getInstance().getEdtSecondi().addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                VariabiliGlobali.getInstance().setSecondiCambioImmagine(Integer.parseInt(OggettiAVideo.getInstance().getEdtSecondi().getText().toString()));

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        // CAMBIO IMMAGINE

        // OPACITA' BOTTONI
        boolean OpacitaBottoni = VariabiliGlobali.getInstance().isOpacitaBottoni();
        OggettiAVideo.getInstance().getSwitchOpacitaBottoni().setChecked(OpacitaBottoni);
        if (OpacitaBottoni) {
            OggettiAVideo.getInstance().getLayOpacitaBottoni().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getLayOpacitaBottoni().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchOpacitaBottoni().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setOpacitaBottoni(isChecked);
                if (isChecked) {
                    OggettiAVideo.getInstance().getLayOpacitaBottoni().setVisibility(LinearLayout.VISIBLE);
                } else {
                    OggettiAVideo.getInstance().getLayOpacitaBottoni().setVisibility(LinearLayout.GONE);
                }

                Utility.getInstance().AzionaTimerOpacitaBottoniera();

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        OggettiAVideo.getInstance().getEdtOpacitaBottoni().setText(Integer.toString(VariabiliGlobali.getInstance().getSecondiOpacitaBottoni()));
        OggettiAVideo.getInstance().getEdtOpacitaBottoni().addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                VariabiliGlobali.getInstance().setSecondiOpacitaBottoni(Integer.parseInt(OggettiAVideo.getInstance().getEdtOpacitaBottoni().getText().toString()));

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        // OPACITA' BOTTONI

        // STELLE
        boolean stelle = VariabiliGlobali.getInstance().isRicercaStelle();
        OggettiAVideo.getInstance().getSwitchStelle().setChecked(stelle);
        if (stelle) {
            OggettiAVideo.getInstance().getLayStelle().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getLayStelle().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchStelle().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setRicercaStelle(isChecked);
                if (isChecked) {
                    OggettiAVideo.getInstance().getLayStelle().setVisibility(LinearLayout.VISIBLE);
                } else {
                    OggettiAVideo.getInstance().getLayStelle().setVisibility(LinearLayout.GONE);
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        OggettiAVideo.getInstance().getEdtStelle().setText(Integer.toString(VariabiliGlobali.getInstance().getStelleDaRicercare()));
        OggettiAVideo.getInstance().getEdtStelle().addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if (!OggettiAVideo.getInstance().getEdtStelle().getText().toString().isEmpty()) {
                    VariabiliGlobali.getInstance().setStelleDaRicercare(Integer.parseInt(OggettiAVideo.getInstance().getEdtStelle().getText().toString()));

                    db_dati db = new db_dati();
                    db.ScriveImpostazioni();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        // STELLE

        // Ricerca Testo
        boolean RicercaTesto = VariabiliGlobali.getInstance().isRicercaTesto();
        OggettiAVideo.getInstance().getSwitchRicercaTesto().setChecked(RicercaTesto);
        if (RicercaTesto) {
            OggettiAVideo.getInstance().getLayRicercaTesto().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getLayRicercaTesto().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchRicercaTesto().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setRicercaTesto(isChecked);
                if (isChecked) {
                    OggettiAVideo.getInstance().getLayRicercaTesto().setVisibility(LinearLayout.VISIBLE);
                } else {
                    OggettiAVideo.getInstance().getLayRicercaTesto().setVisibility(LinearLayout.GONE);
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        OggettiAVideo.getInstance().getEdtTesto().setText(VariabiliGlobali.getInstance().getTestoDaRicercare());
        OggettiAVideo.getInstance().getEdtTesto().addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                VariabiliGlobali.getInstance().setTestoDaRicercare(OggettiAVideo.getInstance().getEdtTesto().getText().toString());

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        // CAMBIO IMMAGINE

        // TAGS
        boolean tags = VariabiliGlobali.getInstance().isRicercaTags();
        OggettiAVideo.getInstance().getSwitchTags().setChecked(tags);
        OggettiAVideo.getInstance().getSwitchTags().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setRicercaTags(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        OggettiAVideo.getInstance().getBtnListaTags().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getLayListaTags().setVisibility(LinearLayout.VISIBLE);
                OggettiAVideo.getInstance().getImgLinguetta1().setVisibility(LinearLayout.GONE);
            }
        });

        OggettiAVideo.getInstance().getSwitchSoloSelezionatiTags().setChecked(false);
        OggettiAVideo.getInstance().getSwitchSoloSelezionatiTags().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setSoloSelezionatiTags(isChecked);

                AdapterListenerTags customAdapterTags = new AdapterListenerTags(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        VariabiliGlobali.getInstance().getListaTags());
                OggettiAVideo.getInstance().getLstTags().setAdapter(customAdapterTags);
            }
        });

        // ImageView imgChiudeListaTags = (ImageView) findViewById(R.id.imgChiudeListaTags);

        /* imgChiudeListaTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgLinguetta1.setVisibility(LinearLayout.VISIBLE);
                layListaTags.setVisibility(LinearLayout.GONE);
            }
        }); */

        OggettiAVideo.getInstance().getImgChiudeESalvaListaTags().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getImgLinguetta1().setVisibility(LinearLayout.VISIBLE);
                OggettiAVideo.getInstance().getLayListaTags().setVisibility(LinearLayout.GONE);

                db_dati db = new db_dati();
                db.ScriveTags();

                // FiltraBrani f = new FiltraBrani();
                // f.FiltraLista(true);
            }
        });
        // TAGS

        boolean eliminaBrani = VariabiliGlobali.getInstance().isEliminaBrani();
        OggettiAVideo.getInstance().getSwitchEliminaBrani().setChecked(eliminaBrani);
        if (eliminaBrani) {
            OggettiAVideo.getInstance().getLayElimina().setVisibility(LinearLayout.VISIBLE);
        } else {
            OggettiAVideo.getInstance().getLayElimina().setVisibility(LinearLayout.GONE);
        }
        OggettiAVideo.getInstance().getSwitchEliminaBrani().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setEliminaBrani(isChecked);
                if (isChecked) {
                    OggettiAVideo.getInstance().getLayElimina().setVisibility(LinearLayout.VISIBLE);
                } else {
                    OggettiAVideo.getInstance().getLayElimina().setVisibility(LinearLayout.GONE);
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });
        OggettiAVideo.getInstance().getEdtLimiteMB().setText(Integer.toString(VariabiliGlobali.getInstance().getLimiteInMb()));
        OggettiAVideo.getInstance().getEdtLimiteMB().addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                VariabiliGlobali.getInstance().setLimiteInMb(Integer.parseInt(OggettiAVideo.getInstance().getEdtLimiteMB().getText().toString()));

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        /* ImageView imgSalvaRicerche = (ImageView) findViewById(R.id.imgSalvaRicerche);
        imgSalvaRicerche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati db = new db_dati();
                db.ScriveImpostazioni();

                // FiltraBrani f = new FiltraBrani();
                // f.FiltraLista(true);

                layRicerche.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgSalvaSettaggi = (ImageView) findViewById(R.id.imgSalvaSettaggi);
        imgSalvaSettaggi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati db = new db_dati();
                db.ScriveImpostazioni();
                laySettaggi.setVisibility(LinearLayout.GONE);
            }
        }); */

        OggettiAVideo.getInstance().getImgAnnullaRicerche().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getLayRicerche().setVisibility(LinearLayout.GONE);
            }
        });

        OggettiAVideo.getInstance().getImgAnnullaSettaggi().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OggettiAVideo.getInstance().getLaySettaggi().setVisibility(LinearLayout.GONE);
            }
        });

        VariabiliGlobali.getInstance().ContaPreferiti();
        VariabiliGlobali.getInstance().ContaTags();

        VariabiliGlobali.getInstance().setePartito(true);
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

    private void InstanziaNotifica() {
        Log.getInstance().ScriveLog("Instanzia notifica");

        Notifica.getInstance().setContext(VariabiliGlobali.getInstance().getContext());
        Notifica.getInstance().setIcona(R.drawable.logo);
        Notifica.getInstance().setTitolo("");
        Notifica.getInstance().setImmagine("");

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

    private void AzionaCuffie() {
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mReceiverComponent = new ComponentName(this, GestioneTastoCuffie.class);
        mAudioManager.registerMediaButtonEventReceiver(mReceiverComponent);

        /* if (VariabiliGlobali.getInstance().getMyReceiverCuffie() != null) {
            try {
                unregisterReceiver(VariabiliGlobali.getInstance().getMyReceiverCuffie());
            } catch (Exception ignored) {

            }
            VariabiliGlobali.getInstance().setMyReceiverCuffie(null);
        }
        VariabiliGlobali.getInstance().setMyReceiverCuffie(new GestoreCuffie());
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(VariabiliGlobali.getInstance().getMyReceiverCuffie(), filter); */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(myReceiver);

        // Toast.makeText(this, "Servizio LooPlayer fermato.", Toast.LENGTH_LONG).show();
    }
}
