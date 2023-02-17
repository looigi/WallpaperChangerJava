package com.looigi.newlooplayer.strutture;

public class StrutturaBranoDB {
    private String id;
    private String NomeBrano;
    private String Bellezza;
    private String Ascoltata;
    private String Testo;
    private String TestoTradotto;
    private int dimensione;

    public int getDimensione() {
        return dimensione;
    }

    public void setDimensione(int dimensione) {
        this.dimensione = dimensione;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeBrano() {
        return NomeBrano;
    }

    public void setNomeBrano(String nomeBrano) {
        NomeBrano = nomeBrano;
    }

    public String getBellezza() {
        return Bellezza;
    }

    public void setBellezza(String bellezza) {
        Bellezza = bellezza;
    }

    public String getAscoltata() {
        return Ascoltata;
    }

    public void setAscoltata(String ascoltata) {
        Ascoltata = ascoltata;
    }

    public String getTesto() {
        return Testo;
    }

    public void setTesto(String testo) {
        Testo = testo;
    }

    public String getTestoTradotto() {
        return TestoTradotto;
    }

    public void setTestoTradotto(String testoTradotto) {
        TestoTradotto = testoTradotto;
    }
}
