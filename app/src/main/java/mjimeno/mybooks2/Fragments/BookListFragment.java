package mjimeno.mybooks2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import mjimeno.mybooks2.Activities.BookListActivity;
import mjimeno.mybooks2.Adapters.BookAdapter;
import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;
import mjimeno.mybooks2.Red.TestearRed;

import static mjimeno.mybooks2.Activities.BookListActivity.filtro;
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
    RecyclerView recyclerViewLibros;
    private DatabaseReference mDatabase;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //detectarEstadoconexion();



        if (getArguments()!=null ){
            //manejo de args}
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.book_list_fragment, container, false);

        recyclerViewLibros = vista.findViewById(R.id.book_list);
        assert recyclerViewLibros != null;
        recyclerViewLibros.hasFixedSize();
        recyclerViewLibros.setLayoutManager(new LinearLayoutManager(getContext()));

        if (!TestearRed.isNetworkConnected(getContext()))
        {
            //Toast.makeText(getContext(),getResources().getString(R.string.Error_Red),Toast.LENGTH_LONG).show();
            //((BookListActivity) getActivity()).cargarFragmentoBBDD();
            cargarDatosBD_Local();

        }
        else {

            CargarDatosFirebase(); // cargamos el metodo donde estan todos los oyentes firebase

        }

     return vista;
    }


    private void CargarDatosFirebase(){
       mDatabase= FirebaseDatabase.getInstance().getReference(BOOK_REFERENCE);
        // obtenemos referencia al nodo


        adapter = new BookAdapter(ITEMS, this);
        recyclerViewLibros.setAdapter(adapter);

        mDatabase.addChildEventListener(new ChildEventListener() {

            List<Book.BookItem> num =  Book.getBooks(); // metodo que retorna la lista de libros de la bd local
            int list = num.size(); // total de registros de la base de datos


            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // utilizamos una query para saber si el libro existe en la base local,sino existe se añade a la base de datos local
              /*
                List<Book.BookItem> libros = Book.BookItem.findWithQuery(Book.BookItem.class, "Select * from Books where id = " + Long.parseLong(dataSnapshot.getKey()));
                int total = libros.size();
                if (total == 0)
                {
                    Book.BookItem bookItemAdded = dataSnapshot.getValue(Book.BookItem.class);
                    String llave = dataSnapshot.getKey();
                    bookItemAdded.setIdentificador(Integer.parseInt(llave));
                    bookItemAdded.setId(Long.parseLong(llave));
                    bookItemAdded.save();
                }
                */


              Book.BookItem libro = dataSnapshot.getValue(Book.BookItem.class);

              if (!Book.exists(libro,dataSnapshot.getKey())){ // llamamos al metodo si no existe libro

                  String llave = dataSnapshot.getKey();
                  libro.setIdentificador(Integer.parseInt(llave));
                  libro.setId(Long.parseLong(llave));
                  libro.save();

              }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(list>0) { // buscamos el objeto a actualizar y lo actualizamos
                    Book.BookItem LibrosBdLocal = Book.BookItem.findById(Book.BookItem.class, num.get(Integer.parseInt(dataSnapshot.getKey())).getId());
                    Book.BookItem libroFirebase = dataSnapshot.getValue(Book.BookItem.class);

                    LibrosBdLocal.setAuthor(libroFirebase.getAuthor());
                    LibrosBdLocal.setTitle(libroFirebase.getTitle());
                    LibrosBdLocal.setDescription(libroFirebase.getDescription());
                    LibrosBdLocal.setUrl_image(libroFirebase.getUrl_image());
                    LibrosBdLocal.setIdentificador(Integer.parseInt(dataSnapshot.getKey()));
                    LibrosBdLocal.setPublication_date(libroFirebase.getPublication_date());
                    LibrosBdLocal.setId(libroFirebase.getId());

                    LibrosBdLocal.save();
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
               // buscamos el objeto y lo borramos localmente
                Book.BookItem LibrosBdLocal = Book.BookItem.findById(Book.BookItem.class, num.get(Integer.parseInt(dataSnapshot.getKey())).getId());
                LibrosBdLocal.delete();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ITEMS.clear();
                MAPA_ITEMS.clear();
                GenericTypeIndicator<List<Book.BookItem>> genericTypeIndicator = new GenericTypeIndicator<List<Book.BookItem>>() {
                };
                List<Book.BookItem> bookItems = dataSnapshot.getValue(genericTypeIndicator);

                // for (Book.BookItem libro : bookItems) {

                //    }

                for (int i = 0; i < bookItems.size(); i++) {
                    //Log.i("Clave",bookItems.get(i))
                    bookItems.get(i).identificador = i;
                    ITEMS.add(bookItems.get(i));
                    MAPA_ITEMS.put(String.valueOf(i), bookItems.get(i));


                }
                //----
                /* otra manera de hacerlo---

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
                Log.i("Error", databaseError.getMessage());
                //   Toast.makeText(getContext(),getResources().getString(R.string.Error_Servidor),Toast.LENGTH_LONG).show();
                // ((BookListActivity) getActivity()).cargarFragmentoBBDD(); // llamamos a un metodo de la activity que cargara los datos de la bd local

                cargarDatosBD_Local(); // cargamos los datos de la bd local


            }
        });
    }
    private void cargarDatosBD_Local(){
        // le pasamos al adaptador los datos de la base de datos local
        ITEMS.clear();
        MAPA_ITEMS.clear();
        List<Book.BookItem> n = Book.BookItem.listAll((Book.BookItem.class));
        for (Book.BookItem bookItem:n){
            ITEMS.add(bookItem);
            MAPA_ITEMS.put(String.valueOf(bookItem.getIdentificador()), bookItem);
        }

        adapter = new BookAdapter(ITEMS,this);
        recyclerViewLibros.setAdapter(adapter);

        if (!TestearRed.isNetworkConnected(getContext())) Snackbar.make(recyclerViewLibros,getResources().getString(R.string.Error_Red),Snackbar.LENGTH_LONG).show();
        else Snackbar.make(recyclerViewLibros,getResources().getString(R.string.Error_Servidor),Snackbar.LENGTH_LONG).show();
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
   // private void prepararLista(@NonNull RecyclerView recyclerView) {// le pasamos el adaptador al recycler view y recibe la lista estática Book.ITEMS y la propia activity como escucha
   //     recyclerView.setAdapter(new BookAdapter(ITEMS, this));
   // }
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
