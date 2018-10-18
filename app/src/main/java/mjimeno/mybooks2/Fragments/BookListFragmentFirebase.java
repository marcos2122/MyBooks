package mjimeno.mybooks2.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import mjimeno.mybooks2.Adapters.BookAdapterFirebase;
import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;

public class BookListFragmentFirebase extends Fragment {
    //constructor vacio
    public BookListFragmentFirebase(){};
    // variables
    private static final String BOOK_REFERENCE="books";
    private RecyclerView recyclerViewBooks;
    private DatabaseReference mDatabase;
    private Query bookQuery;
    private FirebaseRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtenemos la referencia al nodo books
        mDatabase= FirebaseDatabase.getInstance().getReference(BOOK_REFERENCE);
        bookQuery = mDatabase.orderByKey();//consulta ordenada por la KEY


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_list_fragment,container,false);
        recyclerViewBooks =(RecyclerView)view.findViewById(R.id.book_list);
        assert recyclerViewBooks != null;
        recyclerViewBooks.hasFixedSize();
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        // Nesitamos de la clase FirebaseRecyclerOptions para pasarle al constructor de FirebaseRecyclerAdapter
        FirebaseRecyclerOptions bookOptions = new FirebaseRecyclerOptions.Builder<Book.BookItem>()
                .setQuery(bookQuery,Book.BookItem.class).build();// le pasamos la query y la clase
        adapter= new BookAdapterFirebase(bookOptions); // creamos ojeto adapter
        recyclerViewBooks.setAdapter(adapter);//asignamos al recicler el adapter

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening(); //método para empezar a escuchar los cambios
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening(); // elimina el detector de eventos
    }

    public static class BooksHolder extends RecyclerView.ViewHolder{
        View mView;
        /*ViewHolder personalizado que gestione los datos de los libtos
        implementamos un método set para cada uno de nuestros datos de la clase.
         Cada uno de estos métodos recuperarán una referencia a su control correspondiente del layout que hemos creado antes para los elementos de la lista y
         asignarán su contenido a partir del dato recibido como parámetro.
         */
        public BooksHolder(View itemView) {
            super(itemView); // constructor
            mView=itemView;
        }
        public void setPosition(int pos){
            TextView textViewPos = (TextView) mView.findViewById(R.id.textViewIdentificador);
            textViewPos.setText(String.valueOf(pos));
        }
        public void setTitle(String title){
            TextView textViewTitle = (TextView) mView.findViewById(R.id.textViewTitulo);
            textViewTitle.setText(title);
        }

        public void setAuthor(String author){
            TextView textViewAuthor = (TextView) mView.findViewById(R.id.textViewAutor);
            textViewAuthor.setText(author);
        }
    }
}
