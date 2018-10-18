package mjimeno.mybooks2.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;

public class BookAdapterFirebase extends FirebaseRecyclerAdapter<Book.BookItem,BookAdapterFirebase.BooksHolder> {
/*
FirebaseUI tiene una clase genérica, llamada FirebaseRecyclerAdapter, que podemos utilizar con nuestro ViewHolder personalizado que acabamos de crear y
 la clase java que utilicemos para encapsular la información de cada elemento de la lista
*/
//Constructor
    public BookAdapterFirebase(@NonNull FirebaseRecyclerOptions<Book.BookItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BooksHolder holder, int position, @NonNull Book.BookItem model) {
        holder.setIdentificador(position);
        holder.setAuthor(model.getAuthor());
        holder.setTitle(model.getTitle());

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




