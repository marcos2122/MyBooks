package mjimeno.mybooks2.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import mjimeno.mybooks2.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextInputLayout contenedorEmail;
    private TextInputLayout contenedorPassword;
    private TextView actualizarPassword;
    private Button botonIniciarSesion;
    private Button botonRegistro;
    private LoginButton botonFace;
    private CallbackManager callbackManager;
    private SignInButton botonGoogle; // boton proporcionado por Google
    private ProgressBar progressBar;
    // declaramos un oyente para obtener una devolución de llamada cada vez que cambie el estado del token subyacente.
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 777;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // obtenemos token
                .requestEmail()
                .build();//configurar el inicio de sesión de Google para solicitar los datos de usuario requeridos por la aplicación


         googleApiClient = new GoogleApiClient.Builder(this)
                 .enableAutoManage(this,this)
                 .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                 .build(); // mediante googleApiClient podemos tner acceso a las apis de google o sea la de autentificacion



        contenedorEmail =(TextInputLayout)findViewById(R.id.textInputLayout);
        contenedorPassword=(TextInputLayout)findViewById(R.id.textInputLayout2);
        botonIniciarSesion=(Button) findViewById(R.id.buttonIniciarSesion);
        botonRegistro=(Button)findViewById(R.id.buttonRegistrar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        actualizarPassword = (TextView) findViewById(R.id.textViewActualizarPassword);
       // progressBar.setVisibility(View.GONE);


        botonGoogle =(SignInButton)findViewById(R.id.botonGoogle);
        try {
            ((TextView) botonGoogle.getChildAt(0)).setText(getResources().getString(R.string.TextoBotonGoogle));
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
        }


        botonFace = (LoginButton)findViewById(R.id.botonFacebook);
        callbackManager = CallbackManager.Factory.create(); // inicializar call manager

        botonFace.setReadPermissions(Arrays.asList("email","public_profile","user_friends"));
        botonFace.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
               manejarTokenAfirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.ErrorOnCancelFace),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                Log.i("ErrorFacebook",error.getMessage());
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.ErrorOnErrorFace),Toast.LENGTH_SHORT).show();

            }
        });


       botonGoogle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //hacemos un intent para conectarnos con las apis de google
               Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
               startActivityForResult(intent,SIGN_IN_CODE);
           }
       });


        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarForm_RegistrarUsuario();
            }
        });

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarForm_LoginUsuario();

            }
        });

        actualizarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCuadroDialogoPassw();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            //comprueba el estado de sesion, sea la sesion abierta o cerrada
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if (user != null){
                    abrirActivityMain();
                }else{
                    Log.d("SESION","sesión cerrada");

                }
            }
        };


    }

    private void abrirCuadroDialogoPassw() {

        final View view =getLayoutInflater().inflate(R.layout.dialogopassword,null);
        final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
                .setView(view)
                .setTitle(getResources().getString(R.string.RestablecerContrasenyaTitulo))
                .setMessage(getResources().getString(R.string.RestablecerContrasenyaMensaje))
                .setIcon(R.drawable.email_icon)
                .setPositiveButton(getResources().getString(R.string.ConfirmarDialog),null)
                .setNegativeButton(getResources().getString(R.string.CancelarDialog),null)
                .show();
        final TextInputLayout contenedorEmailDialog =(TextInputLayout)view.findViewById(R.id.textInputLayoutEmailDialog);
        Button ok = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"no se cierra",Toast.LENGTH_LONG).show();
                String correo = contenedorEmailDialog.getEditText().getText().toString().trim();
                if (!validarEmail(correo)){

                    contenedorEmailDialog.setErrorEnabled(true);
                    contenedorEmailDialog.setError(getResources().getString(R.string.ErrorEmail));


                }else{

                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(contenedorEmailDialog.getWindowToken(), 0);
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }
                    contenedorEmailDialog.setErrorEnabled(false);
                    restablecerPassword(correo); // llamamos metodo para restablecer contraseña


                }

            }
        });

    }

    private void restablecerPassword(final String email)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),getResources().
                                    getString(R.string.ProgresDialogConfirmacionEnvio)+" "+email,Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }else{

                            Toast.makeText(getApplicationContext(),getResources().
                                    getString(R.string.ProgresDialogCancelacionEnvio)+" "+email,Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

    }

    private void manejarTokenAfirebase(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.ErrorCredencialesFace),Toast.LENGTH_LONG).show();
                    }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


     // obtenemos resultado de autenticar

        if (requestCode ==SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            manejarResultado(result);
        }

    }

    private void manejarResultado(GoogleSignInResult result){
        if (result.isSuccess()){
            AutentificarFirebaseConGoogle(result.getSignInAccount()); // metodo que se encarga de la autenficacion con firebase, le mandamos la cuenta
            }
        else{
            Toast.makeText(this,getResources().getString(R.string.ErrorSesion),Toast.LENGTH_LONG).show();

        }
    }
    private void AutentificarFirebaseConGoogle(GoogleSignInAccount signInAccount){

        //creamos credencial y le proporcionamos el token que conseguimos del objeto cuenta
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);

        //ya tenemos la credencial ahora si podemos autenfiticarnos con firebase,adjuntamos un oyente que nos dira cuando esto termine
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) Toast.makeText(getApplicationContext(),"No se pudo auntenticar con firebase",Toast.LENGTH_LONG).show();
            }
        }) ;

    }

    @Override
    protected void onStart() {
        super.onStart(); //el oyente empieze a escuchar los cambios
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener!= null) mAuth.removeAuthStateListener(mAuthListener);

    }

    private void abrirActivityMain()
    {
        Intent intent = new Intent(getApplicationContext(),BookListActivity.class)
                //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void RegistrarUsuarioFirebase(String email, String password)
    {
        //metodo para registrar usuario segun el resultado de la tarea,abre otra actividad
        //en caso contrario capturo la excepción y muestro al usuario

        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()){
                          FirebaseUser user = mAuth.getCurrentUser();
                          Toast.makeText(getApplicationContext(),"El correo: "+ user.getEmail()+" se ha registrado correctamente",Toast.LENGTH_LONG).show();
                          abrirActivityMain();
                      }else{
                          Log.d("ERROR_REGISTRO",task.getException().getMessage());
                          Toast.makeText(getApplicationContext(),getResources().getString(R.string.ErrorRegistro),
                                  Toast.LENGTH_SHORT).show();
                      }
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });
    }

    private void AutenticarUsuarioFirebase(String email, String password){
        //metodo para autenticar usuario segun el resultado de la tarea,abre otra actividad
        //en caso contrario capturo la excepción y muestro al usuario
        progressBar.setVisibility(View.VISIBLE);
     FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(),"Sesion Iniciada por " + user.getEmail(),Toast.LENGTH_LONG).show();
                        abrirActivityMain();
                    }else{
                        Log.d("ERROR_SESION",task.getException().getMessage());
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.ErrorInicioSesion),Toast.LENGTH_LONG).show();
                    }
                     progressBar.setVisibility(View.INVISIBLE);

                 }
             });

    }

    private void ValidarForm_LoginUsuario() {
        //Metodo para validar el formulario Login
        String email = contenedorEmail.getEditText().getText().toString().trim();
        String passw = contenedorPassword.getEditText().getText().toString().trim();
        hideKeyboard();
        if (TextUtils.isEmpty(email)) {
            contenedorEmail.setError(getResources().getString(R.string.ErrorEmail2));
            contenedorEmail.getEditText().requestFocus();

        } else if (TextUtils.isEmpty(passw)){
            contenedorEmail.setErrorEnabled(false);
            contenedorPassword.setError(getResources().getString(R.string.ErrorPassw2));
            contenedorPassword.getEditText().requestFocus();
        }else{
            contenedorPassword.setErrorEnabled(false);
            contenedorEmail.setErrorEnabled(false);
            AutenticarUsuarioFirebase(email,passw); // llamo al metodo
            //Toast.makeText(getApplicationContext(),"Bien",Toast.LENGTH_LONG).show();
        }
    }
    private void ValidarForm_RegistrarUsuario() {
      //metodo para validar formulario Registro
        String email = contenedorEmail.getEditText().getText().toString().trim();
        String passw = contenedorPassword.getEditText().getText().toString().trim();
        hideKeyboard();
        if (!validarEmail(email)) {
            contenedorEmail.setError(getResources().getString(R.string.ErrorEmail));
            contenedorEmail.getEditText().requestFocus();
        } else if (!validarPassword(passw)) {
            contenedorEmail.setErrorEnabled(false);
            contenedorPassword.setError(getResources().getString(R.string.ErrorPassw));
            contenedorPassword.getEditText().requestFocus();
        } else {
            contenedorPassword.setErrorEnabled(false);
            contenedorEmail.setErrorEnabled(false);
            //Toast.makeText(getApplicationContext(),"Bien",Toast.LENGTH_LONG).show();
            RegistrarUsuarioFirebase(email,passw);

        }

    }
    private boolean validarEmail(String email) {
        //metodo para validar email utilizando la clase Pattern
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    private boolean validarPassword(String passw){
        // que la contaseña tenga mas de 6 caracteres
        return passw.length()>6;
    }
    private void hideKeyboard() {
       //  metodo para ocultar teclado
      try {
          View view = getCurrentFocus();
          if (view != null) {
              ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                      hideSoftInputFromWindow(view.getWindowToken(),0);
          }
      } catch(NullPointerException ex){
          ex.printStackTrace();

          }
      }






    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
