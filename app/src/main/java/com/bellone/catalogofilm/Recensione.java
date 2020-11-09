package com.bellone.catalogofilm;

public class Recensione {

    private final String username;
    private final String testo;
    private final int voto;

    public Recensione(String username, String testo, int voto) {
        this.username = username;
        this.testo = testo;
        this.voto = voto;
    }

    public String getUsername() { return username; }
    public String getTesto() { return testo; }
    public int getVoto() { return voto; }
}
