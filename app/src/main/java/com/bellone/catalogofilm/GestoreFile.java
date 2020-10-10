package com.bellone.catalogofilm;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GestoreFile {

    private GestoreArrayFilm gestoreArrayFilm = null;
    private String file_name = null;

    public GestoreFile(GestoreArrayFilm gestoreArrayFilm, String file_name) {
        this.gestoreArrayFilm = gestoreArrayFilm;
        this.file_name = file_name;
    }

        //metodo che ritorna una lista con tutti i Film salvati (che serve all'array adapter)
    public boolean readFilms(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+file_name);

        String fileJson_str = "";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String riga;
            while((riga=br.readLine()) != null){ fileJson_str += riga; }
            br.close();
        } catch (FileNotFoundException e) { fileJson_str = null;
        } catch (IOException e) { fileJson_str = null; }

        if(fileJson_str != null){

            try {
                JSONObject jsonObject = new JSONObject(fileJson_str);
                JSONArray films = jsonObject.getJSONArray("films");

                for(int i=0; i<films.length(); i++){
                    JSONObject film = films.getJSONObject(i);
                    String titolo, casa_di_produzione, trama, varAppoggio;
                    ArrayList<String> generi, lingue, tag;
                    Regista regista;
                    int durata, anno_di_uscita;

                    titolo = film.getString("titolo");
                    casa_di_produzione = film.getString("casa_di_produzione");
                    if(casa_di_produzione.contains("|")){
                        casa_di_produzione = casa_di_produzione.replace('\"', ' ').replace('|', ',');
                    }

                    trama = film.getString("trama");

                    generi = this.convertStringToStringArray(film.getString("generi"));
                    lingue = this.convertStringToStringArray(film.getString("lingue"));;
                    tag = this.convertStringToStringArray(film.getString("tag"));;

                    regista = this.convertStringToRegista(film.getString("regista"));

                    durata = film.getInt("durata");
                    anno_di_uscita = film.getInt("anno_di_uscita");

                    gestoreArrayFilm.addFilm(titolo, durata, anno_di_uscita, generi, lingue, regista, casa_di_produzione, tag, trama);
                    //gestoreArrayFilm.addFilm(new Film(titolo, durata, anno_di_uscita, generi, lingue, regista, casa_di_produzione, tag, trama));
                }

            } catch (JSONException e) { return false; }
        }
        return true;
    }

    private ArrayList<String> convertStringToStringArray(String stringa){
        String stringWithoutBrackets = stringa.substring(1, stringa.length()-1);
        ArrayList<String> arrayList = new ArrayList<>();

        for(String str: stringWithoutBrackets.split(",")){
            arrayList.add(str.replace('\"', ' '));
        }

        return arrayList;
    }

    private Regista convertStringToRegista(String stringa){
        try {
            JSONObject regista_jsonObject = new JSONObject(stringa);
            String nome, cognome, nazionalita;
            int anno_di_nascita;

            nome = regista_jsonObject.getString("nome");
            cognome = regista_jsonObject.getString("cognome");
            nazionalita = regista_jsonObject.getString("nazionalita");
            anno_di_nascita = regista_jsonObject.getInt("anno_di_nascita");

            return (new Regista(nome, cognome, nazionalita, anno_di_nascita));
        } catch (JSONException e) { e.printStackTrace(); }
        return null;
    }

}
