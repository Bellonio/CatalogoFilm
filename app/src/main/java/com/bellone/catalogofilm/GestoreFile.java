package com.bellone.catalogofilm;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GestoreFile {

    private String files_path;
    private String file_name;

    public GestoreFile(String files_path, String file_name) {
        this.files_path = files_path;
        this.file_name = file_name;
    }

    public boolean fileExist(){
        File file = new File(files_path+file_name);
        return file.exists();
    }

        //metodo che ricevuto un Film come parametro lo serializza con il JSON e lo salva nel file (file
        // impostato in modalita' append)
    public void addFilm(Film film){
        File file = new File(files_path+file_name);
        try {
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            Gson gson = new Gson();
            String film_str = gson.toJson(film);

            bw.append(film_str+"\n");       //ad ogni riga corrisponde un Film
            bw.close();
        } catch (IOException e) {}
    }

        //metodo che ritorna una lista con tutti i Film salvati (che serve all'array adapter) se il file
        // esiste e non si verificano altri problemi
    public ArrayList<Film> readFilms(){
        File file = new File(files_path+file_name);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            Gson gson = new Gson();
            ArrayList<Film> film = new ArrayList<>();
            String riga;
                //legge riga per riga, finche non finisce il file, e aggiunge all'array la riga
                // de-serializzata con il JSON
            while((riga=br.readLine()) != null) {
                Film f = gson.fromJson(riga, Film.class);
                film.add(f);
            }
            br.close();
            return film;
        } catch (FileNotFoundException e) { return new ArrayList<Film>();
        } catch (IOException e) { return null; }
    }
}
