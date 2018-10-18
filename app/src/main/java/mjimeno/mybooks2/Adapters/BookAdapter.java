package mjimeno.mybooks2.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>
{

    private  final List<Book.BookItem> mValues; // variable contenedora de la lista de objetos bookItem
    private  OnItemClickListener escuchaClicksExterna; // declaracion de variable tipo OnClickListener,referencia a nuestra interface

    //constructor, le pasamos la lista
    public BookAdapter(List<Book.BookItem> items, OnItemClickListener escuchaClicksExterna){
        mValues = items;
        this.escuchaClicksExterna = escuchaClicksExterna;
    }
   // Necesitamos hacer esto porque cuando nuestro Fragment implementará esta interface,
   // debemos decirle a onItemClickListener que este Fragment ha implementado la interface.

    @Override
   // infla el layout (archivo xml) que representa a nuestros elementos, y devuelve una instancia de la clase ViewHolder
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        int pos = viewType;//obtiene posicion de la vista
        // le sumo uno porque empieza de 0 al ser tipo vector, para ir acorde con la numeración de los libros
        //prefiero que empieze por uno
        if (par_Impar(pos)) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_par, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_impar, parent, false);
        }
        return new ViewHolder(view);
    }
    private boolean par_Impar(int num)
    { // metodo para saber si es par o impar
        boolean par = false;
        if (num%2==0) par=true;
        else par=false;
        return par;
    }

    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder holder, int position) {
        //enlaza nuestro datos con cada ViewHolder
        holder.mIdView.setText(String.valueOf(mValues.get(position).Identificador));

        holder.mTitulo.setText(mValues.get(position).title);
        holder.mAutor.setText(mValues.get(position).author);

    }

    @Override
    public int getItemViewType(int position){
        return position; }

    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size() > 0 ? mValues.size() : 0;
        } else {
            return 0;
        }
    }
/*
    El método obtenerIdLibro() retorna en el atributo idLibro de cada objeto Libro asociado a los ítems de la lista.
    Este permite enviar por el controlador onClick() el identificador para determinar el detalle de cada libro.
*/
    private String obtenerIdLibro(int posicion) {
        if (posicion != RecyclerView.NO_POSITION) {
           //return String.valueOf(getItemViewType(posicion));

            return String.valueOf(mValues.get(posicion).Identificador);
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Obtener referencias de los componentes visuales para cada elemento
        // Es decir, referencias de los TextViews
        final TextView mIdView;
        final TextView mTitulo;
        final TextView mAutor;



        public ViewHolder(View view)
        {
            super(view);
            view.setClickable(true);
            mIdView = (TextView) view.findViewById(R.id.textViewIdentificador);

            mTitulo = (TextView) view.findViewById(R.id.textViewTitulo);
            mAutor =(TextView) view.findViewById(R.id.textViewAutor);


            view.setOnClickListener(this);// en la clase RecyclerViewHolder, se necesita registrar su oyente
        }

        @Override
        public void onClick(View v) {
            escuchaClicksExterna.onClick(this,obtenerIdLibro(getAdapterPosition()));
        }
    }
         //es una interfaz que transmite los eventos de click al actividad contenedora del recycler view en este caso BookListActivity
        public interface OnItemClickListener {
           void onClick(ViewHolder viewHolder, String idLibro);
    }



}
