package com.looigi.newlooplayer;

import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.strutture.StrutturaBrano;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PuliziaGenerale {
    public void Pulizia() {
        String Path = VariabiliGlobali.getInstance().getPercorsoDIR();

        // Eliminazione schifezze che si creano da sole tipo la cartella HTTP:
        Log.getInstance().ScriveLog("Pulizia eventuale cartella: " + Path + "/http");
        File dir = new File(Path + "/http:");
        if (dir.exists()) {
            String deleteCmd = "rm -r " + Path + "/http:";
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
                Log.getInstance().ScriveLog("Pulizia eventuale cartella: " + Path + "/http: OK");
            } catch (IOException e) {
                Log.getInstance().ScriveLog("Pulizia eventuale cartella: " + Path + "/http: Non esistente");
            }
        }
    }

    public void PulizieProfonde() {
        db_dati db = new db_dati();

        Log.getInstance().ScriveLog("Eliminazione brani in locale che non esistono più in rete (brani brutti)");
        // Eliminazione brani in locale che non esistono più in rete
        db.RitornaTuttiIBrani();
        if (VariabiliGlobali.getInstance().getListaBraniInLocale() != null && VariabiliGlobali.getInstance().getListaBraniInLocale().size() > 0) {
            for (int i = 0; i < VariabiliGlobali.getInstance().getListaBraniInLocale().size() - 1; i++) {
                String PathBrano = VariabiliGlobali.getInstance().getListaBraniInLocale().get(i).getPathBrano();
                if (!Utility.getInstance().EsisteFile(PathBrano)) {
                    Log.getInstance().ScriveLog("Elimino brano e informazioni in quanto inesistente: " + PathBrano);
                    EliminaBraniRilevati(VariabiliGlobali.getInstance().getListaBraniInLocale().get(i));
                } else {
                    int dimensione = Utility.getInstance().DimensioniFile(PathBrano);
                    if (dimensione < 1300) {
                        Log.getInstance().ScriveLog("Elimino brano e informazioni in quanto troppo piccolo: " + PathBrano + " -> " + Integer.toString(dimensione));
                        EliminaBraniRilevati(VariabiliGlobali.getInstance().getListaBraniInLocale().get(i));
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
            Log.getInstance().ScriveLog("Eliminazione brani in locale che non esistono più in rete (brani brutti). Lista brani non caricata");
        }

        Log.getInstance().ScriveLog("Eliminazione cartelle vuote");
        EliminaCartelleVuote bckEliminaCartelleVuote = new EliminaCartelleVuote();
        bckEliminaCartelleVuote.execute();

        Log.getInstance().ScriveLog("Compattazione DB");
        db.CompattaDB();

        Utility.getInstance().VisualizzaErrore("Operazione effettuata");
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
            Log.getInstance().ScriveLog("Eliminazione brani in locale: " + PathBrano);

            Utility.getInstance().EliminaFileUnico(PathBrano);
            int idBrano = db.RicercaBrano(Artista, Album, Brano);
            if (idBrano > -1) {
                db.EliminaBrano(Integer.toString(idBrano));
            } else {
                Log.getInstance().ScriveLog("Eliminazione brani in locale: Brano sul db non rilevato");
            }

            String CartellaBranoTesti = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Testi/";
            CartellaBranoTesti += Artista + "/";
            CartellaBranoTesti += Album + "/";
            String BranoTxt = Brano.replace(s.getEstensione(), "");
            String Lista1 = CartellaBranoTesti + "/" + BranoTxt + ".txt";
            Log.getInstance().ScriveLog("Eliminazione brani in locale: Eliminazione testo 1: " + Lista1);
            Utility.getInstance().EliminaFileUnico(Lista1);
            String Lista2 = CartellaBranoTesti + "/" + BranoTxt + " 2.txt";
            Log.getInstance().ScriveLog("Eliminazione brani in locale: Eliminazione testo 2: " + Lista2);
            Utility.getInstance().EliminaFileUnico(Lista2);
            String ListaTag = CartellaBranoTesti + "/" + BranoTxt + ".TAGS.txt";
            Log.getInstance().ScriveLog("Eliminazione brani in locale: Eliminazione testo Tags: " + ListaTag);
            Utility.getInstance().EliminaFileUnico(ListaTag);
            String ListaData = CartellaBranoTesti + "/" + BranoTxt + ".DATA.txt";
            Log.getInstance().ScriveLog("Eliminazione brani in locale: Eliminazione testo Data: " + ListaData);
            Utility.getInstance().EliminaFileUnico(ListaData);
        // }
    }
}
