package com.looigi.newlooplayer.notifiche;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;

public class PassaggioNotifica extends Activity {
    private Context context;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		context = VariabiliGlobali.getInstance().getContext();
		String action="";

		Log.getInstance().ScriveLog("Notifica: onCreate PassaggioNotifica");

		try {
			action = (String) getIntent().getExtras().get("DO");
			Log.getInstance().ScriveLog("Notifica: Action: " + action);
		} catch (Exception e) {
			Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
		}

		if (action!=null) {
			boolean Chiude=true;

			switch (action) {
				case "apre":
					// Utility.getInstance().CambiaMaschera(R.id.home);
					break;
				case "indietro":
					Utility.getInstance().IndietroBrano();
					if (VariabiliGlobali.getInstance().isMinimizzato()) {
						moveTaskToBack(true);
					}
					break;
				case "avanti":
					Utility.getInstance().AvantiBrano();
					if (VariabiliGlobali.getInstance().isMinimizzato()) {
						moveTaskToBack(true);
					}
					break;
				case "play":
					if (VariabiliGlobali.getInstance().isStaSuonando()) {
						Utility.getInstance().premutoPlay(false);
					} else {
						Utility.getInstance().premutoPlay(true);
					}
					if (VariabiliGlobali.getInstance().isMinimizzato()) {
						moveTaskToBack(true);
					}
					break;
			}

			if (Chiude) {
				finish();
			}
		}
    }
}
