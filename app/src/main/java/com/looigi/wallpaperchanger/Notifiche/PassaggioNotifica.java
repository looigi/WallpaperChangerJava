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
			}

			if (Chiude) {
				finish();
			}
		}
    }
}
