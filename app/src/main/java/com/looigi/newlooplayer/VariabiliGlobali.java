package com.looigi.newlooplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;

// import com.looigi.newlooplayer.cuffie.GestoreCuffie;
// import com.looigi.newlooplayer.cuffie.GestoreCuffie;
import com.looigi.newlooplayer.strutture.StrutturaArtisti;
import com.looigi.newlooplayer.strutture.StrutturaBrano;
import com.looigi.newlooplayer.strutture.StrutturaImmagini;
import com.looigi.newlooplayer.strutture.StrutturaImmaginiDaCambiare;
import com.looigi.newlooplayer.strutture.StrutturaListaAlbum;
import com.looigi.newlooplayer.strutture.StrutturaListaBrani;
import com.looigi.newlooplayer.strutture.StrutturaListaPreferiti;
import com.looigi.newlooplayer.strutture.StrutturaTags;

import java.util.ArrayList;
import java.util.List;

public class VariabiliGlobali {
    private static final VariabiliGlobali ourInstance = new VariabiliGlobali();

    public static VariabiliGlobali getInstance() {
            return ourInstance;
    }

    private VariabiliGlobali() {
    }

    private Integer idUtente;
    private boolean Amministratore = false;
    private Context context;
    private final String NomeApplicazione = "NewLooPlayer";
    private final String PercorsoDIR= Environment.getExternalStorageDirectory().getPath()+"/LooigiSoft/looWebPlayer";
    private final String PercorsoBranoMP3SuURL = "http://looigi.ddns.net:1051";
    private final String UrlWS = "http://looigi.ddns.net:1021";
    private final int QuanteImmaginePerNuovoScarico = 5;
    private Activity FragmentActivityPrincipale;
    private Integer idUltimoBranoAscoltato;
    // private JSONArray listaBraniCompletaInJSON;
    // private List<DettaglioBrano> ListaBraniCompleta;
    // private List<DettaglioBrano> ListaBraniFiltrata;
    private StrutturaBrano StrutturaDelBrano;
    private List<StrutturaBrano> ListaBraniInLocale;
    private MediaPlayer mediaPlayer;
    private boolean FermaTimer = false;
    // private boolean CambiatoBrano = false;
    private List<Integer> ListaBraniAscoltati = new ArrayList<>();
    // private Integer quantiBraniInTotale;
    private List<StrutturaArtisti> Artisti = new ArrayList<>();
    // private List<StrutturaBrano> BraniInLocale = new ArrayList<>();
    private Integer braniTotali;
    private Integer presentiSuDisco;
    private String Preferiti = "";
    private String PreferitiElimina = "";
    private int totPreferiti;
    private int totPreferitiElimina;
    private int totPreferitiTags;
    private int totPreferitiEliminaTags;
    private boolean RicercaPreferiti = false;
    private int qualitaRete;
    private boolean testoAperto = false;
    private int stelleBrano;
    private boolean stelleSuperiori = true;
    private int ascoltato;
    private boolean staLeggendoProssimoBrano = false;
    private boolean soloSelezionati = false;
    private boolean soloSelezionatiTags = false;
    private boolean branosSuSDOriginale;
    private boolean azionaDebug = true;

    private boolean caricatoIlPregresso = false;
    private StrutturaBrano branoPregresso;
    private StrutturaImmagini immagineDaImpostarePregressa;

    private boolean Random = true;
    private boolean CambioImmagine = true;
    private Integer SecondiCambioImmagine = 15;
    private boolean ScaricaBrano = true;
    private boolean VisualizzaInformazioni = false;
    private boolean branoSuSD = false;

    private boolean RicercaTesto = false;
    private String TestoDaRicercare = "";
    private boolean RicercaEsclusione = false;
    private boolean RicercaStelle = false;
    private boolean RicercaMaiAscoltata = false;
    private boolean RicercaTags = false;
    private String TestoDaEscludere = "";
    private Integer StelleDaRicercare = 7;
    private String TagsDaRicercare = "";
    private boolean RetePresente = true;
    private boolean StaSuonando;
    private String ImmagineAttuale;
    private int DurataBranoInSecondi;
    private long lastTimeChiamata = 0;

    // private GestoreCuffie myReceiverCuffie;
    private long lastTimePressed = 0;
    private boolean CuffieInserite = false;

    private boolean screenOn = true;
    private List<StrutturaListaAlbum> listaAlbum;
    private List<StrutturaListaBrani> listaBrani;
    private List<StrutturaTags> listaTags;
    private String PreferitiTags = ";";
    private String PreferitiEliminaTags = ";";
    private boolean AlberoAperto = false;
    // private int posizioneScrollAlberoX;
    // private int posizioneScrollAlberoY;
    private Integer idSelezionato = -1;
    private boolean ePartito = false;

    private boolean BloccaDownload = false;
    private boolean OpacitaBottoni = true;
    private Integer SecondiOpacitaBottoni = 10;

    private boolean MostraOrologio = false;
    private boolean Minimizzato = false;

    private int SpazioOccupatoSuDisco = 0;
    private boolean EliminaDebug = true;
    private String ricercaArtisti = "";
    private int UltimoStatoRete = -999;
    private String TipologiaRete;
    private String UltimoStatoReteStringa = "";
    private String TipoRete = "";
    private boolean EliminaBrani = false;
    private int LimiteInMb = 500;
    private int ImmaginiScaricate = 0;
    private boolean modificheTags = false;
    private String ControllatoUpdate = "";

    private boolean Date = false;
    private boolean DataSuperiore = false;
    private String txtDataSuperiore = "";
    private boolean DataInferiore = false;
    private String txtDataInferiore = "";
    private List<StrutturaImmaginiDaCambiare> ImmaginiAlbumDaCambiare = new ArrayList<>();
    private Integer quanteImmaginiDaScaricareGA = 10;
    private boolean staEditandoAlbum = false;
    private String nomeAlbumGA;
    private String nomeArtistaGA;
    private String annoAlbumGA;

    private int dimeSchermoX;
    private int dimeSchermoY;
    private AudioManager mAudioManager;
    private ComponentName mReceiverComponent;
    private boolean Simulazione = true;
    private boolean SoloLocale = true;
    private List<String> ListaTagsAlbum = new ArrayList<>();
    private List<String> ListaTagsArtista = new ArrayList<>();
    private List<StrutturaListaPreferiti> ListaPreferiti = new ArrayList<>();
    private boolean SkippatoBrano = false;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isStelleSuperiori() {
        return stelleSuperiori;
    }

    public void setStelleSuperiori(boolean stelleSuperiori) {
        this.stelleSuperiori = stelleSuperiori;
    }

    public String getUrlWS() {
        return UrlWS;
    }

    public boolean isSkippatoBrano() {
        return SkippatoBrano;
    }

    public void setSkippatoBrano(boolean skippatoBrano) {
        SkippatoBrano = skippatoBrano;
    }

    public boolean isSoloLocale() {
        return SoloLocale;
    }

    public void setSoloLocale(boolean soloLocale) {
        SoloLocale = soloLocale;
    }

    public List<StrutturaBrano> getListaBraniInLocale() {
        return ListaBraniInLocale;
    }

    public void setListaBraniInLocale(List<StrutturaBrano> listaBraniInLocale) {
        ListaBraniInLocale = listaBraniInLocale;
    }

    public List<StrutturaListaPreferiti> getListaPreferiti() {
        return ListaPreferiti;
    }

    public void setListaPreferiti(List<StrutturaListaPreferiti> listaPreferiti) {
        ListaPreferiti = listaPreferiti;
    }

    public List<String> getListaTagsArtista() {
        return ListaTagsArtista;
    }

    public void setListaTagsArtista(List<String> listaTagsArtista) {
        ListaTagsArtista = listaTagsArtista;
    }

    public List<String> getListaTagsAlbum() {
        return ListaTagsAlbum;
    }

    public void setListaTagsAlbum(List<String> listaTagsAlbum) {
        ListaTagsAlbum = listaTagsAlbum;
    }

    public boolean isSimulazione() {
        return Simulazione;
    }

    public void setSimulazione(boolean simulazione) {
        Simulazione = simulazione;
    }

    public BroadcastReceiver mNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (VariabiliGlobali.getInstance().isStaSuonando()) {
                Utility.getInstance().premutoPlay(false);
            }
        }
    };

    public AudioManager getmAudioManager() {
        return mAudioManager;
    }

    public void setmAudioManager(AudioManager mAudioManager) {
        this.mAudioManager = mAudioManager;
    }

    public ComponentName getmReceiverComponent() {
        return mReceiverComponent;
    }

    public void setmReceiverComponent(ComponentName mReceiverComponent) {
        this.mReceiverComponent = mReceiverComponent;
    }

    public String getNomeAlbumGA() {
        return nomeAlbumGA;
    }

    public void setNomeAlbumGA(String nomeAlbumGA) {
        this.nomeAlbumGA = nomeAlbumGA;
    }

    public String getNomeArtistaGA() {
        return nomeArtistaGA;
    }

    public void setNomeArtistaGA(String nomeArtistaGA) {
        this.nomeArtistaGA = nomeArtistaGA;
    }

    public String getAnnoAlbumGA() {
        return annoAlbumGA;
    }

    public void setAnnoAlbumGA(String annoAlbumGA) {
        this.annoAlbumGA = annoAlbumGA;
    }

    public boolean isStaEditandoAlbum() {
        return staEditandoAlbum;
    }

    public void setStaEditandoAlbum(boolean staEditandoAlbum) {
        this.staEditandoAlbum = staEditandoAlbum;
    }

    public Integer getQuanteImmaginiDaScaricareGA() {
        return quanteImmaginiDaScaricareGA;
    }

    public void setQuanteImmaginiDaScaricareGA(Integer quanteImmaginiDaScaricareGA) {
        this.quanteImmaginiDaScaricareGA = quanteImmaginiDaScaricareGA;
    }

    public List<StrutturaImmaginiDaCambiare> getImmaginiAlbumDaCambiare() {
        return ImmaginiAlbumDaCambiare;
    }

    public void setImmaginiAlbumDaCambiare(List<StrutturaImmaginiDaCambiare> immaginiAlbumDaCambiare) {
        ImmaginiAlbumDaCambiare = immaginiAlbumDaCambiare;
    }

    public int getDimeSchermoX() {
        return dimeSchermoX;
    }

    public void setDimeSchermoX(int dimeSchermoX) {
        this.dimeSchermoX = dimeSchermoX;
    }

    public int getDimeSchermoY() {
        return dimeSchermoY;
    }

    public void setDimeSchermoY(int dimeSchermoY) {
        this.dimeSchermoY = dimeSchermoY;
    }

    public boolean isDate() {
        return Date;
    }

    public void setDate(boolean date) {
        Date = date;
    }

    public boolean isDataInferiore() {
        return DataInferiore;
    }

    public void setDataInferiore(boolean dataInferiore) {
        DataInferiore = dataInferiore;
    }

    public String getTxtDataInferiore() {
        return txtDataInferiore;
    }

    public void setTxtDataInferiore(String txtDataInferiore) {
        this.txtDataInferiore = txtDataInferiore;
    }

    public String getTxtDataSuperiore() {
        return txtDataSuperiore;
    }

    public void setTxtDataSuperiore(String txtDataSuperiore) {
        this.txtDataSuperiore = txtDataSuperiore;
    }

    public boolean isDataSuperiore() {
        return DataSuperiore;
    }

    public void setDataSuperiore(boolean dataSuperiore) {
        DataSuperiore = dataSuperiore;
    }

    public String isControllatoUpdate() {
        return ControllatoUpdate;
    }

    public void setControllatoUpdate(String controllatoUpdate) {
        ControllatoUpdate = controllatoUpdate;
    }

    public boolean isModificheTags() {
        return modificheTags;
    }

    public void setModificheTags(boolean modificheTags) {
        this.modificheTags = modificheTags;
    }

    public int getQuanteImmaginePerNuovoScarico() {
        return QuanteImmaginePerNuovoScarico;
    }

    public int getImmaginiScaricate() {
        return ImmaginiScaricate;
    }

    public void setImmaginiScaricate(int immaginiScaricate) {
        ImmaginiScaricate = immaginiScaricate;
    }

    public boolean isEliminaBrani() {
        return EliminaBrani;
    }

    public void setEliminaBrani(boolean eliminaBrani) {
        EliminaBrani = eliminaBrani;
    }

    public int getLimiteInMb() {
        return LimiteInMb;
    }

    public void setLimiteInMb(int limiteInMb) {
        LimiteInMb = limiteInMb;
    }

    public String getTipoRete() {
        return TipoRete;
    }

    public void setTipoRete(String tipoRete) {
        TipoRete = tipoRete;
    }

    public String getUltimoStatoReteStringa() {
        return UltimoStatoReteStringa;
    }

    public void setUltimoStatoReteStringa(String ultimoStatoReteStringa) {
        UltimoStatoReteStringa = ultimoStatoReteStringa;
    }

    public String getTipologiaRete() {
        return TipologiaRete;
    }

    public void setTipologiaRete(String tipologiaRete) {
        TipologiaRete = tipologiaRete;
    }

    public int getUltimoStatoRete() {
        return UltimoStatoRete;
    }

    public void setUltimoStatoRete(int ultimoStatoRete) {
        UltimoStatoRete = ultimoStatoRete;
    }

    public String getRicercaArtisti() {
        return ricercaArtisti;
    }

    public void setRicercaArtisti(String ricercaArtisti) {
        this.ricercaArtisti = ricercaArtisti;
    }

    public boolean isEliminaDebug() {
        return EliminaDebug;
    }

    public void setEliminaDebug(boolean eliminaDebug) {
        EliminaDebug = eliminaDebug;
    }

    public int getSpazioOccupatoSuDisco() {
        return SpazioOccupatoSuDisco;
    }

    public void setSpazioOccupatoSuDisco(int spazioOccupatoSuDisco) {
        SpazioOccupatoSuDisco = spazioOccupatoSuDisco;
    }

    public boolean isMinimizzato() {
        return Minimizzato;
    }

    public void setMinimizzato(boolean minimizzato) {
        Minimizzato = minimizzato;
    }

    public boolean isMostraOrologio() {
        return MostraOrologio;
    }

    public void setMostraOrologio(boolean mostraOrologio) {
        MostraOrologio = mostraOrologio;
    }

    public boolean isOpacitaBottoni() {
        return OpacitaBottoni;
    }

    public void setOpacitaBottoni(boolean opacitaBottoni) {
        OpacitaBottoni = opacitaBottoni;
    }

    public Integer getSecondiOpacitaBottoni() {
        return SecondiOpacitaBottoni;
    }

    public void setSecondiOpacitaBottoni(Integer secondiOpacitaBottoni) {
        SecondiOpacitaBottoni = secondiOpacitaBottoni;
    }

    public boolean isBloccaDownload() {
        return BloccaDownload;
    }

    public void setBloccaDownload(boolean bloccaDownload) {
        BloccaDownload = bloccaDownload;
    }

    public boolean isePartito() {
        return ePartito;
    }

    public void setePartito(boolean ePartito) {
        this.ePartito = ePartito;
    }

    public Integer getIdSelezionato() {
        return idSelezionato;
    }

    public void setIdSelezionato(Integer idSelezionato) {
        this.idSelezionato = idSelezionato;
    }
/* public int getPosizioneScrollAlberoX() {
        return posizioneScrollAlberoX;
    }

    public void setPosizioneScrollAlberoX(int posizioneScrollAlberoX) {
        if (posizioneScrollAlberoX > 0) {
            this.posizioneScrollAlberoX = posizioneScrollAlberoX;
        }
    }

    public int getPosizioneScrollAlberoY() {
        return posizioneScrollAlberoY;
    }

    public void setPosizioneScrollAlberoY(int posizioneScrollAlberoY) {
        if (posizioneScrollAlberoY > 0) {
            this.posizioneScrollAlberoY = posizioneScrollAlberoY;
        }
    } */

    public boolean isAlberoAperto() {
        return AlberoAperto;
    }

    public void setAlberoAperto(boolean alberoAperto) {
        AlberoAperto = alberoAperto;
    }

    public List<StrutturaListaBrani> getListaBrani() {
        return listaBrani;
    }

    public void setListaBrani(List<StrutturaListaBrani> listaBrani) {
        this.listaBrani = listaBrani;
    }

    public String getPreferitiTags() {
        if (PreferitiTags.isEmpty()) {
            PreferitiTags = ";";
        }

        return PreferitiTags;
    }

    public void setPreferitiTags(String preferitiTags) {
        PreferitiTags = preferitiTags;
        ContaTags();
    }

    public void setPreferitiEliminaTags(String preferitiEliminaTags) {
        PreferitiEliminaTags = preferitiEliminaTags;
        ContaEliminaTags();
    }

    public String getPreferitiEliminaTags() {
        if (PreferitiEliminaTags.isEmpty()) {
            PreferitiEliminaTags = ";";
        }

        return PreferitiEliminaTags;
    }

    public boolean isSoloSelezionatiTags() {
        return soloSelezionatiTags;
    }

    public void setSoloSelezionatiTags(boolean soloSelezionatiTags) {
        this.soloSelezionatiTags = soloSelezionatiTags;
    }

    public List<StrutturaTags> getListaTags() {
        return listaTags;
    }

    public void setListaTags(List<StrutturaTags> listaTags) {
        this.listaTags = listaTags;
    }

    public List<StrutturaListaAlbum> getListaAlbum() {
        return listaAlbum;
    }

    public void setListaAlbum(List<StrutturaListaAlbum> listaBrani) {
        this.listaAlbum = listaBrani;
    }

    public long getLastTimeChiamata() {
        return lastTimeChiamata;
    }

    public void setLastTimeChiamata(long lastTimeChiamata) {
        this.lastTimeChiamata = lastTimeChiamata;
    }

    public StrutturaImmagini getImmagineDaImpostarePregressa() {
        return immagineDaImpostarePregressa;
    }

    public void setImmagineDaImpostarePregressa(StrutturaImmagini immagineDaImpostarePregressa) {
        this.immagineDaImpostarePregressa = immagineDaImpostarePregressa;
    }

    public boolean isCaricatoIlPregresso() {
        return caricatoIlPregresso;
    }

    public void setCaricatoIlPregresso(boolean caricatoIlPregresso) {
        this.caricatoIlPregresso = caricatoIlPregresso;
    }

    public StrutturaBrano getBranoPregresso() {
        return branoPregresso;
    }

    public void setBranoPregresso(StrutturaBrano branoPregresso) {
        this.branoPregresso = branoPregresso;
    }

    public boolean isAmministratore() {
        return Amministratore;
    }

    public void setAmministratore(boolean amministratore) {
        Amministratore = amministratore;
    }

    public boolean isScreenOn() {
        return screenOn;
    }

    public void setScreenOn(boolean screenOn) {
        this.screenOn = screenOn;
    }

    public boolean isCuffieInserite() {
        return CuffieInserite;
    }

    public void setCuffieInserite(boolean cuffieInserite) {
        CuffieInserite = cuffieInserite;
    }

    /* public GestoreCuffie getMyReceiverCuffie() {
        return myReceiverCuffie;
    }

    public void setMyReceiverCuffie(GestoreCuffie myReceiverCuffie) {
        this.myReceiverCuffie = myReceiverCuffie;
    } */

    public long getLastTimePressed() {
        return lastTimePressed;
    }

    public void setLastTimePressed(long lastTimePressed) {
        this.lastTimePressed = lastTimePressed;
    }

    public List<Integer> getListaBraniAscoltati() {
        return ListaBraniAscoltati;
    }

    public boolean isAzionaDebug() {
        return azionaDebug;
    }

    public void setAzionaDebug(boolean azionaDebug) {
        this.azionaDebug = azionaDebug;
    }

    public boolean isBranosSuSDOriginale() {
        return branosSuSDOriginale;
    }

    public void setBranosSuSDOriginale(boolean branosSuSDOriginale) {
        this.branosSuSDOriginale = branosSuSDOriginale;
    }

    public boolean isSoloSelezionati() {
        return soloSelezionati;
    }

    public void setSoloSelezionati(boolean soloSelezionati) {
        this.soloSelezionati = soloSelezionati;
    }
/* public Integer getQuantiBraniInTotale() {
        return quantiBraniInTotale;
    }

    public void setQuantiBraniInTotale(Integer quantiBraniInTotale) {
        this.quantiBraniInTotale = quantiBraniInTotale;
    }

    public List<StrutturaBrano> getBraniInLocale() {
        return BraniInLocale;
    }

    public void setBraniInLocale(List<StrutturaBrano> braniInLocale) {
        BraniInLocale = braniInLocale;
    } */

    public boolean isStaLeggendoProssimoBrano() {
        return staLeggendoProssimoBrano;
    }

    public void setStaLeggendoProssimoBrano(boolean staLeggendoProssimoBrano) {
        this.staLeggendoProssimoBrano = staLeggendoProssimoBrano;
    }

    public int getStelleBrano() {
        return stelleBrano;
    }

    public void setStelleBrano(int stelleBrano) {
        this.stelleBrano = stelleBrano;
    }

    public int getAscoltato() {
        return ascoltato;
    }

    public void setAscoltato(int ascoltato) {
        this.ascoltato = ascoltato;
    }

    public boolean isTestoAperto() {
        return testoAperto;
    }

    public void setTestoAperto(boolean testoAperto) {
        this.testoAperto = testoAperto;
    }

    public int getQualitaRete() {
        return qualitaRete;
    }

    public void setQualitaRete(int qualitaRete) {
        this.qualitaRete = qualitaRete;
    }

    public boolean isRicercaPreferiti() {
        return RicercaPreferiti;
    }

    public void setRicercaPreferiti(boolean ricercaPreferiti) {
        RicercaPreferiti = ricercaPreferiti;
    }

    public String getPreferiti() {
        if (Preferiti.isEmpty()) {
            Preferiti = ";";
        }

        return Preferiti;
    }

    public String getPreferitiElimina() {
        if (PreferitiElimina.isEmpty()) {
            PreferitiElimina = ";";
        }

        return PreferitiElimina;
    }

    public int getTotPreferiti() {
        return totPreferiti;
    }

    public void ContaTags() {
        if (PreferitiTags.contains(";")) {
            String[] tot = PreferitiTags.split(";");
            totPreferitiTags = tot.length - 1;
            if (totPreferitiTags < 0) {
                totPreferitiTags = 0;
            }
        } else {
            totPreferitiTags = 0;
        }

        if (OggettiAVideo.getInstance().getTxtTags() != null) {
            OggettiAVideo.getInstance().getTxtTags().setText("Tags: " + Integer.toString(totPreferitiTags));
        }
    }

    public void ContaEliminaTags() {
        if (PreferitiEliminaTags.contains(";")) {
            String[] tot = PreferitiEliminaTags.split(";");
            totPreferitiEliminaTags = tot.length - 1;
            if (totPreferitiEliminaTags < 0) {
                totPreferitiEliminaTags = 0;
            }
        } else {
            totPreferitiEliminaTags = 0;
        }

        if (OggettiAVideo.getInstance().getTxtEliminaTags() != null) {
            OggettiAVideo.getInstance().getTxtEliminaTags().setText("Eliminati: " + Integer.toString(totPreferitiEliminaTags));
        }
    }

    public void ContaPreferiti() {
        if (Preferiti.contains(";")) {
            String[] tot = Preferiti.split(";");
            totPreferiti = tot.length - 1;
            if (totPreferiti < 0) {
                totPreferiti = 0;
            }
        } else {
            totPreferiti = 0;
        }

        if (OggettiAVideo.getInstance().getTxtPreferiti() != null) {
            OggettiAVideo.getInstance().getTxtPreferiti().setText("Preferiti: " + Integer.toString(totPreferiti));
        }
    }

    public void ContaPreferitiElimina() {
        if (PreferitiElimina.contains(";")) {
            String[] tot = PreferitiElimina.split(";");
            totPreferitiElimina = tot.length - 1;
            if (totPreferitiElimina < 0) {
                totPreferitiElimina = 0;
            }
        } else {
            totPreferitiElimina = 0;
        }

        if (OggettiAVideo.getInstance().getTxtPreferitiElimina() != null) {
            OggettiAVideo.getInstance().getTxtPreferitiElimina().setText("Eliminati: " + Integer.toString(totPreferitiElimina));
        }
    }

    public void setPreferiti(String preferiti) {
        Preferiti = preferiti;
        ContaPreferiti();
    }

    public void setPreferitiElimina(String sPreferitiElimina) {
        PreferitiElimina = sPreferitiElimina;
        ContaPreferitiElimina();
    }

    public Integer getPresentiSuDisco() {
        return presentiSuDisco;
    }

    public void setPresentiSuDisco(Integer presentiSuDisco) {
        this.presentiSuDisco = presentiSuDisco;
    }

    public boolean isBranoSuSD() {
        return branoSuSD;
    }

    public void setBranoSuSD(boolean branoSuSD) {
        this.branoSuSD = branoSuSD;
    }

    public Integer getSecondiCambioImmagine() {
        return SecondiCambioImmagine;
    }

    public void setSecondiCambioImmagine(Integer secondiCambioImmagine) {
        SecondiCambioImmagine = secondiCambioImmagine;
    }

    public boolean isCambioImmagine() {
        return CambioImmagine;
    }

    public void setCambioImmagine(boolean cambioImmagine) {
        CambioImmagine = cambioImmagine;
    }

    public boolean isVisualizzaInformazioni() {
        return VisualizzaInformazioni;
    }

    public void setVisualizzaInformazioni(boolean visualizzaInformazioni) {
        VisualizzaInformazioni = visualizzaInformazioni;
    }

    public boolean isFermaTimer() {
        return FermaTimer;
    }

    public void setFermaTimer(boolean fermaTimer) {
        FermaTimer = fermaTimer;
    }

    public boolean isRandom() {
        return Random;
    }

    public void setRandom(boolean random) {
        Random = random;
    }

    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    public String getNomeApplicazione() {
        return NomeApplicazione;
    }

    public String getPercorsoBranoMP3SuURL() {
        return PercorsoBranoMP3SuURL;
    }

    public Activity getFragmentActivityPrincipale() {
        return FragmentActivityPrincipale;
    }

    public void setFragmentActivityPrincipale(Activity fragmentActivityPrincipale) {
        FragmentActivityPrincipale = fragmentActivityPrincipale;
    }

    public String getPercorsoDIR() {
        return PercorsoDIR;
    }

    public Integer getIdUltimoBranoAscoltato() {
        return idUltimoBranoAscoltato;
    }

    public void setIdUltimoBranoAscoltato(Integer ultimoBranoAscoltato) {
        idUltimoBranoAscoltato = ultimoBranoAscoltato;
    }

    /* public JSONArray getListaBraniCompletaInJSON() {
        return listaBraniCompletaInJSON;
    }

    public void setListaBraniCompletaInJSON(JSONArray listaBraniCompletaInJSON) {
        this.listaBraniCompletaInJSON = listaBraniCompletaInJSON;
    } */

    /* public List<DettaglioBrano> getListaBraniFiltrata() {
        return ListaBraniFiltrata;
    }

    public void setListaBraniFiltrata(List<DettaglioBrano> listaBraniFiltrata) {
        this.ListaBraniFiltrata = listaBraniFiltrata;
    } */

    public boolean isRicercaTesto() {
        return RicercaTesto;
    }

    public void setRicercaTesto(boolean ricercaTesto) {
        RicercaTesto = ricercaTesto;
    }

    public boolean isRicercaEsclusione() {
        return RicercaEsclusione;
    }

    public void setRicercaEsclusione(boolean ricercaEsclusione) {
        RicercaEsclusione = ricercaEsclusione;
    }

    public boolean isRicercaStelle() {
        return RicercaStelle;
    }

    public void setRicercaStelle(boolean ricercaStelle) {
        RicercaStelle = ricercaStelle;
    }

    public boolean isRicercaMaiAscoltata() {
        return RicercaMaiAscoltata;
    }

    public void setRicercaMaiAscoltata(boolean ricercaMaiAscoltata) {
        RicercaMaiAscoltata = ricercaMaiAscoltata;
    }

    public boolean isRicercaTags() {
        return RicercaTags;
    }

    public void setRicercaTags(boolean ricercaTags) {
        RicercaTags = ricercaTags;
    }

    public String getTestoDaRicercare() {
        return TestoDaRicercare;
    }

    public void setTestoDaRicercare(String testoDaRicercare) {
        TestoDaRicercare = testoDaRicercare;
    }

    public String getTestoDaEscludere() {
        return TestoDaEscludere;
    }

    public void setTestoDaEscludere(String testoDaEscludere) {
        TestoDaEscludere = testoDaEscludere;
    }

    public Integer getStelleDaRicercare() {
        return StelleDaRicercare;
    }

    public void setStelleDaRicercare(Integer stelleDaRicercare) {
        StelleDaRicercare = stelleDaRicercare;
    }

    public String getTagsDaRicercare() {
        return TagsDaRicercare;
    }

    public void setTagsDaRicercare(String tagsDaRicercare) {
        TagsDaRicercare = tagsDaRicercare;
    }

    public StrutturaBrano getStrutturaDelBrano() {
        return StrutturaDelBrano;
    }

    public void setStrutturaDelBrano(StrutturaBrano strutturaDelBrano) {
        StrutturaDelBrano = strutturaDelBrano;
    }

    public boolean isRetePresente() {
        return RetePresente;
    }

    public void setRetePresente(boolean retePresente) {
        RetePresente = retePresente;
    }

    public boolean isStaSuonando() {
        return StaSuonando;
    }

    public void setStaSuonando(boolean staSuonando) {
        StaSuonando = staSuonando;
    }

    public boolean isScaricaBrano() {
        return ScaricaBrano;
    }

    public void setScaricaBrano(boolean scaricaBrano) {
        ScaricaBrano = scaricaBrano;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public String getImmagineAttuale() {
        return ImmagineAttuale;
    }

    public void setImmagineAttuale(String immagineAttuale) {
        ImmagineAttuale = immagineAttuale;
    }

    public int getDurataBranoInSecondi() {
        return DurataBranoInSecondi;
    }

    public void setDurataBranoInSecondi(int durataBranoInSecondi) {
        DurataBranoInSecondi = durataBranoInSecondi;
    }

    public List<StrutturaArtisti> getArtisti() {
        return this.Artisti;
    }

    public void AggiungeBranoAdAscoltati(int idBrano) {
        ListaBraniAscoltati.add(idBrano);
    }

    public void PulisceArtisti() {
        Artisti = new ArrayList<>();
    }

    public void AggiungeArtista(StrutturaArtisti Artista) {
        Artisti.add(Artista);
    }

    public Integer getBraniTotali() {
        return braniTotali;
    }

    public void setBraniTotali(Integer braniTotali) {
        this.braniTotali = braniTotali;
    }

    /* public List<DettaglioBrano> getListaBraniCompleta() {
        return ListaBraniCompleta;
    }

    public void setListaBraniCompleta(List<DettaglioBrano> listaBraniCompleta) {
        ListaBraniCompleta = listaBraniCompleta;
    } */
}