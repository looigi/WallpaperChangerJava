package com.looigi.wallpaperchanger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VariabiliGlobali.getInstance().setContext(this);
        VariabiliGlobali.getInstance().setFragmentActivityPrincipale(this);

        if (!VariabiliGlobali.getInstance().isePartito()) {
            Log.getInstance().EliminaFileLog();
            Log.getInstance().ScriveLog(">>>>>>>>>>>>>>>>>>>>>>>>NUOVA SESSIONE<<<<<<<<<<<<<<<<<<<<<<<<");

            Permessi p = new Permessi();
            ciSonoPermessi = p.ControllaPermessi();

            if (ciSonoPermessi) {
                EsegueEntrata();
            }
        } else {

        }
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

        VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startService(new Intent(
                VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                ServizioBackground.class));

        db_dati db = new db_dati();
        db.LeggeImpostazioni();

        ImpostaOggetti();

        ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali();
        bckLeggeImmaginiLocali.execute();
        //         VariabiliGlobali.getInstance().getFragmentActivityPrincipale().stopService(new Intent(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
        //                ServizioBackground.class));
    }

    private void ImpostaOggetti() {
        ImageView imgImpostata = (ImageView) findViewById(R.id.imgImpostata);
        VariabiliGlobali.getInstance().setImgImpostata(imgImpostata);

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
                edtMinuti.setText(Integer.toString(minuti));

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
            }
        });
        btnPiuMinuti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int minuti = VariabiliGlobali.getInstance().getSecondiAlCambio() / 60000;
                minuti++;
                VariabiliGlobali.getInstance().setSecondiAlCambio(minuti * 60000);
                edtMinuti.setText(Integer.toString(minuti));

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
                }

                db_dati db = new db_dati();
                db.ScriveImpostazioni();
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

                break;
        }
    }
}