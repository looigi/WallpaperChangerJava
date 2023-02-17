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

import java.util.List;

public class AdapterListenerArtisti extends BaseAdapter {
    Context context;
    List<StrutturaArtisti> listaArtisti;
    LayoutInflater inflter;
    String Preferiti;
    String PreferitiElimina;

    public AdapterListenerArtisti(Context applicationContext, List<StrutturaArtisti> Artisti) {
        this.context = context;
        this.listaArtisti = Artisti;
        inflter = (LayoutInflater.from(applicationContext));
        this.Preferiti = VariabiliGlobali.getInstance().getPreferiti();
        this.PreferitiElimina = VariabiliGlobali.getInstance().getPreferitiElimina();
    }

    @Override
    public int getCount() {
        return listaArtisti.size();
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
        view = inflter.inflate(R.layout.lista_artisti, null);
        boolean soloSelezionati = VariabiliGlobali.getInstance().isSoloSelezionati();
        boolean selezionato = Preferiti.contains(";" + listaArtisti.get(i).getNomeArtista() + ";");
        boolean eliminato = PreferitiElimina.contains(";" + listaArtisti.get(i).getNomeArtista() + ";");

        LinearLayout layContenitore = (LinearLayout) view.findViewById(R.id.layContenitoreLista);
        View viewDivisore = (View) view.findViewById(R.id.layDivisore);
        TextView artista = (TextView) view.findViewById(R.id.txtArtista);
        TextView tags = (TextView) view.findViewById(R.id.txtTags);
        Switch switchOnOff = (Switch) view.findViewById(R.id.switchArtista);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        Switch switchEliminaOnOff = (Switch) view.findViewById(R.id.switchEliminaPreferiti);

        boolean Visualizza = true;
        if (soloSelezionati) {
            if (!selezionato && !eliminato) {
                Visualizza = false;
            }
        }

        if (!VariabiliGlobali.getInstance().getRicercaArtisti().isEmpty()) {
            if (Visualizza) {
                if (!listaArtisti.get(i).getNomeArtista().toUpperCase().contains(VariabiliGlobali.getInstance().getRicercaArtisti())) {
                    Visualizza = false;
                }
            }
        }

        if (Visualizza) {
            layContenitore.setVisibility(LinearLayout.VISIBLE);
            viewDivisore.setVisibility(LinearLayout.VISIBLE);

            switchOnOff.setChecked(selezionato);
            switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (Preferiti.contains(";" + listaArtisti.get(i).getNomeArtista() + ";")) {
                        Preferiti = Preferiti.replace(listaArtisti.get(i).getNomeArtista() + ";", "");
                        if (Preferiti.isEmpty()) { Preferiti = ";"; }
                        VariabiliGlobali.getInstance().setPreferiti(Preferiti);
                        switchOnOff.setChecked(false);
                    } else {
                        Preferiti +=  listaArtisti.get(i).getNomeArtista() + ";";
                        VariabiliGlobali.getInstance().setPreferiti(Preferiti);
                        switchOnOff.setChecked(true);
                    }
                    // int a = VariabiliGlobali.getInstance().getTotPreferiti();
                }
            });

            switchEliminaOnOff.setChecked(eliminato);
            switchEliminaOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (PreferitiElimina.contains(";" + listaArtisti.get(i).getNomeArtista() + ";")) {
                        PreferitiElimina = PreferitiElimina.replace(listaArtisti.get(i).getNomeArtista() + ";", "");
                        if (PreferitiElimina.isEmpty()) { PreferitiElimina = ";"; }
                        VariabiliGlobali.getInstance().setPreferitiElimina(PreferitiElimina);
                        switchEliminaOnOff.setChecked(false);
                    } else {
                        PreferitiElimina +=  listaArtisti.get(i).getNomeArtista() + ";";
                        VariabiliGlobali.getInstance().setPreferitiElimina(PreferitiElimina);
                        switchEliminaOnOff.setChecked(true);
                    }
                    // int a = VariabiliGlobali.getInstance().getTotPreferiti();
                }
            });

            artista.setText(listaArtisti.get(i).getNomeArtista());
            String listaTags = "";
            for (int ii = 0; ii < listaArtisti.get(i).getTags().size(); ii++) {
                listaTags += listaArtisti.get(i).getTags().get(ii) + " ";
            }

            String UrlImmagine = VariabiliGlobali.getInstance().getPercorsoDIR() +
                    "/ImmaginiMusica" + listaArtisti.get(i).getImmagine();
            if (!Utility.getInstance().EsisteFile(UrlImmagine)) {
                UrlImmagine = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() +
                        "/ImmaginiMusica" + listaArtisti.get(i).getImmagine();

                // listaTags += "\n" + UrlImmagine;
                new DownloadImage(icon, UrlImmagine).execute(UrlImmagine);
            } else {
                // listaTags += "\n" + UrlImmagine;
                Bitmap bitmap = BitmapFactory.decodeFile(UrlImmagine);
                icon.setImageBitmap(bitmap);
            }
            tags.setText(listaTags);
        } else {
            layContenitore.setVisibility(LinearLayout.GONE);
            viewDivisore.setVisibility(LinearLayout.GONE);
            artista.setVisibility(LinearLayout.GONE);
            tags.setVisibility(LinearLayout.GONE);
            switchOnOff.setVisibility(LinearLayout.GONE);
            icon.setVisibility(LinearLayout.GONE);
        }

        return view;
    }
}
