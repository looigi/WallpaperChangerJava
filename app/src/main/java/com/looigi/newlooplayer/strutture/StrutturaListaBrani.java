package com.looigi.newlooplayer.strutture;

public class StrutturaListaBrani {
    private String artista;
    private String album;
    private String traccia;
    private String brano;
    private String id;
    private String anno;

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTraccia() {
        return traccia;
    }

    public void setTraccia(String traccia) {
        this.traccia = traccia;
    }

    public String getBrano() {
        return brano;
    }

    public void setBrano(String brano) {
        this.brano = brano;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
