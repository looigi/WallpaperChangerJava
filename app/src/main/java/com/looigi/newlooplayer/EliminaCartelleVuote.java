package com.looigi.newlooplayer;

import android.os.AsyncTask;
import android.widget.LinearLayout;

import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.strutture.StrutturaBrano;
import com.looigi.newlooplayer.strutture.StrutturaImmagini;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EliminaCartelleVuote extends AsyncTask<String, Integer, String> {
    private List<String> daEliminare = new ArrayList<>();

    public EliminaCartelleVuote() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String p) {
        super.onPostExecute(p);

        Collections.sort(daEliminare);
        for (int i = daEliminare.size() - 1; i >= 0; i--) {
            String Path = daEliminare.get(i);
            Log.getInstance().ScriveLog("Eliminazione cartella vuota: " + Path);
            File d = new File(Path);
            try {
                boolean rit = d.delete();
                if (!rit) {
                    Log.getInstance().ScriveLog("Eliminazione cartella vuota non riuscita");
                }
            } catch (Exception ignored) {
                Log.getInstance().ScriveLog("Eliminazione cartella vuota. Errore: " + Utility.getInstance().PrendeErroreDaException(ignored));
            }
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        File rootPrincipale = new File(VariabiliGlobali.getInstance().getPercorsoDIR() + "/");
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
                int quantiFiles = f.listFiles().length;
                if (quantiFiles == 0) {
                    daEliminare.add(f.getPath());
                }
                walk(f);
            }
        }
    }
}
