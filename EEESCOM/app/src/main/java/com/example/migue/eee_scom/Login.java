package com.example.migue.eee_scom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.migue.eee_scom.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity {

    MaterialEditText edtNuevoNombreDeUsuario,edtNuevoPassword,edtNuevoEmail; // Registro
    MaterialEditText edtNombreDeUsuario,edtPassword; // Login

    Button iniciarSesion_button,registro_button;

    FirebaseDatabase baseDeDatos;
    DatabaseReference usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Firebase

        baseDeDatos = FirebaseDatabase.getInstance();
        usuarios = baseDeDatos.getReference("Usuarios");
        edtNombreDeUsuario = (MaterialEditText)findViewById(R.id.edtNombreDeUsuario);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        iniciarSesion_button = (Button)findViewById(R.id.iniciarSesion_button);
        registro_button = (Button)findViewById(R.id.registrar_button);

        registro_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mostrarMensajeRegistro();
            }
        });

        iniciarSesion_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iniciarSesion(edtNombreDeUsuario.getText().toString(),edtPassword.getText().toString());
            }
        });
    }

    private void iniciarSesion(final String usuario, final String password) {
        usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(usuario).exists()){
                    if(!usuario.isEmpty()){
                        Usuario login = dataSnapshot.child(usuario).getValue(Usuario.class);
                        if(login.getPassword().equals(password)){
                            Toast.makeText(Login.this,"Se inició sesión exitosamente :)",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this,Principal.class));
                        }
                        else{
                            Toast.makeText(Login.this,"Contraseña incorrecta :(",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(Login.this,"Por favor ingresa tu nombre de usuario",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Login.this,"El usuario ingresado no existe :(",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mostrarMensajeRegistro() {
        AlertDialog.Builder mensajeDeAlerta = new AlertDialog.Builder(Login.this);
        mensajeDeAlerta.setTitle("Registrar");
        mensajeDeAlerta.setMessage("Por favor llene los campos requeridos.");

        LayoutInflater inflater = this.getLayoutInflater();
        View registro = inflater.inflate(R.layout.registro,null);

        edtNuevoNombreDeUsuario = (MaterialEditText)registro.findViewById(R.id.edtNuevoNombreDeUsuario);
        edtNuevoPassword = (MaterialEditText)registro.findViewById(R.id.edtNuevoPassword);
        edtNuevoEmail = (MaterialEditText)registro.findViewById(R.id.edtNuevoEmail);

        mensajeDeAlerta.setView(registro);
        mensajeDeAlerta.setIcon(R.drawable.ic_account_circle_black_24dp);

        mensajeDeAlerta.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mensajeDeAlerta.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Usuario usuario = new Usuario(
                        edtNuevoNombreDeUsuario.getText().toString(),
                        edtNuevoPassword.getText().toString(),
                        edtNuevoEmail.getText().toString());
                usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(usuario.getPassword().isEmpty() || usuario.getEmail().isEmpty() || usuario.getEmail().isEmpty()){
                            Toast.makeText(Login.this,"Por favor llena todos los campos :v",Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(dataSnapshot.child(usuario.getNombreDeUsuario()).exists()){
                                Toast.makeText(Login.this,"El nombre de usuario que ingresó " +
                                        "ya existe :(",Toast.LENGTH_LONG).show();
                            }
                            else{
                                usuarios.child(usuario.getNombreDeUsuario()).setValue(usuario);
                                Toast.makeText(Login.this,"Se registró el nuevo" +
                                        "usuario exitosoamente :)",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });

        mensajeDeAlerta.show();
    }
}

