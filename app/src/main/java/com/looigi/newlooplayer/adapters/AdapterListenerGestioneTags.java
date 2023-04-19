package com.looigi.newlooplayer.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.R;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.WebServices.ChiamateWsAmministrazione;
import com.looigi.newlooplayer.strutture.StrutturaTags;

import java.util.List;

public class AdapterListenerGestioneTags extends BaseAdapter {
    Context context;
    List<StrutturaTags> listaTags;
    LayoutInflater inflter;

    public AdapterListenerGestioneTags(Context applicationContext, List<StrutturaTags> Tags) {
        this.context = context;
        this.listaTags = Tags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaTags.size();
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
        view = inflter.inflate(R.layout.lista_gestionetags, null);
        ImageView btnElimina = (ImageView) view.findViewById(R.id.imgEliminaTag);
        ImageView btnModifica = (ImageView) view.findViewById(R.id.imgModificaTag);

        TextView tags = (TextView) view.findViewById(R.id.txtTag);

        tags.setText(listaTags.get(i).getTag());

        btnModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Tag = listaTags.get(i).getTag();
                String idTag = listaTags.get(i).getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale());
                builder.setTitle("Modifica Tag");

                final EditText input = new EditText(VariabiliGlobali.getInstance().getFragmentActivityPrincipale());
                builder.setView(input);
                input.setText(Tag);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWsAmministrazione c = new ChiamateWsAmministrazione();
                        c.ModificaTag(idTag, input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        btnElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String idTag = listaTags.get(i).getId();
                String Tag = listaTags.get(i).getTag();

                AlertDialog.Builder builder = new AlertDialog.Builder(VariabiliGlobali.getInstance().getFragmentActivityPrincipale());
                builder.setTitle("Eliminazione Tag: " + idTag + "-" + Tag);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWsAmministrazione c = new ChiamateWsAmministrazione();
                        c.EliminaTag(idTag);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return view;
    }
}
