package com.looigi.newlooplayer.WebServices;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.looigi.newlooplayer.BuildConfig;
import com.looigi.newlooplayer.MainActivity;
import com.looigi.newlooplayer.R;
import com.looigi.newlooplayer.ServizioBackground;
import com.looigi.newlooplayer.adapters.AdapterListenerArtisti;
import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.adapters.AdapterListenerTags;
import com.looigi.newlooplayer.download.DownloadAPK;
import com.looigi.newlooplayer.download.DownloadBrano;
import com.looigi.newlooplayer.download.DownloadImage;
import com.looigi.newlooplayer.strutture.StrutturaArtisti;
import com.looigi.newlooplayer.strutture.StrutturaBrano;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.strutture.StrutturaImmagini;
import com.looigi.newlooplayer.strutture.StrutturaListaAlbum;
import com.looigi.newlooplayer.strutture.StrutturaListaBrani;
import com.looigi.newlooplayer.strutture.StrutturaTags;
import com.looigi.newlooplayer.treeview.AlberoBrani;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChiamateWs implements TaskDelegate {
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = "http://looigi.ddns.net:1021/";
    private final String ws = "wsMobile.asmx/";
    private final String ws2 = "wsLWP.asmx/";
    private final String NS="http://wsMobile2.org/";
    private final String SA="http://wsMobile2.org/";
    private final String NS2="http://csaricanuovai.org/";
    private final String SA2="http://csaricanuovai.org/";
    private String TipoOperazione = "";
    private boolean branoPregresso;

    /* public void RitornaListaBrani() {
        long sec = (System.currentTimeMillis() - VariabiliGlobali.getInstance().getLastTimeChiamata()) / 1000;
        if (sec < 5 &&
                VariabiliGlobali.getInstance().getLastTimeChiamata() >0) {
            return;
        }
        VariabiliGlobali.getInstance().setLastTimeChiamata(System.currentTimeMillis());

        Log.getInstance().ScriveLog("Ritorna lista brani");

        String NomeFile = VariabiliGlobali.getInstance().getPercorsoDIR() + "/listaBrani.txt";
        if (Utility.getInstance().EsisteFile(NomeFile)) {
            Log.getInstance().ScriveLog("Lista Esistente");
            String contenuto = Utility.getInstance().LeggeFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "listaBrani.txt");
            Utility.getInstance().LettaLista(contenuto);
        } else {
            String Urletto = "RitornaListaBraniMobile";

            TipoOperazione = "RitornaListaBraniMobile";
            Esegue(
                    RadiceWS + ws + Urletto,
                    TipoOperazione,
                    NS,
                    SA,
                    30000,
                    false);
        }
    } */

    public void RitornaVersioneApplicazione() {
        Log.getInstance().ScriveLog("Ritorna versione applicazione");

        String Urletto="RitornaVersioneApplicazione";

        TipoOperazione = "RitornaVersioneApplicazione";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                false);
    }

    public void RitornaListaAlbum(String Artista) {
        Log.getInstance().ScriveLog("Ritorna lista Album");

        String Urletto="RitornaListaAlbumArtista?Artista=" + Artista;

        TipoOperazione = "RitornaListaAlbumArtista";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false);
    }

    public void AggiornaTagsBrano() {
        Log.getInstance().ScriveLog("Aggiorna Tags brano");

        String idBrano = Integer.toString(VariabiliGlobali.getInstance().getStrutturaDelBrano().getIdBrano());
        String Tags = VariabiliGlobali.getInstance().getStrutturaDelBrano().getTags();
        String Urletto="AggiornaTagsBrano?IdBrano=" + idBrano + "&Tags=" + Tags;

        TipoOperazione = "AggiornaTagsBrano";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false);
    }

    public void RitornaListaBrani(String Artista, String Album) {
        Log.getInstance().ScriveLog("Ritorna lista brani per Artista " + Artista + " e album " + Album);

        String Urletto="RitornaListaBraniAlbumArtista?Artista=" + Artista + "&Album=" + Album;

        TipoOperazione = "RitornaListaBraniAlbumArtista";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false);
    }

    public void AggiornaTesto() {
        String id = VariabiliGlobali.getInstance().getStrutturaDelBrano().getIdBrano().toString();
        String Artista = VariabiliGlobali.getInstance().getStrutturaDelBrano().getArtista();
        String Album = VariabiliGlobali.getInstance().getStrutturaDelBrano().getAlbum();
        String Brano = VariabiliGlobali.getInstance().getStrutturaDelBrano().getBrano();
        Log.getInstance().ScriveLog("Scarica testo per id canzone " + id + " per Artista " + Artista + ", album " + Album + " e brano " + Brano);

        String Urletto="ScaricaTesto?idCanzone=" + id + "&Artista=" + Artista + "&Album=" + Album + "&Canzone=" + Brano;

        TipoOperazione = "ScaricaTesto";
        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                30000,
                false);
    }

    public void ScaricaNuovaImmagine(String Artista, String Album, String Brano) {
        Log.getInstance().ScriveLog("Scarica nuova immagine per Artista " + Artista + ", album " + Album + " e brano " + Brano);

        String Urletto="ScaricaNuovaImmagine?Artista=" + Artista + "&Album=" + Album + "&Canzone=" + Brano;

        TipoOperazione = "ScaricaNuovaImmagine";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false);
    }

    public void AggiornaImmagini(String Artista) {
        Log.getInstance().ScriveLog("Aggiornamento immagini per Artista " + Artista);

        String Urletto="AggiornaImmagini?Artista=" + Artista;

        TipoOperazione = "AggiornaImmagini";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false);
    }

    public void RitornaListaTags() {
        Log.getInstance().ScriveLog("Ritorna lista Tags");

        String Urletto="RitornaListaTags";

        TipoOperazione = "RitornaListaTags";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false);
    }

    public void RitornaListaArtisti() {
        Log.getInstance().ScriveLog("Ritorna lista Artisti");

        String Urletto="RitornaArtisti";

        TipoOperazione = "RitornaArtisti";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false);
    }

    public void EliminaListaArtisti() {
        Log.getInstance().ScriveLog("Elimina lista Artisti");

        String Urletto="EliminaListaArtisti";

        TipoOperazione = "EliminaListaArtisti";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false);
    }

    public void RitornaUltimoAggiornamento() {
        Log.getInstance().ScriveLog("Controlla Ultimo Aggiornamento");

        String Urletto="ControllaAggiornamento";

        TipoOperazione = "ControllaAggiornamento";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                false);
    }

    public void RitornaBranoDaID(String idBrano, boolean Pregresso) {
        branoPregresso = Pregresso;
        Log.getInstance().ScriveLog("Caricamento prossimo brano. Pregresso: " + Pregresso);
        VariabiliGlobali.getInstance().setStaLeggendoProssimoBrano(true);
        // OggettiAVideo.getInstance().getImgIndietro().setVisibility(LinearLayout.GONE);
        // OggettiAVideo.getInstance().getImgAvanti().setVisibility(LinearLayout.GONE);
        // OggettiAVideo.getInstance().getImgPlay().setVisibility(LinearLayout.GONE);
        // String idUtente, String Random, String idBranoAttuale, String Stelle, String RicercaTesto, String Tags, String Preferiti

        String Stelle = Integer.toString(VariabiliGlobali.getInstance().getStelleDaRicercare());
        if (!VariabiliGlobali.getInstance().isRicercaStelle()) {
            Stelle = "";
        }

        String Testo = VariabiliGlobali.getInstance().getTestoDaRicercare();
        if (!VariabiliGlobali.getInstance().isRicercaTesto()) {
            Testo = "";
        }

        String Preferiti = VariabiliGlobali.getInstance().getPreferiti();
        String PreferitiElimina = VariabiliGlobali.getInstance().getPreferitiElimina();
        if (!VariabiliGlobali.getInstance().isRicercaPreferiti()) {
            Preferiti = "";
            PreferitiElimina = "";
        }

        String Tags = VariabiliGlobali.getInstance().getPreferitiTags();
        String TagsElimina = VariabiliGlobali.getInstance().getPreferitiEliminaTags();
        if (!VariabiliGlobali.getInstance().isRicercaTags()) {
            Tags = "";
            TagsElimina = "";
        }

        String DataSuperiore = "";
        String DataInferiore = "";

        if (VariabiliGlobali.getInstance().isDate()) {
            if (VariabiliGlobali.getInstance().isDataSuperiore()) {
                if (!VariabiliGlobali.getInstance().getTxtDataSuperiore().isEmpty()) {
                    DataSuperiore = VariabiliGlobali.getInstance().getTxtDataSuperiore();
                }
            }

            if (VariabiliGlobali.getInstance().isDataInferiore()) {
                if (!VariabiliGlobali.getInstance().getTxtDataInferiore().isEmpty()) {
                    DataInferiore = VariabiliGlobali.getInstance().getTxtDataInferiore();
                }
            }
        }

        String Urletto="RitornaProssimoBranoMobile?";
        Urletto += "idUtente=" + VariabiliGlobali.getInstance().getIdUtente();
        Urletto += "&Random=" + (VariabiliGlobali.getInstance().isRandom() ? "S" : "N");
        Urletto += "&idBranoAttuale=" + VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato();
        Urletto += "&Stelle=" + Stelle;
        Urletto += "&RicercaTesto=" + Testo;
        Urletto += "&Tags=" + Tags;
        Urletto += "&Preferiti=" + Preferiti;
        Urletto += "&TagsElimina=" + TagsElimina;
        Urletto += "&PreferitiElimina=" + PreferitiElimina;
        Urletto += "&BranoEsatto=" + idBrano;
        Urletto += "&DataSuperiore=" + DataSuperiore;
        Urletto += "&DataInferiore=" + DataInferiore;

        TipoOperazione = "RitornaProssimoBranoMobile";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                20000,
                true);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        bckAsyncTask = new LetturaWSAsincrona(
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                TimeStampAttuale,
                this);
        bckAsyncTask.execute(Urletto);
    }

    @Override
    public void TaskCompletionResult(String result) {
        if (result.contains("ERROR:")) {
            Log.getInstance().ScriveLog("Ritorno WS " + TipoOperazione + ". ERRORE...");
        } else {
            Log.getInstance().ScriveLog("Ritorno WS " + TipoOperazione + ". OK");
        }

        switch (TipoOperazione) {
            case "ControllaAggiornamento":
                fControllaAggiornamento(result);
                break;
            case "RitornaArtisti":
                RitornaArtisti(result);
                break;
            case "EliminaListaArtisti":
                fEliminaListaArtisti(result);
                break;
            case "RitornaProssimoBranoMobile":
                CaricaBrano(result);
                break;
            case "AggiornaImmagini":
                fAggiornaImmagini(result);
                break;
            case "ScaricaTesto":
                fAggiornaTesto(result);
                break;
            case "RitornaListaTags":
                RitornaListaTags2(result);
                break;
            case "ScaricaNuovaImmagine":
                fScaricaNuovaImmagine(result);
                break;
            case "RitornaListaAlbumArtista":
                RitornaListaAlbumArtista(result);
                break;
            case "AggiornaTagsBrano":
                fAggiornaTagsBrano(result);
                break;
            case "RitornaListaBraniAlbumArtista":
                RitornaListaAlbumArtistaBrani(result);
                break;
            case "RitornaVersioneApplicazione":
                RitornaVersione(result);
                break;
        }
    }

    private void RitornaVersione(String result) {
        if (!result.contains("ERROR:")) {
            String versionName = BuildConfig.VERSION_NAME;
            Log.getInstance().ScriveLog("Versione attuale: " + versionName);
            String[] r = result.split(";");
            String versioneNuova = r[0];
            Log.getInstance().ScriveLog("Ritornata versione: " + versioneNuova);
            if (!versionName.equals(versioneNuova)) {
                String pathNuovo = r[1];
                Log.getInstance().ScriveLog("Ritornata versione path: " + pathNuovo);

                String PATH_TO_APK = RadiceWS + pathNuovo;
                String PATH_DEST = VariabiliGlobali.getInstance().getPercorsoDIR() + "/" + pathNuovo;
                Log.getInstance().ScriveLog("Aggiorno Versione: Effettuo download: " + PATH_TO_APK);
                Log.getInstance().ScriveLog("Aggiorno Versione: Destinazione: " + PATH_DEST);

                DownloadAPK d = new DownloadAPK(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Versioni", PATH_TO_APK, PATH_DEST);
                d.execute(PATH_TO_APK);
            } else {
                Log.getInstance().ScriveLog("Versione uguale");

                VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startService(new Intent(
                        VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        ServizioBackground.class));
            }
        } else {
            Log.getInstance().ScriveLog("Ritornato errore su ritorno versione: " + result);

            VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startService(new Intent(
                    VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                    ServizioBackground.class));
        }
    }

    private void fControllaAggiornamento(String result) {
        Log.getInstance().ScriveLog("Ultimo aggiornato ritornato: " + result);
        if (Utility.getInstance().EsisteFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/UltimoAggiornamento.txt")) {
            String ultimo = Utility.getInstance().LeggeFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "UltimoAggiornamento.txt");
            Log.getInstance().ScriveLog("Ultimo aggiornato effettuato: " + ultimo);
            if (!ultimo.contains(result)) {
                Log.getInstance().ScriveLog("Effettuo aggiornamento");
                Utility.getInstance().EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "UltimoAggiornamento.txt");
                Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "UltimoAggiornamento.txt", result);
                Utility.getInstance().PulisceListe(false);
                VariabiliGlobali.getInstance().setControllatoUpdate("AGGIORNARE");
            } else {
                VariabiliGlobali.getInstance().setControllatoUpdate("AGGIORNATO");
            }
        } else {
            if (result.contains("ERROR:")) {
                Log.getInstance().ScriveLog("Errore sul ritorno ultimo aggiornamento");
                VariabiliGlobali.getInstance().setControllatoUpdate("ERRORE");
            } else {
                Log.getInstance().ScriveLog("File di aggiornamento non esistente. Aggiorno tutto");
                Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "UltimoAggiornamento.txt", result);
                Utility.getInstance().PulisceListe(false);
                VariabiliGlobali.getInstance().setControllatoUpdate("AGGIORNARE");
            }
        }
    }

    private void fEliminaListaArtisti(String result) {
        Log.getInstance().ScriveLog("Eliminazione lista artisti eseguita: " + result);
    }

    private void fAggiornaTesto(String result) {
        String[] t = result.split("\\|");
        String testo = t[0];
        testo = testo.replace("§", "\n");
        testo = testo.replace("%20", " ");
        String testo2 = testo.substring(0, 30);

        Log.getInstance().ScriveLog("Aggiornamento testo brano eseguito: " + testo2);
        OggettiAVideo.getInstance().getTxtTesto().setText(testo);
    }

    private void fAggiornaTagsBrano(String result) {
        Log.getInstance().ScriveLog("Aggiornamento tags brano eseguito: " + result);
        if (result.contains("ERROR")) {
            AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {
            OggettiAVideo.getInstance().getLayTagsBrano().setVisibility(LinearLayout.GONE);
        }
    }

    private void fAggiornaImmagini(String result) {
        Log.getInstance().ScriveLog("Aggiornamento immagini eseguito: " + result);
    }

    private void fScaricaNuovaImmagine(String result) {
        Log.getInstance().ScriveLog("Nuova immagine ritornata: " + result);
        String immagine = result.replace("§",""); // "/var/www/html/CartelleCondivise/ImmaginiMusica/Niko Pandetta/ZZZ-ImmaginiArtista/3762__imgProfilo@__5f74996d753fa.jpg";
        immagine = immagine.replace("/var/www/html/CartelleCondivise", VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL());
        immagine = immagine.replace("ImmaginiMusicaImmaginiMusica", "ImmaginiMusica");

        String Album = VariabiliGlobali.getInstance().getStrutturaDelBrano().getAlbum();
        String Artista = VariabiliGlobali.getInstance().getStrutturaDelBrano().getArtista();
        StrutturaImmagini Imm = new StrutturaImmagini();
        Imm.setAlbum(Album);
        Imm.setNomeImmagine(immagine);
        String[] c = immagine.split("/");
        String NomeImmagine = c[c.length - 1];

        String UrlImmagine = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() +
                "/ImmaginiMusica/" + Artista + "/" + Album + "/" +
                NomeImmagine;
        Imm.setUrlImmagine(UrlImmagine);

        String PathImmagine = VariabiliGlobali.getInstance().getPercorsoDIR() +
                "/ImmaginiMusica/" + Artista + "/" + Album + "/" +
                NomeImmagine;
        Imm.setPathImmagine(PathImmagine);

        String CartellaImmagine = VariabiliGlobali.getInstance().getPercorsoDIR() +
                "/ImmaginiMusica/" + Artista + "/" + Album;
        Imm.setCartellaImmagine(CartellaImmagine);

        VariabiliGlobali.getInstance().getStrutturaDelBrano().getImmagini().add(Imm);

        new DownloadImage(OggettiAVideo.getInstance().getImgSfondo(), immagine).execute(immagine);
    }

    private void RitornaListaAlbumArtistaBrani(String result) {
        // Log.getInstance().ScriveLog("Result: " + result);
        if (!result.equals("anyType{}")) {
            List<StrutturaListaBrani> lista = new ArrayList<>();
            String[] Globale = result.split("§");
            for (int i = 0; i < Globale.length; i++) {
                if (!Globale[i].isEmpty()) {
                    String[] Campi = Globale[i].split(";");

                    StrutturaListaBrani s = new StrutturaListaBrani();
                    s.setArtista(Campi[0]);
                    String Traccia = Campi[2];
                    if (Traccia.isEmpty()) {
                        Traccia = "00";
                    } else {
                        if (Traccia.length() == 1) {
                            Traccia = "0" + Traccia;
                        }
                    }
                    s.setTraccia(Traccia);
                    s.setBrano(Traccia + "-" + Campi[3]);
                    s.setId(Campi[4]);
                    String Anno = Campi[5];
                    if (Anno.isEmpty()) {
                        Anno = "0000";
                    } else {
                        for (int k = Anno.length() + 1; k < 5; k++) {
                            Anno = "0" + Anno;
                        }
                    }
                    s.setAnno(Anno);
                    s.setAlbum(Anno + "-" + Campi[1]);

                    lista.add(s);
                }
            }

            VariabiliGlobali.getInstance().setListaBrani(lista);

            AlberoBrani a = new AlberoBrani();
            a.GeneraAlbero();
        }
    }

    private void RitornaListaAlbumArtista(String result) {
        if (!result.equals("anyType{}")) {
            List<StrutturaListaAlbum> lista = new ArrayList<>();
            String[] Globale = result.split("§");
            for (int i = 0; i < Globale.length; i++) {
                if (!Globale[i].isEmpty()) {
                    String[] Campi = Globale[i].split(";");

                    StrutturaListaAlbum s = new StrutturaListaAlbum();
                    s.setArtista(Campi[0]);
                    String Anno = Campi[1];
                    if (Anno.isEmpty()) {
                        Anno = "0000";
                    } else {
                        for (int k = Anno.length() + 1; k < 5; k++) {
                            Anno = "0" + Anno;
                        }
                    }
                    s.setAnno(Anno);
                    s.setAlbum(Campi[2]);

                    lista.add(s);
                }
            }

            VariabiliGlobali.getInstance().setListaAlbum(lista);
            Log.getInstance().ScriveLog("Album ritornati per artista: " + lista.size());

            AlberoBrani a = new AlberoBrani();
            a.GeneraAlbero();
        }
    }

    private void RitornaListaTags2(String result) {
        Utility.getInstance().EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaTags.txt");
        Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaTags.txt", result);

        RitornaListaTags22(result);
    }

    public void RitornaListaTags22(String result) {
        Log.getInstance().ScriveLog("Tags ritornati: " + result);

        if (result.contains("§")) {
            List<StrutturaTags> l = new ArrayList<>();
            String[] Selezionati = result.split("§");

            for (int i = 0; i < Selezionati.length; i++) {
                if (!Selezionati[i].isEmpty() && !Selezionati[i].equals("\n")) {
                    try {
                        String[] Campi = Selezionati[i].split(";");
                        StrutturaTags s = new StrutturaTags();
                        s.setId(Campi[0]);
                        s.setTag(Campi[1]);

                        l.add(s);
                    } catch (Exception ignored) {
                        Log.getInstance().ScriveLog("Errore nel caricamento dei tags: " + Utility.getInstance().PrendeErroreDaException(ignored));
                    }
                }
            }
            VariabiliGlobali.getInstance().setListaTags(l);

            AdapterListenerTags customAdapterTags = new AdapterListenerTags(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                    VariabiliGlobali.getInstance().getListaTags());
            OggettiAVideo.getInstance().getLstTags().setAdapter(customAdapterTags);

            OggettiAVideo.getInstance().getBtnListaTags().setVisibility(LinearLayout.VISIBLE);

            Log.getInstance().ScriveLog("Tags caricati: " + Integer.toString(l.size() - 1));
        }
    }

    /* private void RitornaListaBrani(String result) {
        Log.getInstance().ScriveLog("Ritorno WS " + TipoOperazione + ". OK");
        // Log.getInstance().ScriveLog(result);

        // Fine elaborazione. Scrivo il file su sd
        String NomeFile = "listaBrani.txt";
        Utility.getInstance().EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR(), NomeFile);
        int ritorno = Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR(), NomeFile, result);
        if (ritorno > 0) {
            // Errore nella scrittura del file
            Log.getInstance().ScriveLog("Errore nella scrittura del file su SD: " + NomeFile);
        } else {
            Log.getInstance().ScriveLog("Scrittura del file su SD eseguita");

            Utility.getInstance().LettaLista(result);
        }
    } */

    private void RitornaArtisti(String result) {
        Utility.getInstance().EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaArtisti.txt");
        Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaArtisti.txt", result);

        RitornaArtisti2(result);
    }

    public void RitornaArtisti2(String result) {
        String[] Globale = result.split("§");

        for (int i = 0; i < Globale.length; i++) {
            try {
                String[] dati = Globale[i].split("\\|");
                String Artista = dati[0];
                String Immagine = dati[1];
                // String UrlImmagine = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuSD() +
                //         "/ImmaginiMusica" + Immagine;
                List<String> listaTags = new ArrayList<>();
                if (!dati[2].isEmpty()) {
                    String[] Tags = dati[2].split("%");
                    for (int k = 0; k < Tags.length; k++) {
                        if (!Tags[k].isEmpty()) {
                            listaTags.add(Tags[k]);
                        }
                    }
                }

                StrutturaArtisti sa = new StrutturaArtisti();
                sa.setNomeArtista(Artista);
                sa.setTags(listaTags);
                sa.setImmagine(Immagine);

                VariabiliGlobali.getInstance().AggiungeArtista(sa);

                OggettiAVideo.getInstance().getBtnLista().setVisibility(LinearLayout.VISIBLE);
            } catch (Exception ignored) {

            }
        }

        AdapterListenerArtisti customAdapter = new AdapterListenerArtisti(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                VariabiliGlobali.getInstance().getArtisti());
        OggettiAVideo.getInstance().getLstArtisti().setAdapter(customAdapter);

        OggettiAVideo.getInstance().ScriveInformazioni();

        AlberoBrani a = new AlberoBrani();
        a.GeneraAlbero();
    }

    boolean ErroreCaricaBrano = false;

    private void CaricaBrano(String result) {
        // OggettiAVideo.getInstance().getImgIndietro().setVisibility(LinearLayout.VISIBLE);
        // OggettiAVideo.getInstance().getImgAvanti().setVisibility(LinearLayout.VISIBLE);
        // OggettiAVideo.getInstance().getImgPlay().setVisibility(LinearLayout.VISIBLE);
        OggettiAVideo.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.VISIBLE);

        // Log.getInstance().ScriveLog("Dati brano: " + result);
        if (result.contains("ERROR:")) {
            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION")) {
                VariabiliGlobali.getInstance().setRetePresente(false);
                OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.VISIBLE);
                VariabiliGlobali.getInstance().setBranoSuSD(true);
                OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setChecked(true);
                OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setEnabled(false);
                ErroreCaricaBrano = true;

                OggettiAVideo.getInstance().ScriveInformazioni();

                Log.getInstance().ScriveLog("Carica brano: Imposto rete non presente");
            }

            Log.getInstance().ScriveLog("Carica brano: Esco per result non valido");

            return;
        } else {
            if (ErroreCaricaBrano) {
                VariabiliGlobali.getInstance().setRetePresente(true);
                OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.GONE);
                OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setEnabled(true);
                if (!VariabiliGlobali.getInstance().isBranosSuSDOriginale()) {
                    VariabiliGlobali.getInstance().setBranoSuSD(false);
                    OggettiAVideo.getInstance().getSwitchPresenteSuDisco().setChecked(false);
                    // Log.getInstance().ScriveLog("Imposto Flag brani su SD: " + VariabiliGlobali.getInstance().isBranoSuSD());
                }
                ErroreCaricaBrano = false;

                OggettiAVideo.getInstance().ScriveInformazioni();

                Log.getInstance().ScriveLog("Carica brano: Imposto rete presente dopo averla disabilitata");
            }
        }

        String[] Globale = result.split("\\|");
        String[] DatiBrano = Globale[0].split(";");
        String[] Immagini = Globale[1].split("§");
        String[] TestoEAltro = Globale[2].split(";");
        if (Globale[2].equals(";;;;")) {
            TestoEAltro = new String[]{"", "", "", ""};
        }

        StrutturaBrano s = new StrutturaBrano();
        s.setIdBrano(Integer.parseInt(DatiBrano[0]));

        s.setQuantiBrani(Integer.parseInt(DatiBrano[1]));
        s.setArtista(DatiBrano[3]);

        s.setAlbum(DatiBrano[4]);

        s.setBrano(DatiBrano[5]);
        String Anno = DatiBrano[6];
        for (int i = Anno.length() + 1; i < 5; i++) {
            Anno = "0" + Anno;
        }
        s.setAnno(Anno);
        String Traccia = DatiBrano[7];
        if (Traccia.length() == 1) {
            Traccia = "0" + Traccia;
        }
        s.setTraccia(Traccia);
        s.setEstensione(DatiBrano[8]);
        s.setData(DatiBrano[9]);
        if (!DatiBrano[10].isEmpty()) {
            s.setDimensione(Integer.parseInt(DatiBrano[10]) / 1024);
        } else {
            s.setDimensione(0);
        }
        try {
            String t = DatiBrano[11].replace("*PV*", ";");
            String[] tt = t.split(";");
            String tags = "";
            for (int i = 0; i < tt.length; i++) {
                if (!tt[i].isEmpty()) {
                    tags += tt[i] + ";";
                }
            }
            if (tags.length() > 0) {
                tags = tags.substring(0, tags.length() -1);
            }
            s.setTags(tags);
        } catch (Exception ignored) {
            s.setTags("");
        }

        String UrlBrano = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() +
                "/MP3/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4] + "/" +
                Traccia + "-" + DatiBrano[5] + DatiBrano[8];
        s.setUrlBrano(UrlBrano);

        String CartellaBrano = VariabiliGlobali.getInstance().getPercorsoDIR() +
                "/Brani/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4];
        s.setCartellaBrano(CartellaBrano);

        String PathBrano = VariabiliGlobali.getInstance().getPercorsoDIR() +
                "/Brani/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4] + "/" +
                Traccia + "-" + DatiBrano[5] + DatiBrano[8];
        s.setPathBrano(PathBrano);

        String CartellaTesto = VariabiliGlobali.getInstance().getPercorsoDIR() +
                "/Testi/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4] + "/";

        if (Utility.getInstance().EsisteFile(PathBrano)) {
            s.setEsisteBranoSuDisco(true);
        } else {
            s.setEsisteBranoSuDisco(false);
        }

        // if (VariabiliGlobali.getInstance().isRetePresente()) {
        /* String Path = UrlBrano;
        if (s.isEsisteBranoSuDisco()) {
            Path = PathBrano;
        } */

        List<StrutturaImmagini> ListaImmagini;
        ListaImmagini = new ArrayList<>();
        String ImmagineDaImpostare = "";
        StrutturaImmagini StruttImmDaImpostare = new StrutturaImmagini();
        for (int i = 0; i < Immagini.length; i++) {
            if (!Immagini[i].isEmpty()) {
                String[] Imm2 = Immagini[i].split(";");
                StrutturaImmagini Imm = new StrutturaImmagini();
                Imm.setAlbum(Imm2[1]);
                Imm.setNomeImmagine(Imm2[2]);

                String UrlImmagine = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() +
                        "/ImmaginiMusica/" + DatiBrano[3] + "/" + Imm2[1] + "/" +
                        Imm2[2];
                Imm.setUrlImmagine(UrlImmagine);

                String PathImmagine = VariabiliGlobali.getInstance().getPercorsoDIR() +
                        "/ImmaginiMusica/" + DatiBrano[3] + "/" + Imm2[1] + "/" +
                        Imm2[2];
                Imm.setPathImmagine(PathImmagine);

                String CartellaImmagine = VariabiliGlobali.getInstance().getPercorsoDIR() +
                        "/ImmaginiMusica/" + DatiBrano[3] + "/" + Imm2[1];
                Imm.setCartellaImmagine(CartellaImmagine);

                /* if (ImmagineDaImpostare.isEmpty()) {
                    ImmagineDaImpostare = UrlImmagine;
                    StruttImmDaImpostare = Imm;
                    if (!branoPregresso) {
                        VariabiliGlobali.getInstance().setImmagineAttuale(ImmagineDaImpostare);
                    }
                } */

                ListaImmagini.add(Imm);
            }
        }
        int immagine = -1;
        for (int i = 0; i < ListaImmagini.size(); i++) {
            if (ListaImmagini.get(i).getAlbum().toUpperCase().contains(s.getAlbum().toUpperCase().trim())) {
                immagine = i;
                break;
            }
        }
        if (immagine == -1) {
            immagine = Utility.getInstance().GeneraNumeroRandom(ListaImmagini.size());
        }
        if (immagine > -1) {
            StrutturaImmagini si = ListaImmagini.get(immagine);
            ImmagineDaImpostare = si.getUrlImmagine();
            StruttImmDaImpostare = si;
            if (!branoPregresso) {
                VariabiliGlobali.getInstance().setImmagineAttuale(ImmagineDaImpostare);
            }
        } else {
            OggettiAVideo.getInstance().getImgSfondo().setImageResource(R.drawable.logo);
        }

        s.setImmagini(ListaImmagini);

        if (TestoEAltro[0].isEmpty()) {
            s.setAscoltata(0);
        } else {
            s.setAscoltata(Integer.parseInt(TestoEAltro[0]));
        }
        if (TestoEAltro[1].isEmpty()) {
            s.setBellezza(0);
        } else {
            s.setBellezza(Integer.parseInt(TestoEAltro[1]));
        }
        String Testo = TestoEAltro[2];
        if (!Testo.isEmpty()) {
            Testo = Testo.replace("§", "\n");
            Testo = Testo.replace("%20", " ");

            String NomeFile = Traccia + "-" + DatiBrano[5] + ".txt";
            if (!Utility.getInstance().EsisteFile(CartellaTesto +  NomeFile)) {
                Utility.getInstance().CreaCartelle(CartellaTesto);
                Utility.getInstance().ScriveFile(CartellaTesto, NomeFile, Testo);
            }
            NomeFile =  Traccia + "-" + DatiBrano[5] + ".2.txt";
            if (!Utility.getInstance().EsisteFile(CartellaTesto +  NomeFile)) {
                String Cosa = s.getBellezza() + ";" + s.getAscoltata();
                Utility.getInstance().CreaCartelle(CartellaTesto);
                Utility.getInstance().ScriveFile(CartellaTesto, NomeFile, Cosa);
            }
        }
        s.setTesto(Testo);

        // Aggiorna stelle al brano
        // db_dati db = new db_dati();
        // db.aggiornaStelleBrano(Integer.toString(s.getIdBrano()), s.getBellezza(), s.getAscoltata());
        // Aggiorna stelle al brano

        if (TestoEAltro.length > 3) {
            s.setTestoTradotto(TestoEAltro[3]);
        } else {
            s.setTestoTradotto("");
        }

        VariabiliGlobali.getInstance().setImmagineDaImpostarePregressa(StruttImmDaImpostare);
        if (!branoPregresso) {
            Utility.getInstance().ImpostaDatiBranoSuccessivo(s, false);
        } else {
            Log.getInstance().ScriveLog("idBrano Pregresso: " + s.getIdBrano());
            Log.getInstance().ScriveLog("Titolo Brano Pregresso: " + s.getBrano());
            Log.getInstance().ScriveLog("Album Pregresso: " + s.getAlbum());
            Log.getInstance().ScriveLog("Artista Pregresso: " + s.getArtista());

            // OggettiAVideo.getInstance().getImgPregresso().setVisibility(LinearLayout.VISIBLE);

            Log.getInstance().ScriveLog("Caricamento pregresso: " + branoPregresso);
            Log.getInstance().ScriveLog("Rete presente: " + VariabiliGlobali.getInstance().isRetePresente());
            Log.getInstance().ScriveLog("Scarica Brano " + VariabiliGlobali.getInstance().isScaricaBrano());
            Log.getInstance().ScriveLog("File: " + s.getPathBrano());
            Log.getInstance().ScriveLog("File esistente: " + Utility.getInstance().EsisteFile(s.getPathBrano()));
            Log.getInstance().ScriveLog("URL: " + s.getUrlBrano());
            if (VariabiliGlobali.getInstance().isRetePresente()) {
                if (VariabiliGlobali.getInstance().isScaricaBrano()) {
                    if (!Utility.getInstance().EsisteFile(s.getPathBrano())) {
                        // DOWNLOAD MP3
                        Log.getInstance().ScriveLog("Scarico il brano in locale");
                        new DownloadBrano(s).execute(s.getUrlBrano());
                    }
                }
            }

            VariabiliGlobali.getInstance().setBranoPregresso(s);
            VariabiliGlobali.getInstance().setCaricatoIlPregresso(true);
            VariabiliGlobali.getInstance().setStaLeggendoProssimoBrano(false);
            OggettiAVideo.getInstance().getTxtBranoPregresso().setText(s.getBrano() + " (" + s.getArtista() + ")");
            OggettiAVideo.getInstance().getLayPregresso().setVisibility(LinearLayout.VISIBLE);
        }
    }

    public void StoppaEsecuzione() {
        bckAsyncTask.cancel(true);
    }
}
