package com.looigi.newlooplayer.WebServices;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.R;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.download.DownloadImage;
import com.looigi.newlooplayer.strutture.StrutturaImmaginiDaCambiare;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChiamateWsAmministrazione implements TaskDelegate {
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = "http://looigi.ddns.net:1021/";
    private final String ws = "wsMobile.asmx/";
    private final String ws2 = "wsLWP.asmx/";
    private final String NS="http://wsMobile2.org/";
    private final String SA="http://wsMobile2.org/";
    private final String NS2="http://csaricanuovai.org/";
    private final String SA2="http://csaricanuovai.org/";
    private String TipoOperazione = "";

    public void RinominaAlbum(String Artista, String Album, String Anno, String NuovoNomeAlbum, String NuovoAnno) {
        Log.getInstance().ScriveLog("Rinomina album. Artista: " + Artista + ", Album: " + Album + ", Anno: " + Anno +
                ", Nuovo Anno: " + NuovoAnno + ", Nuovo Nome Album: " + NuovoNomeAlbum);

        String Urletto="RinominaAlbum?" +
                "idUtente=1" +
                "&Artista=" + Artista +
                "&Album=" + Album +
                "&Anno=" + Anno +
                "&NuovoAnno=" + NuovoAnno +
                "&NuovoNomeAlbum=" + NuovoNomeAlbum;

        TipoOperazione = "RinominaAlbum";

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                10000,
                true);
    }

    public void AggiornaImmagineAlbum(String Artista, String Album, String Anno, String Immagine) {
        Log.getInstance().ScriveLog("Aggiorna immagine album. Artista: " + Artista + ", Album: " + Album + ", Anno: " + Anno + ", Immagine: " + Immagine);

        String Urletto="AggiornaImmagineAlbum?" +
                "Artista=" + Artista +
                "&Album=" + Album +
                "&Anno=" + Anno +
                "&Immagine=" + Immagine;

        TipoOperazione = "AggiornaImmagineAlbum";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                true);
    }

    private String ArtistaAppoggio;
    private String AnnoAppoggio;
    private String AlbumAppoggio;

    public void ScaricaImmagineAlbum(String Artista, String Album, String Anno) {
        Log.getInstance().ScriveLog("Scarica immagine album. Artista: " + Artista + ", Album: " + Album + ", Anno: " + Anno);

        ArtistaAppoggio = Artista;
        AlbumAppoggio = Album;
        AnnoAppoggio = Anno;

        String Urletto="ScaricaImmagineAlbum?" +
                "Artista=" + Artista +
                "&Album=" + Album +
                "&Anno=" + Anno +
                "&QuanteImmagini=" + VariabiliGlobali.getInstance().getQuanteImmaginiDaScaricareGA();
        Urletto = Urletto.replace(" ", "%20");

        TipoOperazione = "ScaricaImmagineAlbum";

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                120000,
                true);
    }

    public void EliminaBrutte() {
        Log.getInstance().ScriveLog("Elimina canzoni brutte");

        String Urletto="EliminaBrutte?idUtente=1";

        TipoOperazione = "EliminaBrutte";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
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
            Log.getInstance().ScriveLog("Ritorno WS Amministrazione " + TipoOperazione + ". ERRORE...");
        } else {
            Log.getInstance().ScriveLog("Ritorno WS Amministrazione " + TipoOperazione + ". OK");
        }

        switch (TipoOperazione) {
            case "EliminaBrutte":
                fEliminabrutte(result);
                break;
            case "ScaricaImmagineAlbum":
                fScaricaImmagineAlbum(result);
                break;
            case "AggiornaImmagineAlbum":
                fAggiornaImmagineAlbum(result);
                break;
            case "RinominaAlbum":
                fRinominaAlbum(result);
                break;
        }
    }

    private void fRinominaAlbum(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            // RINOMINA PATHS
            String VecchioPathBrani = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Brani/" +
                    VariabiliGlobali.getInstance().getNomeArtistaGA() + "/" +
                    VariabiliGlobali.getInstance().getAnnoAlbumGA() + "-" + VariabiliGlobali.getInstance().getNomeAlbumGA();
            String NuovoPathBrani = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Brani/" +
                    VariabiliGlobali.getInstance().getNomeArtistaGA() + "/" +
                    OggettiAVideo.getInstance().getEdtAnnoAlbumGA().getText().toString() + "-" + OggettiAVideo.getInstance().getEdtNomeAlbumGA().getText().toString();
            File oldFolder = new File(VecchioPathBrani);
            File newFolder = new File(NuovoPathBrani);
            boolean success = oldFolder.renameTo(newFolder);
            Log.getInstance().ScriveLog("Rinomina path album: " + VecchioPathBrani + " -> " + NuovoPathBrani + " : " + success);

            String VecchioPathImmaginiBrani = VariabiliGlobali.getInstance().getPercorsoDIR() + "/ImmaginiMusica/" +
                    VariabiliGlobali.getInstance().getNomeArtistaGA() + "/" +
                    VariabiliGlobali.getInstance().getAnnoAlbumGA() + "-" + VariabiliGlobali.getInstance().getNomeAlbumGA();
            String NuovoPathImmaginiBrani = VariabiliGlobali.getInstance().getPercorsoDIR() + "/ImmaginiMusica/" +
                    VariabiliGlobali.getInstance().getNomeArtistaGA() + "/" +
                    OggettiAVideo.getInstance().getEdtAnnoAlbumGA().getText().toString() + "-" + OggettiAVideo.getInstance().getEdtNomeAlbumGA().getText().toString();
            oldFolder = new File(VecchioPathImmaginiBrani);
            newFolder = new File(NuovoPathImmaginiBrani);
            success = oldFolder.renameTo(newFolder);
            Log.getInstance().ScriveLog("Rinomina path immagini album: " + VecchioPathImmaginiBrani + " -> " + NuovoPathImmaginiBrani + " : " + success);

            String VecchioPathTestiBrani = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Testi/" +
                    VariabiliGlobali.getInstance().getNomeArtistaGA() + "/" +
                    VariabiliGlobali.getInstance().getAnnoAlbumGA() + "-" + VariabiliGlobali.getInstance().getNomeAlbumGA();
            String NuovoPathTestiBrani = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Testi/" +
                    VariabiliGlobali.getInstance().getNomeArtistaGA() + "/" +
                    OggettiAVideo.getInstance().getEdtAnnoAlbumGA().getText().toString() + "-" + OggettiAVideo.getInstance().getEdtNomeAlbumGA().getText().toString();
            oldFolder = new File(VecchioPathTestiBrani);
            newFolder = new File(NuovoPathTestiBrani);
            success = oldFolder.renameTo(newFolder);
            Log.getInstance().ScriveLog("Rinomina path testi album: " + VecchioPathTestiBrani + " -> " + NuovoPathTestiBrani + " : " + success);
            // RINOMINA PATHS

            // MODIFICHE SU DB
            Log.getInstance().ScriveLog("Rinomina dati su tabelle");
            db_dati db = new db_dati();
            db.RinominaAlbum(VariabiliGlobali.getInstance().getNomeArtistaGA(),
                    VariabiliGlobali.getInstance().getNomeAlbumGA(),
                    VariabiliGlobali.getInstance().getAnnoAlbumGA(),
                    OggettiAVideo.getInstance().getEdtNomeAlbumGA().getText().toString(),
                    OggettiAVideo.getInstance().getEdtAnnoAlbumGA().getText().toString()
                    );
            // MODIFICHE SU DB

            // ELIMINAZIONE LISTE
            Log.getInstance().ScriveLog("Pulizia liste");
            Utility.getInstance().PulisceListe(false);
            // ELIMINAZIONE LISTE

            VariabiliGlobali.getInstance().setNomeAlbumGA(OggettiAVideo.getInstance().getEdtNomeAlbumGA().getText().toString());
            VariabiliGlobali.getInstance().setAnnoAlbumGA(OggettiAVideo.getInstance().getEdtAnnoAlbumGA().getText().toString());

            OggettiAVideo.getInstance().getTxtNomeAlbumGA().setVisibility(LinearLayout.VISIBLE);
            OggettiAVideo.getInstance().getLayEditGA().setVisibility(LinearLayout.GONE);
            OggettiAVideo.getInstance().getImgAnnullaRinominaAlbumGA().setVisibility(LinearLayout.GONE);
            VariabiliGlobali.getInstance().setStaEditandoAlbum(false);
        }
    }

    private void fAggiornaImmagineAlbum(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            String Immagine = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() + "/ImmaginiMusica/" + result;
            new DownloadImage(OggettiAVideo.getInstance().getImgAlbumGA(), Immagine).execute(Immagine);

            OggettiAVideo.getInstance().getLayCambioImmagineGA().setVisibility(LinearLayout.GONE);
        }
    }

    private int indiceImmagine;
    private List<StrutturaImmaginiDaCambiare> listaImmagini = new ArrayList<>();

    private void fScaricaImmagineAlbum(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            String[] Immagini = result.split("§");
            for (int i = 0; i < Immagini.length; i++) {
                if (!Immagini[i].isEmpty()) {
                    String[] Campi = Immagini[i].split(";");
                    StrutturaImmaginiDaCambiare s = new StrutturaImmaginiDaCambiare();
                    s.setNomeImmagine(Campi[0]);
                    s.setPathImmagine("http://looigi.ddns.net:1021/Appoggio/ImmaginiAlbum/" + Campi[0]);
                    s.setDimensione(Campi[1]);
                    listaImmagini.add(s);
                }
            }
            if (listaImmagini.size() > 0) {
                indiceImmagine = 0;
                DisegnaImmagineDaCambiare();
                OggettiAVideo.getInstance().getLayCambioImmagineGA().setVisibility(LinearLayout.VISIBLE);
                OggettiAVideo.getInstance().getImgSceltaGA().setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ChiamateWsAmministrazione ws = new ChiamateWsAmministrazione();
                        ws.AggiornaImmagineAlbum(ArtistaAppoggio, AlbumAppoggio, AnnoAppoggio, listaImmagini.get(indiceImmagine).getNomeImmagine());
                    }
                });
                OggettiAVideo.getInstance().getImgAvantiGA().setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (indiceImmagine < listaImmagini.size() -1) {
                            indiceImmagine++;
                        } else {
                            indiceImmagine = 0;
                        }
                        DisegnaImmagineDaCambiare();
                    }
                });
                OggettiAVideo.getInstance().getImgIndietroGA().setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (indiceImmagine > 0) {
                            indiceImmagine--;
                        } else {
                            indiceImmagine = listaImmagini.size() -1;
                        }
                        DisegnaImmagineDaCambiare();
                    }
                });
            } else {
                Utility.getInstance().VisualizzaErrore("Nessuna immagine rilevata");
            }
        }
    }

    private void DisegnaImmagineDaCambiare() {
        new DownloadImage(OggettiAVideo.getInstance().getImgSceltaGA(),
                listaImmagini.get(indiceImmagine).getPathImmagine()).execute(listaImmagini.get(indiceImmagine).getPathImmagine());
        OggettiAVideo.getInstance().getTxtInfoGA().setText("Immagine " + (indiceImmagine + 1) + "/" + (listaImmagini.size()) + " Bytes: " +
                listaImmagini.get(indiceImmagine).getDimensione());
    }

    private void fEliminabrutte(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            Toast.makeText(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                    "Canzoni brutte eliminate.", Toast.LENGTH_LONG).show();
        }
    }
}