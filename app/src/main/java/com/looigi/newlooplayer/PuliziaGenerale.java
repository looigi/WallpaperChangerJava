package com.looigi.newlooplayer;

import android.os.Handler;
import android.os.Looper;

import com.looigi.newlooplayer.WebServices.ChiamateWs;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.download.DownloadBrano;
import com.looigi.newlooplayer.download.DownloadFileTesto;
import com.looigi.newlooplayer.strutture.StrutturaBrano;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PuliziaGenerale {
    private Runnable runTimerAttesaPulizia;
    private Handler handlerAttesaPulizia;

    public void PuliziaCartelleInutili() {
        String Path = VariabiliGlobali.getInstance().getPercorsoDIR();

        // Eliminazione schifezze che si creano da sole tipo la cartella HTTP:
        LogPulizia.getInstance().ScriveLog("Pulizia eventuale cartella: " + Path + "/http");
        File dir = new File(Path + "/http:");
        if (dir.exists()) {
            String deleteCmd = "rm -r " + Path + "/http:";
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
                LogPulizia.getInstance().ScriveLog("Pulizia eventuale cartella: " + Path + "/http: OK");
            } catch (IOException e) {
                LogPulizia.getInstance().ScriveLog("Pulizia eventuale cartella: " + Path + "/http: Non esistente");
            }
        }
    }

    public void PulisceTemporanei() {
        LogPulizia.getInstance().ScriveLog("");
        LogPulizia.getInstance().ScriveLog("Pulizia files temporanei e di lavoro");
        File rootPrincipale = new File(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Versioni");
        if (!rootPrincipale.exists()) {
            rootPrincipale.mkdir();
        }
        File[] list = rootPrincipale.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
            } else {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                if (Filetto.toUpperCase().contains(".TXT") || Filetto.toUpperCase().contains(".MP3") || Filetto.toUpperCase().contains(".WMA")) {
                    LogPulizia.getInstance().ScriveLog("Eliminazione file temporaneo: " + Filetto);
                    Utility.getInstance().EliminaFileUnico(Filetto);
                }
            }
        }
    }

    public void PulizieProfonde() {
        db_dati db = new db_dati();

        LogPulizia.getInstance().ScriveLog("");
        LogPulizia.getInstance().ScriveLog("Eliminazione brani in locale che non esistono più in rete (brani brutti)");
        // Eliminazione brani in locale che non esistono più in rete
        db.RitornaTuttiIBrani();
        if (VariabiliGlobali.getInstance().getListaBraniInLocale() != null && VariabiliGlobali.getInstance().getListaBraniInLocale().size() > 0) {
            for (int i = 0; i < VariabiliGlobali.getInstance().getListaBraniInLocale().size() - 1; i++) {
                String PathBrano = VariabiliGlobali.getInstance().getListaBraniInLocale().get(i).getPathBrano();
                if (!Utility.getInstance().EsisteFile(PathBrano)) {
                    LogPulizia.getInstance().ScriveLog("Elimino brano e informazioni in quanto inesistente: " + PathBrano);
                    if (!VariabiliGlobali.getInstance().isSimulazione()) {
                        EliminaBraniRilevati(VariabiliGlobali.getInstance().getListaBraniInLocale().get(i));
                    }
                } else {
                    int dimensione = Utility.getInstance().DimensioniFile(PathBrano);
                    if (dimensione < 1300) {
                        LogPulizia.getInstance().ScriveLog("Elimino brano e informazioni in quanto troppo piccolo: " + PathBrano + " -> " + Integer.toString(dimensione));
                        if (!VariabiliGlobali.getInstance().isSimulazione()) {
                            EliminaBraniRilevati(VariabiliGlobali.getInstance().getListaBraniInLocale().get(i));
                        }
                    } else {
                        String UrlBrano = VariabiliGlobali.getInstance().getListaBraniInLocale().get(i).getUrlBrano();
                        String[] p = UrlBrano.split("/");
                        String Ultimo = p[p.length - 1];
                        UrlBrano = UrlBrano.replace(Ultimo, "");
                        UrlBrano = UrlBrano + VariabiliGlobali.getInstance().getListaBraniInLocale().get(i).getBrano() +
                                VariabiliGlobali.getInstance().getListaBraniInLocale().get(i).getEstensione();

                        EsisteUrl(UrlBrano, VariabiliGlobali.getInstance().getListaBraniInLocale().get(i));
                    }
                }
            }
        } else {
            LogPulizia.getInstance().ScriveLog("Eliminazione brani in locale che non esistono più in rete (brani brutti). Lista brani non caricata");
        }

        LogPulizia.getInstance().ScriveLog("ELIMINAZIONE CARTELLE VUOTE");
        EliminaCartelleVuote bckEliminaCartelleVuote = new EliminaCartelleVuote();
        bckEliminaCartelleVuote.execute();

        LogPulizia.getInstance().ScriveLog("COMPATTAZIONE DB");
        db.CompattaDB();

        if (VariabiliGlobali.getInstance().isSoloLocale()) {
            Utility.getInstance().VisualizzaErrore("Operazione effettuata");
        }
    }

    private void EsisteUrl(String urlString, StrutturaBrano s) {
        new Thread() {
            public void run() {
                try {
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
                    con.setRequestMethod("HEAD");
                    if ((con.getResponseCode() == HttpURLConnection.HTTP_OK)) {
                        // File rilevato
                    } else {
                        EliminaBraniRilevati(s);
                    }
                } catch (Exception e) {
                    EliminaBraniRilevati(s);
                }
            }
        }.start();
    }

    private void EliminaBraniRilevati(StrutturaBrano s) {
        db_dati db = new db_dati();

        // for (int i = 0; i < VariabiliGlobali.getInstance().getListaBraniDaEliminare().size() - 1; i++) {
        String Artista = s.getArtista();
        String Album = s.getAlbum();
        String Brano = s.getBrano();
        String PathBrano = s.getPathBrano();
        LogPulizia.getInstance().ScriveLog("Eliminazione brano in locale: " + PathBrano);

        if (!VariabiliGlobali.getInstance().isSimulazione()) {
            Utility.getInstance().EliminaFileUnico(PathBrano);
        }

        int idBrano = db.RicercaBrano(Artista, Album, Brano);
        if (idBrano > -1) {
            LogPulizia.getInstance().ScriveLog("Eliminazione brano dal db: id " + Integer.toString(idBrano));
            if (!VariabiliGlobali.getInstance().isSimulazione()) {
                db.EliminaBrano(Integer.toString(idBrano));
            }
        } else {
            LogPulizia.getInstance().ScriveLog("Eliminazione brani in locale: Brano sul db non rilevato");
        }

        String CartellaBranoTesti = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Testi/";
        CartellaBranoTesti += Artista + "/";
        CartellaBranoTesti += Album + "/";
        String BranoTxt = Brano.replace(s.getEstensione(), "");

        String Lista1 = CartellaBranoTesti + "/" + BranoTxt + ".txt";
        LogPulizia.getInstance().ScriveLog("Eliminazione brani in locale: Eliminazione testo 1: " + Lista1);
        if (!VariabiliGlobali.getInstance().isSimulazione()) {
            Utility.getInstance().EliminaFileUnico(Lista1);
        }

        String Lista2 = CartellaBranoTesti + "/" + BranoTxt + " 2.txt";
        LogPulizia.getInstance().ScriveLog("Eliminazione brani in locale: Eliminazione testo 2: " + Lista2);
        if (!VariabiliGlobali.getInstance().isSimulazione()) {
            Utility.getInstance().EliminaFileUnico(Lista2);
        }

        String ListaTag = CartellaBranoTesti + "/" + BranoTxt + ".TAGS.txt";
        LogPulizia.getInstance().ScriveLog("Eliminazione brani in locale: Eliminazione testo Tags: " + ListaTag);
        if (!VariabiliGlobali.getInstance().isSimulazione()) {
            Utility.getInstance().EliminaFileUnico(ListaTag);
        }

        String ListaData = CartellaBranoTesti + "/" + BranoTxt + ".DATA.txt";
        LogPulizia.getInstance().ScriveLog("Eliminazione brani in locale: Eliminazione testo Data: " + ListaData);
        if (!VariabiliGlobali.getInstance().isSimulazione()) {
            Utility.getInstance().EliminaFileUnico(ListaData);
        }
        // }
    }

    boolean daInizio = false;

    public void AttesaFinePulizia(boolean DaInizio) {
        daInizio = DaInizio;

        if (handlerAttesaPulizia != null) {
            handlerAttesaPulizia.removeCallbacks(runTimerAttesaPulizia);
            handlerAttesaPulizia = null;
        }

        handlerAttesaPulizia = new Handler(Looper.getMainLooper());
        handlerAttesaPulizia.postDelayed(runTimerAttesaPulizia = new Runnable() {
            @Override
            public void run() {
                if (VariabiliGlobali.getInstance().isEliminaBrani()) {
                    int Limite = VariabiliGlobali.getInstance().getLimiteInMb() * 1024;
                    int occupato = VariabiliGlobali.getInstance().getSpazioOccupatoSuDisco();
                    if (occupato > Limite) {

                        EliminaBraniDaDisco bckElimina = new EliminaBraniDaDisco(false);
                        bckElimina.execute();
                    }
                }

                ChiamateWs c = new ChiamateWs();
                c.AttendePuliziaCompleta(PuliziaGenerale.this);

                handlerAttesaPulizia.postDelayed(this, 10000);
            }
        }, 10000);
    }

    public void TerminePulizia(String result) {
        if (result.equals("SI")) {
            LogPulizia.getInstance().ScriveLog("Pulizia remota completata");
            if (handlerAttesaPulizia != null) {
                handlerAttesaPulizia.removeCallbacks(runTimerAttesaPulizia);
                runTimerAttesaPulizia = null;
            }

            String path = VariabiliGlobali.getInstance().getUrlWS() + "/Logs/PULIZIE/PulizieDiPasqua.txt";

            Log.getInstance().ScriveLog("Scarico il file di testo del log della pulizia in locale: " + path);
            new DownloadFileTesto(path).execute(path);
        } else {
            if (daInizio) {
                if (handlerAttesaPulizia != null) {
                    handlerAttesaPulizia.removeCallbacks(runTimerAttesaPulizia);
                    runTimerAttesaPulizia = null;
                }
                LogPulizia.getInstance().ScriveLog("Nessuna attesa termine pulizia remota...");
            } else {
                LogPulizia.getInstance().ScriveLog("Attesa termine pulizia remota...");
            }
        }
    }
}
