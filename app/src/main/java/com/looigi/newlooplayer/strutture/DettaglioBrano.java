package com.looigi.newlooplayer.strutture;

public class DettaglioBrano {
    private int idBrano;
    private String Titolo;
    private String Tags;
    private int Ascoltata;
    private int Stelle;
    private String Artista;
    private String Album;
    private String Anno;
    private int Traccia;
    private String urlBrano;
    private boolean presenteSuDisco;
    private String Estensione;

    public String getEstensione() {
        return Estensione;
    }

    public void setEstensione(String estensione) {
        Estensione = estensione;
    }

    public String getUrlBrano() {
        return urlBrano;
    }

    public void setUrlBrano(String urlBrano) {
        this.urlBrano = urlBrano;
    }

    public boolean isPresenteSuDisco() {
        return presenteSuDisco;
    }

    public void setPresenteSuDisco(boolean presenteSuDisco) {
        this.presenteSuDisco = presenteSuDisco;
    }

    public int getIdBrano() {
        return idBrano;
    }

    public void setIdBrano(int idBrano) {
        this.idBrano = idBrano;
    }

    public String getTitolo() {
        return Titolo;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public int getAscoltata() {
        return Ascoltata;
    }

    public void setAscoltata(int ascoltata) {
        Ascoltata = ascoltata;
    }

    public int getStelle() {
        return Stelle;
    }

    public void setStelle(int stelle) {
        Stelle = stelle;
    }

    public String getArtista() {
        return Artista;
    }

    public void setArtista(String artista) {
        Artista = artista;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public String getAnno() {
        return Anno;
    }

    public void setAnno(String anno) {
        Anno = anno;
    }

    public int getTraccia() {
        return Traccia;
    }

    public void setTraccia(int traccia) {
        Traccia = traccia;
    }
}
