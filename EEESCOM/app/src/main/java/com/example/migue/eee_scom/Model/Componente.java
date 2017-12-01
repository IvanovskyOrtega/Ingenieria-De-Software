package com.example.migue.eee_scom.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivanovsky on 11/30/2017.
 */


public class Componente {

    public String nombreComponente;
    public String descripcion;

    public Componente(String nombreComponente, String descripcion){
        this.nombreComponente = nombreComponente;
        this.descripcion = descripcion;
    }
}
