package com.bellone.catalogofilm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DettagliActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView lblTitolo = null;
    private TextView lblRegista = null;
    private TextView lblTrama = null;
    private TextView lblLingue = null;
    private TextView lblTag = null;
    private TextView lblAttori = null;
    private TextView lblCasaDiProduzione = null;
    private ListView listViewRecensioni = null;
    private Button btnTrailer = null;

    private Film film;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli);

        lblTitolo = findViewById(R.id.lblTitolo_Dettagli);
        lblRegista = findViewById(R.id.lblRegista_Dettagli);
        lblTrama = findViewById(R.id.lblTrama_Dettagli);
        lblLingue = findViewById(R.id.lblLingue_Dettagli);
        lblTag = findViewById(R.id.lblTag_Dettagli);
        lblAttori = findViewById(R.id.lblAttori_Dettagli);
        lblCasaDiProduzione = findViewById(R.id.lblCasaDiProduzione_Dettagli);
        listViewRecensioni = findViewById(R.id.listViewRecensioni_Dettagli);
        btnTrailer = findViewById(R.id.btnTrailer_Dettagli);
    }

    @Override
    protected void onResume() {
        super.onResume();

            //recupera il film passato tramite l'Intent
        film = (Film) getIntent().getSerializableExtra("film_serializable");

        valorizzaTextView();

        lblTrama.setOnClickListener(this);
        btnTrailer.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lblTrama_Dettagli:
                /*
                * Limita la TextView a 3 righe oppure a 5000, esagerato cosi' da avere
                * un numero di righe sufficienti di sicuro a qualunque trama
                * */

                if(lblTrama.getMaxLines() != 3){
                    ((TextView)view).setMaxLines(3);
                        //Di seguito divide l'ultima riga della stringa alla fine ed aggiunge i "..."
                    ((TextView)view).setEllipsize(TextUtils.TruncateAt.END);
                }else{
                    ((TextView)view).setMaxLines(5000);
                }
                break;
            case R.id.btnTrailer_Dettagli:
                String trailer_path = film.getTrailer_path();
                if(trailer_path == null){
                    trailer_path = "non registrato";
                }
                Toast.makeText(getApplicationContext(), "Link TRAILER: " + trailer_path, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void valorizzaTextView(){
        String str_da_assegnare;

        str_da_assegnare = getString(R.string.lblTitolo) +" "+ film.getTitolo();
        lblTitolo.setText(str_da_assegnare);


        Regista regista = film.getRegista();
        if(regista != null){
            str_da_assegnare = getString(R.string.lblRegista) + " "
                    + regista.getNome() + " " + regista.getCognome() + " ( " + regista.getAnno_nascita() + " )";
            lblRegista.setText(str_da_assegnare);
        }


        str_da_assegnare = film.getTrama();
        lblTrama.setText(str_da_assegnare);


        String contenutoArrayList_string;
        contenutoArrayList_string = getString(R.string.lblLingue) + " " + trasformaArray_inStringa(film.getLingue());
        lblLingue.setText(contenutoArrayList_string);


        contenutoArrayList_string = getString(R.string.lblTag) + " " + trasformaArray_inStringa(film.getTag());
        lblTag.setText(contenutoArrayList_string);


        contenutoArrayList_string = getString(R.string.lblAttori) + " ";
        ArrayList<Attore> attori = film.getAttori();
            //Controllo che ci siano degli attori registrati
        if (attori != null && attori.size() > 0) {
            StringBuilder strBuilderAttori = new StringBuilder();
            for (Attore attore : attori) {
                strBuilderAttori.append(attore.getNome()).append(" ").append(attore.getCognome())
                        .append("(").append(attore.getNazionalita()).append(", ").append(attore.getAnno_nascita())
                        .append("),\n\t\t\t\t\t");
            }
                //tolgo l'ultima virgola
            lblAttori.setText(strBuilderAttori.toString().substring(0, contenutoArrayList_string.lastIndexOf(",") - 1));
        } else {
            str_da_assegnare = "NESSUN ATTORE REGISTRATO PER QUESTO FILM";
            lblAttori.setText(str_da_assegnare);
        }


        str_da_assegnare = getString(R.string.lblCasaProduzione) + " " + film.getCasa_di_produzione();
        lblCasaDiProduzione.setText(str_da_assegnare);


        ArrayList<Recensione> recensioni = film.getRecensioni();
        if(recensioni != null && recensioni.size() > 0){
            PersonalizedArrayAdapter adapter = new PersonalizedArrayAdapter(
                    getApplicationContext(), R.layout.recensione_layout, film.getRecensioni(), 0);
            listViewRecensioni.setAdapter(adapter);
            listViewRecensioni.setSelection(0);
        }
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