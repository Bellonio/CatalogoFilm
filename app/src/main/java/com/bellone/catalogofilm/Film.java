package com.bellone.catalogofilm;

import com.android.volley.RequestQueue;

import java.io.Serializable;
import java.util.ArrayList;

/*Ora che l'immagine la assegno all'imageView utilizzando il semplice URL e la classe Picasso non
     ho piu' l'oggetto Bitmap e percio' non ho il problema del Serializable che da errore. */
public class Film implements Serializable {
    private final String titolo;
    private final String trailer_path;
    private final String trama;
    private final String casa_di_produzione;
    private final Regista regista;
    private int durata, anno_di_uscita;
    private final ArrayList<String> lingue;
    private ArrayList<String> generi;
    private final ArrayList<String> tag;
    private ArrayList<Attore> attori;
    private ArrayList<Recensione> recensioni;
    private String url_locandina = null;


    public Film(RequestQueue queue, String imgUrl, String titolo, int durata, int anno_di_uscita, ArrayList<String> generi, ArrayList<String> lingue,
                    Regista regista, String casa_di_produzione, ArrayList<String> tag, String trama, String trailer_path, ArrayList<Attore> attori
                    , ArrayList<Recensione> recensioni) {

        this.url_locandina = imgUrl;
        this.titolo = titolo;
        this.regista = regista;
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

        /*Costruttore senza ancora gli attori, le recensioni e il percorso del trailer)*/
    public Film(RequestQueue queue, String imgUrl, String titolo, int durata, int anno_di_uscita,
                ArrayList<String> generi, ArrayList<String> lingue,
                Regista regista, String casa_di_produzione, ArrayList<String> tag, String trama) {

        this.url_locandina = imgUrl;
        this.titolo = titolo;
        this.regista = regista;
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
    public String getUrl_locandina() { return url_locandina; }
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
