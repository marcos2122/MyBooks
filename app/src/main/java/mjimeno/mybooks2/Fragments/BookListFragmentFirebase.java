package mjimeno.mybooks2.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import mjimeno.mybooks2.Activities.BookListActivity;
import mjimeno.mybooks2.Adapters.BookAdapter;
import mjimeno.mybooks2.Adapters.BookAdapterFirebase;
import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;
import mjimeno.mybooks2.Red.TestearRed;

public class BookListFragmentFirebase extends Fragment {
    //constructor vacio
    public BookListFragmentFirebase(){};

    // variables
    private static final String BOOK_REFERENCE="books";
    private RecyclerView recyclerViewBooks;
    private DatabaseReference mDatabase;
    private Query bookQuery;
    public FirebaseRecyclerAdapter adapter;
    private BookAdapter adapterLocal;
    private BookListActivity activity;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TestearRed.isNetworkConnected(getContext()))
        {
            Toast.makeText(getContext(),getResources().getString(R.string.Error_Red),Toast.LENGTH_LONG).show();

            ((BookListActivity) getActivity()).cargarFragmentoBBDD();

        }


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
                    .setQuery(bookQuery, Book.BookItem.class).build();// le pasamos la query y la clase
            adapter = new BookAdapterFirebase(bookOptions); // creamos ojeto adapter
            recyclerViewBooks.setAdapter(adapter);//asignamos al recicler el adapter


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //solo para la primera vez guardamos en la bbdd local
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null)
                {
                    Book.BookItem.deleteAll(Book.BookItem.class);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Book.BookItem bookItem = snapshot.getValue(Book.BookItem.class);
                        String key = snapshot.getKey();
                        bookItem.setIdentificador(Integer.parseInt(key));
                        bookItem.setId(Long.parseLong(key));
                        bookItem.save();
                        //ITEMS.add(bookItem);
                       // MAPA_ITEMS.put(key, bookItem);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               Log.i("Excepc2",databaseError.getMessage());
                Toast.makeText(getContext(),getResources().getString(R.string.Error_Servidor),Toast.LENGTH_LONG).show();
             //  recyclerViewBooks.removeAllViewsInLayout();
            //    getActivity().getSupportFragmentManager().beginTransaction()
             //           .replace(R.id.book_detail_container, new BookListFragmentLocal())
                        // .addToBackStack(null)
           //             .commit();
                ((BookListActivity) getActivity()).cargarFragmentoBBDD();



            }
        });




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

    public void onError()
    {
        Toast.makeText(getContext(),getResources().getString(R.string.Error_Servidor),Toast.LENGTH_LONG).show();
    }

}
