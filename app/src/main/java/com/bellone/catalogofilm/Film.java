package com.bellone.catalogofilm;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

public class Film {
    private String titolo, trailer_path, trama, casa_di_produzione;
    private Regista regista;
    private int durata, anno_di_uscita;
    private ArrayList<String> lingue, generi, tag;
    private ArrayList<Attore> attori;
    private ArrayList<Recensione> recensioni;
        //L'immagine e' letta dal sito https://www.imdb.com e le dimensioni sono 182px (width) 268px (height)
    private Bitmap locandina = null;


    public Film(RequestQueue queue, String imgUrl, String titolo, int durata, int anno_di_uscita, ArrayList<String> generi, ArrayList<String> lingue,
                    Regista regista, String casa_di_produzione, ArrayList<String> tag, String trama, String trailer_path, ArrayList<Attore> attori
                    , ArrayList<Recensione> recensioni) {

        leggiLocandinaFilm(queue, imgUrl);
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

        /*costruttore senza ancora gli attori e le recensioni e i percorsi (della copertina e del trailer)*/
    public Film(RequestQueue queue, String imgUrl, String titolo, int durata, int anno_di_uscita, ArrayList<String> generi, ArrayList<String> lingue,
                Regista regista, String casa_di_produzione, ArrayList<String> tag, String trama) {

        leggiLocandinaFilm(queue, imgUrl);
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
    public Bitmap getLocandina() { return locandina; }
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


    /**
     * Metodo per leggere dall'url dato l'immagine della copertina del film. Nell"onResponde"
     * mi viene gia' fornito l'oggetto Bitmap. Come maxWidth e maxHeight ho impostato 0 cosi'
     * che prenda i px in automatico. Bitmap.Config.ALPHA_8 e' una codifica dell'immagine un po'
     * piu' leggera se ho capito bene (each pixel requires 1 byte of memory).
     * @param queue
     * @param imgUrl
     */
    private void leggiLocandinaFilm(RequestQueue queue, String imgUrl){
        ImageRequest imgRequest = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                locandina = response;
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                locandina = null;
                error.printStackTrace();
            }
        });

        queue.add(imgRequest);
    }

}
