package com.looigi.newlooplayer.WebServices;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.newlooplayer.BuildConfig;
import com.looigi.newlooplayer.EliminaBraniDaDisco;
import com.looigi.newlooplayer.PuliziaGenerale;
import com.looigi.newlooplayer.ServizioBackground;
import com.looigi.newlooplayer.adapters.AdapterListenerArtisti;
import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.adapters.AdapterListenerTags;
import com.looigi.newlooplayer.adapters.AdapterListenerTagsBranoGA;
import com.looigi.newlooplayer.adapters.AdapterListenerTagsBranoGAR;
import com.looigi.newlooplayer.adapters.AdapterListenerTagsTuttiGA;
import com.looigi.newlooplayer.adapters.AdapterListenerTagsTuttiGAR;
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

import org.kobjects.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChiamateWs implements TaskDelegate {
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliGlobali.getInstance().getUrlWS() + "/";
    private final String ws = "wsMobile.asmx/";
    private final String ws2 = "wsLWP.asmx/";
    private final String NS="http://wsMobile2.org/";
    private final String SA="http://wsMobile2.org/";
    private final String NS2="http://csaricanuovai.org/";
    private final String SA2="http://csaricanuovai.org/";
    private String TipoOperazione = "";
    private boolean branoPregresso;
    private long inizioChiamata;
    private boolean ControllaTempoEsecuzione = false;
    private boolean accesoStatoReteMancante = false;
    private PuliziaGenerale p;

    public void AttendePuliziaCompleta(PuliziaGenerale p) {
        Log.getInstance().ScriveLog("Attesa termine pulizia");

        String Urletto="AttendePuliziaCompleta";

        this.p = p;

        TipoOperazione = "AttendePuliziaCompleta";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                2000,
                false);
    }

    public void PuliziaCompleta() {
        Log.getInstance().ScriveLog("Pulizie di Pasqua");

        String Urletto="PulizieDiPasqua?Simula=" + (VariabiliGlobali.getInstance().isSimulazione() ? "SI" : "NO");

        TipoOperazione = "PulizieDiPasqua";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                200000,
                true);
    }

    public void RicaricaBrani() {
        Log.getInstance().ScriveLog("Ricarica brani. Eliminazione JSON");

        String Urletto="EliminaJSON?idUtente=1";

        TipoOperazione = "EliminaJSON";
        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                10000,
                true);
    }

    public void RicaricaBrani2() {
        Log.getInstance().ScriveLog("Ricarica brani2. Creazione JSON");

        String Urletto="RefreshCanzoniHard?idUtente=1";

        TipoOperazione = "RefreshCanzoniHard";
        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                200000,
                true);
    }

    public void RitornaVersioneApplicazione() {
        Log.getInstance().ScriveLog("Ritorna versione applicazione");

        String Urletto="RitornaVersioneApplicazione";

        ControllaTempoEsecuzione = true;
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

        Artista = Utility.getInstance().ConverteNome(Artista);

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

    public void RitornaTagsAlbum() {
        String Album = VariabiliGlobali.getInstance().getNomeAlbumGA();
        String Artista = VariabiliGlobali.getInstance().getNomeArtistaGA();
        String Anno = VariabiliGlobali.getInstance().getAnnoAlbumGA();
        /* if (Album.contains("-")) {
            String[] a = Album.split("-");
            Album = a[1];
        } */

        Artista = Utility.getInstance().ConverteNome(Artista);
        Album = Utility.getInstance().ConverteNome(Album);

        Log.getInstance().ScriveLog("Ritorna Tags Album " + Album +
                " Artista " + Artista + " Anno " + Anno);

        String Urletto="RitornaTagsAlbumArtista?Artista=" + Artista +
                "&Album=" + Album +
                "&Anno=" + Anno;

        TipoOperazione = "RitornaTagsAlbum";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                true);
    }

    public void RitornaTagsArtista(String Artista) {
        Log.getInstance().ScriveLog("Ritorna Tags Artista " + Artista);

        Artista = Utility.getInstance().ConverteNome(Artista);

        String Urletto="RitornaTagsAlbumArtista?Artista=" + Artista +
                "&Album=" +
                "&Anno=";

        TipoOperazione = "RitornaTagsArtista";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                true);
    }

    public void AggiornaTagsArtista(String Artista) {
        String t = "";
        for (int i = 0; i < VariabiliGlobali.getInstance().getListaTagsArtista().size(); i++) {
            t += VariabiliGlobali.getInstance().getListaTagsArtista().get(i) + ";";
        }
        String Tags = t;
        Log.getInstance().ScriveLog("Aggiorna Tags Artista " + Artista + ": " + Tags);

        Artista = Utility.getInstance().ConverteNome(Artista);

        String Urletto="AggiornaTagsAlbumArtista?Artista=" + Artista +
                "&Album=" +
                "&Anno=" +
                "&Tags=" + Tags;

        TipoOperazione = "AggiornaTagsArtista";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                true);
    }

    public void AggiornaTagsAlbum() {
        String Album = VariabiliGlobali.getInstance().getNomeAlbumGA();
        String Artista = VariabiliGlobali.getInstance().getNomeArtistaGA();
        String Anno = VariabiliGlobali.getInstance().getAnnoAlbumGA();

        Artista = Utility.getInstance().ConverteNome(Artista);
        Album = Utility.getInstance().ConverteNome(Album);

        String t = "";
        for (int i = 0; i < VariabiliGlobali.getInstance().getListaTagsAlbum().size(); i++) {
            t += VariabiliGlobali.getInstance().getListaTagsAlbum().get(i) + ";";
        }
        String Tags = t;
        Log.getInstance().ScriveLog("Aggiorna Tags Album " + Album +
                " Anno " + Anno + " Artista " + Artista + ": " + Tags);

        String Urletto="AggiornaTagsAlbumArtista?Artista=" + Artista +
                "&Album=" + Album +
                "&Anno=" + Anno +
                "&Tags=" + Tags;

        TipoOperazione = "AggiornaTagsAlbum";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                true);
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
                true);
    }

    public void RitornaListaBrani(String Artista, String Album) {
        Log.getInstance().ScriveLog("Ritorna lista brani per Artista " + Artista + " e album " + Album);

        Artista = Utility.getInstance().ConverteNome(Artista);
        Album = Utility.getInstance().ConverteNome(Album);

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

    public void AggiornaTesto(boolean mostraPopup) {
        String id = VariabiliGlobali.getInstance().getStrutturaDelBrano().getIdBrano().toString();
        String Artista = VariabiliGlobali.getInstance().getStrutturaDelBrano().getArtista();
        String Album = VariabiliGlobali.getInstance().getStrutturaDelBrano().getAlbum();
        String Brano = VariabiliGlobali.getInstance().getStrutturaDelBrano().getBrano();
        Log.getInstance().ScriveLog("Scarica testo per id canzone " + id + " per Artista " + Artista + ", album " + Album + " e brano " + Brano);

        Artista = Utility.getInstance().ConverteNome(Artista);
        Album = Utility.getInstance().ConverteNome(Album);
        Brano = Utility.getInstance().ConverteNome(Brano);

        String Urletto="ScaricaTesto?idCanzone=" + id + "&Artista=" + Artista + "&Album=" + Album + "&Canzone=" + Brano;

        TipoOperazione = "ScaricaTesto";
        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                30000,
                mostraPopup);
    }

    public void ScaricaNuovaImmagine(String Artista, String Album, String Brano) {
        Log.getInstance().ScriveLog("Scarica nuova immagine per Artista " + Artista + ", album " + Album + " e brano " + Brano);

        Artista = Utility.getInstance().ConverteNome(Artista);
        Album = Utility.getInstance().ConverteNome(Album);
        Brano = Utility.getInstance().ConverteNome(Brano);

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
                60000,
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

        ControllaTempoEsecuzione = true;
        TipoOperazione = "ControllaAggiornamento";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                false);
    }

    public void ScatenaAggiornamento() {
        Log.getInstance().ScriveLog("Scatena Aggiornamento");

        String Urletto="ScatenaAggiornamento";

        ControllaTempoEsecuzione = true;
        TipoOperazione = "ScatenaAggiornamento";
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
        String StelleSuperiori = VariabiliGlobali.getInstance().isStelleSuperiori() ? "S" : "N";
        if (!VariabiliGlobali.getInstance().isRicercaStelle()) {
            Stelle = "";
            StelleSuperiori = "";
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
        Urletto += "&StelleSuperiori=" + StelleSuperiori;

        ControllaTempoEsecuzione = true;
        TipoOperazione = "RitornaProssimoBranoMobile";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                20000,
                false);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

        Long tsLong = System.currentTimeMillis()/1000;
        inizioChiamata = tsLong;
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

        long fineChiamata = System.currentTimeMillis()/1000;
        long differenza = fineChiamata - inizioChiamata;
        if (ControllaTempoEsecuzione) {
            if (differenza > 3500) {
                // Ci ha messo troppo tempo
                Log.getInstance().ScriveLog("Ritorno WS " + TipoOperazione + ". Troppo tempo a rispondere: " + differenza);

                OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.VISIBLE);
                VariabiliGlobali.getInstance().setBranoSuSD(true);
                accesoStatoReteMancante = true;
            } else {
                if (accesoStatoReteMancante) {
                    Log.getInstance().ScriveLog("Ritorno WS " + TipoOperazione + ". Ripristino stato rete");
                    accesoStatoReteMancante = false;
                    OggettiAVideo.getInstance().getImgNoNet().setVisibility(LinearLayout.GONE);
                    if (!VariabiliGlobali.getInstance().isBranosSuSDOriginale()) {
                        VariabiliGlobali.getInstance().setBranoSuSD(false);
                    }
                }
            }
        }

        switch (TipoOperazione) {
            case "ControllaAggiornamento":
                fControllaAggiornamento(result);
                break;
            case "ScatenaAggiornamento":
                fScatenaAggiornamento(result);
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
            case "AggiornaTagsAlbum":
                fAggiornaTagsAlbum(result);
                break;
            case "RitornaTagsAlbum":
                fRitornaTagsAlbum(result);
                break;
            case "AggiornaTagsArtista":
                fAggiornaTagsArtista(result);
                break;
            case "RitornaTagsArtista":
                fRitornaTagsArtista(result);
                break;
            case "RitornaListaBraniAlbumArtista":
                RitornaListaAlbumArtistaBrani(result);
                break;
            case "RitornaVersioneApplicazione":
                RitornaVersione(result);
                break;
            case "EliminaJSON":
                fEliminaJSON(result);
                break;
            case "RefreshCanzoniHard":
                fRitornaJSON(result);
                break;
            case "PulizieDiPasqua":
                fPulizieDiPasqua(result);
                break;
            case "AttendePuliziaCompleta":
                fAttendePuliziaCompleta(result);
                break;
        }
    }

    private void fAttendePuliziaCompleta(String result) {
        p.TerminePulizia(result);
    };

    private void fPulizieDiPasqua(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            /* Log.getInstance().ScriveLog("Pulizia lanciata");
            AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Pulizia lanciata in background");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show(); */

            PuliziaGenerale p = new PuliziaGenerale();
            p.AttesaFinePulizia(false);
        }
    }

    private void fRitornaJSON(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            Log.getInstance().ScriveLog("Refresh brani effettuato");
            Utility.getInstance().PulisceListe(false);
            AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Brani ricaricati. E' necessaria una ripartenza");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Utility.getInstance().Uscita();
                        }
                    });
            alertDialog.show();
        }
    }

    private void fEliminaJSON(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            ChiamateWs ws = new ChiamateWs();
            ws.RicaricaBrani2();
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

                // boolean r1 = checkSystemWritePermission();

                VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startService(new Intent(
                        VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        ServizioBackground.class));
            }
        } else {
            Log.getInstance().ScriveLog("Ritornato errore su ritorno versione: " + result);

            // boolean r2 = checkSystemWritePermission();

            VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startService(new Intent(
                    VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                    ServizioBackground.class));
        }
    }

    /* private boolean checkSystemWritePermission() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(VariabiliGlobali.getInstance().getFragmentActivityPrincipale());
            // Log.d("TAG", "Can Write Settings: " + retVal);
            if (retVal){
                // Permission granted by the user
            } else{
                // permission not granted navigate to permission screen
                openAndroidPermissionsMenu();
            }
        }

        return retVal;
    }

    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + VariabiliGlobali.getInstance().getFragmentActivityPrincipale().getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startActivity(intent);
    } */

    private void fScatenaAggiornamento(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            Utility.getInstance().VisualizzaErrore("Aggiornamento propagato...");
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
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            OggettiAVideo.getInstance().getLayTagsBrano().setVisibility(LinearLayout.GONE);
        }
    }

    private void fAggiornaTagsAlbum(String result) {
        Log.getInstance().ScriveLog("Aggiornamento tags album eseguito: " + result);
        if (result.contains("ERROR")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
        }
    }

    private void fAggiornaTagsArtista(String result) {
        Log.getInstance().ScriveLog("Aggiornamento tags artista eseguito: " + result);
        if (result.contains("ERROR")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
        }
    }

    private void fRitornaTagsArtista(String result) {
        Log.getInstance().ScriveLog("Lettura tags artista eseguita: " + result);
        if (result.contains("ERROR")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            if (result.contains("anyType")) {
                // Utility.getInstance().VisualizzaErrore("Ritorno anyType per i tags");
                List<String> l = new ArrayList<>();
                VariabiliGlobali.getInstance().setListaTagsArtista(l);

                String[] r = {};
                AdapterListenerTagsBranoGAR customAdapterTags = new AdapterListenerTagsBranoGAR(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        r);
                OggettiAVideo.getInstance().getLstTagsGAR().setAdapter(customAdapterTags);

                AdapterListenerTagsTuttiGAR customAdapterT = new AdapterListenerTagsTuttiGAR(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        Utility.getInstance().CreaVettoreTagsAlbum(result));
                OggettiAVideo.getInstance().getLstTagsTuttiGAR().setAdapter(customAdapterT);
            } else {
                String[] r = result.split(";");
                List<String> l = new ArrayList<>();
                for (int i = 0; i < r.length; i++) {
                    l.add(r[i]);
                }
                VariabiliGlobali.getInstance().setListaTagsArtista(l);
                AdapterListenerTagsBranoGAR customAdapterTags = new AdapterListenerTagsBranoGAR(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        r);
                OggettiAVideo.getInstance().getLstTagsGAR().setAdapter(customAdapterTags);

                AdapterListenerTagsTuttiGAR customAdapterT = new AdapterListenerTagsTuttiGAR(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        Utility.getInstance().CreaVettoreTagsAlbum(result));
                OggettiAVideo.getInstance().getLstTagsTuttiGAR().setAdapter(customAdapterT);
            }
        }
    }

    private void fRitornaTagsAlbum(String result) {
        Log.getInstance().ScriveLog("Lettura tags album eseguita: " + result);
        if (result.contains("ERROR")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            if (result.contains("anyType")) {
                // Utility.getInstance().VisualizzaErrore("Ritorno anyType per i tags");
                List<String> l = new ArrayList<>();
                VariabiliGlobali.getInstance().setListaTagsAlbum(l);

                String[] r = {};
                AdapterListenerTagsBranoGA customAdapterTags = new AdapterListenerTagsBranoGA(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        r);
                OggettiAVideo.getInstance().getLstTagsGA().setAdapter(customAdapterTags);

                AdapterListenerTagsTuttiGA customAdapterT = new AdapterListenerTagsTuttiGA(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        Utility.getInstance().CreaVettoreTagsAlbum(result));
                OggettiAVideo.getInstance().getLstTagsTuttiGA().setAdapter(customAdapterT);
            } else {
                String[] r = result.split(";");
                List<String> l = new ArrayList<>();
                for (int i = 0; i < r.length; i++) {
                    l.add(r[i]);
                }
                VariabiliGlobali.getInstance().setListaTagsAlbum(l);
                AdapterListenerTagsBranoGA customAdapterTags = new AdapterListenerTagsBranoGA(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        r);
                OggettiAVideo.getInstance().getLstTagsGA().setAdapter(customAdapterTags);

                AdapterListenerTagsTuttiGA customAdapterT = new AdapterListenerTagsTuttiGA(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        Utility.getInstance().CreaVettoreTagsAlbum(result));
                OggettiAVideo.getInstance().getLstTagsTuttiGA().setAdapter(customAdapterT);
            }
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
        Log.getInstance().ScriveLog("Ritorna lista album artisti: " + result.substring(0, 8));
        if (!result.equals("anyType{}")) {
            List<StrutturaListaBrani> lista = new ArrayList<>();
            String[] Globale = result.split("§");
            Log.getInstance().ScriveLog("Ritorna lista album artisti: Rilevati " + Globale.length);
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
        } else {
            Log.getInstance().ScriveLog("Ritorna lista album artisti: Nessun ritorno valido");
        }
    }

    private void RitornaListaAlbumArtista(String result) {
        if (!result.equals("anyType{}")) {
            List<StrutturaListaAlbum> lista = new ArrayList<>();
            String[] Globale = result.split("§");
            for (int i = 0; i < Globale.length; i++) {
                if (!Globale[i].isEmpty()) {
                    try {
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
                    } catch (Exception ignored) {
                        Log.getInstance().ScriveLog("Problemi nel ritorno della lista album artista (" + Globale[i] + "): " +
                                Utility.getInstance().PrendeErroreDaException(ignored));
                    }
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
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            Utility.getInstance().EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaArtisti.txt");
            Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "ListaArtisti.txt", result);

            RitornaArtisti2(result);
        }
    }

    public void RitornaArtisti2(String result) {
        Log.getInstance().ScriveLog("Ritorno artisti");

        String[] Globale = result.split("§");
        Log.getInstance().ScriveLog("Ritorno artisti -> " + Globale.length);

        for (int i = 0; i < Globale.length; i++) {
            if (!Globale[i].trim().replace("\n", "").isEmpty()) {
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
                } catch (Exception ignored) {
                    Log.getInstance().ScriveLog("Ritorno artisti. Errore su parse (" + Globale[i] + "): " + Utility.getInstance().PrendeErroreDaException(ignored));
                }
            }
        }
        Log.getInstance().ScriveLog("Ritorno artisti effettuato");

        OggettiAVideo.getInstance().getBtnLista().setVisibility(LinearLayout.VISIBLE);

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
                /* if (Imm2[2].toUpperCase().contains("COVER_")) {
                    Imm.setAlbum(Imm2[1]);
                } else {
                } */
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
            Utility.getInstance().ImpostaSfondoLogo();
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
        String Testo = "";
        if (TestoEAltro.length > 2) {
            Testo = TestoEAltro[2];
        } else {
            Testo = "";
        }
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
            NomeFile =  Traccia + "-" + DatiBrano[5] + ".TAGS.txt";
            if (!Utility.getInstance().EsisteFile(CartellaTesto +  NomeFile)) {
                String Cosa = s.getTags();
                Utility.getInstance().CreaCartelle(CartellaTesto);
                Utility.getInstance().ScriveFile(CartellaTesto, NomeFile, Cosa);
            }
            NomeFile =  Traccia + "-" + DatiBrano[5] + ".DATA.txt";
            if (!Utility.getInstance().EsisteFile(CartellaTesto +  NomeFile)) {
                String Cosa = s.getData();
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
