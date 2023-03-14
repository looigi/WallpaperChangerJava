package com.looigi.newlooplayer;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.looigi.newlooplayer.WebServices.ChiamateWsAmministrazione;

public class Amministrazione {
    private static final Amministrazione ourInstance = new Amministrazione();

    public static Amministrazione getInstance() {
        return ourInstance;
    }

    private Amministrazione() {
    }

    public void GestioneTastiAmministrazione(Activity MascheraPrincipale) {
        Button btnEliminaBrutte = (Button) MascheraPrincipale.findViewById(R.id.btnEliminaBrutte);

        btnEliminaBrutte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWsAmministrazione ws = new ChiamateWsAmministrazione();
                ws.EliminaBrutte();
            }
        });
    }
}
