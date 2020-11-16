package com.bellone.catalogofilm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

    //Non ho specificato il tipo dell'array adapter cosi' che possa essere qualsiasi tipo (a me serve Film e Recensione)
public class PersonalizedArrayAdapter extends ArrayAdapter {
    private final Context context;
    private final int resource;

    private final int selectedItemPosition;


    public PersonalizedArrayAdapter(@NonNull Context context, int resource, @NonNull List<Film> film) {
        super(context, resource, film);
        this.context = context;
        this.resource = resource;
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
        selectedItemPosition = 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull final ViewGroup parent) {
            //Metodo che viene richiamato quando dev'essere caricato il contenuto di una voce della list view

            //Attraverso l'inflater leggo il file xml dal resource (dall'id del layout xml)
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder")
        View convertView = inflater.inflate(resource, parent, false);

        String str_da_assegnare;
        TextView textView;
            /*Il codice ovviamente sara' differente tra il caso in cui carichiamo i dati per il layout dei film
                da quello delle recensioni, percio' controllo in quale caso mi trovo*/
        if(resource == R.layout.film_layout) {

            Film film = (Film) getItem(position);

            if(film != null){
                String contenutoArrayList_string;


                ImageView imgLocandina = convertView.findViewById(R.id.imgCopertina_Film);
                Picasso.with(context).load(film.getUrl_locandina()).into(imgLocandina);


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
                String durata_str = durata_int/60 +"h : "
                        +durata_int%60+"min";
                str_da_assegnare = context.getString(R.string.lblDurata) + "\n" + durata_str;
                textView.setText(str_da_assegnare);

                /*convertView.findViewById(R.id.layoutDatiFilm_Film).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("mieilog", "onClick: "+film.getTitolo());
                    }
                });*/
            }

        }else if(resource == R.layout.recensione_layout){
            Recensione recensione = (Recensione) getItem(position);
            textView = convertView.findViewById(R.id.lblUsername_Recensione);
            str_da_assegnare = context.getString(R.string.lblUsername)+ recensione.getUsername();
            textView.setText(str_da_assegnare);


            textView = convertView.findViewById(R.id.lblVoto_Recensione);
            str_da_assegnare = context.getString(R.string.lblVoto)+ recensione.getVoto();
            textView.setText(str_da_assegnare);


            textView = convertView.findViewById(R.id.lblTesto_Recensione);
            str_da_assegnare = recensione.getTesto();
            textView.setText(str_da_assegnare);
        }

        return convertView;
    }

    /**
     *  Metodo che va a leggere elemento per elemento l'array di stringhe e ritorna una stringa
     *   unica formattata cosi': "orrore, thriller, ..."
     * @param arrayDiStr    array contenente SEMPLICI stringhe, ad esempio == ["orrore", "thriller", ...]
     * @return  semplice stringa formattata come scritto sopra
     */
    private String trasformaArray_inStringa(ArrayList<String> arrayDiStr){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("");
        for (String str : arrayDiStr) { strBuilder.append(str).append(","); }

        String str_finale;
            //tolgo l'ultima virgola
        str_finale = strBuilder.toString().substring(0, strBuilder.toString().lastIndexOf(","));

        return str_finale;
    }
}