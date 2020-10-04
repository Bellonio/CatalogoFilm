package com.bellone.catalogofilm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewFilm = null;

    private GestoreFile gestoreFile = null;
    private ArrayList<Film> lista_film;
    private PersonalizedArrayAdapter adapter;

    private ArrayList<Film> default_films;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewFilm = findViewById(R.id.listViewFilm_Main);

        default_films = new ArrayList<>();

            //Creo il gestore dei file, gli passo il percorso di dove saranno salvati i file e il nome del file
        gestoreFile = new GestoreFile(getApplicationContext(),getFilesDir().getPath()+"/", "catalogoFilm.txt");

            //per ora la registrazione va semplicemente a creare nuovi oggetti Film, sempre uguali, se il file non esiste ancora
        if(!gestoreFile.fileExist()){
            registraFilm();
            gestoreFile.salvaFilmInFile(default_films);
        }

        lista_film = gestoreFile.readFilms();
        if(lista_film == null){
            Toast.makeText(getApplicationContext(), "SI E' VERIFICATO UN ERRORE.", Toast.LENGTH_LONG).show();
            adapter = null;
        }else if(lista_film.size() == 0){
            Toast.makeText(getApplicationContext(), "NESSUN FILM REGISTRATO !", Toast.LENGTH_LONG).show();
            adapter = null;
        }else{
            adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout, lista_film);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(adapter != null){ listViewFilm.setAdapter(adapter); }
    }

    private void registraFilm(){
        Regista regista = new Regista("Christopher", "Nolan", "britannico", 1970);

        ArrayList<String> lingue = new ArrayList<String>();
        lingue.add("Italiano");
        lingue.add("Inglese");
        lingue.add("Portoghese");
        lingue.add("Spagnolo");
        lingue.add("Messicano");

        ArrayList<String> generi = new ArrayList<>();
        generi.add("Azione");
        generi.add("Fantascienza");
        generi.add("Thriller");
        generi.add("Avventura");

        ArrayList<String> tag = new ArrayList<>();
        tag.add("leonardodicaprio");
        tag.add("warnerbros");

        ArrayList<Attore> attori = new ArrayList<>();
        attori.add(new Attore("Leonardo", "DiCaprio", "statunitense/italiano", 1974));
        attori.add(new Attore("Joseph", "Gordon-Levitt", "californiano", 1981));
        attori.add(new Attore("Ellen", "Philpotts-Page", "canadese", 1987));
        attori.add(new Attore("Ken", "Watanabe", "giapponese", 1959));
        attori.add(new Attore("Marion", "Cotillard", "francese", 1975));
        attori.add(new Attore("Tom", "Hardy", "britannico", 1977));
        attori.add(new Attore("Dileep", "Rao", "statunitense", 1973));
        attori.add(new Attore("Cillian", "Murphy", "irlandese", 1976));
        attori.add(new Attore("Tom", "Berenger", "statunitense", 1949));
        attori.add(new Attore("Pete", "Postlethwaite", "britannico", 1946));
        attori.add(new Attore("Michael", "Caine", "britannico", 1933));
        attori.add(new Attore("Lukas", "Haas", "statunitense", 1976));

        ArrayList<Recensione> recensioni = new ArrayList<>();
        recensioni.add(new Recensione("username12345", "Fantastico", 10));
        recensioni.add(new Recensione("Mario68", "Bel film, un po' lungo forse", 8));
        recensioni.add(new Recensione("Luca", "Consigliato, bellissimo film", 9));


        Film film = new Film(
                "Inception",
                regista,
                "path_image",
                "path_trailer",
                "Dom Cobb è un abilissimo ladro, il migliore al mondo quando si tratta della" +
                        " pericolosa arte dell'estrazione: ovvero il furto di preziosi segreti dal profondo" +
                        " del subconscio mentre si sogna, quando la mente è al massimo della sua vulnerabilità." +
                        " Le abilità di Cobb ne hanno fatto un protagonista di primo piano nel mondo dello spionaggio" +
                        " industriale, ma lo hanno reso un fuggitivo ricercato in tutto il mondo, costretto a lasciarsi" +
                        " alle spalle tutto ciò che ha sempre amato. Ora Cobb ha una chance di redenzione: un ultimo" +
                        " lavoro potrebbe restituirgli la sua vita, ma solo se riuscirà a rendere possibile l'" +
                        "impossibile. Invece di effettuare un colpo perfetto, Cobb e il suo team devono riuscire" +
                        " nell'opposto: non devono rubare un'idea ma impiantarne una nella testa di qualcuno.",
                "Syncopy Films, Warner Bros, Legendary Pictures",
                148, 2010,
                lingue, generi, tag, attori, recensioni

        );

        default_films.add(film);


        regista = new Regista("giovanni", "rana", "italiano", 1970);

        lingue = new ArrayList<String>();
        lingue.add("Italiano");
        lingue.add("Spagnolo");

        generi = new ArrayList<>();
        generi.add("Fantascienza");
        generi.add("Fantasy");

        tag = new ArrayList<>();
        tag.add("rana");
        tag.add("italianFilm");

        attori = new ArrayList<>();
        attori.add(new Attore("luigi", "paolino", "italiano", 1974));
        attori.add(new Attore("Andrea", "leviano", "italiano", 1978));
        attori.add(new Attore("Luca", "Rossi", "italiano", 1980));

        recensioni = new ArrayList<>();
        recensioni.add(new Recensione("username1", "Bel film", 7));
        recensioni.add(new Recensione("Ciccio1234", "Non mi e' piaciuto,\ncorto e senza senso"
                , 4));
        recensioni.add(new Recensione("francesco", "Ottimo film", 8));


        film = new Film(
                "La morte",
                regista,
                "path_image",
                "path_trailer",
                "Domani qualcuno morira, ma domani e' un altro giorno.",
                "Super impresa per film italiani",
                38, 2020,
                lingue, generi, tag, attori, recensioni

        );

        default_films.add(film);
    }
}
