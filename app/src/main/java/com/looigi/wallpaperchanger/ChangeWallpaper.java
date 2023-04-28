package com.looigi.wallpaperchanger;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.webkit.URLUtil;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger.DB.ChiamateWS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChangeWallpaper {
	private final int BordoX = 10;
	private final int BordoY = 10;
	private int SchermoX;
	private int SchermoY;

	public ChangeWallpaper() {
		DisplayMetrics metrics = new DisplayMetrics();
		if (MainActivity.getAppActivity() != null) {
			MainActivity.getAppActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

			SchermoX = metrics.widthPixels; //  * 70 / 100;
			SchermoY = metrics.heightPixels;

			Log.getInstance().ScriveLog("Cambio immagine instanziato. Dimensioni schermo: " +
					SchermoX + "x" + SchermoY);
		} else {
			SchermoX = -1;
			SchermoY = -1;
			Log.getInstance().ScriveLog("ERRORE su Cambio immagine: Context nullo");
		}
	}

	public Boolean setWallpaper(StrutturaImmagine src) {
		if (SchermoX == -1) {
			Log.getInstance().ScriveLog("ERRORE su set wallpaper: dimensioni schermo non impostate");
			return false;
		} else {
			if (!VariabiliGlobali.getInstance().isOffline()) {
				Log.getInstance().ScriveLog("Cambio immagine online");

				ChiamateWS c = new ChiamateWS();
				c.TornaProssimaImmagine();
			} else {
				setWallpaperLocale(src);
			}
		}
		return true;
	}

	public Boolean setWallpaperLocale(StrutturaImmagine src) {
		boolean Ritorno = true;

		if (SchermoX == -1) {
			Log.getInstance().ScriveLog("ERRORE su set wallpaper locale: dimensioni schermo non impostate");
			Ritorno = false;
		} else {
			Context context = MainActivity.getAppContext();

			Log.getInstance().ScriveLog("Cambio immagine: Caricamento bitmap.");

			Bitmap setWallToDevice = PrendeImmagineReale(src);

			if (setWallToDevice != null) {
				Log.getInstance().ScriveLog("Cambio immagine: Applicazione wallpaper.");

				WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
				try {
					Log.getInstance().ScriveLog("Cambio immagine: Impostazione dimensioni " + setWallToDevice.getWidth() + "x" + setWallToDevice.getHeight());

					wallpaperManager.setBitmap(setWallToDevice);

					// wallpaperManager.setWallpaperOffsetSteps(1, 1);
					// if (VariabiliGlobali.getInstance().getStretch().equals("S")) {
					// wallpaperManager.suggestDesiredDimensions(setWallToDevice.getWidth(), setWallToDevice.getHeight());
					// } else {
					// 	wallpaperManager.suggestDesiredDimensions(VariabiliGlobali.getInstance().getDimeWallWidthOriginale(), VariabiliGlobali.getInstance().getDimeWallHeightOriginale());
					// }

					Log.getInstance().ScriveLog("Cambio immagine: Settata bitmap.");
				} catch (IOException e) {
					// Log.getInstance().ScriveLog("Errore: " + u.PrendeErroreDaException(e));
					// e.printStackTrace();

					Log.getInstance().ScriveLog("Cambio immagine: Errore " + Utility.getInstance().PrendeErroreDaException(e));

					// Toast.makeText(VariabiliGlobali.getInstance().getContext(),
					// 		u.PrendeErroreDaException(e),
					// 		Toast.LENGTH_LONG).show();

					Ritorno = false;
				}
			} else {
				Ritorno = false;
			}
		}

		return Ritorno;
	}
	
	private Bitmap PrendeImmagineReale(StrutturaImmagine si) {
		Log.getInstance().ScriveLog("Prende immagine sistemata");

		// Bitmap myBitmap = null;

		if (Utility.getInstance().EsisteFile(si.getPathImmagine())) {
			Log.getInstance().ScriveLog("Cambio immagine. File esistente: " + si.getPathImmagine());

			Bitmap myBitmap = null; // = BitmapFactory.decodeFile(si.getPathImmagine());
			try {
				// myBitmap = getPreview(si.getPathImmagine());

				myBitmap = BitmapFactory.decodeFile(si.getPathImmagine());
			} catch (Exception e) {
				Log.getInstance().ScriveLog("Cambio immagine. Errore preview");
			}

			if (myBitmap != null) {
				// if (VariabiliGlobali.getInstance().getStretch().equals("S")) {
					// Log.getInstance().ScriveLog("Cambio immagine. Stretch = S");

					// if (VariabiliGlobali.getInstance().getModalitaVisua().equals("N")) {
			// 			Log.getInstance().ScriveLog("Cambio immagine. Converte dimensioni");

				/* if (VariabiliGlobali.getInstance().isResize()) {
					myBitmap = ConverteDimensioni(myBitmap);
				} */

			 		// if (VariabiliGlobali.getInstance().isBlur()) {
						// if (myBitmap != null) {
							try {
								// Bitmap Immaginona = Bitmap.createBitmap(VariabiliGlobali.getInstance().getSchermoX(), VariabiliGlobali.getInstance().getSchermoY(), Bitmap.Config.ARGB_8888);
								// Canvas comboImage = new Canvas(Immaginona);
								// float Altezza=(((float) (VariabiliGlobali.getInstance().getSchermoY()))/2)-(myBitmap.getHeight()/2);
								// float Larghezza=(((float) (VariabiliGlobali.getInstance().getSchermoX()))/2)-(myBitmap.getWidth()/2);
								// comboImage.drawBitmap(myBitmap, Larghezza, Altezza, null);
								Log.getInstance().ScriveLog("Cambio immagine. Mette bordo a immagine");

								myBitmap = MetteBordoAImmagine(myBitmap);
							} catch (Exception e) {
								Log.getInstance().ScriveLog("Cambio immagine. Mette bordo a immagine. Errore: " + Utility.getInstance().PrendeErroreDaException(e));
								myBitmap = null;
							}
						// } else {
						// 	Log.getInstance().ScriveLog("Cambio immagine. Converte dimensioni ha ritornato null");

						// 	myBitmap = null;
						// }
					// }
					/* } else {
						Log.getInstance().ScriveLog("Cambio immagine. Create scaled bitmap");

						myBitmap = Bitmap.createScaledBitmap(myBitmap, (int) VariabiliGlobali.getInstance().getSchermoX() - BordoX,
								(int) VariabiliGlobali.getInstance().getSchermoY() - BordoY, true);
					} */
				// }
				// SalvaImmagine(myBitmap);
			} else {
				Log.getInstance().ScriveLog("Bitmap nulla");
			}

			Log.getInstance().ScriveLog("Aggiorno notifica");

			VariabiliGlobali.getInstance().setUltimaImmagine(si);
			// Notifica.getInstance().setContext(VariabiliGlobali.getInstance().getContext());

			// Notifica.getInstance().setTitolo(si.getImmagine());
			// Notifica.getInstance().setImmagine(si.getPathImmagine());
			// Notifica.getInstance().RimuoviNotifica();
			Utility.getInstance().AggiornaNotifica();

			Bitmap ultima = BitmapFactory.decodeFile(si.getPathImmagine());
			VariabiliGlobali.getInstance().getImgImpostata().setImageBitmap(ultima);

			VariabiliGlobali.getInstance().setSecondiPassati(0);
			VariabiliGlobali.getInstance().getTxtTempoAlCambio().setText("Prossimo cambio: " +
					VariabiliGlobali.getInstance().getSecondiPassati() + "/" + VariabiliGlobali.getInstance().getQuantiGiri());

			// Notifica.getInstance().AggiornaNotifica();
			db_dati db = new db_dati();
			db.ScriveImpostazioni();

			VariabiliGlobali.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);

			return myBitmap;
		} else {
			Log.getInstance().ScriveLog("Cambio immagine. Svuoto bitmap. File inesistente.");

			return null;
		}
	}
	
	private Bitmap getPreview(String uri) {
		Log.getInstance().ScriveLog("Get preview");

		try {
			File image = new File(uri);

			BitmapFactory.Options bounds = new BitmapFactory.Options();
			bounds.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(image.getPath(), bounds);
			if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
				return null;

			int originalSize = Math.max(bounds.outHeight, bounds.outWidth);

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = originalSize/(SchermoY+SchermoX);

			Log.getInstance().ScriveLog("Get preview fatto");

			return BitmapFactory.decodeFile(image.getPath(), opts);
		} catch (Exception ignored) {
			Log.getInstance().ScriveLog("Prende Preview errore ");
			return null;
		}
	}

	private Bitmap ConverteDimensioni(Bitmap b) {
		if (b!=null) {
			try {
				Log.getInstance().ScriveLog("Converte dimensioni 1");

				// Bitmap bb=b;
				int width = b.getWidth();
				int height = b.getHeight();

				float p1 = width;
				float p2 = height;
				// if (width > SchermoX || height > SchermoY) {
					p1 = (float) width / ((float) SchermoX);
					p2 = (float) height / ((float) SchermoY);
					float p;
					if (p1 > p2) {
						p = p1;
					} else {
						p = p2;
					}

					p1 = width / p;
					p2 = height / p;
				/* } else {
					p1 = width;
					p2 = height;
				} */
				Bitmap bb = Bitmap.createScaledBitmap(b, (int) p1, (int) p2, true);

				Log.getInstance().ScriveLog("Converte dimensioni 2");

				return bb;
			} catch (Exception ignored) {
				Log.getInstance().ScriveLog("Converte dimensioni errore: " + Utility.getInstance().PrendeErroreDaException(ignored));

				return null;
			}
		} else {
			Log.getInstance().ScriveLog("Converte dimensioni. Ritorno nullo");

			return null;
		}
	}

	private Bitmap MetteBordoAImmagine(Bitmap myBitmapPassata) {
		Log.getInstance().ScriveLog("Mette bordo a immagine");

		Bitmap myBitmap = ConverteDimensioni(myBitmapPassata);

		float dimeImmX = myBitmap.getWidth();
		float dimeImmY = myBitmap.getHeight();

		float posX = ((float)SchermoX / 2) - (dimeImmX / 2);
		float posY = ((float)SchermoY / 2) - (dimeImmY / 2);

		Log.getInstance().ScriveLog("Mette bordo 1");

		// ImmagineX = SchermoX-ImmagineX;
		// ImmagineX = SchermoX;
		// ImmagineY = (SchermoY-ImmagineY)/2;

		// ImmagineX = 500;
		// ImmagineY = 100;

		Bitmap myOutputBitmap = myBitmap.copy(myBitmap.getConfig(), true);
		Bitmap immagineDiSfondo = null;
		try {
			Log.getInstance().ScriveLog("Mette bordo 2");

			RenderScript renderScript = RenderScript.create(MainActivity.getAppContext());
			Allocation blurInput = Allocation.createFromBitmap(renderScript, myBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
			Allocation blurOutput = Allocation.createFromBitmap(renderScript, myOutputBitmap);
			ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
					Element.U8_4(renderScript));
			blur.setInput(blurInput);
			blur.setRadius(25); // radius must be 0 < r <= 25
			blur.forEach(blurOutput);
			blurOutput.copyTo(myOutputBitmap);
			renderScript.destroy();

			immagineDiSfondo = Bitmap.createScaledBitmap(myOutputBitmap, SchermoX, SchermoY, true);

			/* try (FileOutputStream out = new FileOutputStream(VariabiliGlobali.getInstance().getPercorsoDIR() + "/WFREGNA.png")) {
				immagineDiSfondo.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
				// PNG is a lossless format, the compression factor (100) is ignored
			} catch (IOException e) {
				e.printStackTrace();
			} */
		} catch (Exception e) {
			Log.getInstance().ScriveLog("Mette bordo: " + Utility.getInstance().PrendeErroreDaException(e));
			int a = 0;
		}

		// int offset = 50;
		// int divisore = 2;

		Log.getInstance().ScriveLog("Mette bordo 2");

		// Bitmap Immaginona = Bitmap.createBitmap((SharedObjects.getInstance().getSchermoX()) + offset * 2,
// 					SharedObjects.getInstance().getSchermoY() + offset * 2, Bitmap.Config.ARGB_8888);
		Bitmap Immaginona = Bitmap.createBitmap(SchermoX, SchermoY, Bitmap.Config.ARGB_8888);

		Log.getInstance().ScriveLog("Mette bordo 3");

		Canvas canvas1 = new Canvas(Immaginona);
		/* if (posY > 0) {
			try {
				int posizY = (posY / divisore);
				Bitmap croppedSuperiore = Bitmap.createBitmap(myOutputBitmap, 0, 0, SchermoX, posizY);
				Bitmap resizedBitmap = Bitmap.createScaledBitmap(croppedSuperiore, SchermoX, posY, false);
				canvas1.drawBitmap(resizedBitmap, 0, 0, null);
			} catch (Exception ignored) {
				Log.getInstance().ScriveLog("Mette bordo 3: Errore " + Utility.getInstance().PrendeErroreDaException(ignored));
			}
		}

		canvas1.drawBitmap(myBitmap, posX, posY, null);

		Log.getInstance().ScriveLog("Mette bordo 4");

		if (posY > 0) {
			try {
				Bitmap croppedInferiore = Bitmap.createBitmap(myOutputBitmap, 0, myBitmap.getHeight() - (posY / divisore), SchermoX, posY / divisore);
				Bitmap resizedBitmap = Bitmap.createScaledBitmap(croppedInferiore, SchermoX, posY, false);
				canvas1.drawBitmap(resizedBitmap, 0, myBitmap.getHeight() + posY + 1, null);
			} catch (Exception ignored) {
				Log.getInstance().ScriveLog("Mette bordo 4: Errore " + Utility.getInstance().PrendeErroreDaException(ignored));
			}
		}

		Log.getInstance().ScriveLog("Mette bordo uscita"); */
		if(VariabiliGlobali.getInstance().isBlur()) {
			canvas1.drawBitmap(immagineDiSfondo, 0, 0, null);
		}
		canvas1.drawBitmap(myBitmap, posX, posY, null);

		return Immaginona;
	}
}
