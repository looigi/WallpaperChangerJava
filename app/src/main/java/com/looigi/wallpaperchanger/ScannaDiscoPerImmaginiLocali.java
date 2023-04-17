package com.looigi.wallpaperchanger;

import android.os.AsyncTask;
import android.widget.LinearLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScannaDiscoPerImmaginiLocali extends AsyncTask<String, Integer, String> {
    private List<StrutturaImmagine> imms = new ArrayList<>();
    private db_dati db;

    public ScannaDiscoPerImmaginiLocali() {
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        VariabiliGlobali.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
        db = new db_dati();
        db.EliminaImmaginiInLocale();

        Log.getInstance().ScriveLog("Lettura immagini presenti su disco su path: " +
                VariabiliGlobali.getInstance().getPercorsoIMMAGINI());
    }

    @Override
    protected void onPostExecute(String p) {
        super.onPostExecute(p);

        VariabiliGlobali.getInstance().setListaImmagini(imms);
        if(VariabiliGlobali.getInstance().isOffline()) {
            VariabiliGlobali.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate su disco: " + imms.size());
        }

        VariabiliGlobali.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        Log.getInstance().ScriveLog("Lettura immagini effettuata. Immagini rilevate su disco: " +
            imms.size());
    }

    @Override
    protected String doInBackground(String... strings) {
        File rootPrincipale = new File(VariabiliGlobali.getInstance().getPercorsoIMMAGINI());
        if (!rootPrincipale.exists()) {
            rootPrincipale.mkdir();
        }
        walk(rootPrincipale);

        return null;
    }

    private void walk(File root) {
        File[] list = root.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
                walk(f);
            } else {
                StrutturaImmagine si = new StrutturaImmagine();

                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file

                si.setImmagine(Nome);
                si.setPathImmagine(Filetto);

                imms.add(si);

                db.ScriveImmagineInLocale(Nome, Filetto);
            }
        }
    }
}
