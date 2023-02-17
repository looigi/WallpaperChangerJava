package com.looigi.newlooplayer.WebServices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.download.DownloadImage;
import com.looigi.newlooplayer.strutture.StrutturaBrano;
import com.looigi.newlooplayer.strutture.StrutturaImmagini;

import java.util.ArrayList;
import java.util.List;

public class ChiamataWsBellezza implements TaskDelegate {
    private LetturaWSAsincrona bckAsyncTask;

    private String RadiceWS = "http://looigi.ddns.net:1021/";
    private String ws = "wsLWP.asmx/";
    private String NS="http://wsLWP2.org/";
    private String SA="http://wsLWP2.org/";
    private String TipoOperazione = "";
    private int Stelle;

    public void SettaStelle(int Stelle) {
        this.Stelle = Stelle;
        Log.getInstance().ScriveLog("Imposta stelle brano con id " + VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato() + ": " + Integer.toString(Stelle));

        String Urletto="SettaStelle?";
        Urletto += "idUtente=" + VariabiliGlobali.getInstance().getIdUtente();
        Urletto += "&idCanzone=" + VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato();
        Urletto += "&Stelle=" + Integer.toString(Stelle);

        TipoOperazione = "SettaStelle";
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
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
        Log.getInstance().ScriveLog("Ritorno WS " + TipoOperazione + ". OK");

        switch (TipoOperazione) {
            case "SettaStelle":
                ImpostateStelle(result);
                break;
        }
    }

    private void ImpostateStelle(String result) {
        /* String CHIAVE = VariabiliGlobali.getInstance().getStrutturaDelBrano().getArtista().toUpperCase().trim() + "-" +
                VariabiliGlobali.getInstance().getStrutturaDelBrano().getAnno() +
                VariabiliGlobali.getInstance().getStrutturaDelBrano().getAlbum().toUpperCase().trim() + "-" +
                VariabiliGlobali.getInstance().getStrutturaDelBrano().getBrano().toUpperCase().trim(); */

        db_dati db = new db_dati();
        db.aggiornaStelleBrano(Integer.toString(VariabiliGlobali.getInstance().getStrutturaDelBrano().getIdBrano()), this.Stelle,
                VariabiliGlobali.getInstance().getStrutturaDelBrano().getAscoltata());
    }

    public void StoppaEsecuzione() {
        bckAsyncTask.cancel(true);
    }
}
