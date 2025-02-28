package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Affiche extends AppCompatActivity {
    RecyclerView rv;
    EditText edsearch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_affiche);

        rv = findViewById(R.id.rv_aff);
        edsearch = findViewById(R.id.edrech_aff);

        // Initialiser le RecyclerView avec une disposition horizontale
        LinearLayoutManager manager = new LinearLayoutManager(Affiche.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(manager);

        // Récupérer les contacts depuis la base de données
        ContactManager cm = new ContactManager(Affiche.this);
        cm.ouvrir();
        ArrayList<Contact> contacts = cm.getAllContacts();  // Récupérer les contacts depuis la base


        // Passer les données récupérées à l'adaptateur
        MyRecyclerContactAdapter adr = new MyRecyclerContactAdapter(Affiche.this, contacts);
        rv.setAdapter(adr);

        edsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtrer la liste des contacts en fonction du texte saisi
                filtrerContacts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        adr.data = contactsFiltres;  // Mettre à jour la liste dans l'adaptateur
        adr.notifyDataSetChanged();  // Rafraîchir l'affichage
    }
    ArrayList<Contact> contactsFiltres = new ArrayList<>();
    private void filtrerContacts(String texte) {


        // Vérifier si le texte de recherche est vide
        if (texte.isEmpty()) {
            contactsFiltres.addAll(Home.data); // Afficher tous les contacts
        } else {
            for (Contact c : Home.data) {
                // Comparer avec le nom, prénom ou numéro
                if (c.nom.toLowerCase().contains(texte.toLowerCase()) ||
                        c.prenom.toLowerCase().contains(texte.toLowerCase()) ||
                        c.num.contains(texte)) {
                    contactsFiltres.add(c);
                }
            }
        }

    }



}