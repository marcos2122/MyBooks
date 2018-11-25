package mjimeno.mybooks2.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;



public class BookDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";//constante
    private Book.BookItem mItem; // declara una variable de tipo bookItem,libro al cual está ligado la UI
    private WebView myWebview;
    private FloatingActionButton fab;
    private ImageView imagenLibro;
    private TextView cabeceraAutor,autor,cabeceraFecha,fecha,cabeceraDescripcion,descripcion;

    //constructor
    public BookDetailFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    try {
        if (getArguments().containsKey(ARG_ITEM_ID)) { //mira en los argumentos si contiene la clave de el libro
            // Carga el modelo según el identificador
            mItem = Book.MAPA_ITEMS.get(getArguments().getString(ARG_ITEM_ID));// obtenemos el objeto por su clave

            //Activity activity = this.getActivity();
            // CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
           // if (appBarLayout != null) appBarLayout.setTitle(mItem.Titulo);
            if (savedInstanceState != null) {
                getActivity().setTitle(mItem.title); // pone el titulo del libro aunque haya un giro de pantalla
            } else {
                getActivity().setTitle(mItem.title);
            }
        }
    }catch (NullPointerException ex){ex.printStackTrace();}


        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Con el itemLibro se cargan los datos del libro en onCreateView() y setearlos en cada view
        View rootView = inflater.inflate(R.layout.book_detail_fragment, container, false);

        imagenLibro = (ImageView)rootView.findViewById(R.id.imageViewFotoLibro);
        cabeceraAutor = (TextView)rootView.findViewById(R.id.textViewTituloCabecera3);
        autor = (TextView)rootView.findViewById(R.id.textViewAutor);
        cabeceraFecha = (TextView)rootView.findViewById(R.id.textViewFechaPublicacionCabecera);
        fecha = (TextView)rootView.findViewById(R.id.textViewFecha);
        cabeceraDescripcion = (TextView)rootView.findViewById(R.id.textViewDescripcionCabecera);
        descripcion = (TextView)rootView.findViewById(R.id.textViewDescripcion);

        myWebview = (WebView)rootView.findViewById(R.id.myWebView);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getContext(),"hola",Toast.LENGTH_LONG).show();

                myWebview.setVisibility(View.VISIBLE);
                ocultarVistas();
                myWebview.getSettings().setJavaScriptEnabled(true);
                myWebview.loadUrl("file:///android_asset/form.html");
                myWebview.setWebViewClient(new MyWebClient());

            }
        });

        if (mItem != null) {
          autor.setText(mItem.author);
          fecha.setText(mItem.publication_date);
          descripcion.setText(mItem.description);
            Glide.with(this)
                    .load(mItem.url_image)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(imagenLibro);
            // Libreria Glide para la carga de imágenes desde urls con caching integrado
        }

        return rootView;
    }

    private void mostrarVistas(){
        cabeceraAutor.setVisibility(View.VISIBLE);
        autor.setVisibility(View.VISIBLE);
        cabeceraFecha.setVisibility(View.VISIBLE);
        fecha.setVisibility(View.VISIBLE);
        cabeceraDescripcion.setVisibility(View.VISIBLE);
        descripcion.setVisibility(View.VISIBLE);
        imagenLibro.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }
    private void ocultarVistas()
    {
        cabeceraAutor.setVisibility(View.GONE);
        autor.setVisibility(View.GONE);
        cabeceraFecha.setVisibility(View.GONE);
        fecha.setVisibility(View.GONE);
        cabeceraDescripcion.setVisibility(View.GONE);
        descripcion.setVisibility(View.GONE);
        imagenLibro.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
    }
    private class MyWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            Log.i("URL",request.getUrl().toString());

            Uri uri = Uri.parse(request.getUrl().toString());
            //Obtenemos los valores de las parametros de la url
            String name = uri.getQueryParameter("name");
            String num = uri.getQueryParameter("num");
            String date = uri.getQueryParameter("date");


            if (!name.isEmpty() && !num.isEmpty() && !date.isEmpty())
            {
                Toast.makeText(getContext(),getResources().getString(R.string.compra_correcta),Toast.LENGTH_LONG).show();
                myWebview.setVisibility(View.GONE);
                mostrarVistas();
            }
            else{
                Toast.makeText(getContext(),getResources().getString(R.string.compra_faltandatos),Toast.LENGTH_LONG).show();

            }

            return  false;
        }


    }
}
