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

import java.time.temporal.ValueRange;
import java.util.List;

public class AdapterListenerTagsTutti extends BaseAdapter {
    Context context;
    List<StrutturaTags> listaTags;
    LayoutInflater inflter;

    public AdapterListenerTagsTutti(Context applicationContext, List<StrutturaTags> Tags) {
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
                String NuovaLista = VariabiliGlobali.getInstance().getStrutturaDelBrano().getTags();
                NuovaLista += ";" + Tag;
                NuovaLista = NuovaLista.replace(";;", ";");
                if (NuovaLista.length() > 0) {
                    if (NuovaLista.substring(0, 1).equals(";")) {
                        NuovaLista = NuovaLista.substring(1, NuovaLista.length());
                    }
                }
                VariabiliGlobali.getInstance().getStrutturaDelBrano().setTags(NuovaLista);
                String[] nuovaListaTags = NuovaLista.split(";");

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
