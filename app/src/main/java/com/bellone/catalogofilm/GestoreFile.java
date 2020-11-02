package com.bellone.catalogofilm;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GestoreFile {

    private final GestoreArrayFilm gestoreArrayFilm;
    private final RequestQueue queue;
    private final MainActivity activity;

    private boolean flagErroreJson;
    private boolean flagErroreRichiesta;

    public boolean getFlagErroreJson(){ return flagErroreJson; }
    public boolean getFlagErroreRichiesta(){ return flagErroreRichiesta; }

    public GestoreFile(GestoreArrayFilm gestoreArrayFilm, MainActivity activity) {
        this.gestoreArrayFilm = gestoreArrayFilm;
        this.activity = activity;
        this.queue = Volley.newRequestQueue(activity.getApplicationContext());
    }


    /**
     * Metodo che va a fare la richiesta per il JSON, e se tutto va a buon fine
     * richiama il metodo del MainActivity per settare gli ascoltatori. Quest'ultima parte
     * non so quanto sia giusta da fare, ma se volevo fare una classe a parte per la lettura.
     * Tutto questo perche', credo, il metodo termina prima che abbia eseguito l"onResponse",
     * come se fosse un processo a parte, e quindi metodi come l'utilizzo di flag non e' utilizzabile.
     */
    public void readFilms(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, MainActivity.JSON_file_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arrayFilms = response.getJSONArray("films");

                    for(int i=0; i<arrayFilms.length(); i++){
                        JSONObject film = arrayFilms.getJSONObject(i);
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
                        lingue = convertStringToStringArray(film.getString("lingue"));
                        tag = convertStringToStringArray(film.getString("tag"));

                        regista = convertStringToRegista(film.getString("regista"));

                        durata = film.getInt("durata");
                        anno_di_uscita = film.getInt("anno_di_uscita");

                        gestoreArrayFilm.addFilm(titolo, durata, anno_di_uscita, generi, lingue, regista, casa_di_produzione, tag, trama);
                        //gestoreArrayFilm.addFilm(new Film(titolo, durata, anno_di_uscita, generi, lingue, regista, casa_di_produzione, tag, trama));
                    }

                    flagErroreJson = false;
                    flagErroreRichiesta = false;

                    activity.richiestaJsonCompletata();

                } catch (JSONException e) {
                    flagErroreJson = true;
                    e.printStackTrace();
                    activity.richiestaJsonCompletata();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                flagErroreRichiesta = true;
                activity.richiestaJsonCompletata();
            }
        });

        queue.add(jsonObjectRequest);
    }

    /**
     * Nel metodo, prima di tutto rimuove le parentesi "[" "]" dalla stringa, dopodiche' splitta la stringa
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
