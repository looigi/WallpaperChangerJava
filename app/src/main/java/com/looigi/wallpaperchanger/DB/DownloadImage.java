package com.looigi.wallpaperchanger.DB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.autofill.AutofillId;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger.ChangeWallpaper;
import com.looigi.wallpaperchanger.Log;
import com.looigi.wallpaperchanger.StrutturaImmagine;
import com.looigi.wallpaperchanger.Utility;
import com.looigi.wallpaperchanger.VariabiliGlobali;

import org.kobjects.util.Util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    boolean Errore;
    String NomeImmagine;

    public DownloadImage(String NomeImmagine) {
        this.NomeImmagine = NomeImmagine;
        Utility.getInstance().CreaCartelle(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Download");
    }

    protected Bitmap doInBackground(String... urls) {
        Errore = false;
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);

            FileOutputStream outStream;
            try {
                outStream = new FileOutputStream(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Download/Appoggio.jpg"); // .getPathImmagine());
                if (outStream != null & mIcon11 != null) {
                    mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                }

                outStream.flush();
                outStream.close();

                Log.getInstance().ScriveLog("Immagine Scaricata");
            } catch (FileNotFoundException e) {
                Log.getInstance().ScriveLog("Errore nel salvataggio su download Immagine: " + e.getMessage());
                Errore = true;
            } catch (IOException e) {
                Log.getInstance().ScriveLog("Errore nel salvataggio su download Immagine: " + e.getMessage());
                Errore = true;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            Log.getInstance().ScriveLog("Errore sul download immagine: " + e.getMessage());
            Errore = true;
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (!Errore) {
            StrutturaImmagine si = new StrutturaImmagine();
            si.setPathImmagine(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Download/Appoggio.jpg");
            si.setImmagine(NomeImmagine);

            ChangeWallpaper c = new ChangeWallpaper();
            boolean fatto = c.setWallpaperLocale(si);
            Log.getInstance().ScriveLog("---Immagine impostata online: " + fatto + "---");
        } else {
            Log.getInstance().ScriveLog("Errore sul download immagine.");
        }
    }
}