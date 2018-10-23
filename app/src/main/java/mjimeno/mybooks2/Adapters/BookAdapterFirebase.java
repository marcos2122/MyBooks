package mjimeno.mybooks2.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;

import static mjimeno.mybooks2.Models.Book.ITEMS;
import static mjimeno.mybooks2.Models.Book.MAPA_ITEMS;

public class BookAdapterFirebase extends FirebaseRecyclerAdapter<Book.BookItem,BookAdapterFirebase.BooksHolder> {
/*
FirebaseUI tiene una clase genérica, llamada FirebaseRecyclerAdapter, que podemos utilizar con nuestro ViewHolder personalizado que acabamos de crear y
 la clase java que utilicemos para encapsular la información de cada elemento de la lista
*/
private FirebaseRecyclerOptions<Book.BookItem> options;




    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    //Constructor
    public BookAdapterFirebase(@NonNull FirebaseRecyclerOptions<Book.BookItem> options) {
        super(options);

    }

    @Override
    public void onError(@NonNull DatabaseError error) {
        super.onError(error);
      //  Toast.makeText(,"Adapter"+error.getMessage(),Toast.LENGTH_LONG).show();
        Log.i("Excepci",error.getMessage());



    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex);
        //https://github.com/firebase/FirebaseUI-Android/blob/master/database/src/main/java/com/firebase/ui/database/FirebaseIndexArray.java

        if (snapshot!=null)

        {

       // long list = Book.BookItem.count(Book.BookItem.class);

          List<Book.BookItem> n = Book.BookItem.listAll((Book.BookItem.class));
          int list = n.size(); // total de registros de la base de datos

          switch (type) {

               case CHANGED:
                   if(list>0) {
                       Book.BookItem LibrosBdLocal = Book.BookItem.findById(Book.BookItem.class, n.get(Integer.parseInt(snapshot.getKey())).getId());
                       Book.BookItem libroFirebase = snapshot.getValue(Book.BookItem.class);

                       LibrosBdLocal.setAuthor(libroFirebase.getAuthor());
                       LibrosBdLocal.setTitle(libroFirebase.getTitle());
                       LibrosBdLocal.setDescription(libroFirebase.getDescription());
                       LibrosBdLocal.setUrl_image(libroFirebase.getUrl_image());
                       LibrosBdLocal.setIdentificador(Integer.parseInt(snapshot.getKey()));
                       LibrosBdLocal.setPublication_date(libroFirebase.getPublication_date());
                       LibrosBdLocal.setId(libroFirebase.getId());

                       LibrosBdLocal.save();
                   }
                   break;

               case ADDED:
                   //Guardamos registro en la base local, en caso de que los registros de firebase sean mayores o iguales que la lista
                 //de nuestra base local, en caso de que no pues no aañadimos ninguno a la bd local,asi aseguramos la actualización de la base de datos local

                 if(newIndex>=list) {
                     Book.BookItem bookItemAdded = snapshot.getValue(Book.BookItem.class);
                     String key = snapshot.getKey();
                     bookItemAdded.setIdentificador(Integer.parseInt(key));
                     bookItemAdded.setId(Long.parseLong(key));
                     bookItemAdded.save();

                     // List<Book.BookItem> notes =
                   //  Book.BookItem.findWithQuery(Book.BookItem.class,
                   //  "UPDATE  Books SET identificador = "+Integer.parseInt(key)+ " WHERE author = "+ "'"+autor+"'");
                 }

               break;


               case REMOVED:
                   //borramos registro de la base de datos local
                   Book.BookItem LibrosBdLocal = Book.BookItem.findById(Book.BookItem.class, n.get(Integer.parseInt(snapshot.getKey())).getId());
                   LibrosBdLocal.delete();
                  break;

               case MOVED:
                   break;
           }
       }
    }


    @Override
    protected void onBindViewHolder(@NonNull BooksHolder holder, final int position, @NonNull final Book.BookItem model) {


        holder.setIdentificador(position);
        holder.setAuthor(model.getAuthor());
        holder.setTitle(model.getTitle());
        ITEMS.add(model);
        MAPA_ITEMS.put(String.valueOf(position),model);
        int n = ITEMS.size();



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //por hacer
                Book.BookItem m = MAPA_ITEMS.get(String.valueOf(position));
                Toast.makeText(v.getContext(),String.valueOf(model.author),Toast.LENGTH_LONG).show();




            }

        });

    }


    @NonNull
    @Override
    public BookAdapterFirebase.BooksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        int pos = viewType;//obtiene posicion de la vista

        if (esPar(pos)) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_par, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_impar, parent, false);
        }

        return new BookAdapterFirebase.BooksHolder(view);
    }

    private boolean esPar(int num)
    { // metodo para saber si es par o impar
        boolean par = false;
        if (num%2==0) par=true;
        return par;
    }

    @Override
    public int getItemViewType(int position){
        return position; }




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
       public void setIdentificador(int pos){
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




