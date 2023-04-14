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

    public ScannaDiscoPerImmaginiLocali() {
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.getInstance().ScriveLog("Lettura immagini presenti su disco su path: " +
                VariabiliGlobali.getInstance().getPercorsoIMMAGINI());
    }

    @Override
    protected void onPostExecute(String p) {
        super.onPostExecute(p);

        VariabiliGlobali.getInstance().setListaImmagini(imms);
        VariabiliGlobali.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate: " + imms.size());

        Log.getInstance().ScriveLog("Lettura immagini effettuata. Immagini rilevate: " +
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
            }
        }
    }
}
