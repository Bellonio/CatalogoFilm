package com.bellone.catalogofilm;

public class Persona {

    private String nome;
    private String cognome;
    private String nazionalita;
    private int anno_nascita;

    public Persona(String nome, String cognome, String nazionalita, int anno_nascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.nazionalita = nazionalita;
        this.anno_nascita = anno_nascita;
    }

    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getNazionalita() { return nazionalita; }
    public int getAnno_nascita() { return anno_nascita; }
}
