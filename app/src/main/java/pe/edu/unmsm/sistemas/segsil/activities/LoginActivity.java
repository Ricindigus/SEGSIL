package pe.edu.unmsm.sistemas.segsil.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import pe.edu.unmsm.sistemas.segsil.R;

public class LoginActivity extends AppCompatActivity {
    private Button btnIngresar;
    private TextInputEditText edtUsuario;
    private TextInputEditText edtPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private final String TAG = "FIREBASE AUTENTICACION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnIngresar = (Button) findViewById(R.id.login_btnIngresar);
        edtPassword = (TextInputEditText) findViewById(R.id.login_edtPassword);
        edtUsuario = (TextInputEditText) findViewById(R.id.login_edtUsuario);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtUsuario.getText().toString().trim().equals("") && !edtPassword.getText().toString().trim().equals("")){
                    iniciarSesion(edtUsuario.getText().toString(),edtPassword.getText().toString());
                }else{
                    Toast.makeText(LoginActivity.this, "DEBE INGRESAR LOS DATOS SOLICITADOS", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void iniciarSesion(String usuario, String password){
        firebaseAuth.signInWithEmailAndPassword(usuario+"@sistemas.edu.pe",password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            sesionActiva(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "USUARIO O CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();
                            sesionActiva(null);
                        }
                    }
                });
    }

    @SuppressLint("NewApi")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Está seguro que desea SALIR de la aplicación?")
                    .setTitle("Aviso")
                    .setCancelable(false)
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .setPositiveButton("Sí",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finishAffinity();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sesionActiva(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(LoginActivity.this, BienvenidoActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        sesionActiva(currentUser);
    }
}
