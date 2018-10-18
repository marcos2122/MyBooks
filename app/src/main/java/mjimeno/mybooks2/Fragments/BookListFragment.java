package mjimeno.mybooks2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import mjimeno.mybooks2.Adapters.BookAdapter;
import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;

import static mjimeno.mybooks2.Models.Book.ITEMS;
import static mjimeno.mybooks2.Models.Book.MAPA_ITEMS;

public class BookListFragment extends Fragment implements BookAdapter.OnItemClickListener {
    private EscuchaFragmento escucha;
    private static final String BOOK_REFERENCE="books";
    public BookListFragment(){} // constructor vacío
    public static BookListFragment crear(){
        return new BookListFragment();
    }
    BookAdapter adapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //detectarEstadoconexion();



        if (getArguments()!=null){
            //manejo de args
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_list_fragment, container, false);
        View recyclerView = v.findViewById(R.id.book_list);

        assert recyclerView != null;

        //  prepararLista((RecyclerView) recyclerView);//recibe la lista estática ModeloArticulo.ITEMS y al propio fragmento como escucha

        adapter = new BookAdapter(ITEMS, this);
        ((RecyclerView) recyclerView).setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(BOOK_REFERENCE);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ITEMS.clear();
                MAPA_ITEMS.clear();
                GenericTypeIndicator<List<Book.BookItem>> genericTypeIndicator = new GenericTypeIndicator<List<Book.BookItem>>() {
                };
                List<Book.BookItem> bookItems = dataSnapshot.getValue(genericTypeIndicator);
                // int clave = Integer.parseInt(dataSnapshot.getKey());
                // String clave = dataSnapshot.getKey();


                //  for (Book.BookItem libro : bookItems) {
                //       libro[0]
                //   }
                for (int i = 0; i < bookItems.size(); i++) {
                    //Log.i("Clave",bookItems.get(i))
                    bookItems.get(i).identificador = i;
                    ITEMS.add(bookItems.get(i));
                    MAPA_ITEMS.put(String.valueOf(i), bookItems.get(i));


                }
                //----
                /*

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                   Book.BookItem bookItem = snapshot.getValue(Book.BookItem.class);
                    bookItem.Identificador=Integer.parseInt(snapshot.getKey());
                    ITEMS.add(bookItem);
                    MAPA_ITEMS.put(String.valueOf(bookItem.Identificador),bookItem);
                }
                */
                //--
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("Errornase", databaseError.getMessage());

            }
        });

     return v;
    }



    private void detectarEstadoconexion()
    {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Toast.makeText(getContext(), "Conectado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "No Conectado", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

    }
    private void prepararLista(@NonNull RecyclerView recyclerView) {// le pasamos el adaptador al recycler view y recibe la lista estática Book.ITEMS y la propia activity como escucha
        recyclerView.setAdapter(new BookAdapter(ITEMS, this));
    }
    //private void preparar()


    @Override
    public void onAttach(Context context) {//comprobará que la actividad a la que se le ha añadido ese fragmento implementa el callback del fragmento
        super.onAttach(context);
        if (context instanceof EscuchaFragmento) {
            escucha = (EscuchaFragmento) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debes implementar EscuchaFragmento");
        }
    }
    @Override
    public void onDetach() {//desvincula el callback de la actividad:
        super.onDetach();
        escucha = null;
    }
    public void cargarDetalle(String idLibro)
    {
        if(escucha !=null) escucha.alSeleccionarItem(idLibro);
    }
        @Override
    public void onClick(BookAdapter.ViewHolder viewHolder, String idLibro) {
       cargarDetalle(idLibro);
    }
    //Solo es necesario que se ejecute el controlador EscuchaFragmento.alSeleccionarItem() dentro del controlador OnItemClickListener.onClick().
    // Esto propaga el click en cada ítem de la lista hacia la actividad.
    public interface EscuchaFragmento{
       void alSeleccionarItem(String idLibro);
    }
}
