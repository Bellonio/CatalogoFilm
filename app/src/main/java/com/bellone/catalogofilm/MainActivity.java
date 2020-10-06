package com.bellone.catalogofilm;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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

        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        listViewFilm = findViewById(R.id.listViewFilm_Main);

        default_films = new ArrayList<>();

            //Creo il gestore dei file, gli passo il percorso di dove saranno salvati i file e il nome del file
        gestoreFile = new GestoreFile("all_film_json.txt");

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
}
