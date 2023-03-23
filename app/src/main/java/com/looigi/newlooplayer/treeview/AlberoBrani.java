package com.looigi.newlooplayer.treeview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.looigi.newlooplayer.Log;
import com.looigi.newlooplayer.OggettiAVideo;
import com.looigi.newlooplayer.R;
import com.looigi.newlooplayer.Utility;
import com.looigi.newlooplayer.VariabiliGlobali;
import com.looigi.newlooplayer.WebServices.ChiamateWs;
import com.looigi.newlooplayer.WebServices.ChiamateWsAmministrazione;
import com.looigi.newlooplayer.adapters.AdapterListenerTagsTutti;
import com.looigi.newlooplayer.download.DownloadImage;
import com.looigi.newlooplayer.strutture.StrutturaArtisti;

import java.util.ArrayList;
import java.util.List;

public class AlberoBrani {
    int posY = 0;

    public void GeneraAlbero() {
        Log.getInstance().ScriveLog("Creazione albero artisti. Inizio");
        LinearLayout layDestinazione = OggettiAVideo.getInstance().getLayAlbero();

        HorizontalScrollView sh =  new HorizontalScrollView(VariabiliGlobali.getInstance().getContext());
        sh.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

        layDestinazione.removeAllViews();
        layDestinazione.addView(sh);

        ScrollView sv =  new ScrollView(VariabiliGlobali.getInstance().getContext());

        LinearLayout.LayoutParams layout_param= new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layout_param.setMargins(5, 3, 3, 3);
        sv.setLayoutParams(layout_param); // new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

        sh.removeAllViews();
        sh.addView(sv);

        LinearLayout laySottoScroll = new LinearLayout(VariabiliGlobali.getInstance().getContext());
        laySottoScroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        laySottoScroll.setOrientation(LinearLayout.VERTICAL);

        sv.removeAllViews();
        sv.addView(laySottoScroll);

        // TreeNode root = TreeNode.root();

        List<String> Padri = new ArrayList<>();

        String vecchioArtista = "";
        List<StrutturaArtisti> lista = VariabiliGlobali.getInstance().getArtisti();
        for (int i = 0; i < lista.size(); i++) {
            String padre = lista.get(i).getNomeArtista();
            if (!padre.isEmpty()) {
                if (!padre.equals(vecchioArtista)) {
                    vecchioArtista = padre;
                    Padri.add(padre);
                }
            }
        }
        Log.getInstance().ScriveLog("Creazione albero artisti. Padri: " + Padri.size());

        for (int i = 0; i < Padri.size(); i++) {
            // Log.getInstance().ScriveLog("Albero: Padri: " + Padri.get(i));
            // TreeNode nodoPadre = new TreeNode(Padri.get(i));
            // nodoPadre.setLevel(0);
            LinearLayout layPadre = new LinearLayout(VariabiliGlobali.getInstance().getContext());
            LinearLayout.LayoutParams paramspadre = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            paramspadre.setMargins(0,5,0,0);
            final Integer ii = i;
            layPadre.setId(1000 + ii);
            layPadre.setLayoutParams(paramspadre);

            ImageView imgPadre = new ImageView(VariabiliGlobali.getInstance().getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
            imgPadre.setLayoutParams(layoutParams);
            imgPadre.setTag(Padri.get(i));
            imgPadre.setImageResource(R.drawable.icona_cerca);
            imgPadre.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String ArtistaPremuto = imgPadre.getTag().toString();

                    ChiamateWs ws = new ChiamateWs();
                    ws.RitornaTagsArtista(ArtistaPremuto);

                    OggettiAVideo.getInstance().getTxtArtistaGAR().setText(ArtistaPremuto);
                    OggettiAVideo.getInstance().getLayGestioneArtista().setVisibility(LinearLayout.VISIBLE);
                    // Toast.makeText(VariabiliGlobali.getInstance().getContext(),"Modifica Artista; " + ArtistaPremuto, Toast.LENGTH_LONG).show();
                }
            });
            if (VariabiliGlobali.getInstance().isAmministratore()) {
                layPadre.addView(imgPadre);
            }

            TextView txtPadre = new TextView(VariabiliGlobali.getInstance().getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            txtPadre.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            txtPadre.setPadding(5, 0, 0, 0);
            txtPadre.setLayoutParams(params);
            txtPadre.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            txtPadre.setText(Padri.get(i));
            txtPadre.setTextColor(VariabiliGlobali.getInstance().getFragmentActivityPrincipale().getResources().getColor(R.color.colorPrimary));
            txtPadre.setTextSize(20);

            txtPadre.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String ArtistaPremuto = txtPadre.getText().toString();

                    ChiamateWs ws = new ChiamateWs();
                    ws.RitornaListaAlbum(ArtistaPremuto);

                    VariabiliGlobali.getInstance().setIdSelezionato(1000 + ii);
                    // VariabiliGlobali.getInstance().setTxtAlberoScelto(txtPadre);
                }
            });
            layPadre.addView(txtPadre);

            laySottoScroll.addView(layPadre);

            List<String> Figli = new ArrayList<>();
            if (VariabiliGlobali.getInstance().getListaAlbum() != null) {
                for (int albums = 0; albums < VariabiliGlobali.getInstance().getListaAlbum().size(); albums++) {
                    if (VariabiliGlobali.getInstance().getListaAlbum().get(albums).getArtista().equals(Padri.get(i))) {
                        Figli.add(VariabiliGlobali.getInstance().getListaAlbum().get(albums).getAnno() + "-" + VariabiliGlobali.getInstance().getListaAlbum().get(albums).getAlbum());
                    }
                }
            }

            // List<TreeNode> nodiFiglio = new ArrayList<>();
            if (Figli.size() > 0) {
                for (int k = 0; k < Figli.size(); k++) {
                    // Log.getInstance().ScriveLog("Albero: Figlio: " + Figli.get(k) + " per padre " + Padri.get(i));

                    // TreeNode nodoFiglio = new TreeNode(new String(Figli.get(k)));
                    // nodoFiglio.setLevel(1);
                    LinearLayout layFiglio = new LinearLayout(VariabiliGlobali.getInstance().getContext());
                    layFiglio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    layFiglio.setOrientation(LinearLayout.HORIZONTAL);
                    layFiglio.setPadding(50, 0, 0, 0);
                    layFiglio.setWeightSum(5);

                    ImageView imgFiglio = new ImageView(VariabiliGlobali.getInstance().getContext());
                    String PathImmagineF = VariabiliGlobali.getInstance().getPercorsoDIR() +
                            "/ImmaginiMusica/" + Padri.get(i) + "/" + Figli.get(k) + "/Cover_" + Padri.get(i) + ".jpg";
                    if (Utility.getInstance().EsisteFile(PathImmagineF)) {
                        Bitmap bitmap = BitmapFactory.decodeFile(PathImmagineF);
                        imgFiglio.setImageBitmap(bitmap);
                    } else {
                        String UrlImmagine = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() +
                                "/ImmaginiMusica/" + Padri.get(i) + "/" + Figli.get(k) + "/Cover_" + Padri.get(i) + ".jpg";
                        new DownloadImage(imgFiglio, UrlImmagine).execute(UrlImmagine);
                    }
                    String Alb = Figli.get(k);
                    String Anno = "";
                    if (Alb.contains("-")) {
                        String[] A = Alb.split("-");
                        Alb = "";
                        for (int iii = 1; iii < A.length; iii++) {
                            Alb += A[iii] + "-";
                        }
                        if (Alb.length() > 0) {
                            Alb = Alb.substring(0, Alb.length() - 1);
                        }
                        Anno = A[0];
                    }
                    String Album = Padri.get(i) + ";" + Anno + ";" + Alb;
                    imgFiglio.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (VariabiliGlobali.getInstance().isAmministratore()) {
                                String[] AlbInterno = Album.split(";");
                                String Artista = AlbInterno[0];
                                String Album = AlbInterno[2];
                                String Anno = AlbInterno[1];
                                VariabiliGlobali.getInstance().setNomeAlbumGA(Album);
                                VariabiliGlobali.getInstance().setNomeArtistaGA(Artista);
                                VariabiliGlobali.getInstance().setAnnoAlbumGA(Anno);
                                // TextView txtAlbum = (TextView) VariabiliGlobali.getInstance().getFragmentActivityPrincipale().findViewById(R.id.txtNomeAlbumGA);
                                OggettiAVideo.getInstance().getTxtNomeAlbumGA().setText(Album + " (" + Artista + "). Anno " + Anno);
                                Bitmap bitmap = BitmapFactory.decodeFile(PathImmagineF);
                                OggettiAVideo.getInstance().getImgAlbumGA().setImageBitmap(bitmap);
                                OggettiAVideo.getInstance().getImgCambiaAlbumGA().setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        ChiamateWsAmministrazione ws = new ChiamateWsAmministrazione();
                                        ws.ScaricaImmagineAlbum(Artista, Album, Anno);
                                    }
                                });

                                ChiamateWs ws = new ChiamateWs();
                                ws.RitornaTagsAlbum();

                                OggettiAVideo.getInstance().getLayCambioImmagineGA().setVisibility(LinearLayout.GONE);
                                OggettiAVideo.getInstance().getLayGestioneAlbum().setVisibility(LinearLayout.VISIBLE);

                                // Toast.makeText(VariabiliGlobali.getInstance().getFragmentActivityPrincipale(),
                                //         "Premuto album: " + Album, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    layFiglio.addView(imgFiglio);

                    LinearLayout.LayoutParams paramsI = new LinearLayout.LayoutParams(
                            150, 150);
                    paramsI.weight = 1.0f;
                    imgFiglio.setLayoutParams(paramsI);

                    TextView txtFiglio = new TextView(VariabiliGlobali.getInstance().getContext());
                    layFiglio.addView(txtFiglio);

                    LinearLayout.LayoutParams paramsF = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    paramsF.weight = 4.0f;
                    txtFiglio.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    txtFiglio.setLayoutParams(paramsF);

                    txtFiglio.setTextSize(20);
                    txtFiglio.setPadding(10, 0,0,0);
                    txtFiglio.setLines(1);
                    txtFiglio.setMaxLines(1);
                    txtFiglio.setTextColor(VariabiliGlobali.getInstance().getFragmentActivityPrincipale().getResources().getColor(R.color.colorPrimary));
                    txtFiglio.setTag(Padri.get(i));
                    txtFiglio.setText(Figli.get(k));
                    txtFiglio.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            String Artista = txtFiglio.getTag().toString();
                            String AlbumPremuto = txtFiglio.getText().toString();
                            String[] a = AlbumPremuto.split("-");
                            AlbumPremuto = a[1];
                            ChiamateWs ws = new ChiamateWs();
                            ws.RitornaListaBrani(Artista, AlbumPremuto);
                        }
                    });

                    laySottoScroll.addView(layFiglio);

                    List<String> Nipoti = new ArrayList<>();
                    if (VariabiliGlobali.getInstance().getListaBrani() != null) {
                        for (int brano = 0; brano < VariabiliGlobali.getInstance().getListaBrani().size(); brano++) {
                            if (VariabiliGlobali.getInstance().getListaBrani().get(brano).getArtista().equals(Padri.get(i)) &&
                                VariabiliGlobali.getInstance().getListaBrani().get(brano).getAlbum().equals(Figli.get(k))) {
                                Nipoti.add(VariabiliGlobali.getInstance().getListaBrani().get(brano).getBrano());
                            }
                        }
                    }
                    if (Nipoti.size() > 0) {
                        // List<TreeNode> nodiNipote = new ArrayList<>();
                        for (int z = 0; z < Nipoti.size(); z++) {
                            // Log.getInstance().ScriveLog("Albero: Nipote: " + Nipoti.get(z) + " per figlio " + Figli.get(k) + " e per padre " + Padri.get(i));

                            // TreeNode nodoNipote = new TreeNode(new String(Nipoti.get(z)));
                            // nodoNipote.setLevel(2);
                            LinearLayout layNipote = new LinearLayout(VariabiliGlobali.getInstance().getContext());
                            layNipote.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                            layNipote.setOrientation(LinearLayout.HORIZONTAL);
                            layNipote.setPadding(150, 0, 0, 0);
                            layNipote.setWeightSum(5);

                            ImageView imgNipote = new ImageView(VariabiliGlobali.getInstance().getContext());
                            if (Utility.getInstance().EsisteFile(PathImmagineF)) {
                                Bitmap bitmap = BitmapFactory.decodeFile(PathImmagineF);
                                imgNipote.setImageBitmap(bitmap);
                            } else {
                                String UrlImmagine = VariabiliGlobali.getInstance().getPercorsoBranoMP3SuURL() +
                                        "/ImmaginiMusica/" + Padri.get(i) + "/" + Figli.get(k) + "/Cover_" + Padri.get(i) + ".jpg";
                                new DownloadImage(imgNipote, UrlImmagine).execute(UrlImmagine);
                            }
                            layNipote.addView(imgNipote);

                            LinearLayout.LayoutParams paramsN = new LinearLayout.LayoutParams(
                                    150, 150);
                                    paramsN.weight = 1.0f;
                            imgNipote.setLayoutParams(paramsN);

                            TextView txtNipote = new TextView(VariabiliGlobali.getInstance().getContext());
                            layNipote.addView(txtNipote);

                            LinearLayout.LayoutParams paramsN2 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            paramsN2.weight = 4.0f;
                            txtNipote.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                            txtNipote.setLayoutParams(paramsN2);
                            txtNipote.setPadding(10, 0,0,0);
                            txtNipote.setTextSize(20);
                            txtNipote.setLines(1);
                            txtNipote.setMaxLines(1);
                            txtNipote.setTextColor(VariabiliGlobali.getInstance().getFragmentActivityPrincipale().getResources().getColor(R.color.colorPrimary));
                            txtNipote.setTag(Padri.get(i) + ";" + Figli.get(k));
                            txtNipote.setText(Nipoti.get(z));
                            txtNipote.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    String[] Campi = txtNipote.getTag().toString().split(";");
                                    String Artista = Campi[0];
                                    String Album = Campi[1];

                                    String BranoPremuto = txtNipote.getText().toString();
                                    Log.getInstance().ScriveLog("Cerca id brano: Artista " + Artista + " Album " + Album + " Brano " + BranoPremuto);
                                    for (int i = 0; i < VariabiliGlobali.getInstance().getListaBrani().size(); i++) {
                                        if (VariabiliGlobali.getInstance().getListaBrani().get(i).getArtista().equals(Artista) &&
                                                VariabiliGlobali.getInstance().getListaBrani().get(i).getAlbum().equals(Album) &&
                                                VariabiliGlobali.getInstance().getListaBrani().get(i).getBrano().equals(BranoPremuto)) {
                                            String id = VariabiliGlobali.getInstance().getListaBrani().get(i).getId();
                                            Utility.getInstance().CaricamentoBrano(id, false);

                                            // Toast.makeText(VariabiliGlobali.getInstance().getContext(), "Premuto Brano: " + id,
                                            //         Toast.LENGTH_LONG).show();
                                            break;
                                        }
                                    }

                                }
                            });
                            laySottoScroll.addView(layNipote);

                            // nodiNipote.add(nodoNipote);
                        }
                        // nodoFiglio.setChildren(nodiNipote);
                    }

                    // nodiFiglio.add(nodoFiglio);
                    // nodoFiglio.setExpanded(true);
                }
            }

            // if (nodiFiglio.size() > 0) {
            // nodoPadre.setChildren(nodiFiglio);
            // nodoPadre.setExpanded(true);
            // }

            // root.addChild(nodoPadre);
        }

        Log.getInstance().ScriveLog("Creazione albero artisti. Finito");

        // View treeView = new TreeView(root, VariabiliGlobali.getInstance().getContext(), new MyNodeViewFactory()).getView();
        // laySottoScroll.removeAllViews();
        // laySottoScroll.addView(treeView);

        // layDestinazione.removeAllViews();
        // layDestinazione.addView(treeView);

        /*Log.getInstance().ScriveLog("Sposto l'albero a " + VariabiliGlobali.getInstance().getPosizioneScrollAlberoY());  */

        if (VariabiliGlobali.getInstance().getIdSelezionato() > -1) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // posY += 30;// VariabiliGlobali.getInstance().getPosizioneScrollAlberoY();
                    // lay.setVerticalScrollbarPosition(posY);
                    // lay.scrollTo(0, posY);

                    LinearLayout textView = (LinearLayout) layDestinazione.findViewById(VariabiliGlobali.getInstance().getIdSelezionato());
                    int posX = textView.getLeft(); //  VariabiliGlobali.getInstance().getPosizioneScrollAlberoX();
                    int posY = textView.getTop(); //  VariabiliGlobali.getInstance().getPosizioneScrollAlberoY();

                    // Toast.makeText(VariabiliGlobali.getInstance().getContext(),"Altezza albero:  " + lay.getHeight() + " - " + OggettiAVideo.getInstance().getScrollViewAlbero().getHeight(), Toast.LENGTH_LONG).show();
                    // Toast.makeText(VariabiliGlobali.getInstance().getContext(), "Sposto l'albero per id " + VariabiliGlobali.getInstance().getIdSelezionato() + " a " + posX + "-" + posY, Toast.LENGTH_LONG).show();

                    sv.scrollTo(
                            posX,
                            posY
                    );

                    // OggettiAVideo.getInstance().getScrollViewAlbero().setScrollY(posY);
                /* OggettiAVideo.getInstance().getScrollViewAlbero().scrollTo(100, posY);
                OggettiAVideo.getInstance().getScrollViewAlbero().post(new Runnable() {
                    @Override
                    public void run() {
                        OggettiAVideo.getInstance().getScrollViewAlbero().smoothScrollBy(100, posY);
                    }
                });

                handler.postDelayed(this, 1000); */
                }
            }, 10);
        }
    }
}
