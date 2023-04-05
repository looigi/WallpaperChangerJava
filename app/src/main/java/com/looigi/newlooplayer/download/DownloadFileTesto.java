package com.looigi.newlooplayer.download;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.LinearLayout;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.LogPulizia;
import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.strutture.StrutturaBrano;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFileTesto extends AsyncTask<String, String, String> {
    String fileDaScaricare;

    public DownloadFileTesto(String FileDaScaricare) {
        fileDaScaricare = FileDaScaricare;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        Utility.getInstance().CreaCartelle(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Versioni");

        int count;

        try {
            URL url = new URL(f_url[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty(
                    "Content-Type", "audio/mpeg" );
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // urlConnection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = urlConnection.getContentLength();
            // OggettiAVideo.getInstance().getProgressDownload().setMax(lenghtOfFile);

            Log.getInstance().ScriveLog("Download brano. Lunghezza file: " + Long.toString(lenghtOfFile));

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),8192);

            // Output stream
            String path = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Versioni/Pulizia.txt";
            Log.getInstance().ScriveLog("Download file di testo. Creazione file output: " + path);
            OutputStream output = new FileOutputStream(path);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1 && !VariabiliGlobali.getInstance().isBloccaDownload()) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) (total));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
        } catch (Exception e) {
            Log.getInstance().ScriveLog("Download file di testo. Errore: " + e.getMessage());
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String path = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Versioni/Pulizia.txt";
                if (Utility.getInstance().EsisteFile(path)) {
                    int dime = Utility.getInstance().DimensioniFile(path);

                    Log.getInstance().ScriveLog("File scaricato: " + path + ". Dimensioni: " + dime);

                    String testo = Utility.getInstance().LeggeFileUnico(path);
                    String[] t = testo.split("\n");

                    LogPulizia.getInstance().ScriveLog("-----------------------LOG REMOTO-----------------------");
                    for (int i = 0; i < t.length - 1; i++) {
                        LogPulizia.getInstance().ScriveLog(t[i]);
                    }
                    LogPulizia.getInstance().ScriveLog("--------------------FINE LOG  REMOTO--------------------");

                    Utility.getInstance().VisualizzaErrore("Operazione di pulizia remota effettuata");
                } else {
                    Log.getInstance().ScriveLog("File NON scaricato: " + path);
                    Utility.getInstance().VisualizzaErrore("Operazione di pulizia remota NON effettuata per file non scaricato");
                }
            }
        }, 100);
    }

}
