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

    private GestoreArrayFilm gestoreArrayFilm;
    private String file_name;

    public GestoreFile(GestoreArrayFilm gestoreArrayFilm, String file_name) {
        this.gestoreArrayFilm = gestoreArrayFilm;
        this.file_name = file_name;
    }


    /**
     * Con questo metodo leggo il file dalla cartella Downloads del telefono, poi "rileggo"  il file, salvato in una
     * variabile, con il JSON, prendo ogni valore e creo l'oggetto Film da aggiungere alla lista.
     * @return il metodo ritorna false in caso vi sia un errore, altrimenti true
     */
    public boolean readFilms(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+file_name);

        String fileJson_str = "";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String riga;
            while((riga=br.readLine()) != null){ fileJson_str += riga.trim(); }
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

                    generi = convertStringToStringArray(film.getString("generi"));
                    lingue = convertStringToStringArray(film.getString("lingue"));;
                    tag = convertStringToStringArray(film.getString("tag"));;

                    regista = convertStringToRegista(film.getString("regista"));

                    durata = film.getInt("durata");
                    anno_di_uscita = film.getInt("anno_di_uscita");

                    gestoreArrayFilm.addFilm(titolo, durata, anno_di_uscita, generi, lingue, regista, casa_di_produzione, tag, trama);
                    //gestoreArrayFilm.addFilm(new Film(titolo, durata, anno_di_uscita, generi, lingue, regista, casa_di_produzione, tag, trama));
                }

            } catch (JSONException e) { return false; }
        }else{ return false; }
        return true;
    }

    /**
     * Nel metodo prima di tutto rimuove le parentesi "[" "]" dalla stringa, dopodiche' splitta la stringa
     *  in un array di stringhe e le aggiunge ad un ArrayList, togliendo pero' il '"'. Ottenendo cosi', dalla stringa
     *  d'esempio, il seguente arraylist [" thriller "," fantastico "," drammatico "," orrore "]
     * @param stringa   ad esempio: stringa == "["thriller","fantastico","drammatico","orrore"]"
     * @return array di stringhe (nel caso di questo file JSON ritornera' l'array di generi, lingue o tag)
     */
    private ArrayList<String> convertStringToStringArray(String stringa){
        String stringWithoutBrackets = stringa.substring(1, stringa.length()-1);
        ArrayList<String> arrayList = new ArrayList<>();

        for(String str: stringWithoutBrackets.split(",")){
            arrayList.add(str.replace('\"', ' '));
        }

        return arrayList;
    }

    /**
     * Il metodo trasforma la stringa in un oggetto JSON, prende i vari valori e crea l'oggetto Regista
     * @param stringa  ad esempio: stringa == "{"nome":"Rodrigo Cortés","cognome":"Giráldez","nazionalita":"spagnolo","anno_di_nascita":1973}"
     * @return l'oggetto Regista creato, o null in caso avvenga un errore
     */
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
        } catch (JSONException e) { return null; }
    }

}
