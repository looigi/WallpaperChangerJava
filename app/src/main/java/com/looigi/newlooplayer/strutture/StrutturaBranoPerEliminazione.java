package com.looigi.newlooplayer.strutture;

import java.util.Date;
import java.util.List;

public class StrutturaBranoPerEliminazione {
    private String NomeFile;
    private Integer Dimensione;
    private Date DataFile;
    private String Artista;
    private String Album;
    private String Brano;

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

    public String getBrano() {
        return Brano;
    }

    public void setBrano(String brano) {
        Brano = brano;
    }

    public String getNomeFile() {
        return NomeFile;
    }

    public void setNomeFile(String nomeFile) {
        NomeFile = nomeFile;
    }

    public Integer getDimensione() {
        return Dimensione;
    }

    public void setDimensione(Integer dimensione) {
        Dimensione = dimensione;
    }

    public Date getDataFile() {
        return DataFile;
    }

    public void setDataFile(Date dataFile) {
        DataFile = dataFile;
    }
}
