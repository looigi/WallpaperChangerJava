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
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni "
                        + " (UltimaImmagineNome VARCHAR, UltimaImmaginePath VARCHAR, SecondiAlcambio VARCHAR, PathImmagini VARCHAR, Offline VARCHAR);";
                myDB.execSQL(sql);
            }
        } catch (Exception ignored) {
            Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + Utility.getInstance().PrendeErroreDaException(ignored));
        }
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
                        + " (UltimaImmagineNome, UltimaImmaginePath, SecondiAlCambio, PathImmagini, Offline)"
                        + " VALUES ("
                        + "'" + (Imm) + "', "
                        + "'" + (PathImm) + "', "
                        + "'" + (VariabiliGlobali.getInstance().getSecondiAlCambio()) + "', "
                        + "'" + (VariabiliGlobali.getInstance().getPercorsoIMMAGINI()) + "', "
                        + "'" + (VariabiliGlobali.getInstance().isOffline() ? "S" : "N") + "' "
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
}
