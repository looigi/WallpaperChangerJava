package com.looigi.newlooplayer.strutture;

public class StrutturaListaPreferiti {
    private String NomeLista;
    private String Preferiti;
    private String PreferitiElimina;
    private String Tags;
    private String TagsElimina;

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getTagsElimina() {
        return TagsElimina;
    }

    public void setTagsElimina(String tagsElimina) {
        TagsElimina = tagsElimina;
    }

    public String getNomeLista() {
        return NomeLista;
    }

    public void setNomeLista(String nomeLista) {
        NomeLista = nomeLista;
    }

    public String getPreferiti() {
        return Preferiti;
    }

    public void setPreferiti(String preferiti) {
        Preferiti = preferiti;
    }

    public String getPreferitiElimina() {
        return PreferitiElimina;
    }

    public void setPreferitiElimina(String preferitiElimina) {
        PreferitiElimina = preferitiElimina;
    }
}
