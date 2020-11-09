package com.bellone.catalogofilm;

import java.io.Serializable;

/*
* La classe implementa l'interfaccia Serializable cosi' da rendere le classi figlie
*  (Regista e Attore) anche serializzabili, questo per ora mi serve per poter inviare
*  all'activity Dettagli anche l'oggetto Regista
*/
public class Persona implements Serializable {

    private final String nome;
    private final String cognome;
    private final String nazionalita;
    private final int anno_nascita;

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
