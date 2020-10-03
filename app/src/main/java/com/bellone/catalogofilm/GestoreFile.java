package com.bellone.catalogofilm;

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

    public void salvaFilmInFile(ArrayList<Film> films){
        File file = new File(files_path+file_name);
        try {
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            String contenutoFile = "";
            Gson gson = new Gson();

            for(Film film: films){ contenutoFile += gson.toJson(film, Film.class) + "\n"; }

            bw.write(contenutoFile);

            bw.close();
        } catch (IOException e) {}
    }

        //metodo che ricevuto un Film come parametro lo serializza, sotto forma di JSON, e lo salva nel file (file
        // impostato in modalita' append)       ***per ora mai utilizzato***
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

        //metodo che ritorna una lista con tutti i Film salvati (che serve all'array adapter)
    public ArrayList<Film> readFilms(){

            //HO PROVATO A LEGGERE IL FILE JSON, SALVATO IN res>raw

            //NON MI LEGGE TUTTO IL FILE, ARRIVA FINO AL FILM "Inception" e senza neanche finirlo, ho gia' provato a
            // leggere riga per riga (usando il BufferedReader) e sono andato su un sito per rimuovere tutti gli spazi/indentazioni...
            // in eccesso che c'erano ma non basta, non riesce cmq a leggere tutto il file
        /* InputStream inputStream = getResources().openRawResource(R.raw.all_film_json);
        String fileJson = "";
        try {
            int fileSize = inputStream.available();

                //      PARTE DELLA DOCUMENTAZIONE SUL METODO
                //Returns an estimate of the number of bytes that can be read (or
                //    skipped over) from this input stream without blocking by the next
                //    invocation of a method for this input stream. The next invocation
                //    might be the same thread or another thread.  A single read or skip of this
                //    many bytes will not block, but may read or skip fewer bytes.

                //.... infatti lo dice che magari non leggera' l'intero file, ma non ho trovato altri modi ...
                //Note that while some implementations of {@code InputStream} will return
                //    the total number of bytes in the stream, many will not.  It is
                //    never correct to use the return value of this method to allocate
                //    a buffer intended to hold all data in this stream.

            byte[] buffer = new byte[fileSize];

            inputStream.read(buffer);

            inputStream.close();

            fileJson = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            fileJson = null;
        }
        Log.d("mieilog", "onResume: "+fileJson); */

        File file = new File(files_path+file_name);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            Gson gson = new Gson();
            ArrayList<Film> films = new ArrayList<>();
            String riga;
                //legge riga per riga, finche non finisce il file, e aggiunge all'array la riga
                // de-serializzata con il JSON

            while((riga=br.readLine()) != null) { films.add(gson.fromJson(riga, Film.class)); }

            br.close();

            return films;
        } catch (FileNotFoundException e) { return null;
        } catch (IOException e) { return null; }
    }
}
