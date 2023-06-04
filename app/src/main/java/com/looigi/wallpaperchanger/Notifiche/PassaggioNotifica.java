package com.looigi.wallpaperchanger.Notifiche;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.looigi.wallpaperchanger.ChangeWallpaper;
import com.looigi.wallpaperchanger.Log;
import com.looigi.wallpaperchanger.MainActivity;
import com.looigi.wallpaperchanger.R;
import com.looigi.wallpaperchanger.Utility;
import com.looigi.wallpaperchanger.VariabiliGlobali;

import java.util.Date;

public class PassaggioNotifica extends Activity {
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		String action=null;

		// Log.getInstance().ScriveLog("Notifica: onCreate PassaggioNotifica");

		try {
			if (getIntent().getExtras() != null) {
				action = (String) getIntent().getExtras().get("DO");
				// Log.getInstance().ScriveLog("Notifica: Action: " + action);
			}
		} catch (Exception e) {
			// Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
		}

		if (action!=null) {
			boolean Chiude=true;

			switch (action) {
				case "apre":
					break;
				case "prossima":
					VariabiliGlobali.getInstance().setImmagineCambiataConSchermoSpento(false);
					ChangeWallpaper c = new ChangeWallpaper();
					if (!VariabiliGlobali.getInstance().isOffline()) {
						Log.getInstance().ScriveLog("---Cambio Immagine Manuale da notifica OnLine---");
						boolean fatto = c.setWallpaper(null);
						Log.getInstance().ScriveLog("---Immagine cambiata manualmente Online da notifica: " + fatto + "---");
					} else {
						if (VariabiliGlobali.getInstance().getListaImmagini() == null) {
							Log.getInstance().ScriveLog("---Immagini in array nulle---");
						} else {
							Log.getInstance().ScriveLog("---Immagini in array: " + VariabiliGlobali.getInstance().getListaImmagini().size() + "---");
						}
						if (VariabiliGlobali.getInstance().getListaImmagini() != null && VariabiliGlobali.getInstance().getListaImmagini().size() > 0) {
							Log.getInstance().ScriveLog("---Cambio Immagine Manuale da notifica Offline---");
							int numeroRandom = Utility.getInstance().GeneraNumeroRandom(VariabiliGlobali.getInstance().getListaImmagini().size() - 1);
							if (numeroRandom > -1) {
								Log.getInstance().ScriveLog("---Numero Random: " + numeroRandom + "/" + (VariabiliGlobali.getInstance().getListaImmagini().size() - 1) + "---");
								boolean fatto = c.setWallpaper(VariabiliGlobali.getInstance().getListaImmagini().get(numeroRandom));
								Log.getInstance().ScriveLog("---Immagine cambiata manualmente da notifica: " + fatto + "---");
							} else {
								Log.getInstance().ScriveLog("---Immagine NON cambiata manualmente da notifica: Caricamento immagini non terminato---");
							}
						} else {
							Log.getInstance().ScriveLog("---Non riesco a cambiare l'immagine manuale da notifica in quanto l'array è vuoto---");
						}
					}
					VariabiliGlobali.getInstance().setMascheraAperta(false);
					Activity a = MainActivity.getAppActivity();
					if (a != null) {
						a.moveTaskToBack(true);
					} else {
						Log.getInstance().ScriveLog("Chiusura maschera non possibile in quanto l'activity è nulla");
					}
					break;
			}

			if (Chiude) {
				finish();
			}
		}
    }
}
