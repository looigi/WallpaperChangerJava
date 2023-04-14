package com.looigi.wallpaperchanger;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VariabiliGlobali {
    private static final VariabiliGlobali ourInstance = new VariabiliGlobali();

    public static VariabiliGlobali getInstance() {
        return ourInstance;
    }

    private VariabiliGlobali() {
    }

    private final String NomeApplicazione = "WallpaperChanger";
    private final String PercorsoDIR = Environment.getExternalStorageDirectory().getPath()+"/LooigiSoft/WallpaperChanger";
    private String PercorsoIMMAGINI = Environment.getExternalStorageDirectory().getPath()+"/LooigiSoft/looWebPlayer/ImmaginiMusica";
    private boolean AzionaDebug = true;
    private boolean screenOn = true;
    private Activity FragmentActivityPrincipale;
    private Context context;
    private int SecondiAlCambio = 60000;
    private boolean immagineDaCambiare = false;
    private List<StrutturaImmagine> listaImmagini = new ArrayList<>();
    private StrutturaImmagine UltimaImmagine;
    private boolean offline = true;
    private boolean ePartito = false;

    private ImageView imgImpostata;
    private TextView txtPath;
    private TextView txtQuanteImmagini;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isePartito() {
        return ePartito;
    }

    public void setePartito(boolean ePartito) {
        this.ePartito = ePartito;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public TextView getTxtQuanteImmagini() {
        return txtQuanteImmagini;
    }

    public void setTxtQuanteImmagini(TextView txtQuanteImmagini) {
        this.txtQuanteImmagini = txtQuanteImmagini;
    }

    public TextView getTxtPath() {
        return txtPath;
    }

    public void setTxtPath(TextView txtPath) {
        this.txtPath = txtPath;
    }

    public ImageView getImgImpostata() {
        return imgImpostata;
    }

    public void setImgImpostata(ImageView imgImpostata) {
        this.imgImpostata = imgImpostata;
    }

    public void setPercorsoIMMAGINI(String percorsoIMMAGINI) {
        PercorsoIMMAGINI = percorsoIMMAGINI;
    }

    public StrutturaImmagine getUltimaImmagine() {
        return UltimaImmagine;
    }

    public void setUltimaImmagine(StrutturaImmagine ultimaImmagine) {
        UltimaImmagine = ultimaImmagine;
    }

    public List<StrutturaImmagine> getListaImmagini() {
        return listaImmagini;
    }

    public void setListaImmagini(List<StrutturaImmagine> listaImmagini) {
        this.listaImmagini = listaImmagini;
    }

    public String getPercorsoIMMAGINI() {
        return PercorsoIMMAGINI;
    }

    public boolean isImmagineDaCambiare() {
        return immagineDaCambiare;
    }

    public void setImmagineDaCambiare(boolean immagineDaCambiare) {
        this.immagineDaCambiare = immagineDaCambiare;
    }

    public Activity getFragmentActivityPrincipale() {
        return FragmentActivityPrincipale;
    }

    public void setFragmentActivityPrincipale(Activity fragmentActivityPrincipale) {
        FragmentActivityPrincipale = fragmentActivityPrincipale;
    }

    public String getNomeApplicazione() {
        return NomeApplicazione;
    }

    public String getPercorsoDIR() {
        return PercorsoDIR;
    }

    public boolean isAzionaDebug() {
        return AzionaDebug;
    }

    public void setAzionaDebug(boolean azionaDebug) {
        AzionaDebug = azionaDebug;
    }

    public boolean isScreenOn() {
        return screenOn;
    }

    public void setScreenOn(boolean screenOn) {
        this.screenOn = screenOn;
    }

    public int getSecondiAlCambio() {
        return SecondiAlCambio;
    }

    public void setSecondiAlCambio(int secondiAlCambio) {
        SecondiAlCambio = secondiAlCambio;
    }
}
