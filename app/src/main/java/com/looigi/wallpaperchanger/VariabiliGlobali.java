package com.looigi.wallpaperchanger;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

public class VariabiliGlobali {
    private static final VariabiliGlobali ourInstance = new VariabiliGlobali();

    public static VariabiliGlobali getInstance() {
        return ourInstance;
    }

    private VariabiliGlobali() {
    }

    private final String NomeApplicazione = "WallpaperChanger";
    private final String PercorsoDIR= Environment.getExternalStorageDirectory().getPath()+"/LooigiSoft/WallpaperChanger";
    private boolean AzionaDebug = true;
    private boolean screenOn = true;
    private Activity FragmentActivityPrincipale;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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
}
