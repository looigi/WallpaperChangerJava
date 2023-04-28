package com.looigi.wallpaperchanger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private boolean ciSonoPermessi;
    private static Context context;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.context = getApplicationContext();
        MainActivity.activity = this;

        // VariabiliGlobali.getInstance().setContext(this);
        // VariabiliGlobali.getInstance().setFragmentActivityPrincipale(this);

        if (!VariabiliGlobali.getInstance().isePartito()) {
            // Log.getInstance().EliminaFileLog();
            Log.getInstance().ScriveLog(">>>>>>>>>>>>>>>>>>>>>>>>NUOVA SESSIONE<<<<<<<<<<<<<<<<<<<<<<<<");

            Permessi p = new Permessi();
            ciSonoPermessi = p.ControllaPermessi();

            if (ciSonoPermessi) {
                EsegueEntrata();
            }
        } else {
            EsegueEntrata();
        }
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    public static Activity getAppActivity() {
        return MainActivity.activity;
    }

    @Override
    protected void onResume() {
        super.onResume();

        MainActivity.context = getApplicationContext();
        MainActivity.activity = this;

        VariabiliGlobali.getInstance().setMascheraAperta(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (!ciSonoPermessi) {
            int index = 0;
            Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
            for (String permission : permissions) {
                PermissionsMap.put(permission, grantResults[index]);
                index++;
            }

            EsegueEntrata();
        }
    }

    private void EsegueEntrata() {
        TextView txtQuante = (TextView) findViewById(R.id.txtQuanteImmagini);
        VariabiliGlobali.getInstance().setTxtQuanteImmagini(txtQuante);

        if (!VariabiliGlobali.getInstance().isePartito()) {
            MainActivity.getAppActivity().startService(new Intent(
                    MainActivity.getAppActivity(),
                    ServizioBackground.class));
        }

        db_dati db = new db_dati();
        db.CreazioneTabelle();
        db.LeggeImpostazioni();

        ImpostaOggetti();

        if (!VariabiliGlobali.getInstance().isePartito()) {
            boolean letteImmagini = db.CaricaImmaginiInLocale();

            if (!letteImmagini) {
                // if (VariabiliGlobali.getInstance().isOffline()) {
                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali();
                bckLeggeImmaginiLocali.execute();
                // }
            } else {
                if (VariabiliGlobali.getInstance().isOffline()) {
                    int q = VariabiliGlobali.getInstance().getListaImmagini().size();
                    VariabiliGlobali.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate su disco: " + q);
                }
            }
        }
        //         VariabiliGlobali.getInstance().getFragmentActivityPrincipale().stopService(new Intent(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
        //                ServizioBackground.class));
    }

    private void ImpostaOggetti() {
        ImageView imgImpostata = (ImageView) findViewById(R.id.imgImpostata);
        VariabiliGlobali.getInstance().setImgImpostata(imgImpostata);

        TextView txtTempoAlCambio = (TextView) findViewById(R.id.txtTempoAlCambio);
        VariabiliGlobali.getInstance().setTxtTempoAlCambio(txtTempoAlCambio);
        VariabiliGlobali.getInstance().getTxtTempoAlCambio().setText("Prossimo cambio: " +
                VariabiliGlobali.getInstance().getSecondiPassati() + "/" + VariabiliGlobali.getInstance().getQuantiGiri());

        Button btnMenoMinuti = (Button) findViewById(R.id.btnMenoMinuti);
        Button btnPiuMinuti = (Button) findViewById(R.id.btnPiuMinuti);
        TextView edtMinuti = (TextView) findViewById(R.id.txtMinuti);
        int minuti = VariabiliGlobali.getInstance().getSecondiAlCambio() / 60000;
        edtMinuti.setText(Integer.toString(minuti));

        btnMenoMinuti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int minuti = VariabiliGlobali.getInstance().getSecondiAlCambio() / 60000;
                if (minuti > 1) {
                    minuti--;
                } else {
                    minuti = 1;
                }
                VariabiliGlobali.getInstance().setSecondiAlCambio(minuti * 60000);
                VariabiliGlobali.getInstance().setQuantiGiri(VariabiliGlobali.getInstance().getSecondiAlCambio() / VariabiliGlobali.getInstance().getTempoTimer());
                edtMinuti.setText(Integer.toString(minuti));
                VariabiliGlobali.getInstance().getTxtTempoAlCambio().setText("Prossimo cambio: " +
                        VariabiliGlobali.getInstance().getSecondiPassati() + "/" + VariabiliGlobali.getInstance().getQuantiGiri());

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });
        btnPiuMinuti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int minuti = VariabiliGlobali.getInstance().getSecondiAlCambio() / 60000;
                minuti++;
                VariabiliGlobali.getInstance().setSecondiAlCambio(minuti * 60000);
                VariabiliGlobali.getInstance().setQuantiGiri(VariabiliGlobali.getInstance().getSecondiAlCambio() / VariabiliGlobali.getInstance().getTempoTimer());
                edtMinuti.setText(Integer.toString(minuti));
                VariabiliGlobali.getInstance().getTxtTempoAlCambio().setText("Prossimo cambio: " +
                        VariabiliGlobali.getInstance().getSecondiPassati() + "/" + VariabiliGlobali.getInstance().getQuantiGiri());

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        TextView txtPath = (TextView) findViewById(R.id.txtPath);
        VariabiliGlobali.getInstance().setTxtPath(txtPath);
        txtPath.setText(VariabiliGlobali.getInstance().getPercorsoIMMAGINI());
        Button btnCambioPath = (Button) findViewById(R.id.btnCambiaPath);

        btnCambioPath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
            }
        });

        Switch swcOffline = (Switch) findViewById(R.id.switchOffline);
        swcOffline.setChecked(VariabiliGlobali.getInstance().isOffline());
        LinearLayout layOffline = (LinearLayout) findViewById(R.id.layOffline);
        if (!VariabiliGlobali.getInstance().isOffline()) {
            layOffline.setVisibility(LinearLayout.GONE);
        } else {
            layOffline.setVisibility(LinearLayout.VISIBLE);
        }
        swcOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setOffline(isChecked);

                if (!VariabiliGlobali.getInstance().isOffline()) {
                    layOffline.setVisibility(LinearLayout.GONE);
                } else {
                    layOffline.setVisibility(LinearLayout.VISIBLE);
                    if(VariabiliGlobali.getInstance().isOffline()) {
                        if (VariabiliGlobali.getInstance().getTxtQuanteImmagini() != null && VariabiliGlobali.getInstance().getTxtQuanteImmagini().length() > 0) {
                            int q = VariabiliGlobali.getInstance().getTxtQuanteImmagini().length();
                            VariabiliGlobali.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate su disco: " + q);
                        } else {
                            ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali();
                            bckLeggeImmaginiLocali.execute();
                        }
                    } else {
                        VariabiliGlobali.getInstance().getTxtQuanteImmagini().setText("Immagini online: " + VariabiliGlobali.getInstance().getImmaginiOnline());
                    }
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });

        Switch swcBlur = (Switch) findViewById(R.id.switchBlur);
        swcBlur.setChecked(VariabiliGlobali.getInstance().isBlur());
        swcBlur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setBlur(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();

                ChangeWallpaper c = new ChangeWallpaper();
                c.setWallpaperLocale(VariabiliGlobali.getInstance().getUltimaImmagine());
            }
        });

        /* Switch swcResize = (Switch) findViewById(R.id.switchResize);
        swcResize.setChecked(VariabiliGlobali.getInstance().isResize());
        swcResize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setResize(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();

                ChangeWallpaper c = new ChangeWallpaper();
                c.setWallpaper(VariabiliGlobali.getInstance().getUltimaImmagine());
            }
        }); */

        ImageView imgCaricamento = (ImageView) findViewById(R.id.imgCaricamento);
        imgCaricamento.setVisibility(LinearLayout.GONE);
        VariabiliGlobali.getInstance().setImgCaricamento(imgCaricamento);

        ImageView imgRefresh = (ImageView) findViewById(R.id.imgRefresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliGlobali.getInstance().isScreenOn()) {
                    VariabiliGlobali.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                    Log.getInstance().ScriveLog("---Cambio Immagine Manuale---");
                    ChangeWallpaper c = new ChangeWallpaper();
                    if (!VariabiliGlobali.getInstance().isOffline()) {
                        boolean fatto = c.setWallpaper(null);
                        Log.getInstance().ScriveLog("---Immagine cambiata manualmente: " + fatto + "---");
                    } else {
                        int numeroRandom = Utility.getInstance().GeneraNumeroRandom(VariabiliGlobali.getInstance().getListaImmagini().size() - 1);
                        if (numeroRandom > -1) {
                            boolean fatto = c.setWallpaper(VariabiliGlobali.getInstance().getListaImmagini().get(numeroRandom));
                            Log.getInstance().ScriveLog("---Immagine cambiata manualmente: " + fatto + "---");
                        } else {
                            Log.getInstance().ScriveLog("---Immagine NON cambiata manualmente: Caricamento immagini in corso---");
                        }
                    }
                } else {
                    Log.getInstance().ScriveLog("---Cambio Immagine posticipato per schermo spento---");
                    VariabiliGlobali.getInstance().setImmagineDaCambiare(true);
                }
            }
        });

        ImageView imgRefreshLocale = (ImageView) findViewById(R.id.imgRefreshLocale);
        imgRefreshLocale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali();
                bckLeggeImmaginiLocali.execute();
            }
        });

        ImageView imgUscita = (ImageView) findViewById(R.id.imgUscita);
        imgUscita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.getInstance().ScriveLog("Applicazione terminata");
                Notifica.getInstance().RimuoviNotifica();
                MainActivity.getAppActivity().stopService(new Intent(MainActivity.getAppActivity(),
                        ServizioBackground.class));

                System.exit(0);
            }
        });

        if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
            Bitmap ultima = BitmapFactory.decodeFile(VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine());
            imgImpostata.setImageBitmap(ultima);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 9999:
                Uri uri = data.getData();
                Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                        DocumentsContract.getTreeDocumentId(uri));
                ConverteNomeUri c = new ConverteNomeUri();
                String path = c.getPath(this, docUri);

                VariabiliGlobali.getInstance().getTxtPath().setText(path);
                VariabiliGlobali.getInstance().setPercorsoIMMAGINI(path);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();

                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali();
                bckLeggeImmaginiLocali.execute();

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Notifica.getInstance().RimuoviNotifica();
        Log.getInstance().ScriveLog("--->Attività distrutta<---");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        Log.getInstance().ScriveLog("--->Tasto premuto: " + keyCode + "<---");

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliGlobali.getInstance().setMascheraAperta(false);
                moveTaskToBack(true);

                return false;
        }

        return false;
    }
}