package com.example.migue.eee_scom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.migue.eee_scom.Model.Componente;
import com.example.migue.eee_scom.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    MaterialEditText edtNuevoNombreDeUsuario,edtNuevoPassword,edtNuevoEmail; // Registro
    MaterialEditText edtNombreDeUsuario,edtPassword; // Login

    Button iniciarSesion_button,registro_button;

    FirebaseDatabase baseDeDatos;
    DatabaseReference usuarios;
    //DatabaseReference componentes;
    //DatabaseReference componentesRef;

    boolean registroExitoso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Firebase

        baseDeDatos = FirebaseDatabase.getInstance();
        usuarios = baseDeDatos.getReference("Usuarios");

        /**
         * En esta parte llenamos la base
         * **/

        /*componentes = baseDeDatos.getReference("Componentes");
        componentesRef = componentes.getRef();
        Map<String, Componente> componentes = new HashMap<String, Componente>();
        componentes.put("lm555", new Componente("LM555","El temporizador IC 555 es un circuito integrado (chip) que se utiliza en la generación de temporizadores, pulsos y oscilaciones. El 555 puede ser utilizado para proporcionar retardos de tiempo, como un oscilador, y como un circuito integrado flip flop."));
        componentes.put("resistencia", new Componente("Resistencia", "Las resistencias restringen o limitan el flujo de la corriente eléctrica"));
        componentes.put("capacitor",new Componente("Capacitor","Un capacitor es un dispositivo para almacenar energía potencial eléctrica, carga eléctrica, y por ende campo eléctrico."));
        componentes.put("lm741",new Componente("LM741","El LM741 es un amplificador operacional monolítico de altas características. Se ha diseñado para una amplia gama de aplicaciones analógicas. Un alto rango de voltaje en modo común y ausencia de lacth-up tienden a hacer el LM741 ideal para usarlo como un seguidor de tensión. "));
        componentes.put("diodos1n400x",new Componente("Diodo","Un diodo es un componente electrónico de dos terminales que permite la circulación de la corriente eléctrica a través de él en un solo sentido"));
        componentes.put("transistor",new Componente("Transistor","El transistor es un dispositivo electrónico semiconductor utilizado para entregar una señal de salida en respuesta a una señal de entrada. Cumple funciones de amplificador, oscilador, conmutador o rectificador. "));
        componentesRef.setValue(componentes);*/
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
                            Intent start = new Intent(Login.this, Principal.class);
                            start.putExtra("param",login);
                            login = null;
                            startActivity(start);
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
                String emailPattern = "(\\W|^)[\\w.+\\-]*@(gmail|hotmail|outlook|yahoo)\\.com(\\W|$)";
                Pattern pattern = Pattern.compile(emailPattern);
                Matcher matcher = pattern.matcher(usuario.getEmail());
                if(matcher.matches()){
                    String usernamePattern = "^[A-Z|a-z|0-9]{4,15}$";
                    pattern = Pattern.compile(usernamePattern);
                    matcher = pattern.matcher(usuario.getNombreDeUsuario());
                    if(matcher.matches()){
                        String passwordPattern = "^[0-9a-zA-Z]{5,}$";
                        pattern = Pattern.compile(passwordPattern);
                        matcher = pattern.matcher(usuario.getPassword());
                        if(matcher.matches()){
                            usuarios.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(usuario.getPassword().isEmpty() || usuario.getEmail().isEmpty() || usuario.getEmail().isEmpty()){
                                        Toast.makeText(Login.this,"Por favor llena todos los campos",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        if(dataSnapshot.child(usuario.getNombreDeUsuario()).exists()){
                                            Toast.makeText(Login.this,"El nombre de usuario que ingresó " +
                                                    "ya existe :(",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            usuarios.child(usuario.getNombreDeUsuario()).setValue(usuario);
                                            Toast.makeText(Login.this,"Se registró el nuevo" +
                                                    " usuario exitosoamente :)",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            Toast.makeText(Login.this,"La contraseña debe tener una" +
                                            "longitud mayor a 5 y solo debe contener letras y números"
                                    ,Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(Login.this,"El nombre de usuario solo debe " +
                                        "contener letras y números de una longitud de 5 a 15 caracteres"
                                ,Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(Login.this,"Por favor ingresa un email válido",
                            Toast.LENGTH_LONG).show();
                }
                dialogInterface.dismiss();
            }
        });

        mensajeDeAlerta.show();
    }
}
