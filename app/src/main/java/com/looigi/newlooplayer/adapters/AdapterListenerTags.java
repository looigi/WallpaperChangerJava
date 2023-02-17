package com.looigi.newlooplayer.adapters;

import android.content.Context;
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

import com.looigi.newlooplayer.R;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.download.DownloadImage;
import com.looigi.newlooplayer.strutture.StrutturaArtisti;
import com.looigi.newlooplayer.strutture.StrutturaTags;

import java.util.List;

public class AdapterListenerTags extends BaseAdapter {
    Context context;
    List<StrutturaTags> listaTags;
    LayoutInflater inflter;
    String PreferitiTags;
    String PreferitiEliminaTags;

    public AdapterListenerTags(Context applicationContext, List<StrutturaTags> Tags) {
        this.context = context;
        this.listaTags = Tags;
        inflter = (LayoutInflater.from(applicationContext));
        this.PreferitiTags = VariabiliGlobali.getInstance().getPreferitiTags();
        this.PreferitiEliminaTags = VariabiliGlobali.getInstance().getPreferitiEliminaTags();
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
        view = inflter.inflate(R.layout.lista_tags, null);
        boolean soloSelezionati = VariabiliGlobali.getInstance().isSoloSelezionatiTags();
        boolean selezionato = PreferitiTags.contains(";" + listaTags.get(i).getTag() + ";");
        boolean eliminato = PreferitiEliminaTags.contains(";" + listaTags.get(i).getTag() + ";");

        LinearLayout layContenitore = (LinearLayout) view.findViewById(R.id.layContenitoreLista);
        View viewDivisore = (View) view.findViewById(R.id.layDivisore);
        TextView tags = (TextView) view.findViewById(R.id.txtTags);
        Switch switchOnOff = (Switch) view.findViewById(R.id.switchTags);
        Switch switchEliminaOnOff = (Switch) view.findViewById(R.id.switchEliminaTags);

        boolean Visualizza = true;
        if (soloSelezionati) {
            if (!selezionato && !eliminato) {
                Visualizza = false;
            }
        }

        if (Visualizza) {
            layContenitore.setVisibility(LinearLayout.VISIBLE);
            viewDivisore.setVisibility(LinearLayout.VISIBLE);

            switchOnOff.setChecked(selezionato);
            switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (PreferitiTags.contains(";" + listaTags.get(i).getTag() + ";")) {
                        PreferitiTags = PreferitiTags.replace(listaTags.get(i).getTag() + ";", "");
                        if (PreferitiTags.isEmpty()) { PreferitiTags = ";"; }
                        VariabiliGlobali.getInstance().setPreferitiTags(PreferitiTags);
                        switchOnOff.setChecked(false);
                    } else {
                        boolean eliminato = PreferitiEliminaTags.contains(";" + listaTags.get(i).getTag() + ";");
                        if (!eliminato) {
                            PreferitiTags += listaTags.get(i).getTag() + ";";
                            VariabiliGlobali.getInstance().setPreferitiTags(PreferitiTags);
                            switchOnOff.setChecked(true);
                        } else {
                            switchOnOff.setChecked(false);
                        }
                    }
                    // int a = VariabiliGlobali.getInstance().getTotPreferiti();
                }
            });

            tags.setText(listaTags.get(i).getTag());

            switchEliminaOnOff.setChecked(eliminato);
            switchEliminaOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (PreferitiEliminaTags.contains(";" + listaTags.get(i).getTag() + ";")) {
                        PreferitiEliminaTags = PreferitiEliminaTags.replace(listaTags.get(i).getTag() + ";", "");
                        if (PreferitiEliminaTags.isEmpty()) { PreferitiEliminaTags = ";"; }
                        VariabiliGlobali.getInstance().setPreferitiEliminaTags(PreferitiEliminaTags);
                        switchEliminaOnOff.setChecked(false);
                    } else {
                        boolean selezionato = PreferitiTags.contains(";" + listaTags.get(i).getTag() + ";");
                        if (!selezionato) {
                            PreferitiEliminaTags += listaTags.get(i).getTag() + ";";
                            VariabiliGlobali.getInstance().setPreferitiEliminaTags(PreferitiEliminaTags);
                            switchEliminaOnOff.setChecked(true);
                        } else {
                            switchEliminaOnOff.setChecked(false);
                        }
                    }
                    // int a = VariabiliGlobali.getInstance().getTotPreferiti();
                }
            });
        } else {
            layContenitore.setVisibility(LinearLayout.GONE);
            viewDivisore.setVisibility(LinearLayout.GONE);
            tags.setVisibility(LinearLayout.GONE);
            switchOnOff.setVisibility(LinearLayout.GONE);
        }

        return view;
    }
}
