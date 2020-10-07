package com.bellone.catalogofilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewFilm = null;

    private GestoreFile gestoreFile = null;
    private ArrayList<Film> lista_film;
    private PersonalizedArrayAdapter adapter;

    private boolean permissionRequestDone;
    private boolean readExternalStoragePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionRequestDone = false;
        readExternalStoragePermission = false;

        listViewFilm = findViewById(R.id.listViewFilm_Main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED){
            readExternalStoragePermission = true;
        }else{ readExternalStoragePermission = false; }
        permissionRequestDone = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!permissionRequestDone){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{

            if(readExternalStoragePermission){
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

                if(adapter != null){ listViewFilm.setAdapter(adapter); }
            }else{
                Toast.makeText(getApplicationContext(), "MI SERVE IL PERMESSO PER LEGGERE\nI FILE DEL TELEFONO !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
