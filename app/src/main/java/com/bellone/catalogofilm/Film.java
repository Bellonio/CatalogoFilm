package com.bellone.catalogofilm;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

/*Non ho potuto usare implementare nella classe Film l'interfaccia Serializable, e ho invece
   implementato Parcelable xke' la serializzazione dava errore al tentativo di serializzare l'attributo
   "locandina" (tipo Bitmap) ("java.io.NotSerializableException: android.graphics.Bitmap").
  Ma meglio cosi', in questo modo ho esluso alcuni attributi della classe che non mi interessava
   dare alla activity Dettagi.
*/
public class Film implements Parcelable {
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

        /*Costruttore senza ancora gli attori, le recensioni e il percorso del trailer)*/
    public Film(RequestQueue queue, String imgUrl, String titolo, int durata, int anno_di_uscita,
                ArrayList<String> generi, ArrayList<String> lingue,
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

    //Costruttore per rendere Parcelable la classe Film
    protected Film(Parcel in) {
        /*Questi saranno gli unici attributi che passo all'activity Dettagli, i
            restanti saranno = null */

        titolo = in.readString();
        regista = (Regista) in.readSerializable(); //Il Regista e' figlio di Persona, classe Serializzabile
        trailer_path = in.readString();
        trama = in.readString();
        casa_di_produzione = in.readString();
        lingue = in.createStringArrayList();
        tag = in.createStringArrayList();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(titolo);
        parcel.writeSerializable(regista);
        parcel.writeString(trailer_path);
        parcel.writeString(trama);
        parcel.writeString(casa_di_produzione);
        parcel.writeStringList(lingue);
        parcel.writeStringList(tag);
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
     * Metodo per leggere dall'url dato l'immagine della copertina del film. Nell"onResponse"
     * mi viene fornito il semplice oggetto Bitmap. Come maxWidth e maxHeight ho impostato 0 cosi'
     * che prenda i px in automatico. Bitmap.Config.ALPHA_8 e' una codifica dell'immagine un po'
     * piu' leggera se ho capito bene (" https://developer.android.com/reference/android/graphics/Bitmap.Config?authuser=1 "
     * qui ci sono tutte le costanti con la documentazione su come codificano l'immagine).
     * @param queue         la coda delle richieste, uso sempre la stessa in modo da creare veramente una "coda"
     *                          di richieste unica
     * @param imgUrl        URL da cui fare l'ImageRequest
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
