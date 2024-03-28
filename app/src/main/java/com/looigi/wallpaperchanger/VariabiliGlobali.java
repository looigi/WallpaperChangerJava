package com.looigi.wallpaperchanger;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.RemoteViews;
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
    private final String UrlWS = "http://looigi.ddns.net:1050";
    private final String PercorsoImmagineSuURL = "http://looigi.ddns.net:1051";
    private boolean AzionaDebug = true;
    private boolean screenOn = true;
    // private Activity FragmentActivityPrincipale;
    // private Context context;
    private int SecondiAlCambio = 60000;
    // private boolean immagineDaCambiare = false;
    private List<StrutturaImmagine> listaImmagini = new ArrayList<>();
    private StrutturaImmagine UltimaImmagine;
    private int ImmaginiOnline;
    private boolean offline = true;
    private boolean ePartito = false;
    private boolean retePresente = true;

    private ImageView imgImpostata;
    private TextView txtPath;
    private TextView txtQuanteImmagini;
    private boolean blur = true;
    private boolean onOff = true;
    private boolean resize = true;
    private int quantiGiri;
    private int tempoTimer = 30000;
    private ImageView imgCaricamento;
    private TextView txtTempoAlCambio;
    private int SecondiPassati;
    private boolean MascheraAperta = true;
    private boolean scriveTestoSuImmagine = true;
    private String DataAppoggio;
    private String DimeAppoggio;
    // private RemoteViews viewNotifica;

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }

    private boolean ImmagineCambiataConSchermoSpento = false;

    public boolean isImmagineCambiataConSchermoSpento() {
        return ImmagineCambiataConSchermoSpento;
    }

    public void setImmagineCambiataConSchermoSpento(boolean immagineCambiataConSchermoSpento) {
        ImmagineCambiataConSchermoSpento = immagineCambiataConSchermoSpento;
    }
/* public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public RemoteViews getViewNotifica() {
        return viewNotifica;
    }

    public void setViewNotifica(RemoteViews viewNotifica) {
        this.viewNotifica = viewNotifica;
    } */

    public String getDataAppoggio() {
        return DataAppoggio;
    }

    public void setDataAppoggio(String dataAppoggio) {
        DataAppoggio = dataAppoggio;
    }

    public String getDimeAppoggio() {
        return DimeAppoggio;
    }

    public void setDimeAppoggio(String dimeAppoggio) {
        DimeAppoggio = dimeAppoggio;
    }

    public boolean isScriveTestoSuImmagine() {
        return scriveTestoSuImmagine;
    }

    public void setScriveTestoSuImmagine(boolean scriveTestoSuImmagine) {
        this.scriveTestoSuImmagine = scriveTestoSuImmagine;
    }

    public boolean isMascheraAperta() {
        return MascheraAperta;
    }

    public void setMascheraAperta(boolean mascheraAperta) {
        MascheraAperta = mascheraAperta;
    }

    public int getSecondiPassati() {
        return SecondiPassati;
    }

    public void setSecondiPassati(int secondiPassati) {
        SecondiPassati = secondiPassati;
    }

    public TextView getTxtTempoAlCambio() {
        return txtTempoAlCambio;
    }

    public void setTxtTempoAlCambio(TextView txtTempoAlCambio) {
        this.txtTempoAlCambio = txtTempoAlCambio;
    }

    public ImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(ImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public int getTempoTimer() {
        return tempoTimer;
    }

    public int getQuantiGiri() {
        return quantiGiri;
    }

    public void setQuantiGiri(int quantiGiri) {
        this.quantiGiri = quantiGiri;
    }

    public int getImmaginiOnline() {
        return ImmaginiOnline;
    }

    public void setImmaginiOnline(int immaginiOnline) {
        ImmaginiOnline = immaginiOnline;
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isBlur() {
        return blur;
    }

    public void setBlur(boolean blur) {
        this.blur = blur;
    }

    public String getPercorsoImmagineSuURL() {
        return PercorsoImmagineSuURL;
    }

    public String getUrlWS() {
        return UrlWS;
    }

    public boolean isRetePresente() {
        return retePresente;
    }

    public void setRetePresente(boolean retePresente) {
        this.retePresente = retePresente;
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

    /* public boolean isImmagineDaCambiare() {
        return immagineDaCambiare;
    }

    public void setImmagineDaCambiare(boolean immagineDaCambiare) {
        this.immagineDaCambiare = immagineDaCambiare;
    } */

    /* public Activity getFragmentActivityPrincipale() {
        return FragmentActivityPrincipale;
    }

    public void setFragmentActivityPrincipale(Activity fragmentActivityPrincipale) {
        FragmentActivityPrincipale = fragmentActivityPrincipale;
    } */

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
