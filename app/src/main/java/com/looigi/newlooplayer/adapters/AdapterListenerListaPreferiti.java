package com.looigi.newlooplayer.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.R;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.db_locale.db_dati;
import com.looigi.newlooplayer.download.DownloadImage;
import com.looigi.newlooplayer.strutture.StrutturaArtisti;
import com.looigi.newlooplayer.strutture.StrutturaListaPreferiti;

import java.util.List;

public class AdapterListenerListaPreferiti extends BaseAdapter {
    Context context;
    List<StrutturaListaPreferiti> listaPreferiti;
    LayoutInflater inflter;

    public AdapterListenerListaPreferiti(Context applicationContext, List<StrutturaListaPreferiti> listaPreferiti) {
        this.context = context;
        this.listaPreferiti = listaPreferiti;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaPreferiti.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_preferiti, null);

        TextView artista = (TextView) view.findViewById(R.id.txtNomeLista);

        artista.setText(listaPreferiti.get(i).getNomeLista());
        artista.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String lista = listaPreferiti.get(i).getNomeLista();

                db_dati db = new db_dati();
                db.RitornaPreferitiDaLista(lista);

                AdapterListenerArtisti customAdapter = new AdapterListenerArtisti(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        VariabiliGlobali.getInstance().getArtisti());
                OggettiAVideo.getInstance().getLstArtisti().setAdapter(customAdapter);

                OggettiAVideo.getInstance().getEdtNomePreferito().setText(lista);

                OggettiAVideo.getInstance().getLayGestionePreferiti().setVisibility(LinearLayout.GONE);

                AlertDialog alertDialog = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Preferiti e Tags impostati");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        ImageView imgElimina = (ImageView) view.findViewById(R.id.imgEliminaPreferito);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String lista = listaPreferiti.get(i).getNomeLista();

                db_dati db = new db_dati();
                db.EliminaListaPreferiti(lista);

                db.RitornaListaPreferiti();

                AdapterListenerListaPreferiti customAdapter = new AdapterListenerListaPreferiti(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        VariabiliGlobali.getInstance().getListaPreferiti());
                OggettiAVideo.getInstance().getLstListaPreferiti().setAdapter(customAdapter);
            }
        });

        return view;
    }
}
