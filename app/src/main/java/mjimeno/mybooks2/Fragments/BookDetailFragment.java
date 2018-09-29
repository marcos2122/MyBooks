package mjimeno.mybooks2.Fragments;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import mjimeno.mybooks2.Activities.BookDetailActivity;
import mjimeno.mybooks2.Activities.BookListActivity;
import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;



public class BookDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";//constante
    private Book.BookItem mItem; // declara una variable de tipo bookItem,libro al cual está ligado la UI

    //constructor
    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments().containsKey(ARG_ITEM_ID)) { //mira en los argumentos si contiene la clave de el libro
            // Carga el modelo según el identificador
            mItem = Book.MAPA_ITEMS.get(getArguments().getString(ARG_ITEM_ID));// obtenemos el objeto por su clave

           //Activity activity = this.getActivity();
          // CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

          if (savedInstanceState != null) getActivity().setTitle(mItem.Titulo); // pone el titulo del libro aunque haya un giro de pantalla
          else getActivity().setTitle(mItem.Titulo);
          //  if (appBarLayout != null) appBarLayout.setTitle(mItem.Titulo);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Con el itemLibro se cargan los datos del libro en onCreateView() y setearlos en cada view
        View rootView = inflater.inflate(R.layout.book_detalle, container, false);

        if (mItem != null) {
          ((TextView) rootView.findViewById(R.id.textViewAutor)).setText(mItem.Autor);
            ((TextView) rootView.findViewById(R.id.textViewFecha)).setText(mItem.FechaPublicacion);
            ((TextView) rootView.findViewById(R.id.textViewDescripcion)).setText(mItem.Descripcion);
            Glide.with(this)
                    .load(mItem.URL_Imagen)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into((ImageView) rootView.findViewById(R.id.imageViewFotoLibro));
            // Libreria Glide para la carga de imágenes desde urls con caching integrado
        }

        return rootView;
    }
}
