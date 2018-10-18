package mjimeno.mybooks2.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import mjimeno.mybooks2.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout contenedorEmail;
    private TextInputLayout contenedorPassword;
    private Button botonIniciarSesion;
    private Button botonRegistro;
    private ProgressBar progressBar;
    // declaramos un oyente para obtener una devolución de llamada cada vez que cambie el estado del token subyacente.
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contenedorEmail =(TextInputLayout)findViewById(R.id.textInputLayout);
        contenedorPassword=(TextInputLayout)findViewById(R.id.textInputLayout2);
        botonIniciarSesion=(Button) findViewById(R.id.buttonIniciarSesion);
        botonRegistro=(Button)findViewById(R.id.buttonRegistrar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

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
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            //comprueba el estado de sesion, sea la sesion abierta o cerrada
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(getApplicationContext(),BookListActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d("SESION","sesión cerrada");

                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart(); //chequea el estado del usuario
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);

    }

    private void RegistrarUsuarioFirebase(String email,String password)
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
                          Toast.makeText(getApplicationContext(),"Usuario: "+ user.getEmail()+"registrado correctamente",Toast.LENGTH_LONG).show();
                          Intent intent = new Intent(getApplicationContext(),BookListActivity.class);
                          startActivity(intent);
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
                        Intent intent = new Intent(getApplicationContext(),BookListActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
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
        } else if (TextUtils.isEmpty(passw)){
            contenedorEmail.setErrorEnabled(false);
            contenedorPassword.setError(getResources().getString(R.string.ErrorPassw2));
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
        } else if (!validarPassword(passw)) {
            contenedorEmail.setErrorEnabled(false);
            contenedorPassword.setError(getResources().getString(R.string.ErrorPassw));
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
        // metodo para ocultar teclado
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
