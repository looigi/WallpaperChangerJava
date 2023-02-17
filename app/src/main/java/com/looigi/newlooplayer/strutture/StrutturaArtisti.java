package com.looigi.newlooplayer.strutture;

import java.util.List;

public class StrutturaArtisti {
    private String NomeArtista;
    private String Immagine;
    private List<String> Tags;

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> tags) {
        Tags = tags;
    }

    public String getNomeArtista() {
        return NomeArtista;
    }

    public void setNomeArtista(String nomeArtista) {
        NomeArtista = nomeArtista;
    }

    public String getImmagine() {
        return Immagine;
    }

    public void setImmagine(String immagine) {
        Immagine = immagine;
    }
}
