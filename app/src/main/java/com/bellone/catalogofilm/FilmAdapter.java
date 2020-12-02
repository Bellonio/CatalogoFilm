package com.bellone.catalogofilm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmHolder> {

    private ArrayList<Film> films = null;
    private Context context;
    private ViewGroup viewGroup = null;
    private OnItemClickListener listener = null;

    public interface OnItemClickListener{
        void onItemClick(int pos);
        void onImgClick(int pos);
    }

    public static class FilmHolder extends RecyclerView.ViewHolder{
        public ImageView imgCopertina = null;
        public TextView lblTitolo = null;
        public TextView lblAnnoUscita = null;
        public TextView lblGeneri = null;
        public TextView lblDurata = null;

        public FilmHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            imgCopertina = itemView.findViewById(R.id.imgCopertina_Film);
            lblTitolo = itemView.findViewById(R.id.lblTitolo_Film);
            lblAnnoUscita = itemView.findViewById(R.id.lblAnnoUscita_Film);
            lblGeneri = itemView.findViewById(R.id.lblGeneri_Film);
            lblDurata = itemView.findViewById(R.id.lblDurata_Film);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){    //bho se ad esempio si clicca su un elemento appena eliminato
                            listener.onItemClick(pos);
                        }
                    }
                }
            });

            imgCopertina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){    //bho se ad esempio si clicca su un elemento appena eliminato
                            listener.onImgClick(pos);
                        }
                    }
                }
            });
        }
    }

    public FilmAdapter(ArrayList<Film> films, Context context, OnItemClickListener listener, ViewGroup viewGroup) {
        this.films = films;
        this.context = context;
        this.listener = listener;
        this.viewGroup = viewGroup;
    }

    public void setFilms(ArrayList<Film> films) {
        this.films = films;
        onCreateViewHolder(viewGroup, 0);
    }


    @NonNull
    @Override
    public FilmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_layout, parent, false);
        return new FilmHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmHolder holder, int position) {
        Film current_film = films.get(position);

        String str_da_assegnare, contenutoArrayList_string;

        Picasso.with(context).load(current_film.getUrl_locandina()).into(holder.imgCopertina);

        str_da_assegnare = context.getString(R.string.lblTitolo) + " " + current_film.getTitolo();
        holder.lblTitolo.setText(str_da_assegnare);

        str_da_assegnare = context.getString(R.string.lblAnnoUscita) + " "
                + current_film.getAnno_di_uscita();
        holder.lblAnnoUscita.setText(str_da_assegnare);


        contenutoArrayList_string = context.getString(R.string.lblGeneri) + " " + trasformaArray_inStringa(current_film.getGeneri());
        holder.lblGeneri.setText(contenutoArrayList_string);


        int durata_int = current_film.getDurata();
        //Trasformo la durata in minuti in una durata piu' leggibbile per un umano (ore : minuti)
        String durata_str = durata_int/60 +"h : "
                +durata_int%60+"min";
        str_da_assegnare = context.getString(R.string.lblDurata) + "\n" + durata_str;
        holder.lblDurata.setText(str_da_assegnare);
    }

    @Override
    public int getItemCount() {
        return films.size();
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
