package com.bellone.catalogofilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    public static final String[] PERMISSIONS = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE };
    public static final int REQUEST_CODE = 1;

    private boolean permissionRequestDone;
    private boolean readExternalStoragePermission;

    private ListView listViewFilm = null;
    private Spinner spnOrdinamento = null;
    private Button btnSearchFilm = null;
    private EditText txtTitolo = null;
    private EditText txtAnnoUscita = null;
    private EditText txtDatiRegista = null;
    private static TextView lblItemSelected = null;

    private GestoreFile gestoreFile = null;
    private GestoreArrayFilm gestoreArrayFilm = null;
    private PersonalizedArrayAdapter adapter = null;

    private static int listViewPositionSelected;
    public static void setLblItemSelectedValue(String value) {
        lblItemSelected.setText(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestoreArrayFilm = new GestoreArrayFilm();
        gestoreFile = new GestoreFile(gestoreArrayFilm, "all_film_json.txt");


        listViewFilm = findViewById(R.id.listViewFilm_Main);
        spnOrdinamento = findViewById(R.id.spnOrdinamento_Main);
        btnSearchFilm = findViewById(R.id.btnSearchFilm_Main);
        txtTitolo = findViewById(R.id.txtTitoloFilm_Main);
        txtAnnoUscita = findViewById(R.id.txtAnnoUscita_Main);
        txtDatiRegista = findViewById(R.id.txtDatiRegista_Main);
        lblItemSelected = findViewById(R.id.lblItemSelected_Main);


        ArrayList<String> spnValues = new ArrayList<>();
        for(String value: GestoreArrayFilm.ORDER_VALUES){ spnValues.add(value); }
        spnOrdinamento.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spnValues));


        permissionRequestDone = false;
        readExternalStoragePermission = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readExternalStoragePermission = true;
            }else{ readExternalStoragePermission = false; }
        }
        permissionRequestDone = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!permissionRequestDone) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    PERMISSIONS[0]) == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        PERMISSIONS, REQUEST_CODE);

            }else{ readExternalStoragePermission = true; }
            permissionRequestDone = true;
        }

        if(readExternalStoragePermission){
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
            if(adapter != null){ listViewFilm.setAdapter(adapter); }



            spnOrdinamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i != 0){
                        gestoreArrayFilm.orderFilmBy(GestoreArrayFilm.ORDER_VALUES[i]);
                        adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                                , gestoreArrayFilm.getOrdinatedFilms());
                        listViewFilm.setAdapter(adapter);
                        listViewFilm.setSelection(0);
                    }else{
                        adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                                , gestoreArrayFilm.getDefaultFilms());
                        listViewFilm.setAdapter(adapter);
                        listViewFilm.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            btnSearchFilm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(txtTitolo.getText().toString().length() > 0
                            || txtDatiRegista.getText().toString().length() > 0
                            || txtAnnoUscita.getText().toString().length() > 0){

                        String titolo = "";
                        if(txtTitolo.getText().toString().length() > 0){
                            titolo = txtTitolo.getText().toString().trim();
                        }
                        String datiRegista = "";
                        if(txtDatiRegista.getText().toString().length() > 0){
                            datiRegista = txtDatiRegista.getText().toString().trim();
                        }

                        int annoUscita = 0;
                        if(txtAnnoUscita.getText().toString().length() > 0){
                            annoUscita = Integer.parseInt(txtAnnoUscita.getText().toString());
                        }


                        ArrayList<Film> newArrayListFilm = gestoreArrayFilm.searchFilm(titolo, annoUscita, datiRegista);


                        if(newArrayListFilm != null){
                            adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                                    , newArrayListFilm);
                            listViewFilm.setAdapter(adapter);
                        }else{
                            Toast.makeText(getApplicationContext(), "NESSUNA CORRISPONDENZA TROVATA !", Toast.LENGTH_LONG).show();
                        }
                        listViewFilm.setSelection(0);
                    }else{
                        adapter = new PersonalizedArrayAdapter(getApplicationContext(), R.layout.film_layout
                                , gestoreArrayFilm.getDefaultFilms());
                        listViewFilm.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "SCRIVI ALMENO UN DATO !", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "MI SERVE IL PERMESSO PER LEGGERE\nI FILE DEL TELEFONO !"
                    , Toast.LENGTH_LONG).show();
        }
    }
}
