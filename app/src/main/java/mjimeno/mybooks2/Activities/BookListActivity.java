package mjimeno.mybooks2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import mjimeno.mybooks2.Fragments.BookDetailFragment;
import mjimeno.mybooks2.Fragments.BookListFragment;
import mjimeno.mybooks2.R;

//import com.google.firebase.auth.AuthUI;
//import mjimeno.mybooks2.Models.BookItem;
//import mjimeno.mybooks2.dummy.DummyContent;

/**

 */
public class BookListActivity extends AppCompatActivity implements BookListFragment.EscuchaFragmento { // implementa la interfaz declarada en bookadapter

    private boolean mTwoPane;
    Button boton;


    @Override
    public void onBackPressed() {

        //codigo adicional

       // super.onBackPressed();
        int count = getFragmentManager().getBackStackEntryCount();
        Log.d("CUANTOS",String.valueOf(count));

       if (count == 0) {
           // super.onBackPressed();
            this.finish();}
            //additional code
     //   } else {
     //       Toast.makeText(getApplicationContext(),count,Toast.LENGTH_LONG).show();
     //       getFragmentManager().popBackStack();
     //   }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        boton = (Button)findViewById(R.id.button2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if  (findViewById(R.id.book_detail_container) != null) {
             //Este layout container estará presente solo si es una tablet,establecemos un valor
             // boleano a true que indica que se ejecuta la app en tablet

             mTwoPane = true;
            //cargamos el fragmento detalle , el primero de la lista
            //cargarFragmento(String.valueOf(Book.ITEMS.get(0).Identificador));
            cargarFragmento(String.valueOf(0));

        }
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"hola",Toast.LENGTH_LONG).show();
              /*
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName("Marc Jimeno")
                       // .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("USER", "User profile updated.");
                                }
                            }
                        });
                */
                signOut();
                //FirebaseAuth.getInstance().signOut();
            }
        });
        // agregar fragmento de lista
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.book_list_container, BookListFragment.crear())
               // .addToBackStack(null)
                .commit();
       // View recyclerView = findViewById(R.id.book_list);
       // assert recyclerView != null;
      //  prepararLista((RecyclerView) recyclerView);//recibe la lista estática ModeloArticulo.ITEMS y al propio fragmento como escucha
    }
   // private void prepararLista(@NonNull RecyclerView recyclerView){// le pasamos el adaptador al recycler view y recibe la lista estática Book.ITEMS y la propia activity como escucha
   //     recyclerView.setAdapter(new BookAdapter(Book.ITEMS,this));

  //  }

  private void signOut() {

      AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              startActivity(new Intent(getApplicationContext(), LoginActivity.class));
              finish();
          }
      });
  }
   private void cargarFragmento(String id)
   {
       Bundle arguments = new Bundle();
       arguments.putString(BookDetailFragment.ARG_ITEM_ID, id);
       BookDetailFragment fragment = new BookDetailFragment();
       fragment.setArguments(arguments);
       getSupportFragmentManager().beginTransaction()
               .replace(R.id.book_detail_container, fragment)
               .addToBackStack(null)
               .commit();
   }
   /*
    @Override
    public void onClick(BookAdapter.ViewHolder viewHolder, String id) {

       if (mTwoPane) { //si es una tablet cargamos el fragment en su contenedor correspondiente
            cargarFragmento(id);
        } else { // si es un movil con la clase intent abrimos la aplicación detalle y añadimos información sobre id al fragment

           // Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(BookDetailFragment.ARG_ITEM_ID,id);
            startActivity(intent);
        }


    }
*/
    @Override
    public void alSeleccionarItem(String idLibro) {
        if (mTwoPane) { //si es una tablet cargamos el fragment en su contenedor correspondiente
            cargarFragmento(idLibro);
        } else { // si es un movil con la clase intent abrimos la aplicación detalle y añadimos información sobre id al fragment

            // Toast.makeText(getApplicationContext(),idLibro,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(BookDetailFragment.ARG_ITEM_ID,idLibro);
            startActivity(intent);
        }
    }
}



        /*  Otra manera de hacerlo implementando el adaptador del Recycler view pero en la misma activity, he preferido hacer una clase BookAdapter

         // y ponerla en otro package, la he comentado ..


        View recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    } //inicializar y usar el adaptador al llamar al constructor del adaptador y al método setAdapter de RecyclerView

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
       Toast.makeText(getApplicationContext(),"Vuelvo a reciclar",Toast.LENGTH_LONG).show();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, Book.ITEMS, mTwoPane));
   }



/*
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final BookListActivity mParentActivity;
        private final List<BookItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Podemos usar setTag() y getTag() para establecer y obtener objetos personalizados según nuestro requisito. El método setTag() toma un argumento del tipo Object y getTag() devuelve un Object
              //
                Book.BookItem item= ( Book.BookItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(BookDetailFragment.ARG_ITEM_ID, String.valueOf(item.Identificador));
                    BookDetailFragment fragment = new BookDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.book_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    intent.putExtra(BookDetailFragment.ARG_ITEM_ID, String.valueOf(item.Identificador));

                    context.startActivity(intent);
                }
            }
        };
        private boolean par_Impar(int num)
        { // metodo para saber si es par o impar
            boolean par= false;
            if (num%2==0) par=true;
            else par=false;
            return par;
        }

        //creamos constructor al adaptador para que pueda manejar los datos que muestra el RecyclerView
        SimpleItemRecyclerViewAdapter(BookListActivity parent,
                                      List<BookItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //infla el layout (archivo xml) que representa a nuestros elementos,
            // y devuelve una instancia de la clase ViewHolder
            View view;
            int pos = viewType;
            Log.i(TAG, "vista"+( pos+1));

            if (par_Impar(pos+1)) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_list_par, parent, false);

            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.book_list_impar, parent, false);
            }
            return new ViewHolder(view);
        }



        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //enlaza nuestro datos con cada ViewHolder
            holder.mIdView.setText(String.valueOf(mValues.get(position).Identificador));
            holder.mId2View.setText(String.valueOf(mValues.get(position).Identificador));
            holder.mTitulo.setText(mValues.get(position).Titulo);
            holder.mAutor.setText(mValues.get(position).Autor);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }
        @Override
       public int getItemViewType(int position){
            return position; }
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            // Obtener referencias de los componentes visuales para cada elemento
            // Es decir, referencias de los TextViews
            final TextView mIdView;
            final TextView mTitulo;
            final TextView mAutor;
            final TextView mId2View;


            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.textViewIdentificador);
                mId2View = (TextView) view.findViewById(R.id.textViewIdentificador2) ;
                mTitulo = (TextView) view.findViewById(R.id.textViewTitulo);
                mAutor =(TextView) view.findViewById(R.id.textViewAutor);


            }
        }
    }
    */

