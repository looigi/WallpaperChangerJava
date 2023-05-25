package com.looigi.wallpaperchanger;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.system.StructUtsname;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class db_dati {
    private final String PathDB = VariabiliGlobali.getInstance().getPercorsoDIR()+"/DB/";
    private final SQLiteDatabase myDB;

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
            String nomeDB = "dati.db";
            db = MainActivity.getAppContext().openOrCreateDatabase(
                    PathDB + nomeDB, MODE_PRIVATE, null);
        } catch (Exception e) {
            Log.getInstance().ScriveLog("ERRORE Nell'apertura del db: " + Utility.getInstance().PrendeErroreDaException(e));
        }
        return  db;
    }

    public void CreazioneTabelle() {
        try {
            // SQLiteDatabase myDB = ApreDB();
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni "
                        + " (UltimaImmagineNome VARCHAR, UltimaImmaginePath VARCHAR, SecondiAlcambio VARCHAR, PathImmagini VARCHAR, Offline VARCHAR, " +
                        "Blur VARCHAR, Resize VARCHAR, ScriveTesto VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "ListaImmaginiLocali "
                        + " (ImmagineNome VARCHAR, ImmaginePath VARCHAR, Data VARCHAR, Dimensione VARCHAR);";
                myDB.execSQL(sql);
            }
        } catch (Exception ignored) {
            Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + Utility.getInstance().PrendeErroreDaException(ignored));
        }
    }

    public boolean EliminaImmaginiInLocale() {
        if (myDB != null) {
            myDB.execSQL("Delete From Impostazioni");
        }

        return true;
    }

    public boolean ScriveImmagineInLocale(String Nome, String Path, String Data, String Dimensione) {
        if (myDB != null) {
            try {
                String sql = "INSERT INTO"
                        + " ListaImmaginiLocali"
                        + " (ImmagineNome, ImmaginePath, Data, Dimensione)"
                        + " VALUES ("
                        + "'" + Nome.replace("'","''") + "', "
                        + "'" + Path.replace("'","''") + "', "
                        + "'" + Data.replace("'","''") + "', "
                        + "'" + Dimensione.replace("'","''") + "' "
                        + ")";
                myDB.execSQL(sql);
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Su scrittura immagini locali: " + Utility.getInstance().PrendeErroreDaException(e));
                Log.getInstance().ScriveLog("Pulizia tabelle");
                PulisceDatiIL();
                Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();

                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean CaricaImmaginiInLocale() {
        if (myDB != null) {
            try {
                List<StrutturaImmagine> listaImmagini = new ArrayList<>();
                Cursor c = myDB.rawQuery("SELECT * FROM ListaImmaginiLocali", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        StrutturaImmagine s = new StrutturaImmagine();
                        s.setImmagine(c.getString(0));
                        s.setPathImmagine(c.getString(1));
                        s.setDataImmagine(c.getString(2));
                        s.setDimensione(c.getString(3));

                        listaImmagini.add(s);
                    } while (c.moveToNext());

                    VariabiliGlobali.getInstance().setListaImmagini(listaImmagini);
                } else {
                    return false;
                }
            } catch (Exception e) {
                Log.getInstance().ScriveLog("ERRORE Su scrittura immagini locali: " + Utility.getInstance().PrendeErroreDaException(e));
                Log.getInstance().ScriveLog("Pulizia tabelle");
                PulisceDatiIL();
                Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                CaricaImmaginiInLocale();

                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public Boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                String Imm = "";
                if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
                    Imm = VariabiliGlobali.getInstance().getUltimaImmagine().getImmagine();
                }
                String PathImm = "";
                if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
                    PathImm = VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine();
                } else {
                    PathImm = "";
                }
                myDB.execSQL("Delete From Impostazioni");
                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " (UltimaImmagineNome, UltimaImmaginePath, SecondiAlCambio, PathImmagini, Offline, Blur, Resize, ScriveTesto)"
                        + " VALUES ("
                        + "'" + (Imm) + "', "
                        + "'" + (PathImm) + "', "
                        + "'" + (VariabiliGlobali.getInstance().getSecondiAlCambio()) + "', "
                        + "'" + (VariabiliGlobali.getInstance().getPercorsoIMMAGINI()) + "', "
                        + "'" + (VariabiliGlobali.getInstance().isOffline() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isBlur() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isResize() ? "S" : "N") + "', "
                        + "'" + (VariabiliGlobali.getInstance().isScriveTestoSuImmagine()  ? "S" : "N") + "' "
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
                    StrutturaImmagine si = new StrutturaImmagine();
                    si.setImmagine(c.getString(0));
                    si.setPathImmagine(c.getString(1));
                    VariabiliGlobali.getInstance().setUltimaImmagine(si);

                    VariabiliGlobali.getInstance().setSecondiAlCambio(Integer.parseInt(c.getString(2)));
                    VariabiliGlobali.getInstance().setPercorsoIMMAGINI(c.getString(3));
                    VariabiliGlobali.getInstance().setOffline(c.getString(4).equals("S"));
                    VariabiliGlobali.getInstance().setBlur(c.getString(5).equals("S"));
                    VariabiliGlobali.getInstance().setResize(c.getString(6).equals("S"));
                    VariabiliGlobali.getInstance().setScriveTestoSuImmagine(c.getString(7).equals("S"));

                    Log.getInstance().ScriveLog("Secondi al cambio: " + VariabiliGlobali.getInstance().getSecondiAlCambio());
                    Log.getInstance().ScriveLog("Percorso immagini: " + VariabiliGlobali.getInstance().getPercorsoIMMAGINI());
                    Log.getInstance().ScriveLog("Offline: " + VariabiliGlobali.getInstance().isOffline());
                    Log.getInstance().ScriveLog("Blur: " + VariabiliGlobali.getInstance().isBlur());
                    Log.getInstance().ScriveLog("Resize: " + VariabiliGlobali.getInstance().isResize());
                    Log.getInstance().ScriveLog("Scrive testo su immagine: " + VariabiliGlobali.getInstance().isScriveTestoSuImmagine());
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

    public void PulisceDati() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            try {
                myDB.execSQL("Drop Table Impostazioni");
            } catch (Exception ignored) {

            }
        }
    }

    public void PulisceDatiIL() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            try {
                myDB.execSQL("Drop Table ListaImmaginiLocali");
            } catch (Exception ignored) {

            }
        }
    }
}
