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
import com.looigi.newlooplayer.strutture.StrutturaTags;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerTagsTuttiGAR extends BaseAdapter {
    Context context;
    List<StrutturaTags> listaTags;
    LayoutInflater inflter;

    public AdapterListenerTagsTuttiGAR(Context applicationContext, List<StrutturaTags> Tags) {
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
        view = inflter.inflate(R.layout.lista_tagstutti, null);
        ImageView btnAggiunge = (ImageView) view.findViewById(R.id.imgAggiungeTagsBrano);

        TextView tags = (TextView) view.findViewById(R.id.txtTagsBrano);

        tags.setText(listaTags.get(i).getTag());

        btnAggiunge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliGlobali.getInstance().setModificheTags(true);

                String Tag = listaTags.get(i).getTag();
                String ta = "";
                for (int i = 0; i < VariabiliGlobali.getInstance().getListaTagsArtista().size(); i++) {
                    ta += VariabiliGlobali.getInstance().getListaTagsArtista().get(i) + ";";
                }
                String NuovaLista = ta;
                NuovaLista += ";" + Tag;
                NuovaLista = NuovaLista.replace(";;", ";");
                if (NuovaLista.length() > 0) {
                    if (NuovaLista.substring(0, 1).equals(";")) {
                        NuovaLista = NuovaLista.substring(1, NuovaLista.length());
                    }
                }
                // VariabiliGlobali.getInstance().getStrutturaDelBrano().setTags(NuovaLista);
                String[] nuovaListaTags = NuovaLista.split(";");
                List<String> l = new ArrayList<>();
                for (int i = 0; i < nuovaListaTags.length; i++) {
                    l.add(nuovaListaTags[i]);
                }
                VariabiliGlobali.getInstance().setListaTagsArtista(l);

                AdapterListenerTagsBranoGAR customAdapter = new AdapterListenerTagsBranoGAR(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        nuovaListaTags);
                OggettiAVideo.getInstance().getLstTagsGAR().setAdapter(customAdapter);

                AdapterListenerTagsTuttiGAR customAdapterT = new AdapterListenerTagsTuttiGAR(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                        Utility.getInstance().CreaVettoreTagsAlbum(NuovaLista));
                OggettiAVideo.getInstance().getLstTagsTuttiGAR().setAdapter(customAdapterT);
            }
        });
        return view;
    }
}
