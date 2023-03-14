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
import com.looigi.newlooplayer.download.DownloadImage;
import com.looigi.newlooplayer.strutture.StrutturaImmaginiDaCambiare;

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
