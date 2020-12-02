package com.bellone.catalogofilm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String JSON_file_URL =
            "https://raw.githubusercontent.com/Bellonio/CatalogoFilm/AppConNuoveFunzionalita/all_film_json.txt";

    private ConstraintLayout bigLayout = null;
    private Spinner spnOrdinamento = null;
    private Button btnSearchFilm = null;
    private Button btnRemoveFilm = null;
    private EditText txtTitolo = null;
    private EditText txtAnnoUscita = null;
    private EditText txtDatiRegista = null;

    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;
    private FilmAdapter adapter = null;


    private GestoreFile gestoreFile = null;
    private GestoreArrayFilm gestoreArrayFilm = null;
    private PersonalizedArrayAdapter adapterRec = null;

    private boolean flagDefaultArrayFilm;
    private FilmAdapter.OnItemClickListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestoreArrayFilm = new GestoreArrayFilm();
        gestoreFile = new GestoreFile(gestoreArrayFilm, this);


        bigLayout = findViewById(R.id.bigLayout_Main);
        spnOrdinamento = findViewById(R.id.spnOrdinamento_Main);
        btnSearchFilm = findViewById(R.id.btnSearchFilm_Main);
        btnRemoveFilm = findViewById(R.id.btnRemoveFilm_Main);
        txtTitolo = findViewById(R.id.txtTitoloFilm_Main);
        txtAnnoUscita = findViewById(R.id.txtAnnoUscita_Main);
        txtDatiRegista = findViewById(R.id.txtDatiRegista_Main);
        recyclerView = findViewById(R.id.recyclerViewFilms_Main);

        ArrayList<String> spnValues = new ArrayList<>();
            //Riempo lo spinner degli ordinamenti con i valori possibili nella classe GestoreArrayFilm
        spnValues.addAll(Arrays.asList(GestoreArrayFilm.ORDER_VALUES));
        spnOrdinamento.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spnValues));

        gestoreFile.readFilms();

        flagDefaultArrayFilm = true;

        listener = new FilmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Film film = flagDefaultArrayFilm ? gestoreArrayFilm.getDefaultFilms().get(pos)
                        : gestoreArrayFilm.getOrdinatedFilms().get(pos);
                Intent intent = new Intent(getApplicationContext(), DettagliActivity.class);
                intent.putExtra("film_serializable", film);
                startActivity(intent);
            }

            @Override
            public void onImgClick(int pos) {
                Toast.makeText(getApplicationContext(), "Immagine cliccata", Toast.LENGTH_SHORT).show();
            }
        };

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FilmAdapter(gestoreArrayFilm.getDefaultFilms(), this, listener, (ViewGroup) findViewById(android.R.id.content));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

            /*In caso ci fossero errori nella lettura rendo l'activity invisibile, ma prendendo il caso
                in cui l'utente non ha una connessione attiva, vede il msg d'errore, va nelle impostazioni
                attiva il WiFi e torna sull'app. Ora rientra nell"onResume" rende di nuovo visibile l'activity
                e legge il JSON correttamente.*/
        if(bigLayout.getVisibility() == View.INVISIBLE){
            bigLayout.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        spnOrdinamento.setSelection(0);

        String titolo = "", datiRegista = "";
        int annoUscita = 0;

            //Controllo se si tratta di uno dei due button ricerca/rimozione di film
        if(view.getId() == R.id.btnSearchFilm_Main || view.getId() == R.id.btnRemoveFilm_Main){
                //Controlla che almeno un dato sia stato inserito
            if(txtTitolo.getText().toString().length() > 0
                    || txtDatiRegista.getText().toString().length() > 0
                    || txtAnnoUscita.getText().toString().length() > 0) {

                if(txtTitolo.getText().toString().length() > 0){
                    titolo = txtTitolo.getText().toString().trim();
                }
                if(txtDatiRegista.getText().toString().length() > 0){
                    datiRegista = txtDatiRegista.getText().toString().trim();
                }
                if(txtAnnoUscita.getText().toString().length() > 0){
                    annoUscita = Integer.parseInt(txtAnnoUscita.getText().toString());
                }
            }else{
                gestoreArrayFilm.cancelArrayListRicerca();
                    //Nessun dato inserito, ricarica nell'adapter la lista default dei Film
                adapter.setFilms(gestoreArrayFilm.getDefaultFilms());
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "SCRIVI ALMENO UN DATO !", Toast.LENGTH_LONG).show();
            }
        }

        switch (view.getId()){
            case R.id.btnSearchFilm_Main:
                    gestoreArrayFilm.searchFilm(titolo, annoUscita, datiRegista);
                        //Controlla che non sia vuoto
                    if(gestoreArrayFilm.getArrayListFilmRicerca() != null && gestoreArrayFilm.getArrayListFilmRicerca().size() > 0){
                        adapter.setFilms(gestoreArrayFilm.getArrayListFilmRicerca());
                    }else{
                        Toast.makeText(getApplicationContext(), "NESSUNA CORRISPONDENZA TROVATA !", Toast.LENGTH_LONG).show();
                    }
                break;
            case R.id.btnRemoveFilm_Main:
                boolean ris = gestoreArrayFilm.removeFilm(titolo, annoUscita, datiRegista);
                    //Controlla che tutto sia andato a buon fine
                if(ris){
                    adapter.setFilms(gestoreArrayFilm.getDefaultFilms());
                    Toast.makeText(getApplicationContext(), "CORRISPONDENZA TROVATA\nED ELIMINATA !"
                            , Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "NESSUNA o PIU' DI UNA\nCORRISPONDENZA TROVATA !"
                            , Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * Metodo richiamato quando ha terminato la richiesta (la lettura) di tutti i film.
     */
    public void richiestaJsonCompletata(){
            /*Letto il file JSON controlla se qualcosa e' andato storto, errore o file
                json vuoto (quindi arraylist di Film con 0 elementi), e in caso all'adapter assegna null
                e nasconde tutto il contenuto dell'activity*/
        if(gestoreFile.getFlagErroreRichiesta()){
            Toast.makeText(getApplicationContext(),
                    "Errore nel tentativo di lettura!\nCONTROLLA LA TUA CONNESSIONE.", Toast.LENGTH_LONG).show();
            adapter = null;

            bigLayout.setVisibility(View.INVISIBLE);
        }else if(gestoreFile.getFlagErroreJson()){
            Toast.makeText(getApplicationContext(), "Errore durante la lettura!\nRIAVVIA L'APP.", Toast.LENGTH_LONG).show();
            adapter = null;

            bigLayout.setVisibility(View.INVISIBLE);
        }else{
            if(gestoreArrayFilm.getQtaFilms() == 0){
                Toast.makeText(getApplicationContext(), "NESSUN FILM REGISTRATO !", Toast.LENGTH_LONG).show();
                adapter = null;

                bigLayout.setVisibility(View.INVISIBLE);
            }else{
                adapter.notifyDataSetChanged();
            }
            if(adapter == null){ bigLayout.setVisibility(View.INVISIBLE); }


            /*Assegna l'ascoltatore allo spinner con gli ordinamenti. Se la voce selezionata e' "ORDINAMENTO DI DEFAULT"
                l'adapter dovra' caricare la lista di film di default, altrimenti fara' l'ordinamento e carichera'
                l'array ordinato nell'adapter*/
            spnOrdinamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    if(pos != 0){
                        flagDefaultArrayFilm = false;
                        gestoreArrayFilm.orderFilmBy(GestoreArrayFilm.ORDER_VALUES[pos]);
                        adapter.setFilms(gestoreArrayFilm.getOrdinatedFilms());
                    }else{
                        flagDefaultArrayFilm = true;

                        gestoreArrayFilm.cancelOrdinatedFilms();

                        adapter.setFilms(gestoreArrayFilm.getDefaultFilms());
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });


            //Assegna l'ascoltatore al button per la ricerca del film
            btnSearchFilm.setOnClickListener(MainActivity.this);
            btnRemoveFilm.setOnClickListener(MainActivity.this);
        }

    }
}
