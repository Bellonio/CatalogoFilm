package com.bellone.catalogofilm;

import java.util.ArrayList;

public class Film {

    private String titolo, imgCopertina_path, trailer_path, trama, casa_di_produzione;
    private Regista regista;
    private int durata, anno_di_uscita;
    private ArrayList<String> lingue, generi, tag;
    private ArrayList<Attore> attori;
    private ArrayList<Recensione> recensioni;

    public Film(String titolo, int durata, int anno_di_uscita, ArrayList<String> generi, ArrayList<String> lingue,
                    Regista regista, String casa_di_produzione, ArrayList<String> tag, String trama,
                    String imgCopertina_path, String trailer_path, ArrayList<Attore> attori
                    , ArrayList<Recensione> recensioni) {

        this.titolo = titolo;
        this.regista = regista;
        this.imgCopertina_path = imgCopertina_path;
        this.trailer_path = trailer_path;
        this.trama = trama;
        this.casa_di_produzione = casa_di_produzione;
        this.durata = durata;
        this.anno_di_uscita = anno_di_uscita;
        this.lingue = lingue;
        this.generi = generi;
        this.tag = tag;
        this.attori = attori;
        this.recensioni = recensioni;
    }

        //costruttore senza ancora gli attori e le recensioni e i percorsi (image e trailer)
    public Film(String titolo, int durata, int anno_di_uscita, ArrayList<String> generi, ArrayList<String> lingue,
                Regista regista, String casa_di_produzione, ArrayList<String> tag, String trama) {

        this.titolo = titolo;
        this.regista = regista;
        this.imgCopertina_path = null;
        this.trailer_path = null;
        this.trama = trama;
        this.casa_di_produzione = casa_di_produzione;
        this.durata = durata;
        this.anno_di_uscita = anno_di_uscita;
        this.lingue = lingue;
        this.generi = generi;
        this.tag = tag;
        this.attori = null;
        this.recensioni = null;
    }

    public String getTitolo() { return titolo; }
    public Regista getRegista() { return regista;}
    public String getImgCopertina_path() { return imgCopertina_path; }
    public String getTrailer_path() { return trailer_path; }
    public String getTrama() { return trama; }
    public String getCasa_di_produzione() { return casa_di_produzione; }
    public int getDurata() { return durata; }
    public int getAnno_di_uscita() { return anno_di_uscita; }
    public ArrayList<String> getLingue() { return lingue; }
    public ArrayList<String> getGeneri() { return generi; }
    public ArrayList<String> getTag() { return tag; }
    public ArrayList<Attore> getAttori() { return attori; }
    public ArrayList<Recensione> getRecensioni() { return recensioni; }



}
