package com.looigi.newlooplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.R;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerTagsBranoGA extends BaseAdapter {
    Context context;
    String[] listaTags;
    LayoutInflater inflter;

    public AdapterListenerTagsBranoGA(Context applicationContext, String[] Tags) {
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

                String NuoviTags = "";
                for (int i = 0; i < VariabiliGlobali.getInstance().getListaTagsAlbum().size(); i++) {
                    NuoviTags += VariabiliGlobali.getInstance().getListaTagsAlbum().get(i) + ";";
                }
                NuoviTags = NuoviTags.replace(Cosa, "");
                // VariabiliGlobali.getInstance().getStrutturaDelBrano().setTags(NuoviTags);
                NuoviTags = NuoviTags.replace(";;", ";");
                if (NuoviTags.length() > 0) {
                    if (NuoviTags.substring(0, 1).equals(";")) {
                        NuoviTags = NuoviTags.substring(1, NuoviTags.length());
                    }
                }
                String[] nuovaListaTags = NuoviTags.split(";");
                List<String> l = new ArrayList<>();
                for (int i = 0; i < nuovaListaTags.length; i++) {
                    l.add(nuovaListaTags[i]);
                }
                VariabiliGlobali.getInstance().setListaTagsAlbum(l);

                AdapterListenerTagsBranoGA customAdapter = new AdapterListenerTagsBranoGA(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        nuovaListaTags);
                OggettiAVideo.getInstance().getLstTagsGA().setAdapter(customAdapter);

                AdapterListenerTagsTuttiGA customAdapterT = new AdapterListenerTagsTuttiGA(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        Utility.getInstance().CreaVettoreTagsAlbum(NuoviTags));
                OggettiAVideo.getInstance().getLstTagsTuttiGA().setAdapter(customAdapterT);
            }
        });
        return view;
    }
}
