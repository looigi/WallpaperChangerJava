package com.looigi.newlooplayer;

import android.os.AsyncTask;
import android.widget.LinearLayout;

import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.strutture.StrutturaBrano;
import com.looigi.newlooplayer.strutture.StrutturaBranoPerEliminazione;
import com.looigi.newlooplayer.strutture.StrutturaImmagini;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EliminaBraniDaDisco extends AsyncTask<String, Integer, String> {
    private db_dati db;
    int Dimensioni;
    private boolean popup;
    List<StrutturaBranoPerEliminazione> ListaBrani = new ArrayList<>();

    public EliminaBraniDaDisco(boolean Popup) {
        Dimensioni = 0;
        db = new db_dati();
        ListaBrani = new ArrayList<>();
        popup = Popup;

        OggettiAVideo.getInstance().getLayCaricamento().setVisibility(LinearLayout.VISIBLE);
        OggettiAVideo.getInstance().getTxtCaricamento().setVisibility(LinearLayout.VISIBLE);
        OggettiAVideo.getInstance().getTxtCaricamento().setText("Eliminazione brani in corso");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.getInstance().ScriveLog("Eliminazione Files presenti su disco in base alle regole di settaggio");
    }

    @Override
    protected void onPostExecute(String p) {
        super.onPostExecute(p);

        Log.getInstance().ScriveLog("Eliminazione Files. Files: " + ListaBrani.size() +
                " Dimensioni: " + Dimensioni + " Limite: " + VariabiliGlobali.getInstance().getLimiteInMb());

        for (int i = 0; i < ListaBrani.size(); i++) {
            for (int k = i + 1; k < ListaBrani.size(); k++) {
                if (ListaBrani.get(i).getDimensione() < ListaBrani.get(k).getDimensione()) {
                    String AppoggioNome = ListaBrani.get(i).getNomeFile();
                    int AppoggioDime = ListaBrani.get(i).getDimensione();
                    Date AppoggioData = ListaBrani.get(i).getDataFile();
                    String AppoggioArtista = ListaBrani.get(i).getArtista();
                    String AppoggioAlbum = ListaBrani.get(i).getAlbum();
                    String AppoggioBrano = ListaBrani.get(i).getBrano();

                    ListaBrani.get(i).setNomeFile(ListaBrani.get(k).getNomeFile());
                    ListaBrani.get(i).setDimensione(ListaBrani.get(k).getDimensione());
                    ListaBrani.get(i).setDataFile(ListaBrani.get(k).getDataFile());
                    ListaBrani.get(i).setArtista(ListaBrani.get(k).getArtista());
                    ListaBrani.get(i).setAlbum(ListaBrani.get(k).getAlbum());
                    ListaBrani.get(i).setBrano(ListaBrani.get(k).getBrano());

                    ListaBrani.get(k).setNomeFile(AppoggioNome);
                    ListaBrani.get(k).setDimensione(AppoggioDime);
                    ListaBrani.get(k).setDataFile(AppoggioData);
                    ListaBrani.get(k).setArtista(AppoggioArtista);
                    ListaBrani.get(k).setAlbum(AppoggioAlbum);
                    ListaBrani.get(k).setBrano(AppoggioBrano);
                }
            }
        }

        int QuantiFiles = 0;
        Log.getInstance().ScriveLog("Eliminazione files piccoli");
        for (int i = ListaBrani.size() - 1; i >= 0; i--) {
            if (ListaBrani.get(i).getDimensione() < 1000) {
                String NomeFile = ListaBrani.get(i).getNomeFile();
                Dimensioni -= ListaBrani.get(i).getDimensione();
                int idBrano = db.RicercaBrano(ListaBrani.get(i).getArtista(),
                        ListaBrani.get(i).getAlbum(),
                        ListaBrani.get(i).getBrano());

                Utility.getInstance().EliminaFileUnico(NomeFile);
                db.EliminaBrano(Integer.toString(idBrano));
                if (VariabiliGlobali.getInstance().getPresentiSuDisco() != null) {
                    VariabiliGlobali.getInstance().setPresentiSuDisco(VariabiliGlobali.getInstance().getPresentiSuDisco() - 1);
                }
                VariabiliGlobali.getInstance().setSpazioOccupatoSuDisco(VariabiliGlobali.getInstance().getSpazioOccupatoSuDisco() - ListaBrani.get(i).getDimensione());

                ScriveTesto("Eliminazione per piccolo id " + idBrano);
                Log.getInstance().ScriveLog("Eliminazione piccolo: id " +idBrano + " File: " + NomeFile + ". Dimensione: " + ListaBrani.get(i).getDimensione());

                QuantiFiles++;
            }
        }

        int Progressivo = 0;
        int Limite = ((VariabiliGlobali.getInstance().getLimiteInMb() * 1024) * VariabiliGlobali.getInstance().PercentualeDiEliminazioneBrani) /100 ;
        while (Dimensioni > Limite) {
            String NomeFile = ListaBrani.get(Progressivo).getNomeFile();
            Dimensioni -= ListaBrani.get(Progressivo).getDimensione();
            int idBrano = db.RicercaBrano(ListaBrani.get(Progressivo).getArtista(),
                    ListaBrani.get(Progressivo).getAlbum(),
                    ListaBrani.get(Progressivo).getBrano());

            Utility.getInstance().EliminaFileUnico(NomeFile);
            db.EliminaBrano(Integer.toString(idBrano));
            // if (VariabiliGlobali.getInstance().getPresentiSuDisco() != null) {
            //     VariabiliGlobali.getInstance().setPresentiSuDisco(VariabiliGlobali.getInstance().getPresentiSuDisco() - 1);
            // }
            // VariabiliGlobali.getInstance().setSpazioOccupatoSuDisco(VariabiliGlobali.getInstance().getSpazioOccupatoSuDisco() - ListaBrani.get(Progressivo).getDimensione());

            ScriveTesto("Eliminazione id " + idBrano);
            Log.getInstance().ScriveLog("Eliminazione id " +idBrano + " File: " + NomeFile + ". Dimensione: " + ListaBrani.get(Progressivo).getDimensione());

            Progressivo++;

            QuantiFiles++;
        }
        Log.getInstance().ScriveLog("Eliminati " + QuantiFiles + " Files");

        VariabiliGlobali.getInstance().setPresentiSuDisco(db.ContaBrani());
        VariabiliGlobali.getInstance().setSpazioOccupatoSuDisco(db.PrendeDimensioniBrani());

        OggettiAVideo.getInstance().ScriveInformazioni();

        OggettiAVideo.getInstance().getLayCaricamento().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getTxtCaricamento().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getTxtCaricamento().setText("");

        if (!popup) {
            if (!VariabiliGlobali.getInstance().isGiaAvviato()) {
                Utility.getInstance().ProsegueAvvio();
            }
            VariabiliGlobali.getInstance().setGiaAvviato(true);
        } else {
            Utility.getInstance().VisualizzaErrore("Pulizia eseguita.\n\nFiles eliminati: " + QuantiFiles + "\nDimensioni attuali: " + Dimensioni);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        File rootPrincipale = new File(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Brani");
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
                // Log.d("", "Dir: " + f.getAbsoluteFile());
                walk(f);
            } else {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file

                String CartelleBrano = Filetto.replace(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Brani/", "");
                String[] C = CartelleBrano.split("/");
                String Artista = C[0];
                String Aa = C[1];
                String[] A = Aa.split("-");
                String Album = A[1];
                String Anno = A[0];
                String[] N = Nome.split("-");
                String Traccia = N[0];
                // String[] E = N[1].split(".");
                String Estensione = "";
                if (N[1].toUpperCase().contains(".MP3")) {
                    Estensione = ".mp3";
                } else {
                    if (N[1].toUpperCase().contains(".WMA")) {
                        Estensione = ".wma";
                    }
                }
                Nome = Nome.replace(Estensione, "");

                int dime = Utility.getInstance().DimensioniFile(Filetto);
                Dimensioni += dime;
                Date data = Utility.getInstance().DataFile(Filetto);

                StrutturaBranoPerEliminazione s = new StrutturaBranoPerEliminazione();
                s.setNomeFile(Filetto);
                s.setDimensione(dime);
                s.setDataFile(data);
                s.setArtista(Artista);
                s.setAlbum(Album);
                s.setBrano(Nome);

                ListaBrani.add(s);

                ScriveTesto("Elaborazione files: " + ListaBrani.size());
            }
        }
    }

    private void ScriveTesto(String Cosa) {
        VariabiliGlobali.getInstance().getFragmentActivityPrincipale().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OggettiAVideo.getInstance().getTxtCaricamento().setText(Cosa);
            }
        });
    }
}
