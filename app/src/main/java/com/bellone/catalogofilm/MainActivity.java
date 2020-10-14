package com.bellone.catalogofilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

        /*In questa app c'e' solo un permesso da richiedere, ma comunque ho bisogno di un array di stringhe*/
    public static final String[] PERMISSIONS = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE };
    public static final int REQUEST_CODE = 1;

    private boolean permissionRequestDone;
    private boolean readExternalStoragePermission;

    private ConstraintLayout bigLayout = null;
    private ListView listViewFilm = null;
    private Spinner spnOrdinamento = null;
    private Button btnSearchFilm = null;
    private Button btnRemoveFilm = null;
    private EditText txtTitolo = null;
    private EditText txtAnnoUscita = null;
    private EditText txtDatiRegista = null;
    private static TextView lblItemSelected = null;

    private GestoreFile gestoreFile = null;
    private GestoreArrayFilm gestoreArrayFilm = null;
    private PersonalizedArrayAdapter adapter = null;

    public static void setLblItemSelectedValue(String value) { lblItemSelected.setText(value); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestoreArrayFilm = new GestoreArrayFilm();
        gestoreFile = new GestoreFile(gestoreArrayFilm, "all_film_json.txt");


        bigLayout = findViewById(R.id.bigLayout_Main);
        listViewFilm = findViewById(R.id.listViewFilm_Main);
        spnOrdinamento = findViewById(R.id.spnOrdinamento_Main);
        btnSearchFilm = findViewById(R.id.btnSearchFilm_Main);
        btnRemoveFilm = findViewById(R.id.btnRemoveFilm_Main);
        txtTitolo = findViewById(R.id.txtTitoloFilm_Main);
        txtAnnoUscita = findViewById(R.id.txtAnnoUscita_Main);
        txtDatiRegista = findViewById(R.id.txtDatiRegista_Main);
        lblItemSelected = findViewById(R.id.lblItemSelected_Main);


        ArrayList<String> spnValues = new ArrayList<>();
            /*Rimepo lo spinner degli ordinamenti con i valori possibili nella classe GestoreArrayFilm*/
        for(String value: GestoreArrayFilm.ORDER_VALUES){ spnValues.add(value); }
        spnOrdinamento.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spnValues));


        permissionRequestDone = false;
        readExternalStoragePermission = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            /*In questa app, essendoci un solo permesso da richiedere, non servirebbe controllare il REQUEST_CODE*/
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readExternalStoragePermission = true;
            }else{ readExternalStoragePermission = false; }
        }
        permissionRequestDone = true;
        ascoltatoriOnResume();
    }

    @Override
    protected void onResume() {
        super.onResume();

            /*Se, da quando l'app e' stata avviata, non ha ancora richiesto i permessi
                e il permesso non e' stato concesso, chiede il permesso di leggere nella memoria del telefono*/
        if(!permissionRequestDone) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    PERMISSIONS[0]) == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        PERMISSIONS, REQUEST_CODE);

            }else{ readExternalStoragePermission = true; ascoltatoriOnResume(); }
        }

    }

    /**
     * Ho dovuto fare questo metodo perche' alla richiesta dei permessi, se tutto il corpo di questo metodo lo lasciavo
     *  nell'onResume, lui eseguiva gia' questo codice, e quindi anche se davi "consenti" finche' non riavviavi l'app
     *  lui continuava a vedere readExternalStoragePermission == false
     */
    private void ascoltatoriOnResume(){
            /*Se il permesso non e' stato concesso l'app nasconde tutto e segnala semplicemente l'utente con un toast*/
        if(readExternalStoragePermission){
                /*Leggi il file JSON e aggiunge i vari Film all'array, se qualcosa va storto, errore o file
                    json vuoto (quindi arraylist di Film con 0 elementi), all'adapter assegna null e nasconde tutto*/
            boolean flag = gestoreFile.readFilms();
            if(!flag){
                Toast.makeText(getApplicationContext(), "SI E' VERIFICATO UN ERRORE.", Toast.LENGTH_LONG).show();
                adapter = null;
            }else{
                if(gestoreArrayFilm.getQtaFilms() == 0){
                    Toast.makeText(getApplicationContext(), "NESSUN FILM REGISTRATO !", Toast.LENGTH_LONG).show();
                    adapter = null;
                }else{
                    adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                            , gestoreArrayFilm.getDefaultFilms());
                }
            }
            if(adapter != null){ listViewFilm.setAdapter(adapter);
            }else{ bigLayout.setVisibility(View.INVISIBLE); }


                /*Assegna l'ascoltatore allo spinner con gli ordinamenti. Se la voce selezionata e' "ORDINAMENTO DI DEFAULT"
                    l'adapter dovra' caricare la lista di film di default, altrimenti fare il giusto ordinamento e caricare
                    l'array ordinato nell'adapter*/
            spnOrdinamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i != 0){
                        gestoreArrayFilm.orderFilmBy(GestoreArrayFilm.ORDER_VALUES[i]);
                        adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                                , gestoreArrayFilm.getOrdinatedFilms());
                    }else{
                        adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                                , gestoreArrayFilm.getDefaultFilms());
                    }
                    listViewFilm.setAdapter(adapter);
                    listViewFilm.setSelection(0);       //seleziona il primo elemeno, cosi' da vedere la lista dei film dall'inizio
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });


            /*Assegna l'ascoltatore al button per la ricerca del film*/
            btnSearchFilm.setOnClickListener(this);
            btnRemoveFilm.setOnClickListener(this);
        }else{
            bigLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "MI SERVE IL PERMESSO PER LEGGERE\nI FILE DEL TELEFONO !"
                    , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
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
                //Nessun dato inserito, ricarica nell'adapter la lista default dei Film
                adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                        , gestoreArrayFilm.getDefaultFilms());
                Toast.makeText(getApplicationContext(), "SCRIVI ALMENO UN DATO !", Toast.LENGTH_LONG).show();
            }
        }

        switch (view.getId()){
            case R.id.btnSearchFilm_Main:
                    ArrayList<Film> newArrayListFilm = gestoreArrayFilm.searchFilm(titolo, annoUscita, datiRegista);
                        //Controlla che non sia vuoto
                    if(newArrayListFilm != null){
                        adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                                , newArrayListFilm);
                    }else{
                        Toast.makeText(getApplicationContext(), "NESSUNA CORRISPONDENZA TROVATA !", Toast.LENGTH_LONG).show();
                    }
                break;
            case R.id.btnRemoveFilm_Main:
                boolean ris = gestoreArrayFilm.removeFilm(titolo, annoUscita, datiRegista);
                    //Controlla che tutto sia andato a buon fine
                if(ris){
                    adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                            , gestoreArrayFilm.getDefaultFilms());
                    Toast.makeText(getApplicationContext(), "CORRISPONDENZA TROVATA\nED ELIMINATA !"
                            , Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "NESSUNA O PIU' DI UNA\nCORRISPONDENZA TROVATA !"
                            , Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
            //Controllo se si tratta di uno dei due button ricerca/rimozione di film
        if(view.getId() == R.id.btnSearchFilm_Main || view.getId() == R.id.btnRemoveFilm_Main){
            listViewFilm.setAdapter(adapter);
            listViewFilm.setSelection(0);
        }
    }
}
