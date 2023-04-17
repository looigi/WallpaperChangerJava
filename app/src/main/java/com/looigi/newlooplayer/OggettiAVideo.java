package com.looigi.newlooplayer;

import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class OggettiAVideo {
    private static final OggettiAVideo ourInstance = new OggettiAVideo();

    public static OggettiAVideo getInstance() {
        return ourInstance;
    }

    private OggettiAVideo() {
    }

    private TextView txtInizio;
    private TextView txtFine;
    private TextView txtNomeBrano;
    private ImmagineZoomabile imgSfondo;
    private ImageView imgSfondoLogo;
    private ImageView imgIndietro;
    private ImageView imgPlay;
    private ImageView imgAvanti;
    private SeekBar seekBar;
    private TextView txtInformazioni;
    private ImageView imgBellezzza0;
    private ImageView imgBellezzza1;
    private ImageView imgBellezzza2;
    private ImageView imgBellezzza3;
    private ImageView imgBellezzza4;
    private ImageView imgBellezzza5;
    private ImageView imgBellezzza6;
    private ImageView imgBellezzza7;
    private ImageView imgBellezzza8;
    private ImageView imgBellezzza9;
    private ImageView imgBellezzza10;
    private ImageView imgNoNet;
    private ListView lstArtisti;
    private TextView txtPreferiti;
    private TextView txtPreferitiElimina;
    private TextView txtTesto;
    private Button btnLista;
    private TextView txtArtista;
    private TextView txtAlbum;
    private ImageView imgDownloadBrano;
    private ImageView imgCuffie;
    private ImageView imgRest;
    private ImageView imgChiudeListaTags;
    // private ImageView imgPregresso;
    private LinearLayout layPregresso;
    private TextView txtBranoPregresso;
    private LinearLayout layAlbero;
    private Button btnListaTags;
    private TextView txtTags;
    private TextView txtEliminaTags;
    private ListView lstTags;
    private TextView txtTagsBrano;
    private ImageView imgErroreBrano;
    private LinearLayout layContenitore;
    private Switch switchPresenteSuDisco;
    private LinearLayout layCaricamento;
    private TextView txtCaricamento;
    private LinearLayout layDownload;
    // private ProgressBar progressDownload;
    private TextView txtDownload;
    private LinearLayout layTesto;
    private LinearLayout layDown;
    private ImageView imgLinguetta1;
    private ImageView imgLinguetta2;
    private LinearLayout layLista;
    private ImageView imgCambiaPregresso;
    private LinearLayout laySettaggi;
    private ImageView imgMenu;
    private LinearLayout layRicerche;
    private ImageView imgRicerche;
    private Switch switchDebug;
    private Switch switchEliminaDebug;
    private LinearLayout layDebug;
    private Button btnDebug;
    private LinearLayout layShareDebug;
    private Button btnShareDebug;
    private Button btnListe;
    private Switch switchOrologio;
    private DigitalClock clock;
    private Switch switchSoloSelezionati;
    private ImageView imgCercaPreferiti;
    private EditText edtRicercaArtisti;
    private Switch switchPreferiti;
    private Switch switchRandom;
    private Switch switchScaricaBrano;
    private Switch switchVisualizzaInfo;
    private LinearLayout layCambioImmagine;
    private Switch switchCambioImmagine;
    private EditText edtSecondi;
    private LinearLayout layOpacitaBottoni;
    private Switch switchOpacitaBottoni;
    private EditText edtOpacitaBottoni;
    private LinearLayout layStelle;
    private Switch switchStelle;
    private Switch switchStelleSuperiori;
    private EditText edtStelle;
    private LinearLayout layRicercaTesto;
    private Switch switchRicercaTesto;
    private EditText edtTesto;
    private Switch switchTags;
    private ImageView imgChiudeESalvaListaTags;
    private Switch switchSoloSelezionatiTags;
    private Switch switchEliminaBrani;
    private LinearLayout layElimina;
    private EditText edtLimiteMB;
    private ImageView imgAnnullaRicerche;
    private ImageView imgAnnullaSettaggi;
    private LinearLayout layListaTags;
    private ImageView imgTagsBrano;
    private LinearLayout layTagsBrano;
    private ListView lstTagsBrano;
    private ListView lstTagsTutti;
    private LinearLayout laySplash;
    private Switch switchDataSuperiore;
    private EditText edtDataSuperiore;
    private Switch switchDataInferiore;
    private EditText edtDataInferiore;
    private Switch switchDate;
    private Button btnDataSuperiore;
    private Button btnDataInferiore;
    private ImageView imgTags;
    private ImageView imgSuoneria;

    private LinearLayout layGestioneAlbum;
    private LinearLayout layCambioImmagineGA;
    private ImageView imgCambiaAlbumGA;
    private ImageView imgSceltaGA;
    private ImageView imgIndietroGA;
    private ImageView imgAvantiGA;
    private TextView txtInfoGA;
    private ImageView imgAlbumGA;
    private EditText edtQuanteImmaginiGA;
    private ImageView imgRinominaAlbumGA;
    private ImageView imgAnnullaRinominaAlbumGA;
    private LinearLayout layEditGA;
    private EditText edtNomeAlbumGA;
    private EditText edtAnnoAlbumGA;
    private TextView txtNomeAlbumGA;
    private ListView lstTagsGA;
    private ListView lstTagsTuttiGA;
    private ImageView imgSalvaTagsGA;

    private LinearLayout layGestioneArtista;
    private TextView txtArtistaGAR;
    private ListView lstTagsGAR;
    private ListView lstTagsTuttiGAR;
    private ImageView imgSalvaTagsGAR;

    private ListView lstListaPreferiti;
    private LinearLayout layGestionePreferiti;
    private EditText edtNomePreferito;

    public static OggettiAVideo getOurInstance() {
        return ourInstance;
    }

    public Switch getSwitchStelleSuperiori() {
        return switchStelleSuperiori;
    }

    public void setSwitchStelleSuperiori(Switch switchStelleSuperiori) {
        this.switchStelleSuperiori = switchStelleSuperiori;
    }

    public EditText getEdtNomePreferito() {
        return edtNomePreferito;
    }

    public void setEdtNomePreferito(EditText edtNomePreferito) {
        this.edtNomePreferito = edtNomePreferito;
    }

    public LinearLayout getLayGestionePreferiti() {
        return layGestionePreferiti;
    }

    public void setLayGestionePreferiti(LinearLayout layGestionePreferiti) {
        this.layGestionePreferiti = layGestionePreferiti;
    }

    public ListView getLstListaPreferiti() {
        return lstListaPreferiti;
    }

    public void setLstListaPreferiti(ListView lstListaPreferiti) {
        this.lstListaPreferiti = lstListaPreferiti;
    }

    public ImageView getImgChiudeListaTags() {
        return imgChiudeListaTags;
    }

    public void setImgChiudeListaTags(ImageView imgChiudeListaTags) {
        this.imgChiudeListaTags = imgChiudeListaTags;
    }

    public ListView getLstTagsGAR() {
        return lstTagsGAR;
    }

    public void setLstTagsGAR(ListView lstTagsGAR) {
        this.lstTagsGAR = lstTagsGAR;
    }

    public ListView getLstTagsTuttiGAR() {
        return lstTagsTuttiGAR;
    }

    public void setLstTagsTuttiGAR(ListView lstTagsTuttiGAR) {
        this.lstTagsTuttiGAR = lstTagsTuttiGAR;
    }

    public ImageView getImgSalvaTagsGAR() {
        return imgSalvaTagsGAR;
    }

    public void setImgSalvaTagsGAR(ImageView imgSalvaTagsGAR) {
        this.imgSalvaTagsGAR = imgSalvaTagsGAR;
    }

    public TextView getTxtArtistaGAR() {
        return txtArtistaGAR;
    }

    public void setTxtArtistaGAR(TextView txtArtistaGAR) {
        this.txtArtistaGAR = txtArtistaGAR;
    }

    public LinearLayout getLayGestioneArtista() {
        return layGestioneArtista;
    }

    public void setLayGestioneArtista(LinearLayout layGestioneArtista) {
        this.layGestioneArtista = layGestioneArtista;
    }

    public ListView getLstTagsGA() {
        return lstTagsGA;
    }

    public void setLstTagsGA(ListView lstTagsGA) {
        this.lstTagsGA = lstTagsGA;
    }

    public ListView getLstTagsTuttiGA() {
        return lstTagsTuttiGA;
    }

    public void setLstTagsTuttiGA(ListView lstTagsTuttiGA) {
        this.lstTagsTuttiGA = lstTagsTuttiGA;
    }

    public ImageView getImgSalvaTagsGA() {
        return imgSalvaTagsGA;
    }

    public void setImgSalvaTagsGA(ImageView imgSalvaTagsGA) {
        this.imgSalvaTagsGA = imgSalvaTagsGA;
    }

    public ImageView getImgBellezzza10() {
        return imgBellezzza10;
    }

    public void setImgBellezzza10(ImageView imgBellezzza10) {
        this.imgBellezzza10 = imgBellezzza10;
    }

    public ImageView getImgRinominaAlbumGA() {
        return imgRinominaAlbumGA;
    }

    public ImageView getImgAnnullaRinominaAlbumGA() {
        return imgAnnullaRinominaAlbumGA;
    }

    public void setImgAnnullaRinominaAlbumGA(ImageView imgAnnullaRinominaAlbumGA) {
        this.imgAnnullaRinominaAlbumGA = imgAnnullaRinominaAlbumGA;
    }

    public EditText getEdtAnnoAlbumGA() {
        return edtAnnoAlbumGA;
    }

    public void setEdtAnnoAlbumGA(EditText edtAnnoAlbumGA) {
        this.edtAnnoAlbumGA = edtAnnoAlbumGA;
    }

    public LinearLayout getLayEditGA() {
        return layEditGA;
    }

    public void setLayEditGA(LinearLayout layEditGA) {
        this.layEditGA = layEditGA;
    }

    public void setImgRinominaAlbumGA(ImageView imgRinominaAlbumGA) {
        this.imgRinominaAlbumGA = imgRinominaAlbumGA;
    }

    public EditText getEdtNomeAlbumGA() {
        return edtNomeAlbumGA;
    }

    public void setEdtNomeAlbumGA(EditText edtNomeAlbumGA) {
        this.edtNomeAlbumGA = edtNomeAlbumGA;
    }

    public TextView getTxtNomeAlbumGA() {
        return txtNomeAlbumGA;
    }

    public void setTxtNomeAlbumGA(TextView txtNomeAlbumGA) {
        this.txtNomeAlbumGA = txtNomeAlbumGA;
    }

    public EditText getEdtQuanteImmaginiGA() {
        return edtQuanteImmaginiGA;
    }

    public void setEdtQuanteImmaginiGA(EditText edtQuanteImmaginiGA) {
        this.edtQuanteImmaginiGA = edtQuanteImmaginiGA;
    }

    public ImageView getImgAlbumGA() {
        return imgAlbumGA;
    }

    public void setImgAlbumGA(ImageView imgAlbumGA) {
        this.imgAlbumGA = imgAlbumGA;
    }

    public TextView getTxtInfoGA() {
        return txtInfoGA;
    }

    public void setTxtInfoGA(TextView txtInfoGA) {
        this.txtInfoGA = txtInfoGA;
    }

    public ImageView getImgIndietroGA() {
        return imgIndietroGA;
    }

    public void setImgIndietroGA(ImageView imgIndietroGA) {
        this.imgIndietroGA = imgIndietroGA;
    }

    public ImageView getImgAvantiGA() {
        return imgAvantiGA;
    }

    public void setImgAvantiGA(ImageView imgAvantiGA) {
        this.imgAvantiGA = imgAvantiGA;
    }

    public ImageView getImgSceltaGA() {
        return imgSceltaGA;
    }

    public void setImgSceltaGA(ImageView imgSceltaGA) {
        this.imgSceltaGA = imgSceltaGA;
    }

    public LinearLayout getLayCambioImmagineGA() {
        return layCambioImmagineGA;
    }

    public void setLayCambioImmagineGA(LinearLayout layCambioImmagineGA) {
        this.layCambioImmagineGA = layCambioImmagineGA;
    }

    public ImageView getImgCambiaAlbumGA() {
        return imgCambiaAlbumGA;
    }

    public void setImgCambiaAlbumGA(ImageView imgCambiaAlbumGA) {
        this.imgCambiaAlbumGA = imgCambiaAlbumGA;
    }

    public LinearLayout getLayGestioneAlbum() {
        return layGestioneAlbum;
    }

    public void setLayGestioneAlbum(LinearLayout layGestioneAlbum) {
        this.layGestioneAlbum = layGestioneAlbum;
    }

    public LinearLayout getLayShareDebug() {
        return layShareDebug;
    }

    public void setLayShareDebug(LinearLayout layShareDebug) {
        this.layShareDebug = layShareDebug;
    }

    public Button getBtnShareDebug() {
        return btnShareDebug;
    }

    public void setBtnShareDebug(Button btnShareDebug) {
        this.btnShareDebug = btnShareDebug;
    }

    public ImageView getImgSuoneria() {
        return imgSuoneria;
    }

    public void setImgSuoneria(ImageView imgSuoneria) {
        this.imgSuoneria = imgSuoneria;
    }

    public ImageView getImgTags() {
        return imgTags;
    }

    public void setImgTags(ImageView imgTags) {
        this.imgTags = imgTags;
    }

    public Button getBtnDataSuperiore() {
        return btnDataSuperiore;
    }

    public void setBtnDataSuperiore(Button btnDataSuperiore) {
        this.btnDataSuperiore = btnDataSuperiore;
    }

    public Button getBtnDataInferiore() {
        return btnDataInferiore;
    }

    public void setBtnDataInferiore(Button btnDataInferiore) {
        this.btnDataInferiore = btnDataInferiore;
    }

    public Switch getSwitchDate() {
        return switchDate;
    }

    public void setSwitchDate(Switch switchDate) {
        this.switchDate = switchDate;
    }

    public Switch getSwitchDataInferiore() {
        return switchDataInferiore;
    }

    public void setSwitchDataInferiore(Switch switchDataInferiore) {
        this.switchDataInferiore = switchDataInferiore;
    }

    public EditText getEdtDataInferiore() {
        return edtDataInferiore;
    }

    public void setEdtDataInferiore(EditText edtDataInferiore) {
        this.edtDataInferiore = edtDataInferiore;
    }

    public Switch getSwitchDataSuperiore() {
        return switchDataSuperiore;
    }

    public void setSwitchDataSuperiore(Switch switchDataSuperiore) {
        this.switchDataSuperiore = switchDataSuperiore;
    }

    public EditText getEdtDataSuperiore() {
        return edtDataSuperiore;
    }

    public void setEdtDataSuperiore(EditText edtDataSuperiore) {
        this.edtDataSuperiore = edtDataSuperiore;
    }

    public LinearLayout getLaySplash() {
        return laySplash;
    }

    public void setLaySplash(LinearLayout laySplash) {
        this.laySplash = laySplash;
    }

    public Switch getSwitchEliminaDebug() {
        return switchEliminaDebug;
    }

    public void setSwitchEliminaDebug(Switch switchEliminaDebug) {
        this.switchEliminaDebug = switchEliminaDebug;
    }

    public ListView getLstTagsTutti() {
        return lstTagsTutti;
    }

    public void setLstTagsTutti(ListView lstTagsTutti) {
        this.lstTagsTutti = lstTagsTutti;
    }

    public ListView getLstTagsBrano() {
        return lstTagsBrano;
    }

    public void setLstTagsBrano(ListView lstTagsBrano) {
        this.lstTagsBrano = lstTagsBrano;
    }

    public ImageView getImgTagsBrano() {
        return imgTagsBrano;
    }

    public void setImgTagsBrano(ImageView imgTagsBrano) {
        this.imgTagsBrano = imgTagsBrano;
    }

    public LinearLayout getLayTagsBrano() {
        return layTagsBrano;
    }

    public void setLayTagsBrano(LinearLayout layTagsBrano) {
        this.layTagsBrano = layTagsBrano;
    }

    public LinearLayout getLayListaTags() {
        return layListaTags;
    }

    public void setLayListaTags(LinearLayout layListaTags) {
        this.layListaTags = layListaTags;
    }

    public LinearLayout getLayRicerche() {
        return layRicerche;
    }

    public void setLayRicerche(LinearLayout layRicerche) {
        this.layRicerche = layRicerche;
    }

    public ImageView getImgRicerche() {
        return imgRicerche;
    }

    public void setImgRicerche(ImageView imgRicerche) {
        this.imgRicerche = imgRicerche;
    }

    public Switch getSwitchDebug() {
        return switchDebug;
    }

    public void setSwitchDebug(Switch switchDebug) {
        this.switchDebug = switchDebug;
    }

    public LinearLayout getLayDebug() {
        return layDebug;
    }

    public void setLayDebug(LinearLayout layDebug) {
        this.layDebug = layDebug;
    }

    public Button getBtnDebug() {
        return btnDebug;
    }

    public void setBtnDebug(Button btnDebug) {
        this.btnDebug = btnDebug;
    }

    public Button getBtnListe() {
        return btnListe;
    }

    public void setBtnListe(Button btnListe) {
        this.btnListe = btnListe;
    }

    public Switch getSwitchOrologio() {
        return switchOrologio;
    }

    public void setSwitchOrologio(Switch switchOrologio) {
        this.switchOrologio = switchOrologio;
    }

    public DigitalClock getClock() {
        return clock;
    }

    public void setClock(DigitalClock clock) {
        this.clock = clock;
    }

    public Switch getSwitchSoloSelezionati() {
        return switchSoloSelezionati;
    }

    public void setSwitchSoloSelezionati(Switch switchSoloSelezionati) {
        this.switchSoloSelezionati = switchSoloSelezionati;
    }

    public ImageView getImgCercaPreferiti() {
        return imgCercaPreferiti;
    }

    public void setImgCercaPreferiti(ImageView imgCercaPreferiti) {
        this.imgCercaPreferiti = imgCercaPreferiti;
    }

    public EditText getEdtRicercaArtisti() {
        return edtRicercaArtisti;
    }

    public void setEdtRicercaArtisti(EditText edtRicercaArtisti) {
        this.edtRicercaArtisti = edtRicercaArtisti;
    }

    public Switch getSwitchPreferiti() {
        return switchPreferiti;
    }

    public void setSwitchPreferiti(Switch switchPreferiti) {
        this.switchPreferiti = switchPreferiti;
    }

    public Switch getSwitchRandom() {
        return switchRandom;
    }

    public void setSwitchRandom(Switch switchRandom) {
        this.switchRandom = switchRandom;
    }

    public Switch getSwitchScaricaBrano() {
        return switchScaricaBrano;
    }

    public void setSwitchScaricaBrano(Switch switchScaricaBrano) {
        this.switchScaricaBrano = switchScaricaBrano;
    }

    public Switch getSwitchVisualizzaInfo() {
        return switchVisualizzaInfo;
    }

    public void setSwitchVisualizzaInfo(Switch switchVisualizzaInfo) {
        this.switchVisualizzaInfo = switchVisualizzaInfo;
    }

    public LinearLayout getLayCambioImmagine() {
        return layCambioImmagine;
    }

    public void setLayCambioImmagine(LinearLayout layCambioImmagine) {
        this.layCambioImmagine = layCambioImmagine;
    }

    public Switch getSwitchCambioImmagine() {
        return switchCambioImmagine;
    }

    public void setSwitchCambioImmagine(Switch switchCambioImmagine) {
        this.switchCambioImmagine = switchCambioImmagine;
    }

    public EditText getEdtSecondi() {
        return edtSecondi;
    }

    public void setEdtSecondi(EditText edtSecondi) {
        this.edtSecondi = edtSecondi;
    }

    public LinearLayout getLayOpacitaBottoni() {
        return layOpacitaBottoni;
    }

    public void setLayOpacitaBottoni(LinearLayout layOpacitaBottoni) {
        this.layOpacitaBottoni = layOpacitaBottoni;
    }

    public Switch getSwitchOpacitaBottoni() {
        return switchOpacitaBottoni;
    }

    public void setSwitchOpacitaBottoni(Switch switchOpacitaBottoni) {
        this.switchOpacitaBottoni = switchOpacitaBottoni;
    }

    public EditText getEdtOpacitaBottoni() {
        return edtOpacitaBottoni;
    }

    public void setEdtOpacitaBottoni(EditText edtOpacitaBottoni) {
        this.edtOpacitaBottoni = edtOpacitaBottoni;
    }

    public LinearLayout getLayStelle() {
        return layStelle;
    }

    public void setLayStelle(LinearLayout layStelle) {
        this.layStelle = layStelle;
    }

    public Switch getSwitchStelle() {
        return switchStelle;
    }

    public void setSwitchStelle(Switch switchStelle) {
        this.switchStelle = switchStelle;
    }

    public EditText getEdtStelle() {
        return edtStelle;
    }

    public void setEdtStelle(EditText edtStelle) {
        this.edtStelle = edtStelle;
    }

    public LinearLayout getLayRicercaTesto() {
        return layRicercaTesto;
    }

    public void setLayRicercaTesto(LinearLayout layRicercaTesto) {
        this.layRicercaTesto = layRicercaTesto;
    }

    public Switch getSwitchRicercaTesto() {
        return switchRicercaTesto;
    }

    public void setSwitchRicercaTesto(Switch switchRicercaTesto) {
        this.switchRicercaTesto = switchRicercaTesto;
    }

    public EditText getEdtTesto() {
        return edtTesto;
    }

    public void setEdtTesto(EditText edtTesto) {
        this.edtTesto = edtTesto;
    }

    public Switch getSwitchTags() {
        return switchTags;
    }

    public void setSwitchTags(Switch switchTags) {
        this.switchTags = switchTags;
    }

    public ImageView getImgChiudeESalvaListaTags() {
        return imgChiudeESalvaListaTags;
    }

    public void setImgChiudeESalvaListaTags(ImageView imgChiudeESalvaListaTags) {
        this.imgChiudeESalvaListaTags = imgChiudeESalvaListaTags;
    }

    public Switch getSwitchSoloSelezionatiTags() {
        return switchSoloSelezionatiTags;
    }

    public void setSwitchSoloSelezionatiTags(Switch switchSoloSelezionatiTags) {
        this.switchSoloSelezionatiTags = switchSoloSelezionatiTags;
    }

    public Switch getSwitchEliminaBrani() {
        return switchEliminaBrani;
    }

    public void setSwitchEliminaBrani(Switch switchEliminaBrani) {
        this.switchEliminaBrani = switchEliminaBrani;
    }

    public LinearLayout getLayElimina() {
        return layElimina;
    }

    public void setLayElimina(LinearLayout layElimina) {
        this.layElimina = layElimina;
    }

    public EditText getEdtLimiteMB() {
        return edtLimiteMB;
    }

    public void setEdtLimiteMB(EditText edtLimiteMB) {
        this.edtLimiteMB = edtLimiteMB;
    }

    public ImageView getImgAnnullaRicerche() {
        return imgAnnullaRicerche;
    }

    public void setImgAnnullaRicerche(ImageView imgAnnullaRicerche) {
        this.imgAnnullaRicerche = imgAnnullaRicerche;
    }

    public ImageView getImgAnnullaSettaggi() {
        return imgAnnullaSettaggi;
    }

    public void setImgAnnullaSettaggi(ImageView imgAnnullaSettaggi) {
        this.imgAnnullaSettaggi = imgAnnullaSettaggi;
    }

    public ImageView getImgMenu() {
        return imgMenu;
    }

    public void setImgMenu(ImageView imgMenu) {
        this.imgMenu = imgMenu;
    }

    public LinearLayout getLaySettaggi() {
        return laySettaggi;
    }

    public void setLaySettaggi(LinearLayout laySettaggi) {
        this.laySettaggi = laySettaggi;
    }

    public ImageView getImgCambiaPregresso() {
        return imgCambiaPregresso;
    }

    public void setImgCambiaPregresso(ImageView imgCambiaPregresso) {
        this.imgCambiaPregresso = imgCambiaPregresso;
    }

    public LinearLayout getLayLista() {
        return layLista;
    }

    public void setLayLista(LinearLayout layLista) {
        this.layLista = layLista;
    }

    public ImageView getImgLinguetta1() {
        return imgLinguetta1;
    }

    public void setImgLinguetta1(ImageView imgLinguetta1) {
        this.imgLinguetta1 = imgLinguetta1;
    }

    public ImageView getImgLinguetta2() {
        return imgLinguetta2;
    }

    public void setImgLinguetta2(ImageView imgLinguetta2) {
        this.imgLinguetta2 = imgLinguetta2;
    }

    public LinearLayout getLayDown() {
        return layDown;
    }

    public void setLayDown(LinearLayout layDown) {
        this.layDown = layDown;
    }

    public LinearLayout getLayTesto() {
        return layTesto;
    }

    public void setLayTesto(LinearLayout layTesto) {
        this.layTesto = layTesto;
    }

    public TextView getTxtPreferitiElimina() {
        return txtPreferitiElimina;
    }

    public void setTxtPreferitiElimina(TextView txtEliminaPreferiti) {
        this.txtPreferitiElimina = txtEliminaPreferiti;
    }

    public TextView getTxtDownload() {
        return txtDownload;
    }

    public void setTxtDownload(TextView txtDownload) {
        this.txtDownload = txtDownload;
    }

    public LinearLayout getLayDownload() {
        return layDownload;
    }

    public void setLayDownload(LinearLayout layDownload) {
        this.layDownload = layDownload;
    }

    /* public ProgressBar getProgressDownload() {
        return progressDownload;
    }

    public void setProgressDownload(ProgressBar progressDownload) {
        this.progressDownload = progressDownload;
    } */

    public LinearLayout getLayCaricamento() {
        return layCaricamento;
    }

    public void setLayCaricamento(LinearLayout layCaricamento) {
        this.layCaricamento = layCaricamento;
    }

    public TextView getTxtCaricamento() {
        return txtCaricamento;
    }

    public void setTxtCaricamento(TextView txtCaricamento) {
        this.txtCaricamento = txtCaricamento;
    }

    public Switch getSwitchPresenteSuDisco() {
        return switchPresenteSuDisco;
    }

    public void setSwitchPresenteSuDisco(Switch switchPresenteSuDisco) {
        this.switchPresenteSuDisco = switchPresenteSuDisco;
    }

    public LinearLayout getLayContenitore() {
        return layContenitore;
    }

    public void setLayContenitore(LinearLayout layContenitore) {
        this.layContenitore = layContenitore;
    }

    public ImageView getImgErroreBrano() {
        return imgErroreBrano;
    }

    public void setImgErroreBrano(ImageView imgErroreBrano) {
        this.imgErroreBrano = imgErroreBrano;
    }

    public TextView getTxtTagsBrano() {
        return txtTagsBrano;
    }

    public void setTxtTagsBrano(TextView txtTagsBrano) {
        this.txtTagsBrano = txtTagsBrano;
    }

    public ListView getLstTags() {
        return lstTags;
    }

    public void setLstTags(ListView lstTags) {
        this.lstTags = lstTags;
    }

    public TextView getTxtTags() {
        return txtTags;
    }

    public void setTxtTags(TextView txtTags) {
        this.txtTags = txtTags;
    }

    public TextView getTxtEliminaTags() {
        return txtEliminaTags;
    }

    public void setTxtEliminaTags(TextView txtEliminaTags) {
        this.txtEliminaTags = txtEliminaTags;
    }

    public Button getBtnListaTags() {
        return btnListaTags;
    }

    public void setBtnListaTags(Button btnListaTags) {
        this.btnListaTags = btnListaTags;
    }

    public LinearLayout getLayAlbero() {
        return layAlbero;
    }

    public void setLayAlbero(LinearLayout layAlbero) {
        this.layAlbero = layAlbero;
    }

    public LinearLayout getLayPregresso() {
        return layPregresso;
    }

    public void setLayPregresso(LinearLayout layPregresso) {
        this.layPregresso = layPregresso;
    }

    public TextView getTxtBranoPregresso() {
        return txtBranoPregresso;
    }

    public void setTxtBranoPregresso(TextView txtBranoPregresso) {
        this.txtBranoPregresso = txtBranoPregresso;
    }

    /* public ImageView getImgPregresso() {
        return imgPregresso;
    }

    public void setImgPregresso(ImageView imgPregresso) {
        this.imgPregresso = imgPregresso;
    } */

    public ImageView getImgRest() {
        return imgRest;
    }

    public void setImgRest(ImageView imgRest) {
        this.imgRest = imgRest;
    }

    public ImageView getImgCuffie() {
        return imgCuffie;
    }

    public void setImgCuffie(ImageView imgCuffie) {
        this.imgCuffie = imgCuffie;
    }

    public TextView getTxtArtista() {
        return txtArtista;
    }

    public void setTxtArtista(TextView txtArtista) {
        this.txtArtista = txtArtista;
    }

    public TextView getTxtAlbum() {
        return txtAlbum;
    }

    public void setTxtAlbum(TextView txtAlbum) {
        this.txtAlbum = txtAlbum;
    }

    public ImageView getImgDownloadBrano() {
        return imgDownloadBrano;
    }

    public void setImgDownloadBrano(ImageView imgDownloadBrano) {
        this.imgDownloadBrano = imgDownloadBrano;
    }

    public Button getBtnLista() {
        return btnLista;
    }

    public void setBtnLista(Button btnLista) {
        this.btnLista = btnLista;
    }

    public TextView getTxtTesto() {
        return txtTesto;
    }

    public void setTxtTesto(TextView txtTesto) {
        this.txtTesto = txtTesto;
    }

    public TextView getTxtPreferiti() {
        return txtPreferiti;
    }

    public void setTxtPreferiti(TextView txtPreferiti) {
        this.txtPreferiti = txtPreferiti;
    }

    public ListView getLstArtisti() {
        return lstArtisti;
    }

    public void setLstArtisti(ListView lstArtisti) {
        this.lstArtisti = lstArtisti;
    }

    public ImageView getImgNoNet() {
        return imgNoNet;
    }

    public void setImgNoNet(ImageView imgNoNet) {
        this.imgNoNet = imgNoNet;
    }

    public ImageView getImgBellezzza0() {
        return imgBellezzza0;
    }

    public void setImgBellezzza0(ImageView imgBellezzza0) {
        this.imgBellezzza0 = imgBellezzza0;
    }

    public ImageView getImgBellezzza1() {
        return imgBellezzza1;
    }

    public void setImgBellezzza1(ImageView imgBellezzza1) {
        this.imgBellezzza1 = imgBellezzza1;
    }

    public ImageView getImgBellezzza2() {
        return imgBellezzza2;
    }

    public void setImgBellezzza2(ImageView imgBellezzza2) {
        this.imgBellezzza2 = imgBellezzza2;
    }

    public ImageView getImgBellezzza3() {
        return imgBellezzza3;
    }

    public void setImgBellezzza3(ImageView imgBellezzza3) {
        this.imgBellezzza3 = imgBellezzza3;
    }

    public ImageView getImgBellezzza4() {
        return imgBellezzza4;
    }

    public void setImgBellezzza4(ImageView imgBellezzza4) {
        this.imgBellezzza4 = imgBellezzza4;
    }

    public ImageView getImgBellezzza5() {
        return imgBellezzza5;
    }

    public void setImgBellezzza5(ImageView imgBellezzza5) {
        this.imgBellezzza5 = imgBellezzza5;
    }

    public ImageView getImgBellezzza6() {
        return imgBellezzza6;
    }

    public void setImgBellezzza6(ImageView imgBellezzza6) {
        this.imgBellezzza6 = imgBellezzza6;
    }

    public ImageView getImgBellezzza7() {
        return imgBellezzza7;
    }

    public void setImgBellezzza7(ImageView imgBellezzza7) {
        this.imgBellezzza7 = imgBellezzza7;
    }

    public ImageView getImgBellezzza8() {
        return imgBellezzza8;
    }

    public void setImgBellezzza8(ImageView imgBellezzza8) {
        this.imgBellezzza8 = imgBellezzza8;
    }

    public ImageView getImgBellezzza9() {
        return imgBellezzza9;
    }

    public void setImgBellezzza9(ImageView imgBellezzza9) {
        this.imgBellezzza9 = imgBellezzza9;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public TextView getTxtInizio() {
        return txtInizio;
    }

    public void setTxtInizio(TextView txtInizio) {
        this.txtInizio = txtInizio;
    }

    public TextView getTxtFine() {
        return txtFine;
    }

    public void setTxtFine(TextView txtFine) {
        this.txtFine = txtFine;
    }

    public TextView getTxtNomeBrano() {
        return txtNomeBrano;
    }

    public void setTxtNomeBrano(TextView txtNomeBrano) {
        this.txtNomeBrano = txtNomeBrano;
    }

    public ImageView getImgSfondoLogo() {
        return imgSfondoLogo;
    }

    public void setImgSfondoLogo(ImageView imgSfondoLogo) {
        this.imgSfondoLogo = imgSfondoLogo;
    }

    public ImmagineZoomabile getImgSfondo() {
        return imgSfondo;
    }

    public void setImgSfondo(ImmagineZoomabile imgSfondo) {
        this.imgSfondo = imgSfondo;
    }

    public ImageView getImgIndietro() {
        return imgIndietro;
    }

    public void setImgIndietro(ImageView imgIndietro) {
        this.imgIndietro = imgIndietro;
    }

    public ImageView getImgPlay() {
        return imgPlay;
    }

    public void setImgPlay(ImageView imgPlay) {
        this.imgPlay = imgPlay;
    }

    public ImageView getImgAvanti() {
        return imgAvanti;
    }

    public void setImgAvanti(ImageView imgAvanti) {
        this.imgAvanti = imgAvanti;
    }

    public TextView getTxtInformazioni() {
        return txtInformazioni;
    }

    public void setTxtInformazioni(TextView txtInformazioni) {
        this.txtInformazioni = txtInformazioni;
    }

    public void ScriveInformazioni() {
        String Informazioni = "";
        // Informazioni += "Qualità Rete: " + Integer.toString(VariabiliGlobali.getInstance().getQualitaRete()) + "\n";
        if (VariabiliGlobali.getInstance().isRetePresente()) {
            if (VariabiliGlobali.getInstance().getTipologiaRete().equals("WIFI")) {
                Informazioni += "WiFi presente: " + VariabiliGlobali.getInstance().getUltimoStatoReteStringa() + " " + VariabiliGlobali.getInstance().getTipoRete();
            } else {
                Informazioni += "Mobile presente: " + VariabiliGlobali.getInstance().getUltimoStatoReteStringa() + " " + VariabiliGlobali.getInstance().getTipoRete();
            }
        } else {
            Informazioni += "Rete assente";
        }
        if (VariabiliGlobali.getInstance().getBraniTotali() != null && VariabiliGlobali.getInstance().getBraniTotali() > -1) {
            Informazioni += "\nBrani filtrati: " + Integer.toString(VariabiliGlobali.getInstance().getBraniTotali());
        }
        if (VariabiliGlobali.getInstance().getListaBraniAscoltati() != null) {
            Informazioni += "\nBrani Ascoltati: " + Integer.toString(VariabiliGlobali.getInstance().getListaBraniAscoltati().size());
        }
        if (VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato() != null) {
            Informazioni += "\nID Brano: " + Integer.toString(VariabiliGlobali.getInstance().getIdUltimoBranoAscoltato());
        }
        if (VariabiliGlobali.getInstance().getPresentiSuDisco() != null) {
            Informazioni += "\nBrani su disco: " + Integer.toString(VariabiliGlobali.getInstance().getPresentiSuDisco());
        }
        Informazioni += "\nDimensioni su disco: " + Utility.getInstance().ScriveSpazioOccupato();
        if (VariabiliGlobali.getInstance().isEliminaBrani()) {
            float p = VariabiliGlobali.getInstance().getLimiteInMb() * 1024;
            String s = "KB.";
            /* if (p > 1024) {
                p /= 1024;
                s = "Kb.";
            } */
            if (p > 1024) {
                p /= 1024;
                s = "Mb.";
            }
            if (p > 1024) {
                p /= 1024;
                s = "Gb.";
            }
            long p2 = (long)(p * 100);
            float p3 =  (float)p2 / 100;
            Informazioni += "\nLimite disco occupato: " + p3 + " " + s;
        }
        Informazioni += "\nArtisti Totali: " + Integer.toString(VariabiliGlobali.getInstance().getArtisti().size());
        if (VariabiliGlobali.getInstance().isRicercaPreferiti()) {
            Informazioni += "\nPreferiti Totali: " + Integer.toString(VariabiliGlobali.getInstance().getTotPreferiti());
        }
        // Informazioni += "Ascoltato: " + Integer.toString(VariabiliGlobali.getInstance().getAscoltato());

        if (VariabiliGlobali.getInstance().getStrutturaDelBrano() != null) {
            OggettiAVideo.getInstance().getTxtArtista().setText(VariabiliGlobali.getInstance().getStrutturaDelBrano().getArtista());
            OggettiAVideo.getInstance().getTxtAlbum().setText(VariabiliGlobali.getInstance().getStrutturaDelBrano().getAlbum());
            OggettiAVideo.getInstance().getTxtTesto().setText(VariabiliGlobali.getInstance().getStrutturaDelBrano().getTesto());
            OggettiAVideo.getInstance().getTxtNomeBrano().setText(VariabiliGlobali.getInstance().getStrutturaDelBrano().getBrano());
        }
        Informazioni += "\nVersione APK: " + BuildConfig.VERSION_NAME;

        this.txtInformazioni.setText(Informazioni);

        int Stelle = VariabiliGlobali.getInstance().getStelleBrano();
        boolean[] bStelle = {false, false, false, false, false, false, false, false, false, false, false};
        for (int i = 0; i <= Stelle; i++) {
            bStelle[i] = true;
        }
        imgBellezzza0.setImageResource(bStelle[0] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza1.setImageResource(bStelle[1] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza2.setImageResource(bStelle[2] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza3.setImageResource(bStelle[3] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza4.setImageResource(bStelle[4] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza5.setImageResource(bStelle[5] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza6.setImageResource(bStelle[6] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza7.setImageResource(bStelle[7] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza8.setImageResource(bStelle[8] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza9.setImageResource(bStelle[9] ? R.drawable.preferito : R.drawable.preferito_vuoto);
        imgBellezzza10.setImageResource(bStelle[10] ? R.drawable.preferito : R.drawable.preferito_vuoto);
    // }
    }
}
