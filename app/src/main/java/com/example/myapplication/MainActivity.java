package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //déclaration des  composantes
    Button btnval, btnqte;
    EditText ednom, edpsswd;
    CheckBox chkRememberMe;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mettre une interface xml sur l'écran R=ressources.layout.main
        MainActivity.this.setContentView(R.layout.activity_main);









//recuperation des composantes
        ednom = MainActivity.this.findViewById(R.id.ednom_auth);
        edpsswd = findViewById(R.id.edpsswd_auth);
        btnqte = findViewById(R.id.btnqte_auth);
        btnval = findViewById(R.id.btnval_auth);
        //Evenement

        btnqte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();//close the activity
            }
        });
        btnval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = ednom.getText().toString();
                String psswd = edpsswd.getText().toString();
                if (nom.equals("ameni") && psswd.equals("123")) {
                    //passage vers home activity
                    Intent i = new Intent(MainActivity.this, Home.class);
                    startActivity(i);
                    finish();//appel ->onstop ->ondestroy
                } else {
                    //err msg
                    Toast.makeText(MainActivity.this, "err de saisie", Toast.LENGTH_SHORT).show();
                }

            }
        });


        chkRememberMe = findViewById(R.id.chkRememberMe);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

// Vérifier si l'utilisateur est déjà connecté
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent i = new Intent(MainActivity.this, Home.class);
            startActivity(i);
            finish();
        }

// Gestion du bouton de connexion
        btnval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = ednom.getText().toString();
                String psswd = edpsswd.getText().toString();

                ContactManager cm = new ContactManager(MainActivity.this);
                cm.ouvrir();

                if (nom.equals("ameni") && psswd.equals("123")) {
                    // Enregistrer l'état de connexion si "Rester connecté" est coché
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", chkRememberMe.isChecked());
                    editor.putString("username", nom);
                    editor.apply();

                    // Passer à HomeActivity
                    Intent i = new Intent(MainActivity.this, Home.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Nom ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }

                cm.fermer();
            }
        });
    }



}