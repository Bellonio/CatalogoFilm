package com.bellone.catalogofilm;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GestoreArrayFilm {

        /*Valori possibili per l'ordinamento*/
    public static final String[] ORDER_VALUES = new String[]{
            "ORDINAMENTO DI DEFAULT:",  //[0]
            "anno_di_uscitaASC",        //[1]
            "anno_di_uscitaDESC",       //[2]
            "durataASC",                //[3]
            "durataDESC",               //[4]
            "alfabeticamente A-Z",      //[5]
            "alfabeticamente Z-A"       //[6]
    };

    private final ArrayList<Film> default_films;
    private ArrayList<Film> ordinated_films = null;
    private ArrayList<Film> arrayListFilmRicerca;

    public GestoreArrayFilm() {
        default_films = new ArrayList<>();
    }

    public ArrayList<Film> getDefaultFilms(){ return default_films; }
    public ArrayList<Film> getOrdinatedFilms(){ return ordinated_films; }
    public ArrayList<Film> getArrayListFilmRicerca(){ return arrayListFilmRicerca; }

    public int getQtaFilms(){ return default_films.size(); }


    public void cancelOrdinatedFilms(){ ordinated_films = null; }
    public void cancelArrayListRicerca(){ arrayListFilmRicerca  = null; }


    public void addFilm(Film film){
        if(film != null){
            default_films.add(film);
        }
    }

    /**
     * Questo metodo va semplicemente a richiamare il metodo che aggiunge il film alla lista, creando sul momento
     *  l'oggetto Film con i parametri passati.
     */
    public void addFilm(RequestQueue queue, String imgUrl, String titolo, int durata, int anno_di_uscita, ArrayList<String> generi, ArrayList<String> lingue,
                        Regista regista, String casa_di_produzione, ArrayList<String> tag, String trama){

        addFilm(new Film(queue, imgUrl, titolo, durata, anno_di_uscita, generi, lingue, regista, casa_di_produzione, tag, trama));
    }


    public boolean removeFilm(Film film){
        return removeFilm(film.getTitolo(), film.getAnno_di_uscita(),
                (film.getRegista().getNome()+film.getRegista().getCognome()));
    }
    /**
     * Metodo utilizzando il metodo privato di questa classe "findFilmPosition" trova i film con i specifichi
     * valori dati come parametri, e IN CASO NE TROVI SOLTANTO UNO, elimina dalla lista di default
     * (principale) il film in questione
     * @return          true se l'ha eliminato, false in caso contrario
     */
    public boolean removeFilm(String titolo, int anno_di_uscita, String nomeCognome_regista){
        ArrayList<Integer> filmPositions = findFilmPosition(titolo, anno_di_uscita, nomeCognome_regista);

        if(filmPositions.size() == 1) {
            default_films.remove(default_films.get(filmPositions.get(0)));
            return true;
        }else{
            return false;
        }
    }


    /**
     * Con questo metodo vengono ritornati le posizioni di tutti i film con i campi dati.
     * I film possono essere cercati anche senza specificare ogni campo, trovando quindi film con la
     * stessa specifica, per questo il metodo ritorna una lista di posizioni e non una sola posizione.
     * @param titolo                se == "" il film non verra' cercato per il titolo
     * @param anno_di_uscita        se == 0    il film non verra' cercato per l'anno di uscita
     * @param nomeCognome_regista   se == "" il film non verra' cercato per i dati del registra
     * @return array con le posizioni di tutti i film trovati.
     */
    private ArrayList<Integer> findFilmPosition(String titolo, int anno_di_uscita, String nomeCognome_regista){

        /*
        Controlla se e' stato dato un solo campo, in caso contrario per aggiungere la posizione del film
         dovrebbe controllare che i campi del film in posizione "i" corrispondano ai campi dati.
        Inoltre, dato che e' difficile che l'utente sappia ESATTAMENTE il titolo o i dati del regista,
         controllera' che il dato scritto sia CONTENUTO nel campo corrispondente del film (non fara'
          .equals() ma .contains()).

          AD ESEMPIO: se i campi dati in input sono: titolo= "transformers", anno_di_uscita = 0, nomeCognome_regista = ""
            il metodo trovera' i film: "Transformers 1", "Transformer - La vendetta del caduto", "Transformer 3"
                , "Transformer ..."

          Oppure nel secondo caso:

          AD ESEMPIO: se i campi dati in input sono: titolo= "transformers", anno_di_uscita = 2011, nomeCognome_regista = ""
            il metodo trovera' soltanto film: "Transformer 3" (del 2011)
        */

        ArrayList<Integer> film_indexs = new ArrayList<>();

        int i=0;
        while(i<getQtaFilms()){
            if( !titolo.equals("") && anno_di_uscita == 0 && nomeCognome_regista.equals("")
                    || titolo.equals("") && anno_di_uscita != 0 && nomeCognome_regista.equals("")
                    || titolo.equals("") && anno_di_uscita == 0 && !nomeCognome_regista.equals("")){

                if(!titolo.equals("") && default_films.get(i).getTitolo().toLowerCase().contains(titolo.toLowerCase())) {
                    film_indexs.add(i);
                }
                if(default_films.get(i).getAnno_di_uscita() == anno_di_uscita){
                    film_indexs.add(i);
                }
                if(!nomeCognome_regista.equals("") && ( (default_films.get(i).getRegista().getNome()+default_films.get(i).getRegista().getCognome())
                        .equalsIgnoreCase(nomeCognome_regista)
                        || (default_films.get(i).getRegista().getNome().toLowerCase().contains(nomeCognome_regista.toLowerCase())
                            || default_films.get(i).getRegista().getCognome().toLowerCase().contains(nomeCognome_regista.toLowerCase())) )){

                    film_indexs.add(i);
                }
            }else{
                if(!titolo.equals("") && !nomeCognome_regista.equals("") && default_films.get(i).getTitolo().toLowerCase().contains(titolo.toLowerCase())
                        && ( (default_films.get(i).getRegista().getNome()+default_films.get(i).getRegista().getCognome())
                            .equalsIgnoreCase(nomeCognome_regista) )
                            || (nomeCognome_regista.toLowerCase().contains((default_films.get(i).getRegista().getNome()).toLowerCase())
                            || nomeCognome_regista.toLowerCase().contains((default_films.get(i).getRegista().getCognome()).toLowerCase())) ){

                    film_indexs.add(i);
                }else if(!titolo.equals("") && default_films.get(i).getTitolo().toLowerCase().contains(titolo.toLowerCase())
                        && default_films.get(i).getAnno_di_uscita() == anno_di_uscita ){

                    film_indexs.add(i);
                }
            }

            i++;
        }
        return film_indexs;
    }


    /**
     * Metodo che utilizza il metodo privato di questa classe per avere le posizioni dei Film con i
     * campi dati e modifica la lista dei film ricercati.
     */
    public void searchFilm(String titolo, int anno_di_uscita, String nomeCognome_regista){
        ArrayList<Integer> filmPositions = findFilmPosition(titolo, anno_di_uscita, nomeCognome_regista);

        if(filmPositions.size() == 0){
            arrayListFilmRicerca =  null;
        }else{
            if(arrayListFilmRicerca == null || arrayListFilmRicerca.size() > 0){ arrayListFilmRicerca = new ArrayList<>(); }
            for(int pos: filmPositions){
                arrayListFilmRicerca.add(default_films.get(pos));
            }
        }
    }

    /*public void modifyFilm(bho magari la posizione del film modificato e l'oggetto Film con i valori modificati){
        ANCORA DA FARE, MA POTREI CAMBIARE TUTTE LE TEXT VIEW DEL LAYOUT FILM IN EDIT TEXT,
         MA DISABILITANDOLE (SARANNO VISTE E UTILIZZABILI COME SEMPLICI TEXT VIEW) con .setEnable(false).
         POI INSERIREI UN ASCOLTATORE DELL'ON CLICK (oppure long click) SU OGNUNA DI ESSE CHE VADI A FARE
          .setEnable(true) O, SE SONO GIA' ABILITATE, .setEnable(false). POI PRENDEREI IL VALORE
           MODIFICATO E LO ASSEGNEREI ALL'ATTRIBUTO CORRETTO DELL'OGGETTO FILM IN QUESTIONE.

        NON SO ANCORA COME POTREI SALVARE LA MODIFICA ANCHE SUL FILE...
    }*/


    /**
     * Metodo che copia la lista defaultFilm in ordinated_films e ordina quest'ultima secondo
     * cio' che ha scelto l'utente
     */
    public void orderFilmBy(final String orderBy){
        ordinated_films = new ArrayList<>();

        if(arrayListFilmRicerca == null){
            ordinated_films.addAll(default_films);
        }else{
            ordinated_films.addAll(arrayListFilmRicerca);
        }


        Collections.sort(ordinated_films, new Comparator<Film>() {
            @Override
            public int compare(Film f1, Film f2) {
                boolean flagDatiUguali = false;

                if(orderBy.equals(ORDER_VALUES[1])){        //anno_di_uscitaASC
                    if(f1.getAnno_di_uscita() > f2.getAnno_di_uscita()){
                        return 1;
                    }else if(f1.getAnno_di_uscita() < f2.getAnno_di_uscita()){
                        return -1;
                    }else{ flagDatiUguali = true; }

                }else if(orderBy.equals(ORDER_VALUES[2])){      //anno_di_uscitaDESC
                    if(f1.getAnno_di_uscita() < f2.getAnno_di_uscita()){
                        return 1;
                    }else if(f1.getAnno_di_uscita() > f2.getAnno_di_uscita()){
                        return -1;
                    }else{ flagDatiUguali = true; }

                }else if(orderBy.equals(ORDER_VALUES[3])){      //durataASC
                    if(f1.getDurata() > f2.getDurata()){
                        return 1;
                    }else if(f1.getDurata() < f2.getDurata()){
                        return -1;
                    }else{ flagDatiUguali = true; }

                }else if(orderBy.equals(ORDER_VALUES[4])){      //durataDESC
                    if(f1.getDurata() < f2.getDurata()){
                        return 1;
                    }else if(f1.getDurata() > f2.getDurata()){
                        return -1;
                    }else{ flagDatiUguali = true; }

                }else if(orderBy.equals(ORDER_VALUES[5])){      //alfabeticamente A-Z
                    return ordineAlfabetico(f1, f2, true);
                }else if(orderBy.equals(ORDER_VALUES[6])){      //alfabeticamente Z-A
                    return ordineAlfabetico(f1, f2, false);
                }

                //In caso di dati uguali ordina per ordine alfabetico per il titolo
                if(flagDatiUguali){
                    return ordineAlfabetico(f1, f2, true);
                }else{ //QUI CI ENTRA SOLO SE NON E' ENTRATO IN UNO DEGLI IF PRECEDENTI
                    return 0;
                }
            }
        });
    }

    /**
     * Metodo per il confronto di due titoli di film. E' utilizzato nel metodo onCompare di sopra
     * quindi deve ritornare -1 per posizionare il primo film sopra il secondo, 1 per il contrario
     * o 0 per lasciarli nella stessa posizione.
     * Il metodo e' utilizzato per ordinare tra loro due titoli in ordine alfabetico, ma puo' essere
     * ordinamento dalla A alla Z o al contrario, percio' nel secondo caso vado semplicemente ad
     * invertire il ritorno, cosi' da invertire l'ordinamento.
     */
    private int ordineAlfabetico(Film f1, Film f2, boolean A_Z){
        int ritornoPositivo = A_Z ? 1 : -1;

        if(f1.getTitolo().compareTo(f2.getTitolo()) > 0){
            return ritornoPositivo;
        }else if(f1.getTitolo().compareTo(f2.getTitolo()) < 0){
            return -ritornoPositivo;
        }else{
            return 0;
        }
    }
}
