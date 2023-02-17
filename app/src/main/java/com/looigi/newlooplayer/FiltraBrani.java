/* package com.looigi.newlooplayer;

import android.icu.util.ValueIterator;
import android.os.Handler;
import android.os.Looper;

import com.looigi.newlooplayer.strutture.DettaglioBrano;
import com.looigi.newlooplayer.strutture.StrutturaArtisti;
import com.looigi.newlooplayer.strutture.StrutturaBrano;
import com.looigi.newlooplayer.strutture.StrutturaImmagini;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FiltraBrani {
    private Runnable runTimer;
    private Handler handlerTimer;

    public void FiltraLista(boolean ApreDialog) {
        if (ApreDialog) {
            Utility.getInstance().ApriDialog(true,"Filtraggio brani");

            handlerTimer = new Handler(Looper.getMainLooper());
            handlerTimer.postDelayed(runTimer = new Runnable() {
                @Override
                public void run() {
                    FiltraListaFunzione();
                }
            }, 100);
        } else {
            FiltraListaFunzione();
        }
    }

    private void FiltraListaFunzione() {
        List<DettaglioBrano> Ritorno = new ArrayList<>();

        for (int i = 0; i < VariabiliGlobali.getInstance().getListaBraniCompleta().size(); i++) {
            String NomeArtista = VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getArtista();
            String NomeAlbum = VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getAlbum();
            String AnnoAlbum = VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getAnno();
            String ID = Integer.toString(VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getIdBrano());
            String Titolo = VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getTitolo();
            String Traccia = Integer.toString(VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getTraccia());
            if (Traccia.length() == 1) { Traccia = "0" + Traccia; }
            String Tags = VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getTags();
            String Ascoltata = Integer.toString(VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getAscoltata());
            String Estensione = VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getEstensione();
            int Stelle = VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getStelle();
            boolean presenteSuDisco = VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).isPresenteSuDisco();
            String PathBrano = VariabiliGlobali.getInstance().getListaBraniCompleta().get(i).getUrlBrano();

            boolean ok = true;

            if (VariabiliGlobali.getInstance().isRicercaPreferiti()) {
                if (!VariabiliGlobali.getInstance().getPreferiti().contains(";" + NomeArtista + ";")) {
                    ok = false;
                }
            }

            if (ok) {
                if (VariabiliGlobali.getInstance().isRicercaTesto()) {
                    ok = false;

                    String RicercaTesto = VariabiliGlobali.getInstance().getTestoDaRicercare().toUpperCase().trim();
                    if (Titolo.toUpperCase().contains(RicercaTesto) || NomeAlbum.toUpperCase().contains(RicercaTesto) || NomeArtista.toUpperCase().contains(RicercaTesto)) {
                        ok = true;
                    }
                }
            }

            if (ok) {
                if (VariabiliGlobali.getInstance().isBranoSuSD()) {
                    if (!presenteSuDisco) {
                        ok = false;
                    }
                }
            }

            if (ok) {
                if (VariabiliGlobali.getInstance().isRicercaEsclusione()) {
                    String RicercaTesto = VariabiliGlobali.getInstance().getTestoDaEscludere().toUpperCase().trim();
                    if (Titolo.toUpperCase().contains(RicercaTesto) || NomeAlbum.toUpperCase().contains(RicercaTesto) || NomeArtista.toUpperCase().contains(RicercaTesto)) {
                        ok = false;
                    }
                }
            }

            if (ok) {
                if (VariabiliGlobali.getInstance().isRicercaStelle()) {
                    if (Stelle < VariabiliGlobali.getInstance().getStelleDaRicercare()) {
                        ok = false;
                    }
                }
            }

            if (ok) {
                if (VariabiliGlobali.getInstance().isRicercaMaiAscoltata()) {
                    if (Stelle != 0) {
                        ok = false;
                    }
                }
            }

            if (ok) {
                DettaglioBrano db = new DettaglioBrano();
                db.setIdBrano(Integer.parseInt(ID));
                db.setArtista(NomeArtista);
                db.setAlbum(NomeAlbum);
                db.setAnno(AnnoAlbum);
                db.setTitolo(Titolo);
                db.setTags(Tags);
                if (Ascoltata.isEmpty()) {
                    db.setAscoltata(0);
                } else {
                    db.setAscoltata(Integer.parseInt(Ascoltata));
                }
                if (Traccia.isEmpty()) {
                    db.setTraccia(0);
                } else {
                    db.setTraccia(Integer.parseInt(Traccia));
                }
                db.setStelle(Stelle);
                db.setUrlBrano(PathBrano);
                db.setPresenteSuDisco(presenteSuDisco);

                Ritorno.add(db);
            }
        }

        VariabiliGlobali.getInstance().setListaBraniFiltrata(Ritorno);
        // OggettiAVideo.getInstance().ScriveInformazioni();

        Utility.getInstance().ChiudeDialog();

        ControllaSeBranoPresenteInLista();
        // return "";
    }

    private void ControllaSeBranoPresenteInLista() {
        DettaglioBrano sb = new DettaglioBrano();
        int idBrano = VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato();
        boolean ok = false;
        for (int i = 0; i < VariabiliGlobali.getInstance().getListaBraniFiltrata().size(); i++) {
            if (VariabiliGlobali.getInstance().getListaBraniFiltrata().get(i).getIdBrano() == idBrano) {
                sb = VariabiliGlobali.getInstance().getListaBraniFiltrata().get(i);
                ok = true;
                break;
            }
        }
        if (!ok) {
            Utility.getInstance().AvantiBrano();
        } else {
            Utility.getInstance().CaricamentoBrano("");

            OggettiAVideo.getInstance().ScriveInformazioni(sb);
        }
    }

    public String CreaLista() {
        Utility.getInstance().ApriDialog(true, "Crezione lista brani");

        handlerTimer = new Handler(Looper.getMainLooper());
        handlerTimer.postDelayed(runTimer = new Runnable() {
            @Override
            public void run() {
                CreaListaFunzione();
            }
        }, 100);

        return "";
    }

    private void CreaListaFunzione() {
        JSONArray ListaBrani = VariabiliGlobali.getInstance().getListaBraniCompletaInJSON();
        List<DettaglioBrano> Ritorno = new ArrayList<>();
        int presentiSuDisco = 0;
        VariabiliGlobali.getInstance().PulisceArtisti();

        try {
            Ritorno = new ArrayList<DettaglioBrano>();

            for (int i = 0; i < ListaBrani.length(); i++) {
                List<String> listaTags = new ArrayList<String>();
                JSONObject Artista = ListaBrani.getJSONObject(i);
                String NomeArtista = Artista.getString("text");

                // JSONObject AlbumDaInserire = new JSONObject();

                JSONArray Albums = Artista.getJSONArray("children");
                for (int k = 0; k < Albums.length(); k++) {
                    JSONObject Album = Albums.getJSONObject(k);
                    String NomeAlbum = Album.getString("text");
                    String AnnoAlbum = Album.getString("Anno");
                    for (int ii = AnnoAlbum.length() + 1; ii < 5; ii++) {
                        AnnoAlbum = "0" + AnnoAlbum;
                    }

                    // JSONArray BraniDaInserire = new JSONArray();

                    JSONArray Brani = Album.getJSONArray("children");
                    for (int z = 0; z < Brani.length(); z++) {
                        JSONObject Brano = Brani.getJSONObject(z);

                        String ID = Brano.getString("id");
                        String Titolo = Brano.getString("Text");
                        String Traccia = Brano.getString("Traccia");
                        if (Traccia.isEmpty()) {
                            Traccia = "00";
                        } else {
                            if (Traccia.length() == 1) {
                                Traccia = "0" + Traccia;
                            }
                        }
                        String Tags = Brano.getString("Tags");
                        if (!Tags.isEmpty()) {
                            String[] lt = Tags.split("§");
                            for (int l1 = 0; l1 < lt.length; l1++) {
                                String tag = lt[l1];
                                if (!tag.isEmpty() & tag.contains(";")) {
                                    String[] tag2 = tag.split(";");
                                    boolean ok = true;
                                    for (int l2 = 0; l2 < listaTags.size(); l2++) {
                                        if (listaTags.get(l2).equals(tag2[2])) {
                                            ok = false;
                                            break;
                                        }
                                    }
                                    if (ok) {
                                        listaTags.add(tag2[2]);
                                    }
                                }
                            }
                        }
                        String Ascoltata = Brano.getString("Ascoltata");
                        String Estensione = Brano.getString("Estensione");
                        int Stelle = Integer.parseInt(Brano.getString("Stelle"));

                        String PathBrano = VariabiliGlobali.getInstance().getPercorsoDIR() +
                                "/Brani/" + NomeArtista + "/" + AnnoAlbum + "-" + NomeAlbum + "/" +
                                Traccia + "-" + Titolo + Estensione;
                        boolean presenteSuDisco;
                        File f = new File(PathBrano);
                        if (f.exists()) {
                            presenteSuDisco = true;
                            presentiSuDisco++;
                        } else {
                            presenteSuDisco = false;
                        }

                            DettaglioBrano db = new DettaglioBrano();
                            db.setIdBrano(Integer.parseInt(ID));
                            db.setArtista(NomeArtista);
                            db.setAlbum(NomeAlbum);
                            db.setAnno(AnnoAlbum);
                            db.setTitolo(Titolo);
                            db.setTags(Tags);
                            if (Ascoltata.isEmpty()) {
                                db.setAscoltata(0);
                            } else {
                                db.setAscoltata(Integer.parseInt(Ascoltata));
                            }
                            if (Traccia.isEmpty()) {
                                db.setTraccia(0);
                            } else {
                                db.setTraccia(Integer.parseInt(Traccia));
                            }
                            db.setStelle(Stelle);
                            db.setUrlBrano(PathBrano);
                            db.setPresenteSuDisco(presenteSuDisco);
                            db.setEstensione(Estensione);

                            Ritorno.add(db);

                            // BraniDaInserire.put(Brano);
                        // }
                    }
                }

                StrutturaArtisti sa = new StrutturaArtisti();
                sa.setNomeArtista(NomeArtista);
                sa.setTags(listaTags);

                String pathImmagine = VariabiliGlobali.getInstance().getPercorsoDIR() + "/Immagini/" + NomeArtista;
                File f = new File(pathImmagine);
                File[] files = f.listFiles();
                if (files != null) {
                    if (files.length > 0) {
                        int q = files.length;
                        int random = Utility.getInstance().GeneraNumeroRandom(q);
                        try {
                            String nomeFile = files[random].getAbsolutePath() + "/" + ((File) files[random]).listFiles()[0].getName();
                            sa.setImmagine(nomeFile);
                        } catch (Exception ignored) {
                            sa.setImmagine("");
                        }
                    } else {
                        sa.setImmagine("");
                    }
                } else {
                    sa.setImmagine("");
                }

                VariabiliGlobali.getInstance().AggiungeArtista(sa);
            }

            VariabiliGlobali.getInstance().setListaBraniCompleta(Ritorno);
            VariabiliGlobali.getInstance().setPresentiSuDisco(presentiSuDisco);

            Log.getInstance().ScriveLog("Richiamo Filtraggio lista da creazione");

            VariabiliGlobali.getInstance().setListaBraniCompletaInJSON(null);
            VariabiliGlobali.getInstance().setBraniTotali(Ritorno.size());

            // Utility.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPercorsoDIR(), "ListaBrani.txt", Ritorno.toString());

            AdapterListener customAdapter = new AdapterListener(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                    VariabiliGlobali.getInstance().getArtisti());
            OggettiAVideo.getInstance().getLstArtisti().setAdapter(customAdapter);

            FiltraLista(false);
        } catch (JSONException e) {
            // return e.getMessage();
        }
    }
}
*/