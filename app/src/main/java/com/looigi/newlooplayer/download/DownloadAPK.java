package com.looigi.newlooplayer.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.core.content.FileProvider;

import com.looigi.newlooplayer.BuildConfig;
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

public class DownloadAPK extends AsyncTask<String, String, String> {
    String Folder;
    String Path;
    String PathDest;

    public DownloadAPK(String folder, String path, String dest) {
        Folder = folder;
        Path = path;
        PathDest = dest;
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
        Utility.getInstance().CreaCartelle(Folder);

        int count;

        try {
            URL url = new URL(f_url[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty(
                    "Content-Type", "application/vnd.android.package-archive" );
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // urlConnection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = urlConnection.getContentLength();
            // OggettiAVideo.getInstance().getProgressDownload().setMax(lenghtOfFile);

            Log.getInstance().ScriveLog("Download APK. Lunghezza file: " + Long.toString(lenghtOfFile));

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),8192);

            // Output stream
            Log.getInstance().ScriveLog("Download APK. Creazione file output: " + PathDest);
            OutputStream output = new FileOutputStream(PathDest);

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
                Log.getInstance().ScriveLog("Download APK bloccato. Elimino file " + PathDest);
                VariabiliGlobali.getInstance().setBloccaDownload(false);
                Utility.getInstance().EliminaFileUnico(PathDest);
            }
            // VariabiliGlobali.getInstance().setPresentiSuDisco(VariabiliGlobali.getInstance().getPresentiSuDisco() + 1);
        } catch (Exception e) {
            Log.getInstance().ScriveLog("Download APK. Errore: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        /* final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        File fileImagePath = new File(PathDest);
        Uri uri = FileProvider.getUriForFile(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                BuildConfig.APPLICATION_ID + ".fileprovider", fileImagePath);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startActivity(intent); */

        File fileImagePath = new File(PathDest);
        Uri apkUri = FileProvider.getUriForFile(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                BuildConfig.APPLICATION_ID + ".fileprovider", fileImagePath);
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setDataAndType(apkUri,"application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startActivity(intent);
    }
}
