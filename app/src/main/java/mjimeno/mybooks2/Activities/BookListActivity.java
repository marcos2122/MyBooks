package mjimeno.mybooks2.Activities;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.StringSignature;
import com.facebook.login.LoginManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orm.SugarDb;
import com.orm.SugarRecord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import mjimeno.mybooks2.Constants.AppConstant;
import mjimeno.mybooks2.Fragments.BookDetailFragment;
import mjimeno.mybooks2.Fragments.BookListFragment;
import mjimeno.mybooks2.Fragments.BookListFragmentFirebase;
import mjimeno.mybooks2.Fragments.BookListFragmentLocal;
import mjimeno.mybooks2.Fragments.PresentacionFragmentDetalle;
import mjimeno.mybooks2.Manifest;
import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;
import mjimeno.mybooks2.Red.ConnectivityChangeReceiver;
//import com.google.firebase.auth.AuthUI;
//import mjimeno.mybooks2.Models.BookItem;
//import mjimeno.mybooks2.dummy.DummyContent;

/**

 */
public class BookListActivity extends AppCompatActivity
        implements
        BookListFragment.EscuchaFragmento,
        NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener { // implementa la interfaz declarada en bookadapter

    public static boolean mTwoPane;
    private TextView usuario,email;
    private ImageView imagenUsuario;
    private ProgressBar progressBarFoto;
    private static final int GALLERY_INTENT = 1;
    private StorageReference mStorage;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;
    private ConnectivityChangeReceiver connectivityChangeReceiver = new ConnectivityChangeReceiver();





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     progressBarFoto.setVisibility(View.VISIBLE);
        //https://stackoverflow.com/questions/37374868/how-to-get-url-from-firebase-storage-getdownloadurl
        if (requestCode== GALLERY_INTENT && resultCode==RESULT_OK){
             final Uri uriImagenSeleccionada = data.getData();
          //obtengo una referencia donde se va almacenar la foto
           final StorageReference mStorageRef = mStorage.child("fotosUsuario").child(uriImagenSeleccionada.getLastPathSegment());
           //subir foto al storage firebase
            mStorageRef.putFile(uriImagenSeleccionada).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();

                }
                return mStorageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){

                        //la imagen se ha subido correctamente, entonces capturamos su url
                       final Uri downloadUri = task.getResult();
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUri)
                                .build();
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBarFoto.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.Foto_Actualizada),Toast.LENGTH_SHORT).show();
                                Uri photo = user.getPhotoUrl();
                                //imagenUsuario.setImageURI(photo);
/*
                                Glide.with(BookListActivity.this)
                                        .load(photo)
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .centerCrop()
                                        .into(imagenUsuario);

*/


                                Glide.with(getApplicationContext()).load(photo).asBitmap().centerCrop().fitCenter().into(new BitmapImageViewTarget(imagenUsuario) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        imagenUsuario.setImageDrawable(circularBitmapDrawable);
                                    }
                                });



                                }


                        });
                    }
                    else{
                        progressBarFoto.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.Foto_Error),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              //  .requestIdToken(getString(R.string.default_web_client_id)) // obtenemos token
                .requestEmail()
                .build();//configurar el inicio de sesión de Google para solicitar los datos de usuario requeridos por la aplicación


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build(); // mediante googleApiClient podemos tner acceso a las apis de google o sea la de autentificacion


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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //https://es.stackoverflow.com/questions/162173/como-modificar-el-actionbar-de-navigation-drawer-activity

        mStorage= FirebaseStorage.getInstance().getReference(); // obtener referencia al FirebaseStore

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view); //referenciamos el navigation View

        View view = navigationView.getHeaderView(0);
        usuario = (TextView)view.findViewById(R.id.TextViewUsuario);
        email = (TextView) view.findViewById(R.id.TextViewEmail);
        imagenUsuario= (ImageView) view.findViewById(R.id.imageViewUsuario);
        progressBarFoto = (ProgressBar) view.findViewById(R.id.progressBarFoto);
       // https://stackoverflow.com/questions/46372281/cannot-update-user-profile-specially-photo-in-firebase-android
        imagenUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),GALLERY_INTENT);
            }
        });


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null); // para que los iconos del navigation salgan sin sombra de fondo

        recuperarNotificaciones(); // metodo que gestiona las notificaciones

        recuperarDatosUsuario(); // metodo que recupera los datos de usuario

        SugarDb db = new SugarDb(this);
        db.onCreate(db.getDB()); //crear base de datos




        if  (findViewById(R.id.book_detail_container) != null) {

            if (savedInstanceState==null){
                getSupportFragmentManager()
                          .beginTransaction()
                            .replace(R.id.book_detail_container,  new PresentacionFragmentDetalle())
                // .addToBackStack(null)
                          .commit();
            }else  cargarFragmento(String.valueOf(0));
            mTwoPane = true;
             }

        else{
               mTwoPane = false;
               this.setTitle(R.string.app_name);
            }

        getSupportFragmentManager()
                .beginTransaction() // cargamos lista de libros
                .replace(R.id.book_list_container, BookListFragment.crear())
                // .addToBackStack(null)
                .commit();
        //Log.d("Reinicia","siAplicacion");
        //Toast.makeText(getApplicationContext(), "Se reinicia",Toast.LENGTH_LONG).show();




    }


   private void recuperarNotificaciones()
   {
       if (getIntent()!=null && getIntent().getAction()!=null)
       {
           if (getIntent().getAction().equalsIgnoreCase(AppConstant.DELETE_BOOK_ACTION))
           {
               String position = getIntent().getStringExtra("book_position");
               if (Book.existsbook(position)){ // metodo en la clase book que mira que exista en la lista
                   Book.borrarLibro(position); // metodo en la clase book que borra el libro

               }else{
                   Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_book)+" "+position,Toast.LENGTH_SHORT).show();
               }


           }else if (getIntent().getAction().equalsIgnoreCase(AppConstant.SHOW_DETAILS_BOOK)){
               String position = getIntent().getStringExtra("book_position");
               int count= Book.ITEMS.size();
               if (Integer.parseInt(position)<count) {
                   if (mTwoPane) { //si es una tablet cargamos el fragment en su contenedor correspondiente
                       cargarFragmento(position);
                   } else { // si es un movil con la clase intent abrimos la aplicación detalle y añadimos información sobre id al fragment
                       Intent intent = new Intent(this, BookDetailActivity.class);
                       intent.putExtra(BookDetailFragment.ARG_ITEM_ID, position);
                       startActivity(intent);
                   }
               }else {Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_book)+" "+position,Toast.LENGTH_SHORT).show();}


           }
       }

   }

    private void cargarFragmento(String id)
    {

        Bundle arguments = new Bundle(); //cargar fragmento detalle
        arguments.putString(BookDetailFragment.ARG_ITEM_ID, id);
        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.book_detail_container, fragment)
                // .addToBackStack(null)
                .commit();
    }

  private void signOut() {
        //FirebaseUI es una biblioteca creada a partir del SDK de Firebase Authentication que proporciona flujos directos de IU para usar en la app.
        //FirebaseUI proporciona el método para salir de Firebase Authentication
/*
      AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              startActivity(new Intent(getApplicationContext(), LoginActivity.class)
                      .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
              finish();
          }
      });

      */

       mAuth.signOut();
      LoginManager.getInstance().logOut();


      Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
          @Override
          public void onResult(@NonNull Status status) {
              if (status.isSuccess()){
                  Intent intent = new Intent(getApplicationContext(),LoginActivity.class)
                          //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                . addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                  startActivity(intent);
                  finish();
              }
          }
      });

  }



   private void recuperarDatosUsuario(){

        //Recuperar información de usuario usando los metodos de intancia de FirebaseUser
       mAuth=FirebaseAuth.getInstance();
       mAuthListener = new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               FirebaseUser user = mAuth.getCurrentUser();
               if (user != null)
               {
                   usuario.setText(user.getDisplayName());
                   email.setText((user.getEmail()));
                   Uri photo = user.getPhotoUrl();
                   if (photo!=null) {
                       Glide.with(getApplicationContext()).load(photo).asBitmap().centerCrop().fitCenter().into(new BitmapImageViewTarget(imagenUsuario) {
                           @Override
                           protected void setResource(Bitmap resource) {
                               RoundedBitmapDrawable circularBitmapDrawable =
                                       RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                               circularBitmapDrawable.setCircular(true);
                               imagenUsuario.setImageDrawable(circularBitmapDrawable);
                           }
                       });

                 /*
                    Glide.with(BookListActivity.this)
                            .load(photo)
                            .fitCenter()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)

                            .into(imagenUsuario);


*/
                   }
                   else
                   {
                       imagenUsuario.setImageResource(R.mipmap.icono_foto_usuario);
                   }

               }else {
                   volverLogin();
               }

           }
       };



    }

    private void volverLogin()
    {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendTextAndImageToAnyoneApp()
    {
        Uri iconoApp = Uri.parse("android.resource://" + getPackageName()
                + "/mipmap/" + "ic_launcher3"); // objeto uri con la imagen del icono de la app

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND); // action para enviar información

        shareIntent.putExtra(Intent.EXTRA_TEXT,getResources().getString(R.string.texto_descriptivo));// añadimos texto
        shareIntent.putExtra(Intent.EXTRA_STREAM,iconoApp); // añadimos imagen icono

        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent,getResources().getString(R.string.detalles_app)));
    }

    private void shareWithWhatsApp()  {

        //Actualmente Es posible enviar imágenes y texto a través de WhatsApp descargando la imagen al dispositivo de almacenamiento externo y
        // luego compartir la imagen en WhatsApp.

         Uri iconoApp = Uri.parse("android.resource://" + getPackageName()
                    + "/mipmap/" + "ic_launcher3"); // objeto uri con la imagen del icono de la app

        Bitmap bitmap = null;
        try { //convertir el uri a bitmap
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), iconoApp);
        } catch (IOException e) {
            e.printStackTrace();
        }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.texto_descriptivo));
            try {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //añadimos la imagen al dispositivo
                String path = MediaStore.Images.Media.insertImage(BookListActivity.this.getContentResolver(), bitmap, "", null);
                Uri uri = Uri.parse(path);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/*");
                intent.setPackage("com.whatsapp");
            }
            catch (SecurityException|NullPointerException ex){
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_almacenamiento),Toast.LENGTH_LONG).show();
                //En Android 6.0+, el usuario debe otorgar el permiso de almacenamiento a la aplicación

            }



        try {
            startActivity(intent);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_whatsapp),Toast.LENGTH_LONG).show(); //no esta instalado whatsapp
        }
    }


    private void copyToClipboard()
    {

        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE); // obtenemos una referencia de la clase clipBoardManager
        ClipData clipData = ClipData.newPlainText(getResources().getString(R.string.label_texto),getResources().getString(R.string.texto_descriptivo)); //añadimos una etiqueta y el texto al objeto Clipdata

        try{
            clipboardManager.setPrimaryClip(clipData);// añadimos al portapapeles el clipdata
            if (clipboardManager.hasPrimaryClip())
            {
                ClipData.Item item=clipboardManager.getPrimaryClip().getItemAt(0); // obtenemos el texto del portapapeles para luego mostrarlo
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.alerta)+item.getText().toString(),Toast.LENGTH_LONG).show();
                //mostramos alerta

            }

        }catch (NullPointerException ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_portapapeles),Toast.LENGTH_LONG).show();
        }


    }



    @Override
    public void alSeleccionarItem(String idLibro) {
        if (mTwoPane) { //si es una tablet cargamos el fragment en su contenedor correspondiente
            cargarFragmento(idLibro);
        } else { // si es un movil con la clase intent abrimos la aplicación detalle y añadimos información sobre id al fragment
           // cargarFragmento(idLibro);
            // Toast.makeText(getApplicationContext(),idLibro,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(BookDetailFragment.ARG_ITEM_ID,idLibro);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityChangeReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityChangeReceiver,intentFilter);



    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);


    }

    @Override
    protected void onStop() {
        super.onStop();

       if (mAuthListener!= null)mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //infla menu y añade los items al action bar
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //manejar los items del action bar
        int id = item.getItemId();
        if (id == R.id.action_closeSesion){
            AlertDialog alertDialog = new AlertDialog.Builder(BookListActivity.this).create();
            alertDialog.setTitle(getResources().getString(R.string.action_closeSesion));
            alertDialog.setMessage(getResources().getString(R.string.action_pregunta_closeSesion));

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.SI),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            signOut();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.NO),
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //manejar los clicks de los items del navigator view
        boolean fragmentTransaction = false;
        Fragment fragment = null;

        switch (item.getItemId()){
            case  R.id.nav_listarLibros:

                fragment = new BookListFragment();
                fragmentTransaction =true;
                break;

            case R.id.nav_listarLibrosBD:
                 fragment = new BookListFragmentLocal();
                 fragmentTransaction=true;

                break;

            case R.id.nav_compartir:

                sendTextAndImageToAnyoneApp();
                break;

            case R.id.nav_copiar:
                copyToClipboard();
                break;

            case  R.id.nav_whatsapp:

                shareWithWhatsApp();
                break;

            case  R.id.nav_send:
               // String token = FirebaseInstanceId.getInstance().getToken();
              //  Log.d("TOKEN" ,token);
                break;
        }

        if (fragmentTransaction){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_list_container,fragment)
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

