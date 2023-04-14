package com.looigi.wallpaperchanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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

        Log.getInstance().ScriveLog("");
        Log.getInstance().ScriveLog(">>>>>>>>>>>>>>>>>>>>>>>>NUOVA SESSIONE<<<<<<<<<<<<<<<<<<<<<<<<");

        Permessi p = new Permessi();
        ciSonoPermessi = p.ControllaPermessi();

        if (ciSonoPermessi) {
            EsegueEntrata();
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
        VariabiliGlobali.getInstance().getFragmentActivityPrincipale().startService(new Intent(
                VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                ServizioBackground.class));
        //         VariabiliGlobali.getInstance().getFragmentActivityPrincipale().stopService(new Intent(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
        //                ServizioBackground.class));
    }
}