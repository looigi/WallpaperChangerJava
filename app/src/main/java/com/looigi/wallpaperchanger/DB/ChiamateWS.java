package com.looigi.wallpaperchanger.DB;

import com.looigi.wallpaperchanger.Log;
import com.looigi.wallpaperchanger.Utility;
import com.looigi.wallpaperchanger.VariabiliGlobali;

public class ChiamateWS implements TaskDelegate {
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliGlobali.getInstance().getUrlWS() + "/";
    private String ws = "looVF.asmx/";
    private String NS="http://looVF.org/";
    private String SA="http://looVF.org/";
    private String TipoOperazione = "";

    public void TornaProssimaImmagine() {
        String Urletto="TornaProssimaImmagine";
        boolean ApriDialog = false;
        TipoOperazione = "TornaProssimaImmagine";

        Esegue(
                RadiceWS + ws + Urletto,
                "TornaProssimaImmagine",
                NS,
                SA,
                15000,
                ApriDialog);
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
            case "TornaProssimaImmagine":
                fTornaProssimaImmagine(result);
                break;
        }
    }

    public void StoppaEsecuzione() {
        bckAsyncTask.cancel(true);
    }

    private void fTornaProssimaImmagine(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(result);
        } else {
            // 2433;/var/www/html/CartelleCondivise/SfondiDir/Donne/MetalWomen/df89106251200cc0021db5ae3e32.jpg
            String[] c = result.split(";");
            String quanteImmagini = c[0];
            String Immagine = c[1].replace("/var/www/html/CartelleCondivise", "");
            Immagine = VariabiliGlobali.getInstance().getPercorsoImmagineSuURL() + Immagine;
            String[] cc = Immagine.split("/");
            String NomeImmagine = cc[cc.length - 1];
            VariabiliGlobali.getInstance().getTxtQuanteImmagini().setText("Numero immagini online: " + quanteImmagini);

            VariabiliGlobali.getInstance().setImmaginiOnline(Integer.parseInt(quanteImmagini));

            new DownloadImage(NomeImmagine).execute(Immagine);
        }
    }
}
