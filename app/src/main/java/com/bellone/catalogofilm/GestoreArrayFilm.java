package com.bellone.catalogofilm;

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
            "durataDESC"                //[4]
    };

    private ArrayList<Film> default_films;

        /*Questo sara' una copia dell'array dei film ma ordinato in un certo modo. Questo l'ho fatto per tenermi
            sempre l'ordinamento originale (di lettura) dei film, pero' so che in caso ci fossero migliaia di Film
            una copia significherebbe avere dati rindondanti...*/
    private ArrayList<Film> ordinated_films = null;

    public GestoreArrayFilm() {
        default_films = new ArrayList<>();
    }

    public ArrayList<Film> getDefaultFilms(){ return default_films; }
    public ArrayList<Film> getOrdinatedFilms(){ return ordinated_films; }
    public int getQtaFilms(){ return default_films.size(); }


    public void addFilm(Film film){
        if(film != null){
            default_films.add(film);
        }
    }

    /**
     * Questo metodo va semplicemente a richiamare il metodo che aggiunge il film alla lista, creando sul momento
     *  l'oggetto Film con i parametri passati.
     * @param titolo
     * @param durata
     * @param anno_di_uscita
     * @param generi
     * @param lingue
     * @param regista
     * @param casa_di_produzione
     * @param tag
     * @param trama
     */
    public void addFilm(String titolo, int durata, int anno_di_uscita, ArrayList<String> generi, ArrayList<String> lingue,
                            Regista regista, String casa_di_produzione, ArrayList<String> tag, String trama){

        addFilm(new Film(titolo, durata, anno_di_uscita, generi, lingue, regista, casa_di_produzione, tag, trama));
    }


    public boolean removeFilm(Film film){
        return removeFilm(film.getTitolo(), film.getAnno_di_uscita(),
                (film.getRegista().getNome()+film.getRegista().getCognome()));
    }
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
     * stessa specifica, per questo il metodo ritorna una lista di posizioni.
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
         controllera' che il dato scritto sia contenuto nel campo corrispondente del film.

          AD ESEMPIO: se i campi dati in input sono: titolo= "transformers", anno_di_uscita = 0, nomeCognome_regista = ""
            il metodo trovera' i film: "Transformers 1", "Transformer - La vendetta del caduto", "Transformer 3"
                , "Transformer ..."

          Oppure nel secondo caso:

          AD ESEMPIO: se i campi dati in input sono: titolo= "transformers", anno_di_uscita = 2011, nomeCognome_regista = ""
            il metodo trovera' soltanto film: "Transformer 3"
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
     * campi dati e ritorna una lista.
     * @param titolo
     * @param anno_di_uscita
     * @param nomeCognome_regista
     * @return array di Film, in caso trovi almeno un Film, altrimenti null
     */
    public ArrayList<Film> searchFilm(String titolo, int anno_di_uscita, String nomeCognome_regista){
        ArrayList<Integer> filmPositions = findFilmPosition(titolo, anno_di_uscita, nomeCognome_regista);

        if(filmPositions.size() == 0){
            return null;
        }else{
            ArrayList<Film> film_trovati = new ArrayList<>();
            for(int pos: filmPositions){
                film_trovati.add(default_films.get(pos));
            }
            return film_trovati;
        }

    }

    /**
     * Metodo che utilizza il metodo privato di questa classe per avere le posizioni dei Film con i
     * campi dati e ritorna una lista.
     * @param titolo
     * @param anno_di_uscita
     * @param nomeCognome_regista
     * @param parametroInutile          parametro che mi permette di utilizzare il polimorfismo dei metodi
     *                                      differenziando questo metodo dal precedente
     * @return array di posizioni, percio' ritorna semplicemente il valore di ritorno del metodo che richiama
     */
    public ArrayList<Integer> searchFilm(String titolo, int anno_di_uscita, String nomeCognome_regista, int parametroInutile){
        return findFilmPosition(titolo, anno_di_uscita, nomeCognome_regista);
    }


    public void modifyFilm(/*bho magari la posizione del film modificato e l'oggetto Film con i valori modificati*/){
        /*
        ANCORA DA FARE, MA POTREI CAMBIARE TUTTE LE TEXT VIEW DEL LAYOUT FILM IN EDIT TEXT,
         MA DISABILITANDOLE (SARANNO VISTE E UTILIZZABILI COME SEMPLICI TEXT VIEW) con .setEnable(false).
         POI INSERIREI UN ASCOLTATORE DELL'ON CLICK (oppure long click) SU OGNUNA DI ESSE CHE VADI A FARE
          .setEnable(true) O, SE SONO GIA' ABILITATE, .setEnable(false). POI PRENDEREI IL VALORE
           MODIFICATO E LO ASSEGNEREI ALL'ATTRIBUTO CORRETTO DELL'OGGETTO FILM IN QUESTIONE.

        NON SO ANCORA COME POTREI SALVARE LA MODIFICA ANCHE SUL FILE...
        */
    }


    public void orderFilmBy(final String orderBy){
        ordinated_films = new ArrayList<>();
        for(Film film: default_films){ ordinated_films.add(film); }

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

                }

                if(flagDatiUguali){
                    if(f1.getTitolo().compareTo(f2.getTitolo()) > 0){
                        return 1;
                    }else if(f1.getTitolo().compareTo(f2.getTitolo()) < 0){
                        return -1;
                    }else{
                        return 0;
                    }
                }else{ //QUI CI ENTRA SOLO SE NON E' ENTRATO IN UNO DEGLI IF PRECEDENTI
                    return 0;
                }
            }
        });
    }
}
