package com.looigi.newlooplayer.db_locale;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.strutture.StrutturaBrano;
import com.looigi.newlooplayer.strutture.StrutturaImmagini;
import com.looigi.newlooplayer.strutture.StrutturaListaPreferiti;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class db_dati {
    private String PathDB = VariabiliGlobali.getInstance().getPercorsoDIR()+"/DB/";
    private String NomeDB = "dati.db";
    private SQLiteDatabase myDB;

    public db_dati() {
        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception ignored) {

        }
        myDB = ApreDB();
    }

    private SQLiteDatabase ApreDB() {
        SQLiteDatabase db = null;
        try {
            db = VariabiliGlobali.getInstance().getContext().openOrCreateDatabase(
                    PathDB + NomeDB, MODE_PRIVATE, null);
        } catch (Exception e) {
            Log.getInstance().ScriveLog("ERRORE Nell'apertura del db: " + Utility.getInstance().PrendeErroreDaException(e));
        }
        return  db;
    }

    public void CreazioneTabelle() {
        try {
            // SQLiteDatabase myDB = ApreDB();
            if (myDB != null) {
                myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                        + "Utente "
                        + "(id INT(7));");
                myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                        + "Ultima "
                        + "(id INT(7));");
                myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                        + "Preferiti "
                        + "(Pref VARCHAR, Eliminati VARCHAR);");
                myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                        + "Tags "
                        + "(Tag VARCHAR, Eliminati VARCHAR);");
                myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                        + "BraniInLocale "
                        + " (idBrano VARCHAR, quantiBrani VARCHAR,  Artista VARCHAR,  Album VARCHAR,  Brano VARCHAR, "
                        + "Anno VARCHAR,  Traccia VARCHAR,  Estensione VARCHAR, Data VARCHAR,  Dimensione VARCHAR, "
                        + "Ascoltata VARCHAR, Bellezza VARCHAR, Testo VARCHAR, TestoTradotto VARCHAR, UrlBrano VARCHAR, "
                        + "PathBrano VARCHAR, CartellaBrano VARCHAR, EsisteBranoSuDisco VARCHAR, Tags VARCHAR);");
                myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                        + "ImmaginiInLocale "
                        + "(idBrano VARCHAR, Progressivo VARCHAR, Album VARCHAR, NomeImmagine VARCHAR, "
                        + "UrlImmagine VARCHAR, PathImmagine VARCHAR, CartellaImmagine VARCHAR)");
                myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                        + "ListePreferiti "
                        + "(NomePreferito VARCHAR, Preferito VARCHAR, PreferitoElimina VARCHAR, Tags VARCHAR, TagsElimina VARCHAR)");
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni "
                        + " (RicercaTesto VARCHAR, RicercaEsclusione VARCHAR, RicercaStelle VARCHAR, " +
                        "RicercaMaiAscoltata VARCHAR, RicercaTags VARCHAR, TestoDaRicercare VARCHAR, " +
                        "TestoDaEscludere VARCHAR, StelleDaRicercare VARCHAR, TagsDaRicercare VARCHAR, " +
                        "Random VARCHAR, CambioImmagine VARCHAR, VisuaInfo VARCHAR, SecondiCambioImmagine VARCHAR, " +
                        "branoSuDisco VARCHAR, RicercaPreferiti VARCHAR, Debug VARCHAR, Opacita VARCHAR, SecondiOpacita VARCHAR, " +
                        "MostraOrologio VARCHAR, EliminaBrani VARCHAR, LimiteInMb VARCHAR, DataSuperiore VARCHAR, EdtDataSuperiore VARCHAR, " +
                        "DataInferiore VARCHAR, EdtDataInferiore VARCHAR, Date VARCHAR, ImmaginiDaScaricare VARCHAR);";
                myDB.execSQL(sql);
            }
        } catch (Exception ignored) {
            Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + Utility.getInstance().PrendeErroreDaException(ignored));
        }
    }

    public void RinominaAlbum(String Artista, String VecchioAlbum, String VecchioAnno, String NuovoAlbum, String NuovoAnno) {
        if (myDB != null) {
            // MODIFICA BRANI
            String sql = "Update BraniInLocale Set " +
                    "Album='" + NuovoAlbum + "', Anno='" + NuovoAnno + "' " +
                    "Where Artista='" + Artista + "' And Album='" + VecchioAlbum + "' And Anno='" + VecchioAnno + "'";
            Log.getInstance().ScriveLog("Rinomina album su db: " + sql);
            try {
                myDB.execSQL(sql);
            } catch (Exception ignored) {
                Log.getInstance().ScriveLog("ERRORE Nella rinomina dell'album: " + Utility.getInstance().PrendeErroreDaException(ignored));
            }

            // MODIFICA NOME ALBUM IMMAGINI
            sql = "Update ImmaginiInLocale Set " +
                    "Album='" + NuovoAlbum + "' " +
                    "Where Album='" + VecchioAlbum + "'";
            Log.getInstance().ScriveLog("Rinomina nome album su tabella immagini: " + sql);
            try {
                myDB.execSQL(sql);
            } catch (Exception ignored) {
                Log.getInstance().ScriveLog("ERRORE Nella rinomina del nome album sulle immagini: " + Utility.getInstance().PrendeErroreDaException(ignored));
            }

            // http://looigi.ddns.net:1051/ImmaginiMusica/Within Temptation/2008-Black Symphony/Cover_Within Temptation.jpg
            String nuovoUrl = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() + "/ImmaginiMusica/" + Artista + "/" +
                    NuovoAnno + "-" + NuovoAlbum + "/Cover_" + Artista + ".jpg";
            // /storage/emulated/0/LooigiSoft/looWebPlayer/ImmaginiMusica/Within Temptation/2008-Black Symphony/Cover_Within Temptation.jpg
            String nuovoPath = VariabiliGlobali.getInstance().getPercorsoDIR() + "/ImmaginiMusica/" + Artista + "/" +
                    NuovoAnno + "-" + NuovoAlbum + "/Cover_" + Artista + ".jpg";
            // /storage/emulated/0/LooigiSoft/looWebPlayer/ImmaginiMusica/Within Temptation/2008-Black Symphony
            String nuovaCartella = VariabiliGlobali.getInstance().getPercorsoDIR() + "/ImmaginiMusica/" + Artista + "/" +
                    NuovoAnno + "-" + NuovoAlbum;
            sql = "Update ImmaginiInLocale Set " +
                    "UrlImmagine='" + nuovoUrl + "', " +
                    "PathImmagine='" + nuovoPath + "', " +
                    "CartellaImmagine='" + nuovaCartella + "' " +
                    "Where Album='" + NuovoAlbum + "'";
            Log.getInstance().ScriveLog("Rinomina dati immagine su tabella immagini: " + sql);
            try {
                myDB.execSQL(sql);
            } catch (Exception ignored) {
                Log.getInstance().ScriveLog("ERRORE Nella rinomina dei dati immagine sulle immagini: " + Utility.getInstance().PrendeErroreDaException(ignored));
            }
        }
    }

    public String ContaBraniFiltrati() {
        String quantiBrani = "";

        if (myDB != null) {
            try {
                String Altro = "";
                if (VariabiliGlobali.getInstance().isRicercaTesto()) {
                    String RicercaTesto = VariabiliGlobali.getInstance().getTestoDaRicercare().toUpperCase().trim().replace("'", "''");
                    Altro += "Where (Artista Like '%" + RicercaTesto + "%' Or Album Like '%" + RicercaTesto + "%' Or Brano Like '%" + RicercaTesto + "%') ";
                }

                if (VariabiliGlobali.getInstance().isRicercaEsclusione()) {
                    String RicercaTesto = VariabiliGlobali.getInstance().getTestoDaEscludere().toUpperCase().trim();
                    if (Altro.isEmpty()) {
                        Altro = "Where (Artista Not Like '%" + RicercaTesto + "%' And Album Not Like '%" + RicercaTesto + "%' And Brano Not Like '%" + RicercaTesto + "%') ";
                    } else {
                        Altro += " And (Artista Not Like '%" + RicercaTesto + "%' And Album Not Like '%" + RicercaTesto + "%' And Brano Not Like '%" + RicercaTesto + "%') ";
                    }
                }

                // if (VariabiliGlobali.getInstance().isRetePresente()) {
                if (VariabiliGlobali.getInstance().isRicercaStelle()) {
                    int Stelle = VariabiliGlobali.getInstance().getStelleBrano();
                    if (Stelle == 0) {
                        if (Altro.isEmpty()) {
                            Altro = "Where Bellezza = 0 ";
                        } else {
                            Altro += "And Bellezza = 0 ";
                        }
                    } else {
                        if (Altro.isEmpty()) {
                            Altro = "Where Bellezza >= " + Stelle + " ";
                        } else {
                            Altro += "And Bellezza >= " + Stelle + " ";
                        }
                    }
                }
                // }

                String Preferiti = VariabiliGlobali.getInstance().getPreferiti();
                Preferiti = Preferiti.replace(";;", ";");
                if (VariabiliGlobali.getInstance().isRicercaPreferiti()) {
                    String[] pref = Preferiti.split(";");
                    if (!Preferiti.isEmpty() && Preferiti.contains(";") && !Preferiti.equals(";")) {
                        if (Altro.isEmpty()) {
                            Altro = "Where (";
                        } else {
                            Altro += " And (";
                        }
                        for (int i = 0; i < pref.length; i++) {
                            if (!pref[i].isEmpty()) {
                                Altro += "Artista = '" + pref[i] + "' Or ";
                            }
                        }
                        if (!Altro.isEmpty()) {
                            Altro = Altro.substring(0, Altro.length() - 4) + ")";
                        }
                    }

                    String PreferitiElimina = VariabiliGlobali.getInstance().getPreferitiElimina();
                    PreferitiElimina = PreferitiElimina.replace(";;", ";");
                    if (!PreferitiElimina.isEmpty() && PreferitiElimina.contains(";") && !PreferitiElimina.equals(";")) {
                        if (Altro.isEmpty()) {
                            Altro = "Where (";
                        } else {
                            Altro += " And (";
                        }
                        String[] prefElim = PreferitiElimina.split(";");
                        boolean messo = false;
                        for (int i = 0; i < prefElim.length; i++) {
                            if (!prefElim[i].isEmpty()) {
                                Altro += "Artista != '" + prefElim[i] + "' And ";
                                messo = true;
                            }
                        }
                        if (messo) {
                            Altro = Altro.substring(0, Altro.length() - 5) + ")";
                        }
                    }
                }

                if (VariabiliGlobali.getInstance().isRicercaTags()) {
                    String Tags = VariabiliGlobali.getInstance().getPreferitiTags();
                    Tags = Tags.replace(";;", ";");
                    if (!Tags.isEmpty() && Tags.contains(";") && !Tags.equals(";")) {
                        if (Altro.isEmpty()) {
                            Altro = "Where (";
                        } else {
                            Altro += " And (";
                        }
                        String[] t = Tags.split(";");
                        for (int i = 0; i < t.length; i++) {
                            if (!t[i].isEmpty()) {
                                Altro += "Instr(Tags, ';' || '" + t[i] + "' || ';') > 0 And ";
                            }
                        }
                        if (!Altro.isEmpty()) {
                            Altro = Altro.substring(0, Altro.length() - 5) + ")";
                        }
                    }

                    String TagsElimina = VariabiliGlobali.getInstance().getPreferitiEliminaTags();
                    TagsElimina = TagsElimina.replace(";;", ";");
                    if (!TagsElimina.isEmpty() && TagsElimina.contains(";") && !TagsElimina.equals(";")) {
                        if (Altro.isEmpty()) {
                            Altro = "Where (";
                        } else {
                            Altro += " And (";
                        }
                        String[] t = TagsElimina.split(";");
                        boolean messo = false;
                        for (int i = 0; i < t.length; i++) {
                            if (!t[i].isEmpty()) {
                                Altro += "Instr(Tags, ';' || '" + t[i] + "' || ';') = 0 And ";
                                messo = true;
                            }
                        }
                        if (messo) {
                            Altro = Altro.substring(0, Altro.length() - 5) + ")";
                        }
                    }
                }

                if (VariabiliGlobali.getInstance().isDate()) {
                    if (VariabiliGlobali.getInstance().isDataSuperiore()) {
                        if (Altro.isEmpty()) {
                            Altro = "Where ";
                        } else {
                            Altro += " And ";
                        }
                        Altro += "datetime(substr(data, 7, 4) || '-' || substr(data, 4, 2) || '-' || substr(data, 1, 2)) > '" + VariabiliGlobali.getInstance().getTxtDataSuperiore() + "'";
                    } else {
                        if (VariabiliGlobali.getInstance().isDataInferiore()) {
                            if (Altro.isEmpty()) {
                                Altro = "Where ";
                            } else {
                                Altro += " And ";
                            }
                            Altro += "datetime(substr(data, 7, 4) || '-' || substr(data, 4, 2) || '-' || substr(data, 1, 2)) < '" + VariabiliGlobali.getInstance().getTxtDataInferiore() + "'";
                        }
                    }
                }

                /* if (VariabiliGlobali.getInstance().isRicercaMaiAscoltata()) {
                    if (Altro.isEmpty()) {
                        Altro = "Where Bellezza = 0";
                    } else {
                        Altro += "And Bellezza = 0";
                    }
                } */

                String SQL = "SELECT Count(*) FROM BraniInLocale " + Altro;
                Log.getInstance().ScriveLog("Conteggia brani filtrati: " + SQL);

                Cursor c = myDB.rawQuery(SQL, null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    if (c.getInt(0) > 0) {
                        quantiBrani = Integer.toString(c.getInt(0)) + ";FILTRO";
                    } else {
                        SQL = "SELECT Count(*) FROM BraniInLocale";
                        Log.getInstance().ScriveLog("Conteggia brani NON filtrati 1 in quanto non ho trovato nulla prima: " + SQL);

                        Cursor c2 = myDB.rawQuery(SQL, null);
                        c2.moveToFirst();
                        if (c2.getCount() > 0) {
                            quantiBrani = Integer.toString(c2.getInt(0)) + ";NOFILTRO";
                        } else {
                            Log.getInstance().ScriveLog("Ritorno 0... Nessun brano in locale");
                            quantiBrani = "0;NULLA";
                        }
                    }
                } else {
                    SQL = "SELECT Count(*) FROM BraniInLocale";
                    Log.getInstance().ScriveLog("Conteggia brani NON filtrati 2 in quanto non ho trovato nulla prima: " + SQL);

                    Cursor c2 = myDB.rawQuery(SQL, null);
                    c2.moveToFirst();
                    if (c2.getCount() > 0) {
                        quantiBrani = Integer.toString(c.getInt(0)) + ";NOFILTRO";
                    } else {
                        Log.getInstance().ScriveLog("Ritorno 0... Nessun brano in locale");
                        quantiBrani = "0;NULLA";
                    }
                }
            } catch(SQLException e) {
                // Ok = Utility.getInstance().PrendeErroreDaException(e);
                quantiBrani = "0;ERRORE";
                Log.getInstance().ScriveLog("ERRORE Nel conteggio dei brani: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return quantiBrani;
    }

    public int CercaBrano(int qualeBrano, boolean FiltroPulito) {
        int idBrano = -1;

        if (myDB != null) {
            try {
                String Altro = "";
                if (FiltroPulito) {
                    if (VariabiliGlobali.getInstance().isRicercaTesto()) {
                        String RicercaTesto = VariabiliGlobali.getInstance().getTestoDaRicercare().toUpperCase().trim().replace("'", "''");
                        Altro += "Where (Artista Like '%" + RicercaTesto + "%' Or Album Like '%" + RicercaTesto + "%' Or Brano Like '%" + RicercaTesto + "%') ";
                    }
                    if (VariabiliGlobali.getInstance().isRicercaEsclusione()) {
                        String RicercaTesto = VariabiliGlobali.getInstance().getTestoDaEscludere().toUpperCase().trim();
                        if (Altro.isEmpty()) {
                            Altro = "Where (Artista Not Like '%" + RicercaTesto + "%' And Album Not Like '%" + RicercaTesto + "%' And Brano Not Like '%" + RicercaTesto + "%') ";
                        } else {
                            Altro += "And (Artista Not Like '%" + RicercaTesto + "%' And Album Not Like '%" + RicercaTesto + "%' And Brano Not Like '%" + RicercaTesto + "%') ";
                        }
                    }
                    // if (VariabiliGlobali.getInstance().isRetePresente()) {
                    if (VariabiliGlobali.getInstance().isRicercaStelle()) {
                        int Stelle = VariabiliGlobali.getInstance().getStelleBrano();
                        if (Stelle == 0) {
                            if (Altro.isEmpty()) {
                                Altro = "Where Bellezza = 0 ";
                            } else {
                                Altro += "And Bellezza = 0 ";
                            }
                        } else {
                            if (Altro.isEmpty()) {
                                Altro = "Where Bellezza >= " + Stelle + " ";
                            } else {
                                Altro += "And Bellezza >= " + Stelle + " ";
                            }
                        }
                    }
                    // }
                /* if (VariabiliGlobali.getInstance().isRicercaMaiAscoltata()) {
                    if (Altro.isEmpty()) {
                        Altro = "Where Bellezza = 0";
                    } else {
                        Altro += "And Bellezza = 0";
                    }
                } */

                    String Preferiti = VariabiliGlobali.getInstance().getPreferiti();
                    Preferiti = Preferiti.replace(";;", ";");
                    if (VariabiliGlobali.getInstance().isRicercaPreferiti()) {
                        String[] pref = Preferiti.split(";");
                        if (!Preferiti.isEmpty() && Preferiti.contains(";") && !Preferiti.equals(";")) {
                            if (Altro.isEmpty()) {
                                Altro = "Where (";
                            } else {
                                Altro += " And (";
                            }
                            for (int i = 0; i < pref.length; i++) {
                                if (!pref[i].isEmpty()) {
                                    Altro += "Artista = '" + pref[i] + "' Or ";
                                }
                            }
                            if (!Altro.isEmpty()) {
                                Altro = Altro.substring(0, Altro.length() - 4) + ")";
                            }
                        }

                        String PreferitiElimina = VariabiliGlobali.getInstance().getPreferitiElimina();
                        PreferitiElimina = PreferitiElimina.replace(";;", ";");
                        if (!PreferitiElimina.isEmpty() && PreferitiElimina.contains(";") && !PreferitiElimina.equals(";")) {
                            if (Altro.isEmpty()) {
                                Altro = "Where (";
                            } else {
                                Altro += " And (";
                            }
                            String[] prefElim = PreferitiElimina.split(";");
                            boolean messo = false;
                            for (int i = 0; i < prefElim.length; i++) {
                                if (!prefElim[i].isEmpty()) {
                                    Altro += "Artista != '" + prefElim[i] + "' And ";
                                    messo = true;
                                }
                            }
                            if (messo) {
                                Altro = Altro.substring(0, Altro.length() - 5) + ")";
                            }
                        }
                    }

                    if (VariabiliGlobali.getInstance().isRicercaTags()) {
                        String Tags = VariabiliGlobali.getInstance().getPreferitiTags();
                        Tags = Tags.replace(";;", ";");
                        if (!Tags.isEmpty() && Tags.contains(";") && !Tags.equals(";")) {
                            if (Altro.isEmpty()) {
                                Altro = "Where (";
                            } else {
                                Altro += " And (";
                            }
                            String[] t = Tags.split(";");
                            for (int i = 0; i < t.length; i++) {
                                if (!t[i].isEmpty()) {
                                    Altro += "Instr(Tags, ';' || '" + t[i] + "' || ';') > 0 And ";
                                }
                            }
                            if (!Altro.isEmpty()) {
                                Altro = Altro.substring(0, Altro.length() - 5) + ")";
                            }
                        }

                        String TagsElimina = VariabiliGlobali.getInstance().getPreferitiEliminaTags();
                        TagsElimina = TagsElimina.replace(";;", ";");
                        if (!TagsElimina.isEmpty() && TagsElimina.contains(";") && !TagsElimina.equals(";")) {
                            if (Altro.isEmpty()) {
                                Altro = "Where (";
                            } else {
                                Altro += " And (";
                            }
                            String[] t = TagsElimina.split(";");
                            boolean messo = false;
                            for (int i = 0; i < t.length; i++) {
                                if (!t[i].isEmpty()) {
                                    Altro += "Instr(Tags, ';' || '" + t[i] + "' || ';') = 0 And ";
                                    messo = true;
                                }
                            }
                            if (messo) {
                                Altro = Altro.substring(0, Altro.length() - 5) + ")";
                            }
                        }
                    }

                    if (VariabiliGlobali.getInstance().isDate()) {
                        if (VariabiliGlobali.getInstance().isDataSuperiore()) {
                            if (Altro.isEmpty()) {
                                Altro = "Where ";
                            } else {
                                Altro += " And ";
                            }
                            Altro += "datetime(substr(data, 7, 4) || '-' || substr(data, 4, 2) || '-' || substr(data, 1, 2)) > '" + VariabiliGlobali.getInstance().getTxtDataSuperiore() + "'";
                        } else {
                            if (VariabiliGlobali.getInstance().isDataInferiore()) {
                                if (Altro.isEmpty()) {
                                    Altro = "Where ";
                                } else {
                                    Altro += " And ";
                                }
                                Altro += "datetime(substr(data, 7, 4) || '-' || substr(data, 4, 2) || '-' || substr(data, 1, 2)) < '" + VariabiliGlobali.getInstance().getTxtDataInferiore() + "'";
                            }
                        }
                    }
                } else {
                    // NEssun filtro in quanto nella prima ricerca non ha trovato nulla
                }

                String SQL = "SELECT * FROM BraniInLocale " + Altro;
                Log.getInstance().ScriveLog("Ricerca brano con filtro: " + SQL);
                Log.getInstance().ScriveLog("Indice da ricercare con filtro: " + qualeBrano);

                if (qualeBrano > -1) {
                    Cursor c = myDB.rawQuery(SQL, null);
                    c.moveToFirst();
                    if (c.getCount() > 0) {
                        int conta = 0;
                        do {
                            conta++;
                            if (conta >= qualeBrano) {
                                idBrano = Integer.parseInt(c.getString(0));
                                break;
                            }
                        } while (c.moveToNext());
                    } else {
                        Log.getInstance().ScriveLog("Brano non rilevato");
                        idBrano = -1;
                    }
                } else {
                    idBrano = -1;
                }
            } catch(SQLException e) {
                // Ok = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nella ricerca del brano: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return idBrano;
    }

    public int RicercaBrano(String Artista, String Album, String Brano) {
        int idBrano = 0;

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT idBrano FROM BraniInLocale Where Artista = ? And Album = ? And Brano = ?",  new String[] {Artista, Album, Brano});
                c.moveToFirst();
                if (c.getCount() > 0) {
                    idBrano = c.getInt(0);
                }
            } catch(SQLException e) {
                // Ok = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nella ricerca del brano: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return idBrano;
    }

    public int ContaBrani() {
        int quanti = 0;

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT Count(*) FROM BraniInLocale", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    quanti = c.getInt(0);
                }
            } catch(SQLException e) {
                // Ok = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nel conteggio dei brani: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return quanti;
    }

    public boolean EliminaListaPreferiti(String NomeLista) {
        boolean Ok = true;

        if (myDB != null) {
            try {
                String SQL = "Delete From ListePreferiti Where NomePreferito = '" + NomeLista + "'";
                myDB.execSQL(SQL);
            } catch (Exception e) {
                String err = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nell'eliminazione della lista " + NomeLista + ": " + err);
                AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
                alertDialog.setTitle("ERROR");
                alertDialog.setMessage("Errore nell'eliminazione della lista " + NomeLista + ": " + err);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                Ok = false;
            }
        }

        return Ok;
    }

    public boolean SalvaListaPreferiti(String NomeLista) {
        boolean Ok = true;

        if (myDB != null) {
            try {
                String SQL = "Delete From ListePreferiti Where NomePreferito = '" + NomeLista + "'";
                myDB.execSQL(SQL);
            } catch (Exception e) {
                String err = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nell'eliminazione della lista " + NomeLista + ": " + err);
                AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
                alertDialog.setTitle("ERROR");
                alertDialog.setMessage("Errore nel salvataggio della lista " + NomeLista + " durante l'eliminazione: " + err);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                Ok = false;
            }

            if (Ok) {
                String Lista = VariabiliGlobali.getInstance().getPreferiti();
                String ListaElimina = VariabiliGlobali.getInstance().getPreferitiElimina();
                String PreferitiTags = VariabiliGlobali.getInstance().getPreferitiTags();
                String PreferitiEliminaTags = VariabiliGlobali.getInstance().getPreferitiEliminaTags();

                try {
                    String SQL = "Insert Into ListePreferiti Values('" + NomeLista + "', '" + Lista + "', '" + ListaElimina + "', '" +  PreferitiTags + "', '" +  PreferitiEliminaTags + "')";
                    myDB.execSQL(SQL);
                } catch (Exception e) {
                    String err = Utility.getInstance().PrendeErroreDaException(e);
                    Log.getInstance().ScriveLog("ERRORE Nel salvataggio della lista  " + NomeLista + ": " + err);
                    AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.setMessage("Errore nel salvataggio della lista " + NomeLista + ": " + err);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    Ok = false;
                }
            }
        }

        return Ok;
    }

    public boolean RitornaPreferitiDaLista(String Lista) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM ListePreferiti Where NomePreferito = ?",  new String[] {Lista});
                c.moveToFirst();
                if (c.getCount() > 0) {
                    VariabiliGlobali.getInstance().setPreferiti(c.getString(1));
                    VariabiliGlobali.getInstance().setPreferitiElimina(c.getString(2));
                    VariabiliGlobali.getInstance().setPreferitiTags(c.getString(3));
                    VariabiliGlobali.getInstance().setPreferitiEliminaTags(c.getString(4));
                }
            } catch(SQLException e) {
                // Ok = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nel ritorno della lista preferiti: " + Utility.getInstance().PrendeErroreDaException(e));
                Log.getInstance().ScriveLog("Pulizia tabelle preferiti");
                PulisceTabellaPreferiti();
                Log.getInstance().ScriveLog("Creazione tabelle preferiti");
                CreazioneTabelle();
                RitornaPreferitiDaLista(Lista);
            }
        }

        return true;
    }

    public boolean RitornaListaPreferiti() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM ListePreferiti", null);
                c.moveToFirst();
                List<StrutturaListaPreferiti> lista = new ArrayList<>();
                if (c.getCount() > 0) {
                    do {
                        StrutturaListaPreferiti s = new StrutturaListaPreferiti();
                        s.setNomeLista(c.getString(0));
                        s.setPreferiti(c.getString(1));
                        s.setPreferitiElimina(c.getString(2));
                        s.setTags(c.getString(3));
                        s.setTagsElimina(c.getString(4));

                        lista.add(s);
                    } while (c.moveToNext());
                    VariabiliGlobali.getInstance().setListaPreferiti(lista);
                } else {
                    VariabiliGlobali.getInstance().setListaPreferiti(lista);
                }
            } catch(SQLException e) {
                // Ok = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nel ritorno della lista preferiti: " + Utility.getInstance().PrendeErroreDaException(e));
                Log.getInstance().ScriveLog("Pulizia tabelle preferiti");
                PulisceTabellaPreferiti();
                Log.getInstance().ScriveLog("Creazione tabelle preferiti");
                CreazioneTabelle();
                RitornaListaPreferiti();
            }
        }

        return true;
    }

    public boolean EliminaBrano(String idBrano) {
        if (myDB != null) {
            try {
                String SQL = "Delete From BraniInLocale Where idBrano = " + idBrano;
                myDB.execSQL(SQL);
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nell'eliminazione del brano: " + Utility.getInstance().PrendeErroreDaException(e));
            }

            try {
                String SQL = "Delete From ImmaginiInLocale Where idBrano = " + idBrano;
                myDB.execSQL(SQL);
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nell'eliminazione delle immagini del brano: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return true;
    }

    public int PrendeDimensioniBrani() {
        int totSize = 0;

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT Sum(Dimensione) FROM BraniInLocale", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    totSize = c.getInt(0);
                }
            } catch(SQLException e) {
                // Ok = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nel conteggio dei brani: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return totSize;
    }

    public StrutturaBrano RitornaBrano(String idBrano) {
        StrutturaBrano s = new StrutturaBrano();
        Log.getInstance().ScriveLog("Lettura brano " + idBrano);
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM BraniInLocale Where idBrano = ?",  new String[] {idBrano});
                c.moveToFirst();
                if (c.getCount() > 0) {
                    s.setIdBrano(Integer.parseInt(c.getString(0)));
                    s.setQuantiBrani(0);
                    s.setArtista(c.getString(2));
                    s.setAlbum(c.getString(3));
                    s.setBrano(c.getString(4));
                    s.setAnno(c.getString(5));
                    s.setTraccia(c.getString(6));
                    s.setEstensione(c.getString(7));
                    s.setData(c.getString(8));
                    s.setDimensione(c.getString(9) == null || c.getString(9).equals("null") ? 0 : Integer.parseInt(c.getString(9)));
                    s.setAscoltata(c.getString(10) == null || c.getString(10).equals("null") ? 0 : Integer.parseInt(c.getString(10)));
                    s.setBellezza(c.getString(11) == null || c.getString(11).equals("null") ? 0 : Integer.parseInt(c.getString(11)));
                    s.setTesto(c.getString(12) != null || c.getString(12).equals("null") ? c.getString(12).replace("*N*", "\n") : "");
                    s.setTestoTradotto(c.getString(13) != null || c.getString(13).equals("null") ? c.getString(13).replace("*N*", "\n") : "");
                    s.setUrlBrano(c.getString(14));
                    s.setPathBrano(c.getString(15));
                    s.setCartellaBrano(c.getString(16));
                    s.setEsisteBranoSuDisco(c.getString(17) == "S" ? true : false);
                    s.setTags(c.getString(18));

                    VariabiliGlobali.getInstance().setBranosSuSDOriginale(VariabiliGlobali.getInstance().isBranoSuSD());
                } else {
                    return null;
                }
                c.close();
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nella lettura del brano: " + Utility.getInstance().PrendeErroreDaException(e));

                return null;
            }

            s.setImmagini(new ArrayList<>());
            try {
                List<StrutturaImmagini> listaImmagini = new ArrayList<>();
                Cursor c = myDB.rawQuery("SELECT * FROM ImmaginiInLocale Where idBrano = ?",  new String[] {idBrano});
                c.moveToFirst();
                if (c.getCount() > 0) {
                    do {
                        StrutturaImmagini i = new StrutturaImmagini();
                        i.setAlbum(c.getString(2));
                        i.setNomeImmagine(c.getString(3));
                        i.setUrlImmagine(c.getString(4));
                        i.setPathImmagine(c.getString(5));
                        i.setCartellaImmagine(c.getString(6));

                        listaImmagini.add(i);
                    } while (c.moveToNext());
                }
                s.setImmagini(listaImmagini);
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nella lettura delle immagini: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return s;
    }

    public Boolean EliminaTuttiIBrani() {
        Log.getInstance().ScriveLog("Eliminazione brani e immagini locali");

        if (myDB != null) {
            try {
                String SQL = "Delete From BraniInLocale";
                myDB.execSQL(SQL);
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nell'eliminazione dei brani': " + Utility.getInstance().PrendeErroreDaException(e));
            }

            try {
                String SQL = "Delete From ImmaginiInLocale";
                myDB.execSQL(SQL);
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nell'eliminazione delle immagini': " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return true;
    }

    public Boolean AggiungeBrano(StrutturaBrano s) {
        String Ok = "";
        // Log.getInstance().ScriveLog("Aggiungo brano al db in locale: " + s.getBrano());

        if (myDB != null) {
            // Prima di salvare il brano controllo se esiste
            try {
                String SQL = "Select * From BraniInLocale Where Artista='" + s.getArtista().replace("'","''") + "' "
                    + "And Album='" + s.getAlbum().replace("'","''") + "' "
                    + "And Brano='" + s.getBrano().replace("'","''") + "'";
                Cursor c = myDB.rawQuery(SQL, null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    return false;
                }
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nella lettura del brano per l'aggiunta: " + Utility.getInstance().PrendeErroreDaException(e));
            }

            try {
                String SQL = "INSERT INTO "
                        + "BraniInLocale "
                        + " (idBrano, quantiBrani,  Artista,  Album,  Brano, "
                        + "Anno,  Traccia,  Estensione, Data,  Dimensione, "
                        + "Ascoltata, Bellezza, Testo, TestoTradotto, UrlBrano, "
                        + "PathBrano, CartellaBrano, EsisteBranoSuDisco, Tags) "
                        + "VALUES ("
                        + "'" + s.getIdBrano() + "', "
                        + "'" + s.getQuantiBrani() + "', "
                        + "'" + s.getArtista().replace("'","''") + "', "
                        + "'" + s.getAlbum().replace("'","''") + "', "
                        + "'" + s.getBrano().replace("'","''") + "', "
                        + "'" + s.getAnno() + "',"
                        + "'" + s.getTraccia() + "', "
                        + "'" + s.getEstensione() + "', "
                        + "'" + s.getData() + "', "
                        + "'" + s.getDimensione() + "', "
                        + "'" + s.getAscoltata() + "', "
                        + "'" + s.getBellezza() + "', "
                        + "'" + (s.getTesto() != null ? s.getTesto().replace("'","''").replace("\n", "*N*") : "") + "', "
                        + "'" + (s.getTestoTradotto() != null ? s.getTestoTradotto().replace("'","''").replace("\n", "*N*") : "") + "', "
                        + "'" + s.getUrlBrano().replace("'","''") + "', "
                        + "'" + s.getPathBrano().replace("'","''") + "', "
                        + "'" + s.getCartellaBrano().replace("'","''") + "', "
                        + "'" + (s.isEsisteBranoSuDisco() ? "S" : "N") + "', "
                        + "'" + (s.getTags() != null ? s.getTags().replace("'","''") : "") + "'"
                        + ")";
                myDB.execSQL(SQL);

                AggiungeImmaginiBrano(s.getIdBrano().toString(), s.getImmagini());
            } catch (SQLException e) {
                Log.getInstance().ScriveLog("ERRORE Nell'aggiunta del brano: " + Utility.getInstance().PrendeErroreDaException(e));
                Log.getInstance().ScriveLog("Pulizia tabelle brani");
                PulisceBraniInLocale();
                Log.getInstance().ScriveLog("Creazione tabelle brani");
                CreazioneTabelle();
                AggiungeBrano(s);

                return false;
            }
        }

        return true;
    }

    public Boolean AggiungeImmaginiBrano(String idBrano, List<StrutturaImmagini> imm) {
        String Ok = "";
        // Log.getInstance().ScriveLog("Aggiungo immagini brano al db in locale: " + idBrano);

        if (myDB != null) {
            try {
                String SQL = "Delete From ImmaginiInLocale Where idBrano='" + idBrano + "'";

                myDB.execSQL(SQL);
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nella pulizia delle immagini: " + Utility.getInstance().PrendeErroreDaException(e));
            }

            try {
                int Progressivo = 1;

                for (int i = 0; i < imm.size(); i++) {
                    StrutturaImmagini imm2 = imm.get(i);

                    String SQL = "INSERT INTO "
                            + "ImmaginiInLocale "
                            + "(idBrano, Progressivo, Album, NomeImmagine, "
                            + "UrlImmagine, PathImmagine, CartellaImmagine) "
                            + "VALUES ("
                            + "'" + idBrano + "', "
                            + "'" + Progressivo + "', "
                            + "'" + imm2.getAlbum().replace("'","''") + "', "
                            + "'" + imm2.getNomeImmagine().replace("'","''") + "', "
                            + "'" + imm2.getUrlImmagine().replace("'","''") + "', "
                            + "'" + imm2.getPathImmagine().replace("'","''") + "', "
                            + "'" + imm2.getCartellaImmagine().replace("'","''") + "' "
                            + ")";
                    myDB.execSQL(SQL);

                    Progressivo++;
                }
            } catch (SQLException e) {
                Log.getInstance().ScriveLog("ERRORE Nell'aggiunta dell'immagine: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return true;
    }

    public boolean aggiornaStelleBrano(String idBrano, int Stelle, int Ascoltata) {
        String Ok = "";
        Log.getInstance().ScriveLog("Aggiorno dati brano '" + idBrano + "' al db in locale: Stelle " + Stelle + " Ascoltata " + Ascoltata);

        if (myDB != null) {
            try {
                String strSQL = "UPDATE BraniInLocale SET Bellezza = " + Integer.toString(Stelle) + ", " +
                        "Ascoltata = " + Integer.toString(Ascoltata) + " " +
                        "WHERE idBrano = " + idBrano;

                myDB.execSQL(strSQL);
            } catch (SQLException e) {
                Log.getInstance().ScriveLog("ERRORE Nell'aggiornamento stelle del brano: " + Utility.getInstance().PrendeErroreDaException(e));

                return false;
            }
        }

        return true;
    }

    public Boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Impostazioni");
                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " (RicercaTesto, RicercaEsclusione, RicercaStelle, "
                        + "RicercaMaiAscoltata, RicercaTags, TestoDaRicercare, "
                        + "TestoDaEscludere, StelleDaRicercare, TagsDaRicercare, "
                        + "Random, CambioImmagine, VisuaInfo, SecondiCambioImmagine, "
                        + "branoSuDisco, RicercaPreferiti, Debug, Opacita, SecondiOpacita, "
                        + "MostraOrologio, EliminaBrani, LimiteInMb, DataSuperiore, EdtDataSuperiore, "
                        + "DataInferiore, EdtDataInferiore, Date, ImmaginiDaScaricare)"
                        + " VALUES ("
                        + "'" + (VariabiliGlobali.getInstance().isRicercaTesto() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isRicercaEsclusione() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isRicercaStelle() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isRicercaMaiAscoltata() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isRicercaTags() ? "S" : "N") + "', "
                        + "'" + VariabiliGlobali.getInstance().getTestoDaRicercare() + "', "
                        + "'" + VariabiliGlobali.getInstance().getTestoDaEscludere() + "', "
                        + "'" + VariabiliGlobali.getInstance().getStelleDaRicercare().toString() + "', "
                        + "'" + VariabiliGlobali.getInstance().getTagsDaRicercare() + "', "
                        + "'" + (VariabiliGlobali.getInstance().isRandom() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isCambioImmagine() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isVisualizzaInformazioni() ? "S" : "N") + "', "
                        + "'" + VariabiliGlobali.getInstance().getSecondiCambioImmagine() + "', "
                        + "'" + (VariabiliGlobali.getInstance().isBranoSuSD() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isRicercaPreferiti() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isAzionaDebug() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isOpacitaBottoni() ? "S" : "N") + "', "
                        + "'" + VariabiliGlobali.getInstance().getSecondiOpacitaBottoni() + "', "
                        + "'" + (VariabiliGlobali.getInstance().isMostraOrologio() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isEliminaBrani() ? "S" : "N") + "', "
                        + "'" + VariabiliGlobali.getInstance().getLimiteInMb() + "', "
                        + "'" + (VariabiliGlobali.getInstance().isDataSuperiore() ? "S" : "N") + "', "
                        + "'" + VariabiliGlobali.getInstance().getTxtDataSuperiore() + "', "
                        + "'" + (VariabiliGlobali.getInstance().isDataInferiore() ? "S" : "N") + "', "
                        + "'" + VariabiliGlobali.getInstance().getTxtDataInferiore() + "', "
                        + "'" + (VariabiliGlobali.getInstance().isDate() ? "S" : "N") + "', "
                        + "'" + VariabiliGlobali.getInstance().getQuanteImmaginiDaScaricareGA().toString() + "' "
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                Log.getInstance().ScriveLog("ERRORE Su scrittura impostazioni: " + Utility.getInstance().PrendeErroreDaException(e));
                Log.getInstance().ScriveLog("Pulizia tabelle");
                PulisceDati();
                Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();

                return false;
            }
        }

        return true;
    }

    public boolean LeggeImpostazioni() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    VariabiliGlobali.getInstance().setRicercaTesto(c.getString(0).equals("S"));
                    VariabiliGlobali.getInstance().setRicercaEsclusione(c.getString(1).equals("S"));
                    VariabiliGlobali.getInstance().setRicercaStelle(c.getString(2).equals("S"));
                    VariabiliGlobali.getInstance().setRicercaMaiAscoltata(c.getString(3).equals("S"));
                    VariabiliGlobali.getInstance().setRicercaTags(c.getString(4).equals("S"));
                    VariabiliGlobali.getInstance().setTestoDaRicercare(c.getString(5));
                    VariabiliGlobali.getInstance().setTestoDaEscludere(c.getString(6));
                    VariabiliGlobali.getInstance().setStelleDaRicercare(Integer.parseInt(c.getString(7)));
                    VariabiliGlobali.getInstance().setTagsDaRicercare(c.getString(8));
                    VariabiliGlobali.getInstance().setRandom(c.getString(9).equals("S"));
                    VariabiliGlobali.getInstance().setCambioImmagine(c.getString(10).equals("S"));
                    VariabiliGlobali.getInstance().setVisualizzaInformazioni(c.getString(11).equals("S"));
                    VariabiliGlobali.getInstance().setSecondiCambioImmagine(Integer.parseInt(c.getString(12)));
                    VariabiliGlobali.getInstance().setBranoSuSD(c.getString(13).equals("S"));
                    VariabiliGlobali.getInstance().setRicercaPreferiti(c.getString(14).equals("S"));
                    VariabiliGlobali.getInstance().setAzionaDebug(c.getString(15).equals("S"));
                    VariabiliGlobali.getInstance().setOpacitaBottoni(c.getString(16).equals("S"));
                    VariabiliGlobali.getInstance().setSecondiOpacitaBottoni(Integer.parseInt(c.getString(17)));
                    VariabiliGlobali.getInstance().setMostraOrologio(c.getString(18).equals("S"));
                    VariabiliGlobali.getInstance().setEliminaBrani(c.getString(19).equals("S"));
                    VariabiliGlobali.getInstance().setLimiteInMb(Integer.parseInt(c.getString(20)));
                    VariabiliGlobali.getInstance().setDataSuperiore(c.getString(21).equals("S"));
                    VariabiliGlobali.getInstance().setTxtDataSuperiore(c.getString(22));
                    VariabiliGlobali.getInstance().setDataInferiore(c.getString(23).equals("S"));
                    VariabiliGlobali.getInstance().setTxtDataInferiore(c.getString(24));
                    VariabiliGlobali.getInstance().setDate(c.getString(25).equals("S"));
                    VariabiliGlobali.getInstance().setQuanteImmaginiDaScaricareGA(Integer.parseInt(c.getString(26)));

                    VariabiliGlobali.getInstance().setBranosSuSDOriginale(VariabiliGlobali.getInstance().isBranoSuSD());
                } else {
                    return false;
                }
                c.close();
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nella lettura delle impostazioni: " + Utility.getInstance().PrendeErroreDaException(e));
                Log.getInstance().ScriveLog("Pulizia tabelle");
                PulisceDati();
                Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();

                return false;
            }
        }

        return true;
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public void PulisceBraniInLocale() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            myDB.execSQL("Drop Table BraniInLocale");
            myDB.execSQL("Drop Table ImmaginiInLocale");
        }
    }

    public void PulisceTabellaPreferiti() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            myDB.execSQL("Drop Table ListePreferiti");
        }
    }

    public void PulisceDati() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            myDB.execSQL("Drop Table Impostazioni");
        }
    }

    public void PuliscePreferiti() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            myDB.execSQL("Drop Table Preferiti");
        }
    }

    public void PulisceTags() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            myDB.execSQL("Drop Table Tags");
        }
    }

    public String ScrivePreferiti() {
        String Ok = "";

        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Preferiti");
                myDB.execSQL("INSERT INTO"
                        + " Preferiti"
                        + " (Pref, Eliminati)"
                        + " VALUES ('" + VariabiliGlobali.getInstance().getPreferiti() + "', '" + VariabiliGlobali.getInstance().getPreferitiElimina() + "') ");
            } catch (SQLException e) {
                Log.getInstance().ScriveLog("ERRORE Nella scrittura dei preferiti: " + Utility.getInstance().PrendeErroreDaException(e));
                Ok = "ERROR: " + Utility.getInstance().PrendeErroreDaException(e);
                PuliscePreferiti();
                CreazioneTabelle();
                ScrivePreferiti();
            }
        }

        return Ok;
    }

    public boolean LeggePreferiti() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT Pref, Eliminati FROM Preferiti", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    String Pref = c.getString(0);
                    VariabiliGlobali.getInstance().setPreferiti(Pref);
                    String Elim = c.getString(1);
                    VariabiliGlobali.getInstance().setPreferitiElimina(Elim);
                } else {
                    return false;
                }
                c.close();
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nella lettura dei preferiti: " + Utility.getInstance().PrendeErroreDaException(e));
                PuliscePreferiti();
                CreazioneTabelle();
                ScrivePreferiti();
            }
        }

        return true;
    }

    public String ScriveTags() {
        String Ok = "";

        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Tags");
                myDB.execSQL("INSERT INTO"
                        + " Tags"
                        + " (Tag, Eliminati)"
                        + " VALUES ('" + VariabiliGlobali.getInstance().getPreferitiTags() + "', '" + VariabiliGlobali.getInstance().getPreferitiEliminaTags() + "') ");
            } catch (SQLException e) {
                Ok = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nella scrittura dei tags: " + Utility.getInstance().PrendeErroreDaException(e));
                PulisceTags();
                CreazioneTabelle();
                ScriveTags();
            }
        }

        return Ok;
    }

    public boolean LeggeTags() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT Tag, Eliminati FROM Tags", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    String T = c.getString(0);
                    VariabiliGlobali.getInstance().setPreferitiTags(T);
                    String E = c.getString(1);
                    VariabiliGlobali.getInstance().setPreferitiEliminaTags(E);
                } else {
                    return false;
                }
                c.close();
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nella lettura dei tags: " + Utility.getInstance().PrendeErroreDaException(e));
                PulisceTags();
                CreazioneTabelle();
                ScriveTags();
            }
        }

        return true;
    }

    public String ScriveIdUtente() {
        String Ok = "";

        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Utente");
                myDB.execSQL("INSERT INTO"
                        + " Utente"
                        + " (id)"
                        + " VALUES (" + VariabiliGlobali.getInstance().getIdUtente() + ") ");
            } catch (SQLException e) {
                Ok = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nella scrittura dell'id utente: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return Ok;
    }

    public String ScriveUltimoBranoAscoltato() {
        String Ok = "";

        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Ultima");
                myDB.execSQL("INSERT INTO"
                        + " Ultima"
                        + " (id)"
                        + " VALUES (" + VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato() + ") ");
            } catch (SQLException e) {
                Ok = Utility.getInstance().PrendeErroreDaException(e);
                Log.getInstance().ScriveLog("ERRORE Nella scrittura dell'ultimo brano ascoltato: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return Ok;
    }

    public boolean LeggeIdUtente() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT id FROM Utente", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    int idUtente = c.getInt(0);
                    if (idUtente > 0) {
                        VariabiliGlobali.getInstance().setIdUtente(idUtente);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
                c.close();
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Nella lettura dell'id utente: " + Utility.getInstance().PrendeErroreDaException(e));
                return false;
            }
        }

        return true;
    }

    public Integer LeggeUltimoBranoAscoltato() {
        int idBrano = -1;

        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT id FROM Ultima", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    idBrano = c.getInt(0);
                    if (idBrano > 0) {
                        VariabiliGlobali.getInstance().setIdUltimoBranoAscoltato(idBrano);
                    }
                }
                c.close();
            } catch (Exception e) {
                idBrano = -1;
                Log.getInstance().ScriveLog("ERRORE Nella lettura dell'ultimo brano ascoltato: " + Utility.getInstance().PrendeErroreDaException(e));
            }
        }

        return idBrano;
    }
}
