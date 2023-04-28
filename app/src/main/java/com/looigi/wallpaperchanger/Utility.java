package com.looigi.wallpaperchanger;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Random;

public class Utility {
    private ProgressDialog progressDialog;

    private static final Utility ourInstance = new Utility();

    public static Utility getInstance() {
        return ourInstance;
    }

    private Utility() {
    }

    public String PrendeErroreDaException(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return TransformError(errors.toString());
    }

    private String TransformError(String error) {
        String Return = error;

        if (Return.length() > 250) {
            Return = Return.substring(0, 247) + "...";
        }
        Return = Return.replace("\n", " ");

        return Return;
    }

    public void CopiaFile(String srcF, String dstF) throws IOException {
        File src = new File(srcF);
        File dst = new File(dstF);
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    public Integer ScriveFile(String Path, String fileName, String CosaScrivere) {
        try {
            File newFolder = new File(Path);
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            try {
                File file = new File(Path, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }

                Writer out = new BufferedWriter(new FileWriter(file, true), 1024);
                out.write(CosaScrivere);
                out.close();

                return 0;
            } catch (Exception ex) {
                // System.out.println("ex: " + ex);
                return -1;
            }
        } catch (Exception e) {
            // System.out.println("e: " + e);
            return -2;
        }
    }

    public boolean EliminaFile(String Path, String fileName) {
        File file = new File(Path + "/" + fileName);
        return file.delete();
    }

    public boolean EliminaFileUnico(String fileName) {
        if (EsisteFile(fileName)) {
            File file = new File(fileName);
            return file.delete();
        } else {
            return false;
        }
    }

    public boolean EsisteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public String LeggeFile(String Path, String fileName) {
        File file = new File(Path + '/' + fileName);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
            return "ERROR: " + e.getMessage();
        }

        return text.toString();
    }

    public String LeggeFileUnico(String fileName) {
        File file = new File(fileName);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
            return "ERROR: " + e.getMessage();
        }

        return text.toString();
    }

    public int DimensioniFile(String sFile) {
        File file = new File(sFile);
        return Integer.parseInt(String.valueOf(file.length() / 1024));
    }

    public Date DataFile(String sFile) {
        File file = new File(sFile);
        return new Date(file.lastModified());
    }

    public void CreaCartelle(String Path) {
        String[] Pezzetti = Path.split("/");
        String DaCreare = "";

        for (int i = 0; i < Pezzetti.length; i++) {
            if (!Pezzetti[i].isEmpty()) {
                DaCreare += "/" + Pezzetti[i];
                File newFolder = new File(DaCreare);
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
            }
        }
    }

    public int GeneraNumeroRandom(int NumeroMassimo) {
        if (NumeroMassimo > 0) {
            final int random = new Random().nextInt(NumeroMassimo);

            return random;
        } else {
            return -1;
        }
    }

    public void ChiudeDialog() {
        try {
            progressDialog.dismiss();
        } catch (Exception ignored) {
        }
    }

    public void ApriDialog(boolean ApriDialog, String tOperazione) {
        if (!ApriDialog) {
            // OggettiAVideo.getInstance().getImgRest().setVisibility(LinearLayout.VISIBLE);
        } else {
            try {
                progressDialog = new ProgressDialog(MainActivity.getAppContext());
                progressDialog.setMessage("Attendere Prego...\n\n" + tOperazione);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            } catch (Exception ignored) {

            }
        }
    }

    public void VisualizzaErrore(String Errore) {
        VariabiliGlobali.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        Log.getInstance().ScriveLog("Visualizzo messaggio di errore. Schermo acceso: " + VariabiliGlobali.getInstance().isScreenOn());
        if (VariabiliGlobali.getInstance().isScreenOn()) {
            MainActivity.getAppActivity().runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.getAppActivity()).create();
                    alertDialog.setTitle("Messaggio");
                    alertDialog.setMessage(Errore);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    // alertDialog.show();
                }
            });
        } else {
            Log.getInstance().ScriveLog("Schermo spento. Non visualizzo messaggio di errore: " + Errore);
        }
    }

    public void InstanziaNotifica() {
        Log.getInstance().ScriveLog("Instanzia notifica");

        Notifica.getInstance().setContext(MainActivity.getAppContext());
        if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
            Notifica.getInstance().setTitolo(VariabiliGlobali.getInstance().getUltimaImmagine().getImmagine());
            Notifica.getInstance().setImmagine(VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine());
        } else {
            Notifica.getInstance().setTitolo("");
            Notifica.getInstance().setImmagine("");
        }

        Notifica.getInstance().CreaNotifica();
    }

    public void AggiornaNotifica() {
        Log.getInstance().ScriveLog("Aggiorna notifica");

        Notifica.getInstance().setContext(MainActivity.getAppContext());
        Notifica.getInstance().setImmagine(VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine());
        Notifica.getInstance().setTitolo(VariabiliGlobali.getInstance().getUltimaImmagine().getImmagine());
        /*
        if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
            Notifica.getInstance().setTitolo(VariabiliGlobali.getInstance().getUltimaImmagine().getImmagine());
            Notifica.getInstance().setImmagine(VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine());
        } else {
            Notifica.getInstance().setTitolo("");
            Notifica.getInstance().setImmagine("");
        } */

        Notifica.getInstance().AggiornaNotifica();
    }
}
