package com.bellone.catalogofilm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PersonalizedArrayAdapter extends ArrayAdapter implements View.OnClickListener {
    private Context context;
    private int resource;                           //id del layout usato (layout_film o layout_recensioni)
    private ArrayList<Film> array_film = null;
    private ArrayList<Recensione> recensioni = null;

    private View convertView;

    private Film film;
    private Recensione recensione;
    private ListView listViewRecensioni;
    private int selectedItemPosition;


        //Nel layout del film ho usato un'altra listview, e anch'essa ha bisogno di un array adapter
        // personalizzato. Quindi ci sono due costruttori uno per l'array adapter con l'array di Film
        // e l'altro con oggetti Recensione. Per il secondo ho dovuto differenziarlo dal primo aggiungendo
        // un parametro (che serve solamente a differenziarlo dall'altro costruttore, visto che entrambi
        // avevano gli stessi parametri)
    public PersonalizedArrayAdapter(@NonNull Context context, int resource, @NonNull List<Film> film) {
        super(context, resource, film);
        this.context = context;
        this.resource = resource;
        this.array_film = (ArrayList<Film>) film;
        selectedItemPosition = 0;
    }

        //diverso numero di parametri
    public PersonalizedArrayAdapter(@NonNull Context context, int resource, @NonNull List<Recensione> recensioni
            , int parametroInutile) {
        super(context, resource, recensioni);
        this.context = context;
        this.resource = resource;
        this.recensioni = (ArrayList<Recensione>) recensioni;
        selectedItemPosition = 0;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, parent, false);

        String stringa_scritta = "";
        TextView textView;
        if(resource == R.layout.film_layout) {

            film = (Film)getItem(position);
            String contenutoArrayList_string = "";


            textView = convertView.findViewById(R.id.lblTitolo_Film);
            stringa_scritta = context.getString(R.string.lblTitolo) + " " + film.getTitolo();
            textView.setText(stringa_scritta);


            textView = convertView.findViewById(R.id.lblAnnoUscita_Film);
            stringa_scritta = context.getString(R.string.lblAnnoUscita) + " "
                    + String.valueOf(film.getAnno_di_uscita());
            textView.setText(stringa_scritta);


            for (String str : film.getGeneri()) { contenutoArrayList_string += str + ","; }
            textView = convertView.findViewById(R.id.lblGeneri_Film);
                                                                        //tolgo l'ultima virgola
            textView.setText(contenutoArrayList_string.substring(0, contenutoArrayList_string.lastIndexOf(",")));


            textView = convertView.findViewById(R.id.lblDurata_Film);
            int durata_int = film.getDurata();
            String durata_str = String.valueOf(Integer.valueOf(durata_int/60))+"h : "
                    +String.valueOf(durata_int%60)+"min";
            stringa_scritta = context.getString(R.string.lblDurata) + "\n\t\t\t" + durata_str;
            textView.setText(stringa_scritta);


            textView = convertView.findViewById(R.id.lblTrama_Film);
                                //ci sono circa 40 caratteri in una riga. Mostro 10 righe
            stringa_scritta = film.getTrama();
            if(stringa_scritta.length() > 40*10){
                stringa_scritta = stringa_scritta.substring(0, 40*10) + "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t. . .";
            }
            textView.setText(stringa_scritta);

            textView.setOnClickListener(this);



            textView = convertView.findViewById(R.id.lblRegista_Film);
            Regista regista = film.getRegista();
            stringa_scritta = context.getString(R.string.lblRegista) + " "
                    + regista.getNome() + " " + regista.getCognome() + " (" + String.valueOf(regista.getAnno_nascita()) + ")";
            textView.setText(stringa_scritta);


            contenutoArrayList_string = context.getString(R.string.lblLingue) + " ";
            for (String str : film.getLingue()) { contenutoArrayList_string += str + ","; }
            textView = convertView.findViewById(R.id.lblLingue_Film);
                                                                //tolgo l'ultima virgola
            textView.setText(contenutoArrayList_string.substring(0, contenutoArrayList_string.lastIndexOf(",")));


            textView = convertView.findViewById(R.id.lblAttori_Film);
            contenutoArrayList_string = context.getString(R.string.lblAttori) + " ";
            if(film.getAttori() != null && film.getAttori().size() > 0) {
                for (Attore attore : film.getAttori()) {
                    contenutoArrayList_string += attore.getNome() + " " + attore.getCognome() + "(" + attore.getNazionalita()
                            + ", " + String.valueOf(attore.getAnno_nascita()) + "),\n\t\t\t\t\t";
                }
                                                        //tolgo l'ultima virgola
                textView.setText(contenutoArrayList_string.substring(0, contenutoArrayList_string.lastIndexOf(",")-1));
            }else{
                textView.setText("NESSUN ATTORE REGISTRATO PER QUESTO FILM");
            }

            textView = convertView.findViewById(R.id.lblCasaDiProduzione_Film);
            stringa_scritta = context.getString(R.string.lblCasaProduzione) + " " + film.getCasa_di_produzione();
            textView.setText(stringa_scritta);


            Button button;
            button = convertView.findViewById(R.id.btnTrailer_Film);
            if(film.getTrailer_path() != null) {
                button.setOnClickListener(this);
            }else{ button.setVisibility(View.INVISIBLE); }

            button = convertView.findViewById(R.id.btnPrecedente_Film);
            if(film.getRecensioni() != null && film.getRecensioni().size() > 0) {
                button.setOnClickListener(this);
                button = convertView.findViewById(R.id.btnSuccessivo_Film);
                button.setOnClickListener(this);
            }else{
                button.setVisibility(View.INVISIBLE);
                button = convertView.findViewById(R.id.btnSuccessivo_Film);
                button.setVisibility(View.INVISIBLE);
            }


            if(film.getRecensioni() != null && film.getRecensioni().size() > 0){
                listViewRecensioni = convertView.findViewById(R.id.listViewRecensioni_Film);
                PersonalizedArrayAdapter adapter = new PersonalizedArrayAdapter(
                        parent.getContext(), R.layout.recensione_layout, film.getRecensioni(), 0);
                listViewRecensioni.setAdapter(adapter);
                listViewRecensioni.setSelection(selectedItemPosition);
            }

        }else if(resource == R.layout.recensione_layout){
            recensione = (Recensione)getItem(position);
            textView = convertView.findViewById(R.id.lblUsername_Recensione);
            stringa_scritta = context.getString(R.string.lblUsername)+recensione.getUsername();
            textView.setText(stringa_scritta);


            textView = convertView.findViewById(R.id.lblVoto_Recensione);
            stringa_scritta = context.getString(R.string.lblVoto)+String.valueOf(recensione.getVoto());
            textView.setText(stringa_scritta);


            textView = convertView.findViewById(R.id.lblTesto_Recensione);
            stringa_scritta = recensione.getTesto();
            textView.setText(stringa_scritta);
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
                //Come scritto nel main, l'onClick sui button funziona sempre, MA la listview (delle recensioni)
                // che predera' in considerazione sara' quella dell'item selezionato (della listview grande dei Film)
            listViewRecensioni.setSelection(selectedItemPosition);
        }
    }
}
