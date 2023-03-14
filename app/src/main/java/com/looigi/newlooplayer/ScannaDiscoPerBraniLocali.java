package com.looigi.newlooplayer;

import android.os.AsyncTask;
import android.widget.LinearLayout;

import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.strutture.StrutturaBrano;
import com.looigi.newlooplayer.strutture.StrutturaBranoDB;
import com.looigi.newlooplayer.strutture.StrutturaImmagini;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScannaDiscoPerBraniLocali extends AsyncTask<String, Integer, String> {
    private List<StrutturaBrano> BraniInLocale = new ArrayList<>();
    private List<StrutturaImmagini> imms = new ArrayList<>();
    private db_dati db;

    public ScannaDiscoPerBraniLocali() {
        VariabiliGlobali.getInstance().setPresentiSuDisco(0);
        BraniInLocale = new ArrayList<>();
        db = new db_dati();
        OggettiAVideo.getInstance().getLayCaricamento().setVisibility(LinearLayout.VISIBLE);
        OggettiAVideo.getInstance().getTxtCaricamento().setVisibility(LinearLayout.VISIBLE);
        OggettiAVideo.getInstance().getTxtCaricamento().setText("Caricamento brani in corso");
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.getInstance().ScriveLog("Lettura Files presenti su disco");
    }

    @Override
    protected void onPostExecute(String p) {
        super.onPostExecute(p);

        // VariabiliGlobali.getInstance().setPresentiSuDisco(BraniInLocale.size());
        // VariabiliGlobali.getInstance().setBraniInLocale(BraniInLocale);
        // StringBuilder s = new StringBuilder();
        /* for (int i = 0; i < BraniInLocale.size(); i++) {
            dime += BraniInLocale.get(i).getDimensione();

            if (BraniInLocale.get(i).getIdBrano() == null) {
                BraniInLocale.get(i).setIdBrano(i);
            }
            db.AggiungeBrano(BraniInLocale.get(i));
            s.append(BraniInLocale.get(i).getIdBrano()).append(";");
            s.append(BraniInLocale.get(i).getQuantiBrani()).append(";");
            s.append(BraniInLocale.get(i).getArtista().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
            s.append(BraniInLocale.get(i).getAlbum().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
            s.append(BraniInLocale.get(i).getBrano().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
            s.append(BraniInLocale.get(i).getAnno()).append(";");
            s.append(BraniInLocale.get(i).getTraccia()).append(";");
            s.append(BraniInLocale.get(i).getEstensione()).append(";");
            s.append(BraniInLocale.get(i).getData()).append(";");
            s.append(BraniInLocale.get(i).getDimensione()).append(";");
            s.append(BraniInLocale.get(i).getAscoltata()).append(";");
            s.append(BraniInLocale.get(i).getBellezza()).append(";");
            s.append(BraniInLocale.get(i).getTesto().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
            s.append(BraniInLocale.get(i).getTestoTradotto().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
            s.append(BraniInLocale.get(i).getUrlBrano().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
            s.append(BraniInLocale.get(i).getPathBrano().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
            s.append(BraniInLocale.get(i).getCartellaBrano().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
            s.append(BraniInLocale.get(i).isEsisteBranoSuDisco()).append(";");
            if (BraniInLocale.get(i).getTags() != null) {
                s.append(BraniInLocale.get(i).getTags().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
            } else {
                s.append(";");
            }
            s.append("|");

            // Immagini
            for (int k = 0; k < BraniInLocale.get(i).getImmagini().size(); k++) {
                s.append(BraniInLocale.get(i).getImmagini().get(k).getAlbum().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
                s.append(BraniInLocale.get(i).getImmagini().get(k).getNomeImmagine().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
                s.append(BraniInLocale.get(i).getImmagini().get(k).getUrlImmagine().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
                s.append(BraniInLocale.get(i).getImmagini().get(k).getPathImmagine().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*")).append(";");
                s.append(BraniInLocale.get(i).getImmagini().get(k).getCartellaImmagine().replace(";", "*PV*").replace("|", "*P*").replace("§", "*SS*"));
                s.append("£");
            }
            // Immagini

            s.append("§");
        } */

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = formatter.format(date);
        Utility.getInstance().EliminaFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "BraniInLocale.txt");
        Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Liste", "BraniInLocale.txt", strDate);

        VariabiliGlobali.getInstance().setPresentiSuDisco(db.ContaBrani());
        VariabiliGlobali.getInstance().setSpazioOccupatoSuDisco(db.PrendeDimensioniBrani());

        OggettiAVideo.getInstance().ScriveInformazioni();

        OggettiAVideo.getInstance().getLayCaricamento().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getTxtCaricamento().setVisibility(LinearLayout.GONE);
        OggettiAVideo.getInstance().getTxtCaricamento().setText("");
    }

    @Override
    protected String doInBackground(String... strings) {
        File rootPrincipale = new File(VariabiliGlobali.getInstance().getPercorsoDIR() + "/Brani");
        if (!rootPrincipale.exists()) {
            rootPrincipale.mkdir();
        }
        walk(rootPrincipale);

        int dime = 0;
        for (int i = 0; i < BraniInLocale.size(); i++) {
            dime += BraniInLocale.get(i).getDimensione();

            if (BraniInLocale.get(i).getIdBrano() == null) {
                BraniInLocale.get(i).setIdBrano(i);
            }
            db.AggiungeBrano(BraniInLocale.get(i));
        }
        // VariabiliGlobali.getInstance().setSpazioOccupatoSuDisco(dime);
        Log.getInstance().ScriveLog("Files presenti su disco: " + BraniInLocale.size());
        Log.getInstance().ScriveLog("Dimensione Files presenti su disco: " + Integer.toString(dime));

        return null;
    }

    private void walk(File root) {
        File[] list = root.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
                // Log.d("", "Dir: " + f.getAbsoluteFile());
                walk(f);
            } else {
                // Log.d("", "File: " + f.getAbsoluteFile());
                StrutturaBrano sb = new StrutturaBrano();

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

                sb.setPathBrano(Filetto);
                sb.setArtista(Artista);
                sb.setAlbum(Album);
                sb.setAnno(Anno);
                sb.setTraccia(Traccia);
                sb.setBrano(Nome);
                sb.setEstensione(Estensione);
                String UrlBrano = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() +
                        "/MP3/" + Artista + "/" + Anno + "-" + Album + "/" +
                        Traccia + "-" + Nome + Estensione;
                sb.setUrlBrano(UrlBrano);
                String CartellaBrano = VariabiliGlobali.getInstance().getPercorsoDIR() +
                        "/Brani/" + Artista + "/" + Anno + "-" + Album;
                sb.setCartellaBrano(CartellaBrano);

                imms = new ArrayList<>();
                String PathImmagine = VariabiliGlobali.getInstance().getPercorsoDIR() +
                        "/ImmaginiMusica/" + Artista + "/" + Anno + "-" + Album;
                // Log.getInstance().ScriveLog("-------------" + PathImmagine);
                File rootImmagini = new File(PathImmagine);
                if (rootImmagini.exists()) {
                    String CartellaImmagine = VariabiliGlobali.getInstance().getPercorsoDIR() +
                            "/ImmaginiMusica/" + Artista + "/" + Anno + "-" + Album;
                    String UrlImmagine = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() +
                            "/ImmaginiMusica/" + Artista + "/" + Anno + "-" + Album + "/";
                    scannaImmagini(rootImmagini, Album, UrlImmagine, CartellaImmagine);
                }
                sb.setImmagini(imms);
                int dime = Utility.getInstance().DimensioniFile(Filetto);
                sb.setDimensione(dime);

                /* String CHIAVE = Artista.toUpperCase().trim() + "-" + Anno + Album.toUpperCase().trim() + "-" + Nome.toUpperCase().trim();
                db_dati db = new db_dati();
                StrutturaBranoDB Ritorno = db.CercaBrano(CHIAVE);
                if (!Ritorno.getNomeBrano().contains("ERROR:")) {
                    sb.setIdBrano(Integer.parseInt(Ritorno.getId()));
                    sb.setTesto(Ritorno.getTesto());
                    sb.setTestoTradotto(Ritorno.getTestoTradotto());
                    sb.setBellezza(Integer.parseInt(Ritorno.getBellezza()));
                    sb.setAscoltata(Integer.parseInt(Ritorno.getAscoltata()));
                } else {
                    sb.setIdBrano(-1);
                    sb.setTesto("");
                    sb.setTestoTradotto("");
                    sb.setBellezza(0);
                    sb.setAscoltata(0);
                } */

                String CartellaTesto = VariabiliGlobali.getInstance().getPercorsoDIR() +
                        "/Testi/" + Artista + "/" + Anno + "-" + Album;
                // LETTURA TESTO
                String NomeFile = Nome + ".txt";
                String Testo = Utility.getInstance().LeggeFile(CartellaTesto, NomeFile);
                if (!Testo.contains("ERROR:")) {
                    sb.setTesto(Testo);
                } else {
                    sb.setTesto("");
                }
                sb.setTestoTradotto("");

                // LETTURA BELLEZZA / ASCOLTATA
                NomeFile = Nome + ".2.txt";
                String Altro = Utility.getInstance().LeggeFile(CartellaTesto, NomeFile);
                if (!Altro.contains("ERROR:")) {
                    String[] Cosa = Altro.replace("\n", "").split(";");
                    sb.setBellezza(Integer.parseInt(Cosa[0]));
                    sb.setAscoltata(Integer.parseInt(Cosa[1]));
                } else {
                    sb.setBellezza(0);
                    sb.setAscoltata(0);
                }

                // LETTURA TAGS
                NomeFile = Nome + ".TAGS.txt";
                String Tags = Utility.getInstance().LeggeFile(CartellaTesto, NomeFile);
                if (!Tags.contains("ERROR:")) {
                    sb.setTags(Tags);
                } else {
                    sb.setTags("");
                }

                // LETTURA DATE
                NomeFile = Nome + ".DATA.txt";
                String Data = Utility.getInstance().LeggeFile(CartellaTesto, NomeFile);
                if (!Data.contains("ERROR:")) {
                    sb.setData(Data);
                } else {
                    sb.setData("");
                }

                sb.setEsisteBranoSuDisco(true);

                BraniInLocale.add(sb);

                VariabiliGlobali.getInstance().getFragmentActivityPrincipale().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        OggettiAVideo.getInstance().getTxtCaricamento().setText("Caricamento brani in corso: " + (BraniInLocale.size() + 1));
                    }
                });
            }
        }
    }

    private void scannaImmagini(File root, String Album, String UrlImmagine, String CartellaImmagini) {
        File[] list = root.listFiles();

        for (File f : list) {
            if (f.isDirectory()) {
                scannaImmagini(f, Album, UrlImmagine, CartellaImmagini);
            } else {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                if (!Filetto.toUpperCase().contains(".DB")) {
                    String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file

                    StrutturaImmagini si = new StrutturaImmagini();
                    si.setPathImmagine(Filetto);
                    si.setAlbum(Album);
                    si.setCartellaImmagine(CartellaImmagini);
                    si.setUrlImmagine(UrlImmagine + Nome);
                    si.setNomeImmagine(Nome);

                    imms.add(si);
                }
            }
        }
    }
}
