package com.bellone.catalogofilm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewFilm = null;
    private Button btnPrecedente = null;
    private Button btnSuccessivo = null;
    private TextView lblInfoSelezione = null;


    private GestoreFile gestoreFile = null;
    private ArrayList<Film> lista_film;
    private PersonalizedArrayAdapter adapter;
    private int posizione_item_selezionato;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            //Creo il gestore dei file, gli passo il percorso di dove saranno salvati i file e il nome del file
        gestoreFile = new GestoreFile(getFilesDir().getPath()+"/", "catalogoFilm.txt");

        listViewFilm = findViewById(R.id.listViewFilm_Main);
        btnPrecedente = findViewById(R.id.btnPrecedenteFilm_Main);
        btnSuccessivo = findViewById(R.id.btnSuccessivoFilm_Main);
        lblInfoSelezione = findViewById(R.id.lblInfoSelezione_Main);

            //per ora la registrazione va semplicemente a creare nuovi oggetti Film, sempre uguali
        registraFilm();


        lista_film = gestoreFile.readFilms();
        if(lista_film.size() > 0) {
            adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout, lista_film);
        }else if(listViewFilm != null){
            Toast.makeText(getApplicationContext(), "NESSUN FILM REGISTRATO !", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "SI E' VERIFICATO UN ERRORE.", Toast.LENGTH_SHORT).show();
        }
        posizione_item_selezionato = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        listViewFilm.setAdapter(adapter);

        aggiornaTextViewInfo();

        btnPrecedente.setOnClickListener(this);
        btnSuccessivo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //Quando si scorre la ListView e non si usano i Button cambia l'item selezionato, ma non sono
        // riuscito a gestirlo, quindi ho messo un padding enorme al layout degli item cosi' che venga
        // mostrato UN solo item per volta nello schermo (questo perche' nel layout degli item ci sono
        // button, e funzionano sull'item selezionato)

        boolean flag_item_selected_changed = false;
        switch (v.getId()){
            case R.id.btnPrecedenteFilm_Main:
                if(posizione_item_selezionato > 0){
                    posizione_item_selezionato--;
                    flag_item_selected_changed = true;
                }
                break;
            case R.id.btnSuccessivoFilm_Main:
                if(posizione_item_selezionato < lista_film.size()-1){
                    posizione_item_selezionato++;
                    flag_item_selected_changed = true;
                }
                break;
        }
        if(flag_item_selected_changed){ aggiornaTextViewInfo(); }

    }

        //metodo che serve semplicemente a cambiare il numero che identifica quale item si ha selezionato al momento
    private void aggiornaTextViewInfo(){
        listViewFilm.setSelection(posizione_item_selezionato);
        lblInfoSelezione.setText(String.valueOf(posizione_item_selezionato+1)+" / "+String.valueOf(lista_film.size()));
    }


    private void registraFilm(){
            //dato che gestoreFile non va a sovvrascivere il file ma aggiunge i Film in coda, va a fare questo solo
            // la prima volta che si avvia l'app, quando il file ancora non esiste
        if(!gestoreFile.fileExist()) {
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

            gestoreFile.addFilm(film);

            regista = new Regista("giovanni", "rana", "ita", 1920);

            lingue = new ArrayList<String>();
            lingue.add("Italiano");
            lingue.add("Spagnolo");

            generi = new ArrayList<>();
            generi.add("Fantascienza");
            generi.add("Fantasy");

            tag = new ArrayList<>();
            tag.add("bello");
            tag.add("morte");

            attori = new ArrayList<>();
            attori.add(new Attore("luigi", "paolino", "italiano", 1947));
            attori.add(new Attore("Josh", "levi", "canadese", 1928));
            attori.add(new Attore("marge", "Phillip", "canadese", 1910));
            attori.add(new Attore("skjgsblgudi", "sdgg", "tedesco", 1999));

            recensioni = new ArrayList<>();
            recensioni.add(new Recensione("132465", "brutto", 10));
            recensioni.add(new Recensione("16458", "na merda", 8));
            recensioni.add(new Recensione("sfsdf", "fantastico !", 8));
            recensioni.add(new Recensione("1dfsdfsd6458", "uno schifo", 8));


            film = new Film(
                    "Lucas grey",
                    regista,
                    "path_image",
                    "path_trailer",
                    "DOMANI QUALCUNO MORIRA' E IO RIDERO' TANTISSIMO !",
                    "Films, Super mario Bros, Legendary OSLO",
                    68, 2000,
                    lingue, generi, tag, attori, recensioni

            );

            gestoreFile.addFilm(film);

            regista = new Regista("giovanni", "rana", "ita", 1920);

            lingue = new ArrayList<String>();
            lingue.add("Italiano");

            generi = new ArrayList<>();
            generi.add("Fantascienza");
            generi.add("Storico");
            generi.add("Sportivo");
            generi.add("Finanziario");
            generi.add("Fantasy");

            tag = new ArrayList<>();
            tag.add("bello");

            attori = new ArrayList<>();
            attori.add(new Attore("luigi", "paolino", "italiano", 1947));
            attori.add(new Attore("Josh", "levi", "canadese", 1928));
            attori.add(new Attore("marge", "Phillip", "canadese", 1910));
            attori.add(new Attore("skjgsblgudi", "sdgg", "tedesco", 1999));
            attori.add(new Attore("dsfs", "afaf", "italiano", 2018));

            recensioni = new ArrayList<>();
            recensioni.add(new Recensione("132465", "brutto", 10));
            recensioni.add(new Recensione("164asd58", "na figaata", 8));
            recensioni.add(new Recensione("sgfdg", "na merda", 8));
            recensioni.add(new Recensione("164sgdfg58", "na schifezza", 8));


            film = new Film(
                    "Luca mongeeeeee",
                    regista,
                    "path_image",
                    "path_trailer",
                    "SE NON MUOIO\n MI SUOICIDO\nsCeGlI IL BIO! \n FILENI !!! !",
                    "Fi",
                    68, 2000,
                    lingue, generi, tag, attori, recensioni

            );

            gestoreFile.addFilm(film);
        }
    }

}
