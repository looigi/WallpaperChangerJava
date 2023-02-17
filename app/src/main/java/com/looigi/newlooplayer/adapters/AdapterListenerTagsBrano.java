package com.looigi.newlooplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillId;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.R;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.strutture.StrutturaTags;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerTagsBrano extends BaseAdapter {
    Context context;
    String[] listaTags;
    LayoutInflater inflter;

    public AdapterListenerTagsBrano(Context applicationContext, String[] Tags) {
        this.context = context;
        this.listaTags = Tags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaTags.length;
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
        view = inflter.inflate(R.layout.lista_tagsbrano, null);
        ImageView btnElimina = (ImageView) view.findViewById(R.id.imgEliminaTagsBrano);

        TextView tags = (TextView) view.findViewById(R.id.txtTagsBrano);

        tags.setText(listaTags[i]);

        btnElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliGlobali.getInstance().setModificheTags(true);

                String Tag = listaTags[i];

                String Cosa;
                if (i == listaTags.length - 1) {
                    Cosa = Tag;
                } else {
                    Cosa = Tag + ";";
                }
                String NuoviTags = VariabiliGlobali.getInstance().getStrutturaDelBrano().getTags().replace(Cosa, "");
                VariabiliGlobali.getInstance().getStrutturaDelBrano().setTags(NuoviTags);
                NuoviTags = NuoviTags.replace(";;", ";");
                if (NuoviTags.length() > 0) {
                    if (NuoviTags.substring(0, 1).equals(";")) {
                        NuoviTags = NuoviTags.substring(1, NuoviTags.length());
                    }
                }
                String[] nuovaListaTags = NuoviTags.split(";");

                AdapterListenerTagsBrano customAdapter = new AdapterListenerTagsBrano(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        nuovaListaTags);
                OggettiAVideo.getInstance().getLstTagsBrano().setAdapter(customAdapter);

                AdapterListenerTagsTutti customAdapterT = new AdapterListenerTagsTutti(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        Utility.getInstance().CreaVettoreTags());
                OggettiAVideo.getInstance().getLstTagsTutti().setAdapter(customAdapterT);
            }
        });
        return view;
    }
}
