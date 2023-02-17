/* package com.looigi.newlooplayer.WebServices;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.MainActivity;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;

import org.kobjects.util.Util;

public class ChiamataWsListe implements TaskDelegate {
    private LetturaWSAsincrona bckAsyncTask;

    private String RadiceWS = "http://looigi.ddns.net:1021/";
    private String ws = "wsMobile.asmx/";
    private String NS="http://wsMobile2.org/";
    private String SA="http://wsMobile2.org/";
    private String TipoOperazione = "";

    public void RitornaListaBrani() {
        Log.getInstance().ScriveLog("Ritorna Lista Brani");
        String Urletto="RitornaListaBrani";

        TipoOperazione = "RitornaListaBrani";
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
        Log.getInstance().ScriveLog("Ritorno WS " + TipoOperazione + ". OK");

        // Fine elaborazione. Scrivo il file su sd
        String NomeFile = "listaBrani.txt";
        Utility.getInstance().EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR(), NomeFile);
        int ritorno = Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR(), NomeFile, result);
        if (ritorno > 0) {
            // Errore nella scrittura del file
            Log.getInstance().ScriveLog("Errore nella scrittura del file su SD: " + NomeFile);
        } else {
            Log.getInstance().ScriveLog("Scrittura del file su SD eseguita");

            // Utility.getInstance().LettaListaBrani(result);
        }
    }

    public void StoppaEsecuzione() {
        bckAsyncTask.cancel(true);
    }
}
*/