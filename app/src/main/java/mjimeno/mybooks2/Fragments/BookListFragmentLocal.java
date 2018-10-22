package mjimeno.mybooks2.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mjimeno.mybooks2.Adapters.BookAdapterLocal;
import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;

public class BookListFragmentLocal extends Fragment {
    public BookListFragmentLocal(){}

    private RecyclerView recyclerView;
    private BookAdapterLocal adapterLocal;
    private List<Book.BookItem> libros = new ArrayList<>();
    private Long cuentaInicial;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_list_fragment_local,container,false);
        // configurar recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.book_list_local);
        assert recyclerView != null;
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //miramos la cantidad de registros en la bbdd
        cuentaInicial = Book.BookItem.count(Book.BookItem.class);

        if (cuentaInicial>=0)
        {
            libros = Book.BookItem.listAll(Book.BookItem.class);
            adapterLocal = new BookAdapterLocal(getContext(),libros);
            recyclerView.setAdapter(adapterLocal);

            if (libros.isEmpty())
                Snackbar.make(recyclerView,"No hay libros",Snackbar.LENGTH_LONG).show();
        }

        return view;
    }





}
