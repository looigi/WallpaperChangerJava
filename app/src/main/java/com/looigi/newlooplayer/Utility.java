package com.looigi.newlooplayer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.newlooplayer.WebServices.ChiamataWsBellezza;
import com.looigi.newlooplayer.WebServices.ChiamateWs;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.download.DownloadBrano;
import com.looigi.newlooplayer.download.DownloadImage;
import com.looigi.newlooplayer.notifiche.Notifica;
import com.looigi.newlooplayer.strutture.StrutturaBrano;
import com.looigi.newlooplayer.strutture.StrutturaImmagini;
import com.looigi.newlooplayer.strutture.StrutturaTags;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.Random;

public class Utility {
    private static final Utility ourInstance = new Utility();

    public static Utility getInstance() {
        return ourInstance;
    }

    private Utility() {
    }

    private Runnable runTimer;
    private Handler handlerTimer;
    private int SecondiPassati;
    private int SecondiPassatiCambioImmagine;
    private int secondiControlloRete;
    private int SecondiPerCaricamentoPregresso;
    // private ProgressDialog progressDialog;
    // private boolean ultimoStatoRete; // int ultimoStatoRete = -999;
    private Runnable runTimerOpacita;
    private Handler handlerTimerOpacita;

    public String PrendeErroreDaException(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return TransformError(errors.toString());
    }

    private String TransformError(String error) {
        String Return=error;

        if (Return.length()>250) {
            Return=Return.substring(0,247)+"...";
        }
        Return=Return.replace("\n"," ");

        return Return;
    }

    public Integer ScriveFile(String Path, String fileName, String CosaScrivere) {
        try {
            File newFolder = new File(Path);
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            try {
                File file = new File(Path, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }

                Writer out = new BufferedWriter(new FileWriter(file, true), 1024);
                out.write(CosaScrivere);
                out.close();

                return 0;
            } catch (Exception ex) {
                // System.out.println("ex: " + ex);
                return -1;
            }
        } catch (Exception e) {
            // System.out.println("e: " + e);
            return -2;
        }
    }

    public boolean EliminaFile(String Path, String fileName) {
        File file = new File(Path + "/" + fileName);
        return file.delete();
    }

    public boolean EliminaFileUnico(String fileName) {
        if (EsisteFile(fileName)) {
            File file = new File(fileName);
            return file.delete();
        } else {
            return false;
        }
    }

    public boolean EsisteFile(String fileName) {
        File file = new File(fileName);
        if(file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public String LeggeFile(String Path, String fileName) {
        File file = new File(Path + '/' + fileName);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            return "ERROR: " + e.getMessage();
        }

        return text.toString();
    }

    public int DimensioniFile(String sFile) {
        File file = new File(sFile);
        return Integer.parseInt(String.valueOf(file.length()/1024));
    }

    public Date DataFile(String sFile) {
        File file = new File(sFile);
        return new Date(file.lastModified());
    }

    public void CreaCartelle(String Path) {
        String[] Pezzetti = Path.split("/");
        String DaCreare = "";

        for (int i = 0; i < Pezzetti.length; i++) {
            if (!Pezzetti[i].isEmpty()) {
                DaCreare += "/" + Pezzetti[i];
                File newFolder = new File(DaCreare);
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
            }
        }
    }

    /*
    public void LeggeListaBrani() {
        String NomeFile = "jsonBrani.json";
        String Lista = LeggeFile(VariabiliGlobali.getInstance().getPercorsoDIR(), NomeFile);

        if (Lista.contains("ERROR: ")) {
            Log.getInstance().ScriveLog("Lettura lista brani. Errore: " + Lista);

            ChiamataWsListe c = new ChiamataWsListe();
            c.RitornaListaMP3();
        } else {
            Log.getInstance().ScriveLog("Lettura lista brani. OK");

            LettaListaBrani(Lista);
        }
    }

    public void LettaListaBrani(String Lista) {
        boolean Ok = true;

        try {
            VariabiliGlobali.getInstance().setListaBraniCompletaInJSON(new JSONArray(Lista));
        } catch (Exception e) {
            VariabiliGlobali.getInstance().setListaBraniCompletaInJSON(null);
            Ok = false;

            Log.getInstance().ScriveLog("Errore nel parsing della lista brani: " + e.getMessage());
        }
        if (Ok) {
            Log.getInstance().ScriveLog("Parsing della lista brani OK");

            Log.getInstance().ScriveLog("Creazione e Filtraggio lista");
            FiltraBrani f = new FiltraBrani();
            String RitornoFiltraggio = f.CreaLista();
        }
    }
    */

    public void CaricamentoBrano(String idBrano, boolean Pregresso) {
        // VariabiliGlobali.getInstance().setCambiatoBrano(true);
        OggettiAVideo.getInstance().getImgErroreBrano().setVisibility(LinearLayout.GONE);
        // OggettiAVideo.getInstance().getImgPlay().setVisibility(LinearLayout.VISIBLE);
        VariabiliGlobali.getInstance().setBloccaDownload(true);

        Log.getInstance().ScriveLog("Caricamento brano: " + idBrano + ". Modalità pregressa: " + Pregresso);
        String id = idBrano.isEmpty() ? VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato().toString() : idBrano;
        ChiamateWs c = new ChiamateWs();
        c.RitornaBranoDaID(idBrano, Pregresso);

        // OggettiAVideo.getInstance().ScriveInformazioni();
    }

    public String ConverteSecondiInTempo(int SecondiPassati) {
        int Secondi = SecondiPassati;
        int Minuti = 0;
        while (Secondi > 59) {
            Secondi -= 60;
            Minuti++;
        }
        String m = Integer.toString(Minuti);
        String s = Integer.toString(Secondi);
        if (m.length() == 1) { m = "0" + m; };
        if (s.length() == 1) { s = "0" + s; };

        return m + ":" + s;
    }

    public boolean DurataBrano(String Url) {
        Log.getInstance().ScriveLog("Acquisizione durata brano: " + Url);

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(Url);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int timeInmillisec = Integer.parseInt( time );
            int duration = timeInmillisec / 1000;

            VariabiliGlobali.getInstance().setDurataBranoInSecondi(duration);
            OggettiAVideo.getInstance().getSeekBar().setMax(VariabiliGlobali.getInstance().getDurataBranoInSecondi());
            OggettiAVideo.getInstance().getSeekBar().setProgress(0);
            OggettiAVideo.getInstance().getTxtInizio().setText("00:00");
            OggettiAVideo.getInstance().getTxtFine().setText(ConverteSecondiInTempo(VariabiliGlobali.getInstance().getDurataBranoInSecondi()));

            Log.getInstance().ScriveLog("Acquisita durata brano: " + Long.toString(duration));

            return true;
        } catch (Exception e) {
            Log.getInstance().ScriveLog("Errore acquisizione durata brano: " + e.getMessage());
            OggettiAVideo.getInstance().getSeekBar().setMax(0);
            OggettiAVideo.getInstance().getSeekBar().setProgress(0);

            VariabiliGlobali.getInstance().setDurataBranoInSecondi(-1);

            return false;
        }
    }

    public void EsegueBranoInStreaming() {
        String DaDove = "Url";
        String url = VariabiliGlobali.getInstance().getStrutturaDelBrano().getUrlBrano();

        if (Utility.getInstance().EsisteFile(VariabiliGlobali.getInstance().getStrutturaDelBrano().getPathBrano())) {
            DaDove = "SD";
            url = VariabiliGlobali.getInstance().getStrutturaDelBrano().getPathBrano();
        }

        Log.getInstance().ScriveLog("Carico il brano da " + DaDove + " ed eventualmente lo eseguo: " + url +
                " - Play: " + VariabiliGlobali.getInstance().isStaSuonando());

        if (VariabiliGlobali.getInstance().isStaSuonando()) {
            FermaTimer();
        }

        VariabiliGlobali.getInstance().getMediaPlayer().stop();
        VariabiliGlobali.getInstance().getMediaPlayer().reset();

        // VariabiliGlobali.getInstance().setMediaPlayer(new MediaPlayer());

        // try {
        Uri myUri = Uri.parse(url);
        Log.getInstance().ScriveLog("Brano da suonare: " + myUri);

        VariabiliGlobali.getInstance().getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            VariabiliGlobali.getInstance().getMediaPlayer().setDataSource(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(), myUri);
            VariabiliGlobali.getInstance().getMediaPlayer().prepare();
        } catch (IOException e) {
            Log.getInstance().ScriveLog("Errore nella preparazione del brano da url: " + e.getMessage());
        }
        /* } catch (Exception e) {
            Log.getInstance().ScriveLog("Errore nel caricamento del brano da url: " + e.getMessage());
        } */

            /* VariabiliGlobali.getInstance().getMediaPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                }
            }); */

            Log.getInstance().ScriveLog("Brano da suonare: Sta suonando: " + VariabiliGlobali.getInstance().isStaSuonando());
            if (VariabiliGlobali.getInstance().isStaSuonando()) {
                handlerTimer.removeCallbacks(runTimer);
                runTimer = null;
                VariabiliGlobali.getInstance().setFermaTimer(false);
                FaiPartireTimer();

                VariabiliGlobali.getInstance().getMediaPlayer().start();
            }

            VariabiliGlobali.getInstance().getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Termine Brano
                    Log.getInstance().ScriveLog("Terminato il brano. Skippo da Utility");
                    AvantiBrano();
                }
            });

            if (VariabiliGlobali.getInstance().isScaricaBrano()) {
                if (!EsisteFile(VariabiliGlobali.getInstance().getStrutturaDelBrano().getPathBrano())) {
                    // DOWNLOAD FILE
                    Log.getInstance().ScriveLog("Scarico il brano in locale");
                    new DownloadBrano(VariabiliGlobali.getInstance().getStrutturaDelBrano()).execute(VariabiliGlobali.getInstance().getStrutturaDelBrano().getUrlBrano());
                }
            }
    }

    public void ImpostaPosizioneBrano(int Posizione) {
        if (VariabiliGlobali.getInstance().getMediaPlayer() != null) {
            VariabiliGlobali.getInstance().getMediaPlayer().seekTo(Posizione * 1000);
            SecondiPassati = Posizione;
        }
    }

    public void FermaTimer() {
        VariabiliGlobali.getInstance().setFermaTimer(true);
    }

    public void FaiRipartireTimer() {
        VariabiliGlobali.getInstance().setFermaTimer(false);
    }

    public void FaiPartireTimer() {
        Log.getInstance().ScriveLog("Fatto Partire Timer");

        SecondiPassati = 0;
        SecondiPassatiCambioImmagine = 0;
        secondiControlloRete = 0;
        SecondiPerCaricamentoPregresso = 0;

        if (handlerTimer != null) {
            handlerTimer.removeCallbacks(runTimer);
            runTimer = null;
        }

        handlerTimer = new Handler(Looper.getMainLooper());
        handlerTimer.postDelayed(runTimer = new Runnable() {
            @Override
            public void run() {
                secondiControlloRete++;
                if (secondiControlloRete > 4) {
                    secondiControlloRete = 0;

                    int statoRete = ControllaRete(); //  InternetDetector.getInstance().isOnline();
                    /* if (statoRete != VariabiliGlobali.getInstance().getUltimoStatoRete()) {
                        VariabiliGlobali.getInstance().setUltimoStatoRete(statoRete);
                        VariabiliGlobali.getInstance().setRetePresente(statoRete);
                        if (statoRete) {
                            OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.GONE);
                            if (!VariabiliGlobali.getInstance().isBranosSuSDOriginale()) {
                                VariabiliGlobali.getInstance().setBranoSuSD(false);
                            }
                            // FiltraBrani f = new FiltraBrani();
                            // f.FiltraLista(true);
                        } else {
                            OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.VISIBLE);
                            VariabiliGlobali.getInstance().setBranoSuSD(true);
                            // FiltraBrani f = new FiltraBrani();
                            // f.FiltraLista(true);
                        }
                    } */
                }

                if (!VariabiliGlobali.getInstance().isFermaTimer()) {
                    // Gestisce tutto il timer asincrono durante il play
                    SecondiPassati++;
                    OggettiAVideo.getInstance().getTxtInizio().setText(ConverteSecondiInTempo(SecondiPassati));
                    if (SecondiPassati <= OggettiAVideo.getInstance().getSeekBar().getMax()) {
                        OggettiAVideo.getInstance().getSeekBar().setProgress(SecondiPassati);
                    }

                    if (SecondiPassati == 7) {
                        ChiamateWs ws = new ChiamateWs();
                        if (VariabiliGlobali.getInstance().getStrutturaDelBrano() != null) {
                            ws.AggiornaImmagini(VariabiliGlobali.getInstance().getStrutturaDelBrano().getArtista());
                        }
                    }

                    if (!VariabiliGlobali.getInstance().isStaLeggendoProssimoBrano()) {
                        SecondiPassatiCambioImmagine++;
                        if (SecondiPassatiCambioImmagine >= VariabiliGlobali.getInstance().getSecondiCambioImmagine()) {
                            SecondiPassatiCambioImmagine = 0;
                            CambioImmagine();
                        }
                    }

                    SecondiPerCaricamentoPregresso++;
                    if (SecondiPerCaricamentoPregresso > 27) {
                        if (!VariabiliGlobali.getInstance().isCaricatoIlPregresso()) {
                            VariabiliGlobali.getInstance().setCaricatoIlPregresso(true);
                            if (!VariabiliGlobali.getInstance().isBranoSuSD()) {
                                CaricamentoBrano("", true);
                            }
                        }
                    }
                }

                handlerTimer.postDelayed(this, 1000);
            }
        },1000);
    }

    int vecchiaQualitaRete = -999;

    public int ControllaRete() {
        int qualitaRete = -1;
        ConnectionQuality cq = new ConnectionQuality();
        String Connessione = cq.RitornaQualitaConnessione();
        if (!Connessione.isEmpty()) {
            if (Connessione.contains(";")) {
                String[] c = Connessione.split(";");
                qualitaRete = Integer.parseInt(c[0]);
                if (qualitaRete == -1) {
                    if (vecchiaQualitaRete != qualitaRete) {
                        Log.getInstance().ScriveLog("Rete Sconosciuta");
                    }
                } else {
                    String Tipologia = c[1];
                    VariabiliGlobali.getInstance().setTipologiaRete(Tipologia);
                    String Qualita = c[2];
                    VariabiliGlobali.getInstance().setUltimoStatoReteStringa(Qualita);
                    if (Tipologia.equals("WIFI")) {
                        // if (VariabiliGlobali.getInstance().getUltimoStatoRete() != -999) {
                        // qualitaRete = 99;
                        if (qualitaRete != VariabiliGlobali.getInstance().getUltimoStatoRete()) {
                            VariabiliGlobali.getInstance().setUltimoStatoRete(qualitaRete);
                            Log.getInstance().ScriveLog("Rete WIFI: " + qualitaRete);
                            // Log.getInstance().ScriveLog("Flag brani su SD Originale: " + VariabiliGlobali.getInstance().isBranosSuSDOriginale());
                            // Log.getInstance().ScriveLog("Flag brani su SD: " + VariabiliGlobali.getInstance().isBranoSuSD());
                            VariabiliGlobali.getInstance().setRetePresente(true);
                            OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.GONE);
                            OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setEnabled(true);
                            if (!VariabiliGlobali.getInstance().isBranosSuSDOriginale()) {
                                VariabiliGlobali.getInstance().setBranoSuSD(false);
                                OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setChecked(false);
                                // Log.getInstance().ScriveLog("Imposto Flag brani su SD: " + VariabiliGlobali.getInstance().isBranoSuSD());
                            }

                            OggettiAVideo.getInstance().ScriveInformazioni();
                        }
                        // } else {
                        //     VariabiliGlobali.getInstance().setUltimoStatoRete(5);
                        // }
                    } else {
                        // if (VariabiliGlobali.getInstance().getUltimoStatoRete() != -999) {
                        if (qualitaRete != VariabiliGlobali.getInstance().getUltimoStatoRete()) {
                            VariabiliGlobali.getInstance().setUltimoStatoRete(qualitaRete);
                            if (qualitaRete < 1) {
                                // RETE SCARSA
                                Log.getInstance().ScriveLog("Rete scarsa: " + qualitaRete);
                                // Log.getInstance().ScriveLog("Flag brani su SD Originale: " + VariabiliGlobali.getInstance().isBranosSuSDOriginale());
                                // Log.getInstance().ScriveLog("Flag brani su SD: " + VariabiliGlobali.getInstance().isBranoSuSD());
                                VariabiliGlobali.getInstance().setRetePresente(false);
                                OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.VISIBLE);
                                VariabiliGlobali.getInstance().setBranoSuSD(true);
                                OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setChecked(true);
                                OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setEnabled(false);

                                OggettiAVideo.getInstance().ScriveInformazioni();
                                // FiltraBrani f = new FiltraBrani();
                                // f.FiltraLista(true);
                            } else {
                                // RETE OK
                                Log.getInstance().ScriveLog("Rete OK: " + qualitaRete);
                                // Log.getInstance().ScriveLog("Flag brani su SD Originale: " + VariabiliGlobali.getInstance().isBranosSuSDOriginale());
                                // Log.getInstance().ScriveLog("Flag brani su SD: " + VariabiliGlobali.getInstance().isBranoSuSD());
                                VariabiliGlobali.getInstance().setRetePresente(true);
                                OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.GONE);
                                // if (!VariabiliGlobali.getInstance().isBranosSuSDOriginale()) {
                                VariabiliGlobali.getInstance().setBranoSuSD(false);
                                OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setChecked(false);
                                // Log.getInstance().ScriveLog("Imposto Flag brani su SD: " + VariabiliGlobali.getInstance().isBranoSuSD());
                                // }
                                OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setEnabled(true);

                                OggettiAVideo.getInstance().ScriveInformazioni();
                                // FiltraBrani f = new FiltraBrani();
                                // f.FiltraLista(true);
                            }
                            // Log.getInstance().ScriveLog("Qualità Rete Bassa: " + Integer.toString(qualitaRete));
                        }
                        // }
                    }
                }

                vecchiaQualitaRete = qualitaRete;
            }
        }

        return qualitaRete;
    }

    public void AzzeraSecondiCambioImmagine() {
        SecondiPassatiCambioImmagine = 0;
        SecondiPassati = 0;
        SecondiPerCaricamentoPregresso = 0;
    }

    public void premutoPlay(boolean Accende) {
        if (VariabiliGlobali.getInstance().getMediaPlayer() != null) {
            if (!Accende) {
                Log.getInstance().ScriveLog("Metto in pausa");
                VariabiliGlobali.getInstance().setStaSuonando(false);
                OggettiAVideo.getInstance().getImgPlay().setImageResource(R.drawable.icona_suona);
                VariabiliGlobali.getInstance().getMediaPlayer().pause();
                Utility.getInstance().FermaTimer();
                Notifica.getInstance().setStaSuonando(false);
            } else {
                Log.getInstance().ScriveLog("Faccio play");
                VariabiliGlobali.getInstance().setStaSuonando(true);
                OggettiAVideo.getInstance().getImgPlay().setImageResource(R.drawable.icona_pausa);
                VariabiliGlobali.getInstance().getMediaPlayer().start();
                Utility.getInstance().FaiRipartireTimer();
                Notifica.getInstance().setStaSuonando(true);

                VariabiliGlobali.getInstance().getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // Termine Brano
                        Log.getInstance().ScriveLog("Terminato il brano. Skippo da Main");
                        Utility.getInstance().AvantiBrano();
                    }
                });
            }

            Notifica.getInstance().AggiornaNotifica();
        } else {
            // Brano non caricato... Provvedere
        }
    }

    public void IndietroBrano() {
        VariabiliGlobali.getInstance().setImmaginiScaricate(0);
        VariabiliGlobali.getInstance().setCaricatoIlPregresso(false);
        VariabiliGlobali.getInstance().setBranoPregresso(null);
        VariabiliGlobali.getInstance().setStaLeggendoProssimoBrano(false);
        // OggettiAVideo.getInstance().getImgPregresso().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getTxtBranoPregresso().setText("");
        OggettiAVideo.getInstance().getLayPregresso().setVisibility(LinearLayout.GONE);

        Log.getInstance().ScriveLog("Indietro Brano");
        Log.getInstance().ScriveLog("Brani ascoltati: " + VariabiliGlobali.getInstance().getListaBraniAscoltati().size());
        if (VariabiliGlobali.getInstance().getListaBraniAscoltati().size() > 1) {
            int attuale = VariabiliGlobali.getInstance().getListaBraniAscoltati().get(VariabiliGlobali.getInstance().getListaBraniAscoltati().size() - 2);
            CaricamentoBrano(Integer.toString(attuale), false);
        }
    }

    public int GeneraNumeroRandom(int NumeroMassimo)  {
        if (NumeroMassimo > 0) {
            final int random = new Random().nextInt(NumeroMassimo);

            return random;
        } else {
            return -1;
        }
    }

    public void AvantiBranoInLocale() {
        db_dati db = new db_dati();
        StrutturaBrano sb = new StrutturaBrano();
        /* List<StrutturaBrano> ListaBrani = new ArrayList<>();
        for (int i = 0; i < VariabiliGlobali.getInstance().getBraniInLocale().size() - 1; i++) {
            boolean Ok = false;

            String Titolo = VariabiliGlobali.getInstance().getBraniInLocale().get(i).getBrano();
            String NomeAlbum = VariabiliGlobali.getInstance().getBraniInLocale().get(i).getAlbum();
            String NomeArtista = VariabiliGlobali.getInstance().getBraniInLocale().get(i).getArtista();
            int Stelle = VariabiliGlobali.getInstance().getStelleBrano();

            if (VariabiliGlobali.getInstance().isRicercaTesto()) {
                String RicercaTesto = VariabiliGlobali.getInstance().getTestoDaRicercare().toUpperCase().trim();
                if (Titolo.toUpperCase().contains(RicercaTesto) | NomeAlbum.toUpperCase().contains(RicercaTesto) & NomeArtista.toUpperCase().contains(RicercaTesto)) {
                    Ok = true;
                }
            } else {
                Ok = true;
            }

            if (Ok) {
                if (VariabiliGlobali.getInstance().isRicercaEsclusione()) {
                    String RicercaTesto = VariabiliGlobali.getInstance().getTestoDaEscludere().toUpperCase().trim();
                    if (Titolo.toUpperCase().contains(RicercaTesto) || NomeAlbum.toUpperCase().contains(RicercaTesto) || NomeArtista.toUpperCase().contains(RicercaTesto)) {
                        Ok = false;
                    }
                }
            }

            if (Ok) {
                if (VariabiliGlobali.getInstance().isRetePresente()) {
                    if (VariabiliGlobali.getInstance().isRicercaStelle()) {
                        if (Stelle < VariabiliGlobali.getInstance().getStelleDaRicercare()) {
                            Ok = false;
                        }
                    }
                }
            }

            if (Ok) {
                if (VariabiliGlobali.getInstance().isRicercaMaiAscoltata()) {
                    if (Stelle != 0) {
                        Ok = false;
                    }
                }
            }

            if (Ok) {
                ListaBrani.add(VariabiliGlobali.getInstance().getBraniInLocale().get(i));
            }
        } */

        if (VariabiliGlobali.getInstance().isRandom()) {
            int quantiBrani = db.ContaBraniFiltrati(); // VariabiliGlobali.getInstance().getQuantiBraniInLocale(); // ListaBrani.size();
            int random = GeneraNumeroRandom(quantiBrani);
            Log.getInstance().ScriveLog("Avanti brano in locale. Random: " + Integer.toString(random) + "/" + Integer.toString(quantiBrani));
            try {
                int idBrano = db.CercaBrano(random); //  VariabiliGlobali.getInstance().getBraniInLocale().get(random);
                sb = db.RitornaBrano(Integer.toString(idBrano));
            } catch (Exception e) {
                sb = null;
            }
        } else {
            Log.getInstance().ScriveLog("Avanti brano in locale. Progressivo: " + Integer.toString(1));
        }

        if (sb != null) {
            Log.getInstance().ScriveLog("Avanti brano in locale. ID: " + sb.getIdBrano());
            Log.getInstance().ScriveLog("Avanti brano in locale. Brano: " + sb.getBrano());
            Log.getInstance().ScriveLog("Avanti brano in locale. Artista: " + sb.getArtista());
            Log.getInstance().ScriveLog("Avanti brano in locale. Album: " + sb.getAlbum());
            Log.getInstance().ScriveLog("Avanti brano in locale. Path: " + sb.getPathBrano());
            Log.getInstance().ScriveLog("Avanti brano in locale. Numero Immagini: " + sb.getImmagini().size());
            VariabiliGlobali.getInstance().setStrutturaDelBrano(sb);

            String Path = sb.getPathBrano();
            boolean durata = Utility.getInstance().DurataBrano(Path);

            String ImmagineDaImpostare = "";
            StrutturaImmagini StruttImmDaImpostare = new StrutturaImmagini();
            if (sb.getImmagini().size() > 0) {
                ImmagineDaImpostare = sb.getImmagini().get(0).getNomeImmagine();
                StruttImmDaImpostare = sb.getImmagini().get(0);
            }
            Log.getInstance().ScriveLog("Avanti brano in locale. Immagine da impostare: " + ImmagineDaImpostare);
            VariabiliGlobali.getInstance().setImmagineAttuale(ImmagineDaImpostare);

            Bitmap bitmap = BitmapFactory.decodeFile(StruttImmDaImpostare.getPathImmagine());
            OggettiAVideo.getInstance().getImgSfondo().setImageBitmap(bitmap);

            Utility.getInstance().AzzeraSecondiCambioImmagine();
            Utility.getInstance().EsegueBranoInStreaming();
            OggettiAVideo.getInstance().ScriveInformazioni();

            VariabiliGlobali.getInstance().setFermaTimer(true);
            Utility.getInstance().FaiPartireTimer();

            VariabiliGlobali.getInstance().setStaLeggendoProssimoBrano(false);

            ImpostaDatiBranoSuccessivo(sb, false);
        }
    }

    public void AvantiBrano() {
        VariabiliGlobali.getInstance().setImmaginiScaricate(0);

        if (VariabiliGlobali.getInstance().isCaricatoIlPregresso() & VariabiliGlobali.getInstance().getBranoPregresso() != null) {
            ImpostaDatiBranoSuccessivo(VariabiliGlobali.getInstance().getBranoPregresso(), true);
            return;
        }

        VariabiliGlobali.getInstance().setCaricatoIlPregresso(false);
        VariabiliGlobali.getInstance().setBranoPregresso(null);
        VariabiliGlobali.getInstance().setStaLeggendoProssimoBrano(false);
        // OggettiAVideo.getInstance().getImgPregresso().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getTxtBranoPregresso().setText("");
        OggettiAVideo.getInstance().getLayPregresso().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getImgErroreBrano().setVisibility(LinearLayout.GONE);

        // int NumeroBrani = VariabiliGlobali.getInstance().getListaBraniFiltrata().size();
        if (VariabiliGlobali.getInstance().isBranoSuSD()) {
            AvantiBranoInLocale();
        } else {
            int Attuale;
            if (VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato() != null) {
                Attuale = VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato();
            } else {
                Attuale = 1;
                VariabiliGlobali.getInstance().setIdUltimoBranoAscoltato(1);
            }

            Log.getInstance().ScriveLog("Avanti Brano.");
            Log.getInstance().ScriveLog("Random: " + VariabiliGlobali.getInstance().isRandom());
            Log.getInstance().ScriveLog("Ultimo Brano Ascoltato: " + Attuale);
        /* Log.getInstance().ScriveLog("Numero Brani in lista: " + NumeroBrani);

        DettaglioBrano dbr = new DettaglioBrano();

        if (VariabiliGlobali.getInstance().isRandom()) {
            int NumeroRandom = GeneraNumeroRandom(NumeroBrani);
            if (NumeroRandom > -1) {
                dbr = VariabiliGlobali.getInstance().getListaBraniFiltrata().get(NumeroRandom);
                Attuale = dbr.getIdBrano();
            } else {
                Attuale = -1;
            }
        } else {
            Attuale += 1;
            if (Attuale > VariabiliGlobali.getInstance().getListaBraniFiltrata().size() - 1) {
                Attuale = 0;
            }
            dbr = VariabiliGlobali.getInstance().getListaBraniFiltrata().get(Attuale);
            Attuale = dbr.getIdBrano();
        }

        if (Attuale > -1) {
            VariabiliGlobali.getInstance().AggiungeBranoAdAscoltati(Attuale);
            VariabiliGlobali.getInstance().setIdUltimoBranoAscoltato(Attuale);

            db_dati db = new db_dati();
            db.ScriveUltimoBranoAscoltato();

            CaricamentoBrano(Integer.toString(Attuale));

            OggettiAVideo.getInstance().ScriveInformazioni(dbr);
        } else {
            OggettiAVideo.getInstance().getTxtInformazioni().setText("Nessun brano rilevato");
        } */

            CaricamentoBrano("", false);
        }
    }

    public void CambioImmagine() {
        if (VariabiliGlobali.getInstance().getStrutturaDelBrano() == null) {
            return;
        }
        Log.getInstance().ScriveLog("Cambio immagine");
        if (VariabiliGlobali.getInstance().isScreenOn()) {
            VariabiliGlobali.getInstance().setImmaginiScaricate(VariabiliGlobali.getInstance().getImmaginiScaricate() + 1);
            if (VariabiliGlobali.getInstance().getImmaginiScaricate() >= VariabiliGlobali.getInstance().getQuanteImmaginePerNuovoScarico()) {
                // Scarica nuova immagine
                VariabiliGlobali.getInstance().setImmaginiScaricate(0);
                ChiamateWs ws = new ChiamateWs();
                ws.ScaricaNuovaImmagine(VariabiliGlobali.getInstance().getStrutturaDelBrano().getArtista(),
                        VariabiliGlobali.getInstance().getStrutturaDelBrano().getAlbum(),
                        VariabiliGlobali.getInstance().getStrutturaDelBrano().getBrano());
            } else {
                int QuanteImmagini = VariabiliGlobali.getInstance().getStrutturaDelBrano().getImmagini().size();
                int NuovaImmagine = GeneraNumeroRandom(QuanteImmagini);
                Log.getInstance().ScriveLog("Cambio immagine: Immagini " + Integer.toString(QuanteImmagini));
                Log.getInstance().ScriveLog("Cambio immagine: Nuova Immagine " + Integer.toString(NuovaImmagine));
                if (NuovaImmagine > -1) {
                    StrutturaImmagini imm = VariabiliGlobali.getInstance().getStrutturaDelBrano().getImmagini().get(NuovaImmagine);
                    VariabiliGlobali.getInstance().setImmagineAttuale(imm.getUrlImmagine());
                    if (!EsisteFile(imm.getPathImmagine())) {
                        new DownloadImage(OggettiAVideo.getInstance().getImgSfondo(), imm.getUrlImmagine()).execute(VariabiliGlobali.getInstance().getImmagineAttuale());
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(imm.getPathImmagine());
                        OggettiAVideo.getInstance().getImgSfondo().setImageBitmap(bitmap);

                        Log.getInstance().ScriveLog("Immagine Impostata: " + imm.getPathImmagine());
                        Notifica.getInstance().setImmagine(imm.getPathImmagine());
                        Notifica.getInstance().AggiornaNotifica();
                    }
                }
            }
        }
    }

    public void SettaBellezza(int Stelle) {
        if (VariabiliGlobali.getInstance().isAmministratore()) {
            ChiamataWsBellezza c = new ChiamataWsBellezza();
            c.SettaStelle(Stelle);
        }
    }

    public void ChiudeDialog() {
        OggettiAVideo.getInstance().getImgRest().setVisibility(LinearLayout.GONE);
        /* try {
            progressDialog.dismiss();
        } catch (Exception ignored) {
        } */
    }

    public void ApriDialog(boolean ApriDialog, String tOperazione) {
        OggettiAVideo.getInstance().getImgRest().setVisibility(LinearLayout.VISIBLE);
        /* if (ApriDialog) {
            try {
                progressDialog = new ProgressDialog(VariabiliGlobali.getInstance().getContext());
                progressDialog.setMessage("Attendere Prego...\n\n" + tOperazione);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            } catch (Exception ignored) {

            }
        } */
    }

    public void ImpostaDatiBranoSuccessivo(StrutturaBrano s, boolean Pregresso) {
        OggettiAVideo.getInstance().getTxtTagsBrano().setText(s.getTags());
        Log.getInstance().ScriveLog("Tags brano: " + s.getTags());
        VariabiliGlobali.getInstance().setIdUltimoBranoAscoltato(s.getIdBrano());

        db_dati db = new db_dati();
        db.ScriveUltimoBranoAscoltato();

        VariabiliGlobali.getInstance().setBraniTotali(s.getQuantiBrani());

        String Path = s.getPathBrano();
        if (!EsisteFile(Path)) {
            Path = s.getUrlBrano();
        }
        boolean durata = Utility.getInstance().DurataBrano(Path);
        if (!durata) {
            Log.getInstance().ScriveLog("Impossibile rilevare la durata del brano: " + Path);
            // OggettiAVideo.getInstance().getImgPlay().setVisibility(LinearLayout.GONE);
            OggettiAVideo.getInstance().getImgErroreBrano().setVisibility(LinearLayout.VISIBLE);
        } else {
            // OggettiAVideo.getInstance().getImgPlay().setVisibility(LinearLayout.VISIBLE);
            OggettiAVideo.getInstance().getImgErroreBrano().setVisibility(LinearLayout.GONE);
        }
        VariabiliGlobali.getInstance().setAscoltato(s.getAscoltata());
        VariabiliGlobali.getInstance().setStelleBrano(s.getBellezza());

        // if (!VariabiliGlobali.getInstance().isBranoSuSD()) {
            // String CHIAVE = s.getArtista().toUpperCase().trim() + "-" + s.getAnno() + s.getAlbum().toUpperCase().trim() + "-" + s.getBrano().toUpperCase().trim();
        //     db.AggiungeBrano(s);
        // }

        Log.getInstance().ScriveLog("idBrano Da Suonare: " + s.getIdBrano());
        Log.getInstance().ScriveLog("Titolo Brano Da Suonare: " + s.getBrano());
        Log.getInstance().ScriveLog("Album Da Suonare: " + s.getAlbum());
        Log.getInstance().ScriveLog("Artista Da Suonare: " + s.getArtista());
        Log.getInstance().ScriveLog("Durata Da Suonare: " + Long.toString(VariabiliGlobali.getInstance().getDurataBranoInSecondi()));

        VariabiliGlobali.getInstance().setStrutturaDelBrano(s);

        StrutturaImmagini StruttImmDaImpostare = VariabiliGlobali.getInstance().getImmagineDaImpostarePregressa();
        if (StruttImmDaImpostare != null && StruttImmDaImpostare.getPathImmagine() != null) {
            if (!Utility.getInstance().EsisteFile(StruttImmDaImpostare.getPathImmagine())) {
                new DownloadImage(OggettiAVideo.getInstance().getImgSfondo(), StruttImmDaImpostare.getUrlImmagine()).execute(StruttImmDaImpostare.getUrlImmagine());
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(StruttImmDaImpostare.getPathImmagine());
                OggettiAVideo.getInstance().getImgSfondo().setImageBitmap(bitmap);
            }
        } else {
            OggettiAVideo.getInstance().getImgSfondo().setImageResource(R.drawable.logo);
        }

        VariabiliGlobali.getInstance().AggiungeBranoAdAscoltati(s.getIdBrano());
        VariabiliGlobali.getInstance().setIdUltimoBranoAscoltato(s.getIdBrano());

        db.ScriveUltimoBranoAscoltato();

        Utility.getInstance().EsegueBranoInStreaming();
        OggettiAVideo.getInstance().ScriveInformazioni();

        VariabiliGlobali.getInstance().setStaLeggendoProssimoBrano(false);
        Utility.getInstance().AzzeraSecondiCambioImmagine();
        // }

        VariabiliGlobali.getInstance().setCaricatoIlPregresso(false);
        VariabiliGlobali.getInstance().setStaLeggendoProssimoBrano(false);
        VariabiliGlobali.getInstance().setBranoPregresso(null);
        // OggettiAVideo.getInstance().getImgPregresso().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getTxtBranoPregresso().setText("");
        OggettiAVideo.getInstance().getLayPregresso().setVisibility(LinearLayout.GONE);

        Notifica.getInstance().setTitolo(s.getBrano());
        Notifica.getInstance().setArtista(s.getArtista());
        Notifica.getInstance().setAlbum(s.getAlbum());
        if (VariabiliGlobali.getInstance().getImmagineDaImpostarePregressa() != null) {
            Notifica.getInstance().setImmagine(VariabiliGlobali.getInstance().getImmagineDaImpostarePregressa().getPathImmagine());
        }
        Notifica.getInstance().setStaSuonando(VariabiliGlobali.getInstance().isStaSuonando());
        Notifica.getInstance().AggiornaNotifica();
    }

    public void CambiaBranoPregresso() {
        CaricamentoBrano("", true);
    }

    /* public void LettaLista(String lista) {
        if (lista != "") {
            String[] listona = lista.split("§");
            List<StrutturaListaBrani> ss = new ArrayList<>();
            for (int i = 0; i < listona.length; i++) {
                if (!listona[i].isEmpty()) {
                    String[] campi = listona[i].split(";");
                    Log.getInstance().ScriveLog(listona[i]);

                    try {
                        StrutturaListaBrani s = new StrutturaListaBrani();
                        s.setId(campi[0]);
                        s.setArtista(campi[1]);
                        s.setAnno(campi[2]);
                        s.setAlbum(campi[3]);
                        s.setTraccia(campi[4]);
                        s.setBrano(campi[5]);

                        ss.add(s);
                    } catch (Exception ignored) {

                    }
                }
            }
            VariabiliGlobali.getInstance().setListaBrani(ss);

            AlberoBrani a = new AlberoBrani();
            a.GeneraAlbero();
        }
    } */

    public void AzionaTimerOpacitaBottoniera() {
        if (VariabiliGlobali.getInstance().isOpacitaBottoni()) {
            if (handlerTimerOpacita != null) {
                handlerTimerOpacita.removeCallbacks(runTimerOpacita);
                runTimerOpacita = null;
            }

            handlerTimerOpacita = new Handler(Looper.getMainLooper());
            handlerTimerOpacita.postDelayed(runTimerOpacita = new Runnable() {
                @Override
                public void run() {
                    OggettiAVideo.getInstance().getLayContenitore().setAlpha((float) 0.15);
                }
            }, VariabiliGlobali.getInstance().getSecondiOpacitaBottoni() * 1000);
        }
    }

    public String ScriveSpazioOccupato() {
        float Spazio = VariabiliGlobali.getInstance().getSpazioOccupatoSuDisco();
        String cosa = "KB.";

        if (Spazio > 1024) {
            Spazio /= 1024;
            cosa = "Mb.";
        }
        if (Spazio > 1024) {
            Spazio /= 1024;
            cosa = "Gb.";
        }
        if (Spazio > 1024) {
            Spazio /= 1024;
            cosa = "Tb.";
        }
        float Spazio2 =  ((int)(Spazio * 100) / 100);

        return Float.toString(Spazio2) + " " + cosa;
    }

    /* public void LeggeBraniDaSd(String result) {
        String[] Globale = result.split("§");

        List<StrutturaBrano> listaBrani = new ArrayList<>();
        int dime = 0;
        for (int i = 0; i < Globale.length; i++) {
            String[] Parti = Globale[i].split("\\|");
                String[] Brano = Parti[0].split(";");
                if (!Brano[0].equals("\n")) {
                    String[] ImmaginiBrano = new String[0];
                    if (Parti.length > 1) {
                        ImmaginiBrano = Parti[1].split("£");
                    }

                    StrutturaBrano sb = new StrutturaBrano();
                    if (Brano[0].isEmpty()) {
                        sb.setIdBrano(-1);
                    } else {
                        sb.setIdBrano(Integer.parseInt(Brano[0]));
                    }
                    if (!Brano[1].equals("null")) {
                        sb.setQuantiBrani(Integer.parseInt(Brano[1]));
                    } else {
                        sb.setQuantiBrani(0);
                    }
                    sb.setArtista(Brano[2]);
                    sb.setAlbum(Brano[3]);
                    sb.setBrano(Brano[4]);
                    sb.setAnno(Brano[5]);
                    sb.setTraccia(Brano[6]);
                    sb.setEstensione(Brano[7]);
                    sb.setData(Brano[8]);
                    sb.setDimensione(Integer.parseInt(Brano[9]));
                    dime += Integer.parseInt(Brano[9]);
                    sb.setAscoltata(Integer.parseInt(Brano[10]));
                    sb.setBellezza(Integer.parseInt(Brano[11]));
                    sb.setTesto(Brano[12]);
                    sb.setTestoTradotto(Brano[13]);
                    sb.setUrlBrano(Brano[14]);
                    sb.setPathBrano(Brano[15]);
                    sb.setCartellaBrano(Brano[16]);
                    sb.setEsisteBranoSuDisco(Brano[17].equals("true") ? true : false);
                    if (Brano.length > 18) {
                        sb.setTags(Brano[18]);
                    }

                    List<StrutturaImmagini> listaImmagini = new ArrayList<>();
                    for (int k = 0; k < ImmaginiBrano.length; k++) {
                        String[] CampiImmagine = ImmaginiBrano[k].split(";");

                        StrutturaImmagini si = new StrutturaImmagini();
                        si.setAlbum(CampiImmagine[0]);
                        si.setNomeImmagine(CampiImmagine[1]);
                        si.setUrlImmagine(CampiImmagine[2]);
                        si.setPathImmagine(CampiImmagine[3]);
                        si.setCartellaImmagine(CampiImmagine[4]);

                        listaImmagini.add(si);
                    }

                    sb.setImmagini(listaImmagini);

                    listaBrani.add(sb);
                }
        }

        VariabiliGlobali.getInstance().setSpazioOccupatoSuDisco(dime);

        VariabiliGlobali.getInstance().setPresentiSuDisco(listaBrani.size());
        VariabiliGlobali.getInstance().setBraniInLocale(listaBrani);
        Log.getInstance().ScriveLog("Files presenti su disco: " + listaBrani.size());
        Log.getInstance().ScriveLog("Dimensione Files presenti su disco: " + Integer.toString(dime));

        OggettiAVideo.getInstance().ScriveInformazioni();
    } */

    public void ProsegueAvvio() {
        db_dati db = new db_dati();
        Log.getInstance().ScriveLog("Lettura db brani in locale");
        // String result = Utility.getInstance().LeggeFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaBraniInLocale.txt");
        // Utility.getInstance().LeggeBraniDaSd(result);
        int QuantiBrani = db.ContaBrani();
        VariabiliGlobali.getInstance().setPresentiSuDisco(QuantiBrani);
        Log.getInstance().ScriveLog("Brani in locale: " + QuantiBrani);

        if (VariabiliGlobali.getInstance().getUltimoStatoRete() < 2) {
            Log.getInstance().ScriveLog("Rete non presente, cerco il prossimo brano");
            Utility.getInstance().AvantiBrano();
        } else {
            if (VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato() != null) {
                Log.getInstance().ScriveLog("Caricamento ultimo brano: " + Integer.toString(VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato()));
                Utility.getInstance().CaricamentoBrano(Integer.toString(VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato()), false);
            } else {
                Log.getInstance().ScriveLog("Ultimo brano non impostato... Prendo il successivo");
                Utility.getInstance().AvantiBrano();
            }
        }
    }

    public List<StrutturaTags> CreaVettoreTags() {
        String Tags = VariabiliGlobali.getInstance().getStrutturaDelBrano().getTags();
        String[] ListaTags = Tags.split("-");

        List<StrutturaTags> lista = new ArrayList<>();
        for (int i = 0; i < VariabiliGlobali.getInstance().getListaTags().size(); i++) {
            boolean Ok = true;
            for (int k = 0; k < ListaTags.length; k++) {
                if (VariabiliGlobali.getInstance().getListaTags().get(i).getTag().equals(ListaTags[k])) {
                    Ok = false;
                    break;
                }
            }
            if (Ok) {
                lista.add(VariabiliGlobali.getInstance().getListaTags().get(i));
            }
        }

        Collections.sort(lista, new Comparator<StrutturaTags>(){
            public int compare(StrutturaTags obj1, StrutturaTags obj2) {
                // ## Ascending order
                return obj1.getTag().compareToIgnoreCase(obj2.getTag()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });

        return lista;
    }

    public void PulisceListe(boolean mostraDialog) {
        EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaArtisti.txt");
        EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaTags.txt");
        EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "BraniInLocale.txt");

        ChiamateWs ws = new ChiamateWs();
        ws.EliminaListaArtisti();

        if (mostraDialog) {
            AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("File di lista eliminati");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public String ControllaData(String DataOraPassata) {
        String dataora = DataOraPassata;
        // if (dataora.length() == 10) {
        if (!dataora.contains("/")) {
            // Controllo formale sulle barre
            return "";
        } else {
            String c[] = dataora.split("/");
            if (c.length < 3) {
                // Controllo sul numero delle barre
                return "";
            } else {
                try {
                    int giorno = Integer.parseInt(c[0]);
                    int mese = Integer.parseInt(c[1]);
                    String anno = c[2];

                    if (giorno < 1 || giorno > 31) {
                        // Controllo formale sul giorno
                        return "";
                    } else {
                        if (giorno > 30 && (mese == 2 || mese == 4 || mese == 6 || mese == 9 || mese == 11)) {
                            // Controllo sul giorno maggiore di 30 per i mesi di 30
                            return "";
                        } else {
                            if (mese < 1 || mese > 12) {
                                // Controllo formale sul mese
                                return "";
                            } else {
                                if (anno.length() != 4) {
                                    // Controllo lunghezza anno
                                    return "";
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

                                    return dataora;
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {
                    return "";
                }
            }
        }
    }

    public String getCurrentDate(DatePicker picker){
        StringBuilder builder=new StringBuilder();;
        builder.append(picker.getDayOfMonth()+"/");
        builder.append((picker.getMonth() + 1)+"/");//month is 0 based
        builder.append(picker.getYear());

        return builder.toString();
    }
}
