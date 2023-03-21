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

public class AdapterListenerTagsBranoGAR extends BaseAdapter {
    Context context;
    String[] listaTags;
    LayoutInflater inflter;

    public AdapterListenerTagsBranoGAR(Context applicationContext, String[] Tags) {
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
                for (int i = 0; i < VariabiliGlobali.getInstance().getListaTagsArtista().size(); i++) {
                    NuoviTags += VariabiliGlobali.getInstance().getListaTagsArtista().get(i) + ";";
                }
                // VariabiliGlobali.getInstance().getStrutturaDelBrano().setTags(NuoviTags);
                NuoviTags = NuoviTags.replace(Cosa, "");
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
                VariabiliGlobali.getInstance().setListaTagsArtista(l);

                AdapterListenerTagsBranoGAR customAdapter = new AdapterListenerTagsBranoGAR(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        nuovaListaTags);
                OggettiAVideo.getInstance().getLstTagsGAR().setAdapter(customAdapter);

                AdapterListenerTagsTuttiGAR customAdapterT = new AdapterListenerTagsTuttiGAR(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        Utility.getInstance().CreaVettoreTagsAlbum(NuoviTags));
                OggettiAVideo.getInstance().getLstTagsTuttiGAR().setAdapter(customAdapterT);
            }
        });
        return view;
    }
}
