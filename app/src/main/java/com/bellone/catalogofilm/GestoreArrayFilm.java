package com.bellone.catalogofilm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GestoreArrayFilm {
    public static final String[] ORDER_VALUES = new String[]{
            "ORDINAMENTO DI DEFAULT:",  //[0]
            "anno_di_uscitaASC",        //[1]
            "anno_di_uscitaDESC",       //[2]
            "durataASC",                //[3]
            "durataDESC"                //[4]
    };

    private ArrayList<Film> films = null;
        //NON SO QUANTO SIA GIUSTO AVERE UNA COPIA DI UN ARRAY SEMPRE A DISPOSIZIONE, ORA CI SONO POCHI ELEMENTI MA IN
        // CASO CE NE FOSSERO MIGLIAIA IO METTO SEMPRE IN RAM UNA COPIA DI QUELLI...
    private ArrayList<Film> ordinated_films = null;

    public GestoreArrayFilm() {
        this.films = new ArrayList<>();
    }

    public ArrayList<Film> getDefaultFilms(){ return films; }
    public ArrayList<Film> getOrdinatedFilms(){ return ordinated_films; }
    public int getQtaFilms(){ return films.size(); }



    public void addFilm(Film film){
        if(film != null){
            films.add(film);
        }
    }
    public void addFilm(String titolo, int durata, int anno_di_uscita, ArrayList<String> generi, ArrayList<String> lingue,
                            Regista regista, String casa_di_produzione, ArrayList<String> tag, String trama){

        this.addFilm(new Film(titolo, durata, anno_di_uscita, generi, lingue, regista, casa_di_produzione, tag, trama));
    }


    public boolean removeFilm(Film film){
        return removeFilm(film.getTitolo(), film.getAnno_di_uscita(),
                (film.getRegista().getNome()+film.getRegista().getCognome()));
    }
    public boolean removeFilm(String titolo, int anno_di_uscita, String nomeCognome_regista){
        ArrayList<Integer> filmPositions = this.findFilmPosition(titolo, anno_di_uscita, nomeCognome_regista);

        if(filmPositions.size() == 1) {
            films.remove(films.get(filmPositions.get(0)));
            return true;
        }else{
            return false;
        }
    }


    private ArrayList<Integer> findFilmPosition(String titolo, int anno_di_uscita, String nomeCognome_regista){
        ArrayList<Integer> film_indexs = new ArrayList<>();

        int i=0;
        while(i<this.getQtaFilms()){
            if( !titolo.equals("") && anno_di_uscita == 0 && nomeCognome_regista.equals("")
                    || titolo.equals("") && anno_di_uscita != 0 && nomeCognome_regista.equals("")
                    || titolo.equals("") && anno_di_uscita == 0 && !nomeCognome_regista.equals("")){

                if(!titolo.equals("") && films.get(i).getTitolo().toLowerCase().contains(titolo.toLowerCase())) {
                    film_indexs.add(i);
                }
                if(films.get(i).getAnno_di_uscita() == anno_di_uscita){
                    film_indexs.add(i);
                }
                if(!nomeCognome_regista.equals("") && ( (films.get(i).getRegista().getNome()+films.get(i).getRegista().getCognome())
                        .equalsIgnoreCase(nomeCognome_regista)
                        || (films.get(i).getRegista().getNome().toLowerCase().contains(nomeCognome_regista.toLowerCase())
                            || films.get(i).getRegista().getCognome().toLowerCase().contains(nomeCognome_regista.toLowerCase())) )){

                    film_indexs.add(i);
                }
            }else{
                if(!titolo.equals("") && !nomeCognome_regista.equals("") && films.get(i).getTitolo().toLowerCase().contains(titolo.toLowerCase())
                        && ( (films.get(i).getRegista().getNome()+films.get(i).getRegista().getCognome())
                            .equalsIgnoreCase(nomeCognome_regista) )
                            || (nomeCognome_regista.toLowerCase().contains((films.get(i).getRegista().getNome()).toLowerCase())
                            || nomeCognome_regista.toLowerCase().contains((films.get(i).getRegista().getCognome()).toLowerCase())) ){

                    film_indexs.add(i);
                }else if(!titolo.equals("") && films.get(i).getTitolo().toLowerCase().contains(titolo.toLowerCase())
                        && films.get(i).getAnno_di_uscita() == anno_di_uscita ){

                    film_indexs.add(i);
                }
            }

            i++;
        }
        return film_indexs;
    }


    public ArrayList<Film> searchFilm(String titolo, int anno_di_uscita, String nomeCognome_regista){
        ArrayList<Integer> filmPositions = this.findFilmPosition(titolo, anno_di_uscita, nomeCognome_regista);

        if(filmPositions.size() == 0){
            return null;
        }else{
            ArrayList<Film> film_trovati = new ArrayList<>();
            for(int pos: filmPositions){
                film_trovati.add(films.get(pos));
            }
            return film_trovati;
        }

    }
    public ArrayList<Integer> searchFilm(String titolo, int anno_di_uscita, String nomeCognome_regista, int parametroInutile){
        return this.findFilmPosition(titolo, anno_di_uscita, nomeCognome_regista);
    }


    public void modifyFilm(/*bho magari la posizione del film modificato e l'oggetto Film modificato*/){
        /*
        ANCORA DA FARE, MA HO GIA' PENSATO, FORSE, COME FARO'. CAMBIERO' TUTTE LE TEXT VIEW DEL LAYOUT FILM IN EDIT TEXT,
         MA DISABILITANDOLE (SARANNO VISTE E UTILIZZABILI COME SEMPLICI TEXT VIEW) con .setEnable(false).
         POI INSERIRI UN ASCOLTATORE DELL'ON CLICK (oppure long click) SU OGNUNA DI ESSE CHE VADI A FARE .setEnable(true) O, SE SONO GIA'
         ABILITATE, .setEnable(false). POI PRENDEREI IL VALORE MODIFICATO E LO ASSEGNEREI AL CAMPO CORRETTO DELL'OGGETTO FILM.

        NON SO ANCORA COME POTREI SALVARE LA MODIFICA ANCHE SU FILE...
        */
    }


    public void orderFilmBy(final String orderBy){
        ordinated_films = new ArrayList<>();
        for(Film film: films){ ordinated_films.add(film); }

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
