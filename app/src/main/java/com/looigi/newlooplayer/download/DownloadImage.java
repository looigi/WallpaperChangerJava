package com.looigi.newlooplayer.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.notifiche.Notifica;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    String Immagine;
    String PathImmagine;
    String CartellaImmagine;
    boolean Errore;

    public DownloadImage(ImageView bmImage, String Immagine) {
        this.bmImage = bmImage;
        this.Immagine = Immagine;
        PathImmagine = Immagine;
        PathImmagine = PathImmagine.replace(VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() + "/", "");
        PathImmagine = VariabiliGlobali.getInstance().getPercorsoDIR() + "/" + PathImmagine;
        CartellaImmagine = "";
        String[] c = PathImmagine.split("/");
        for (int i = 0; i < c.length - 1; i++) {
            CartellaImmagine += c[i] + "/";
        }
    }

    protected Bitmap doInBackground(String... urls) {
        Errore = false;
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            // e.printStackTrace();
            Log.getInstance().ScriveLog("Errore sul download immagine: " + e.getMessage());
            Errore = true;
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (!Errore) {
            OggettiAVideo.getInstance().getImgSfondo().setVisibility(LinearLayout.VISIBLE);
            OggettiAVideo.getInstance().getImgSfondoLogo().setVisibility(LinearLayout.GONE);

            bmImage.setImageBitmap(result);

            BitmapDrawable drawable = (BitmapDrawable) bmImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            Log.getInstance().ScriveLog("URL per salvataggio immagine: " + this.Immagine);
            Log.getInstance().ScriveLog("Creo cartelle per salvataggio immagine: " + this.CartellaImmagine);
            Log.getInstance().ScriveLog("Path salvataggio immagine: " + this.PathImmagine);
            Utility.getInstance().CreaCartelle(this.CartellaImmagine); // .getCartellaImmagine());

            FileOutputStream outStream;
            try {
                outStream = new FileOutputStream(this.PathImmagine); // .getPathImmagine());
                if (outStream != null & bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                }
                /* 100 to keep full quality of the image */

                outStream.flush();
                outStream.close();

                Log.getInstance().ScriveLog("Immagine Scaricata: " + this.PathImmagine);
                Notifica.getInstance().setImmagine(this.PathImmagine);
                Notifica.getInstance().AggiornaNotifica();
            } catch (FileNotFoundException e) {
                Log.getInstance().ScriveLog("Errore nel salvataggio su download Immagine: " + e.getMessage());
            } catch (IOException e) {
                Log.getInstance().ScriveLog("Errore nel salvataggio su download Immagine: " + e.getMessage());
            }
        } else {
            Log.getInstance().ScriveLog("Errore sul download immagine. Imposto logo");

            OggettiAVideo.getInstance().getImgSfondo().setVisibility(LinearLayout.GONE);
            OggettiAVideo.getInstance().getImgSfondoLogo().setVisibility(LinearLayout.VISIBLE);
        }
    }
}