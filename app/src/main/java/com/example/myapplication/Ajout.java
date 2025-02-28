package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Ajout extends AppCompatActivity {
    Button btnValider, btnAnnuler;
    EditText ednom, edprenom, ednumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ajout);
        ednom = findViewById(R.id.ednom_ajout);
        edprenom = findViewById(R.id.edprenom_ajout);
        ednumero = findViewById(R.id.ednum_ajout);
        btnValider = findViewById(R.id.btnval_ajout);
        btnAnnuler = findViewById(R.id.btnann_ajout);


        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nom = ednom.getText().toString();
                String prenom = edprenom.getText().toString();
                String num = ednumero.getText().toString();

                Contact c = new Contact(num, nom, prenom);

                //****************************
                Toast.makeText(Ajout.this, "Validation avec succes ", Toast.LENGTH_SHORT).show();
//partie base
                ContactManager cm = new ContactManager(Ajout.this);
                cm.ouvrir();
                cm.ajout(num, nom, prenom);
                Home.data = cm.getAllContacts();
                //cm.fermer();
                //passage vers home activity
                Intent i =new Intent(Ajout.this,Home.class);
                startActivity(i);
                Toast.makeText(Ajout.this, "Validation avec succes : user "+ednom.getText()+" "+edprenom.getText()+" "+ednumero.getText(), Toast.LENGTH_SHORT).show();


            }
        });
        btnAnnuler.setOnClickListener(v -> {
            Intent i = new Intent(Ajout.this, Home.class);  // Créer un Intent pour Home
            startActivity(i);  // Démarrer Home
            finish();  // Fermer l'activité actuelle (Ajout)
            Toast.makeText(Ajout.this, "Ajout annulé", Toast.LENGTH_SHORT).show();
        });

    }
}