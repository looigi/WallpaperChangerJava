package com.looigi.newlooplayer.download;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.LinearLayout;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.strutture.StrutturaBrano;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadBrano extends AsyncTask<String, String, String> {
    StrutturaBrano sb;

    public DownloadBrano(StrutturaBrano s) {
        sb = s;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // showDialog(progress_bar_type);
        // OggettiAVideo.getInstance().getProgressDownload().setVisibility(LinearLayout.VISIBLE);
        OggettiAVideo.getInstance().getLayDownload().setVisibility(LinearLayout.VISIBLE);
        OggettiAVideo.getInstance().getTxtDownload().setText("Inizio download");

        VariabiliGlobali.getInstance().setBloccaDownload(false);
        OggettiAVideo.getInstance().getImgDownloadBrano().setVisibility(LinearLayout.VISIBLE);
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int Limite = VariabiliGlobali.getInstance().getLimiteInMb() * 1024;
        int occupato = VariabiliGlobali.getInstance().getSpazioOccupatoSuDisco();
        if (occupato > Limite) {
            VariabiliGlobali.getInstance().setBloccaDownload(true);

            VariabiliGlobali.getInstance().getFragmentActivityPrincipale().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    OggettiAVideo.getInstance().getImgDownloadBrano().setVisibility(LinearLayout.GONE);
                    OggettiAVideo.getInstance().getLayDownload().setVisibility(LinearLayout.GONE);
                }
            });

            Log.getInstance().ScriveLog("Download brano annullato. Spazio occupato maggiore del limite impostato: " + occupato + "/" + Limite);
            return null;
        }

        Utility.getInstance().CreaCartelle(sb.getCartellaBrano());

        int count;

        try {
            java.net.URL url = new URL(f_url[0]);
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
            Log.getInstance().ScriveLog("Download brano. Creazione file output: " + sb.getPathBrano());
            OutputStream output = new FileOutputStream(sb.getPathBrano());

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

            if (VariabiliGlobali.getInstance().isBloccaDownload()) {
                Log.getInstance().ScriveLog("Download brano bloccato. Elimino file " + sb.getCartellaBrano() + " " + sb.getBrano() + sb.getEstensione());
                VariabiliGlobali.getInstance().setBloccaDownload(false);
                Utility.getInstance().EliminaFile(sb.getCartellaBrano(), sb.getBrano() + sb.getEstensione());
            }
            // VariabiliGlobali.getInstance().setPresentiSuDisco(VariabiliGlobali.getInstance().getPresentiSuDisco() + 1);
        } catch (Exception e) {
            Log.getInstance().ScriveLog("Download brano. Errore: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        // pDialog.setProgress(Integer.parseInt(progress[0]));
        float p = Float.parseFloat(progress[0]);
        String s = "B.";
        if (p > 1024) {
            p /= 1024;
            s = "Kb.";
        }
        if (p > 1024) {
            p /= 1024;
            s = "Mb.";
        }
        if (p > 1024) {
            p /= 1024;
            s = "Gb.";
        }
        int p2 = (int)(p * 100);
        float p3 =  (float)p2 / 100;
        OggettiAVideo.getInstance().getTxtDownload().setText("Download MP3 - " + p3 + " " + s);
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        // dismissDialog(progress_bar_type);
        if (!VariabiliGlobali.getInstance().isBloccaDownload() && !VariabiliGlobali.getInstance().isSkippatoBrano()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!VariabiliGlobali.getInstance().isCaricatoIlPregresso()) {
                        // Prende durata brano solo in caso di nuovo brano e non di pregresso
                        boolean durata = Utility.getInstance().DurataBrano(sb.getPathBrano());
                        if (!durata) {
                            Log.getInstance().ScriveLog("Impossibile rilevare la durata del brano: " + sb.getPathBrano());
                            // OggettiAVideo.getInstance().getImgPlay().setVisibility(LinearLayout.GONE);
                            OggettiAVideo.getInstance().getImgErroreBrano().setVisibility(LinearLayout.VISIBLE);
                        } else {
                            // OggettiAVideo.getInstance().getImgPlay().setVisibility(LinearLayout.VISIBLE);
                            OggettiAVideo.getInstance().getImgErroreBrano().setVisibility(LinearLayout.GONE);
                        }
                    }

                    // Do something after 5s = 5000ms
                    int dime = Utility.getInstance().DimensioniFile(sb.getPathBrano());
                    Log.getInstance().ScriveLog("File scaricato: " + sb.getPathBrano() + ". Dimensioni: " + dime);
                    if (dime > 1000) {
                        db_dati db = new db_dati();
                        sb.setDimensione(dime);
                        db.AggiungeBrano(sb);
                        VariabiliGlobali.getInstance().setPresentiSuDisco(db.ContaBrani());
                        VariabiliGlobali.getInstance().setSpazioOccupatoSuDisco(db.PrendeDimensioniBrani());

                        OggettiAVideo.getInstance().ScriveInformazioni();
                    } else {
                        Log.getInstance().ScriveLog("Elimino file. Dimensioni troppo piccole");
                        Utility.getInstance().EliminaFileUnico(sb.getPathBrano());
                    }

                    VariabiliGlobali.getInstance().getFragmentActivityPrincipale().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OggettiAVideo.getInstance().getImgDownloadBrano().setVisibility(LinearLayout.GONE);
                            OggettiAVideo.getInstance().getLayDownload().setVisibility(LinearLayout.GONE);
                        }
                    });

                    // OggettiAVideo.getInstance().getProgressDownload().setVisibility(LinearLayout.GONE);
                }
            }, 100);
        } else {
            if (VariabiliGlobali.getInstance().isSkippatoBrano()) {
                Log.getInstance().ScriveLog("Skippato brano");
            }
            OggettiAVideo.getInstance().getImgDownloadBrano().setVisibility(LinearLayout.GONE);
            OggettiAVideo.getInstance().getLayDownload().setVisibility(LinearLayout.GONE);
            VariabiliGlobali.getInstance().setBloccaDownload(false);
        }

        VariabiliGlobali.getInstance().setSkippatoBrano(false);
    }

}
