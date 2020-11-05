package com.bellone.catalogofilm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PersonalizedArrayAdapter extends ArrayAdapter implements View.OnClickListener {
    private Context context;
    private int resource;
    private ArrayList<Film> array_film = null;
    private ArrayList<Recensione> recensioni = null;

    private View convertView;

    private Film film;
    private Recensione recensione;
    private ListView listViewRecensioni;
    private int selectedItemPosition;


    public PersonalizedArrayAdapter(@NonNull Context context, int resource, @NonNull List<Film> film) {
        super(context, resource, film);
        this.context = context;
        this.resource = resource;
        this.array_film = (ArrayList<Film>) film;
        selectedItemPosition = 0;
    }

    /**
     * Dato che il layout dei Film ha, come view per vedere le recensioni, un altra ListView, ho usato il polimorfismo per
     *  utilizzare questa stessa classe per gestire gli elementi della ListView dei rilm e delle recensioni (in verita'
     *   per ora le recensioni non sono ancora caricate nel file json).
     * Semplicemente nei costruttori vengono valorizzati o l'arraylist di Film o l'arraylist di Recensione.
     *
     * @param context               parametro importante perche' alle varie View del layout assegno il nome del
     *                                  campo (esempio titolo, durata, tag,...username, voto,...), letto dal file
     *                                  strings.xml (utilizzando qui un metodo della classe Context) + il
     *                                  valore dell'oggetto Film/Recensione
     * @param resource              l'id della risorsa ossia del layout da dare alla voce della listview
     * @param recensioni   o film, nel caso del precedente costruttore,     array con i dati
     * @param parametroInutile      dato che il tipo dei parametri dei due costruttori e' uguale, ho aggiunto un parametro
     *                                  che in verita' non uso
     */

    public PersonalizedArrayAdapter(@NonNull Context context, int resource, @NonNull List<Recensione> recensioni
            , int parametroInutile) {
        super(context, resource, recensioni);
        this.context = context;
        this.resource = resource;
        this.recensioni = (ArrayList<Recensione>) recensioni;
        selectedItemPosition = 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull final ViewGroup parent) {
            /*Metodo che viene richiamato quando dev'essere caricato il contenuto di una voce della list view*/

            /*Attraverso l'inflater leggo il file xml dal resource (dall'id del layout xml)*/
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, parent, false);

        String str_da_assegnare = "";
        TextView textView;
            /*Il codice ovviamente sara' differente tra il caso in cui carichiamo i dati per il layout dei film
                da quello delle recensioni, percio' controllo in quale caso mi trovo*/
        if(resource == R.layout.film_layout) {

                //Un semplice contatore della posizione del film rispetto alla quantita' di film letti
            MainActivity.setLblItemSelectedValue(position+1+"/"+array_film.size());

            film = (Film)getItem(position);
            String contenutoArrayList_string = "";


            if(film.getLocandina() != null){
                ImageView imgLocandina = convertView.findViewById(R.id.imgCopertina_Film);
                imgLocandina.setImageBitmap(film.getLocandina());
            }


            textView = convertView.findViewById(R.id.lblTitolo_Film);
            str_da_assegnare = context.getString(R.string.lblTitolo) + " " + film.getTitolo();
            textView.setText(str_da_assegnare);


            textView = convertView.findViewById(R.id.lblAnnoUscita_Film);
            str_da_assegnare = context.getString(R.string.lblAnnoUscita) + " "
                    + film.getAnno_di_uscita();
            textView.setText(str_da_assegnare);


            contenutoArrayList_string = context.getString(R.string.lblGeneri) + " " + trasformaArray_inStringa(film.getGeneri());
            textView = convertView.findViewById(R.id.lblGeneri_Film);
            textView.setText(contenutoArrayList_string);


            textView = convertView.findViewById(R.id.lblDurata_Film);
            int durata_int = film.getDurata();
                //Trasformo la durata in minuti in una durata piu' leggibbile per un umano (ore : minuti)
            String durata_str = Integer.valueOf(durata_int/60)+"h : "
                    +durata_int%60+"min";
            str_da_assegnare = context.getString(R.string.lblDurata) + "\n" + durata_str;
            textView.setText(str_da_assegnare);


            textView = convertView.findViewById(R.id.lblTrama_Film);
            str_da_assegnare = film.getTrama();
                /*Ci dovrebbero essere circa 40 caratteri per riga all'interno della TextView "lblTrama",
                    percio' vado a controllare se la trama e' piu' lunga di 7 righe, in caso vado a tagliare la
                    trama dopo 7 righe e assegnarle un ascoltatore dell'onClick per far espandere la TextView*/
            if(str_da_assegnare.length() > 40*7){
                str_da_assegnare = str_da_assegnare.substring(0, 40*7) + "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t . . .";
                textView.setOnClickListener(this);
            }
            textView.setText(str_da_assegnare);


            textView = convertView.findViewById(R.id.lblRegista_Film);
            Regista regista = film.getRegista();
            str_da_assegnare = context.getString(R.string.lblRegista) + " "
                    + regista.getNome() + " " + regista.getCognome() + " ( " + regista.getAnno_nascita() + " )";
            textView.setText(str_da_assegnare);


            contenutoArrayList_string = context.getString(R.string.lblLingue) + " " + trasformaArray_inStringa(film.getLingue());
            textView = convertView.findViewById(R.id.lblLingue_Film);
            textView.setText(contenutoArrayList_string);


            textView = convertView.findViewById(R.id.lblAttori_Film);
            contenutoArrayList_string = context.getString(R.string.lblAttori) + " ";

            ArrayList<Attore> attori = film.getAttori();
                //Controllo se ci sono degli attori registrati
            if(attori != null && attori.size() > 0) {
                for (Attore attore : attori) {
                    contenutoArrayList_string += attore.getNome() + " " + attore.getCognome() + "(" + attore.getNazionalita()
                            + ", " + attore.getAnno_nascita() + "),\n\t\t\t\t\t";
                }
                                                        //tolgo l'ultima virgola
                textView.setText(contenutoArrayList_string.substring(0, contenutoArrayList_string.lastIndexOf(",")-1));
            }else{
                textView.setText("NESSUN ATTORE REGISTRATO PER QUESTO FILM");
            }


            textView = convertView.findViewById(R.id.lblCasaDiProduzione_Film);
            str_da_assegnare = context.getString(R.string.lblCasaProduzione) + " " + film.getCasa_di_produzione();
            textView.setText(str_da_assegnare);


                /*Di seguito vado ad assegnare l'ascoltatore onClick ai vari button, se i dati sono registrati.
                    Per ora infatti i dati relativi al percorso del trailer e alla lista di recensioni non sono
                    presenti nel file, percio' questi button saranno resi invisibili*/

            Button button;
            button = convertView.findViewById(R.id.btnTrailer_Film);
            if(film.getTrailer_path() != null) {
                button.setOnClickListener(this);
            }else{ button.setVisibility(View.INVISIBLE); }


            if(film.getRecensioni() != null && film.getRecensioni().size() > 0){
                    //I seguenti button servirebbero per passare da una recensione all'altra
                button = convertView.findViewById(R.id.btnPrecedente_Film);
                button.setOnClickListener(this);

                button = convertView.findViewById(R.id.btnSuccessivo_Film);
                button.setOnClickListener(this);


                    //Creo un arrayAdapter personalizzato anche per le recensioni
                listViewRecensioni = convertView.findViewById(R.id.listViewRecensioni_Film);
                PersonalizedArrayAdapter adapter = new PersonalizedArrayAdapter(
                        parent.getContext(), R.layout.recensione_layout, film.getRecensioni(), 0);
                listViewRecensioni.setAdapter(adapter);
                listViewRecensioni.setSelection(selectedItemPosition);
            }else{
                button = convertView.findViewById(R.id.btnPrecedente_Film);
                button.setVisibility(View.INVISIBLE);

                button = convertView.findViewById(R.id.btnSuccessivo_Film);
                button.setVisibility(View.INVISIBLE);
            }

        }else if(resource == R.layout.recensione_layout){
            recensione = (Recensione)getItem(position);
            textView = convertView.findViewById(R.id.lblUsername_Recensione);
            str_da_assegnare = context.getString(R.string.lblUsername)+recensione.getUsername();
            textView.setText(str_da_assegnare);


            textView = convertView.findViewById(R.id.lblVoto_Recensione);
            str_da_assegnare = context.getString(R.string.lblVoto)+recensione.getVoto();
            textView.setText(str_da_assegnare);


            textView = convertView.findViewById(R.id.lblTesto_Recensione);
            str_da_assegnare = recensione.getTesto();
            textView.setText(str_da_assegnare);
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lblTrama_Film:
                ((TextView)v).setText(film.getTrama());
                break;
            case R.id.btnTrailer_Film:
                Toast.makeText(context, "Link TRAILER: " + film.getTrailer_path(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnPrecedente_Film:
                if(selectedItemPosition > 0) {
                    selectedItemPosition--;
                }
                break;
            case R.id.btnSuccessivo_Film:
                if(selectedItemPosition < film.getRecensioni().size()) {
                    selectedItemPosition++;
                }
                break;
        }
        if(v.getId() == R.id.btnPrecedente_Film || v.getId() == R.id.btnSuccessivo_Film){
            listViewRecensioni.setSelection(selectedItemPosition);
        }
    }

    /**
     *  Metodo che va a leggere elemento per elemento l'array di stringhe e ritorna una stringa
     *   unica formattata cosi': "orrore, thriller, ..."
     * @param arrayDiStr    array contenente SEMPLICI stringhe, ad esempio == ["orrore", "thriller", ...]
     * @return  semplice stringa formattata come scritto sopra
     */
    private String trasformaArray_inStringa(ArrayList<String> arrayDiStr){
        String str_finale = "";
        for (String str : arrayDiStr) { str_finale += str + ","; }

            //tolgo l'ultima virgola
        str_finale = str_finale.substring(0, str_finale.lastIndexOf(","));

        return str_finale;
    }

}
