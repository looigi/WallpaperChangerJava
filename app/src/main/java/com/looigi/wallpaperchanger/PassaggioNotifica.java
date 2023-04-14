package com.looigi.wallpaperchanger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

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
					break;
				case "indietro":
					break;
				case "avanti":
					break;
				case "play":
					break;
			}

			if (Chiude) {
				finish();
			}
		}
    }
}
