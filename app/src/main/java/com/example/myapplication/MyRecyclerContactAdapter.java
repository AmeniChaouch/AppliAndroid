package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class MyRecyclerContactAdapter extends RecyclerView.Adapter<MyRecyclerContactAdapter.MyViewHolder> {

    Context con;
    ArrayList<Contact> data;
MyViewHolder vh ;
    public MyRecyclerContactAdapter(Context con, ArrayList<Contact> data) {
        this.con = con;
        this.data = data;
    }

    @Override
    @NonNull
    public MyRecyclerContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(con);
        View v = inf.inflate(R.layout.view_contact, parent, false); // ← Ajout du paramètre parent pour respecter les bonnes pratiques
        return new MyViewHolder(v); // ← Créer une nouvelle instance au lieu de retourner `vh` non initialisé
    }


    @Override
    public void onBindViewHolder(@NonNull MyRecyclerContactAdapter.MyViewHolder holder, int position) {

        Contact c = data.get(position);
        holder.tvnom.setText(c.nom);
        holder.tvprenom.setText(c.prenom);
        holder.tvnum.setText(c.num);
        //set checked true or false if admis
        // ajout de linterface if call ou non totlob direct
        // splash screen 1 seconde nbaddal licon  manifest doc intent filter la mettre en activity 
        //restez connecte case a coche shared preference var connected false ki ycheki twalli true

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDelete,imgEdit,imgCall;
        TextView tvnom,tvprenom,tvnum;
        public MyViewHolder(@NonNull View v) {
            super(v);
             tvnom = v.findViewById(R.id.tvnom_contact);
            tvprenom = v.findViewById(R.id.tvprenom_contact);
            tvnum = v.findViewById(R.id.tvnum_contact);

            //recuperation des btn
             imgDelete = v.findViewById(R.id.imageViewDelete_contact);
             imgEdit = v.findViewById(R.id.imageViewEdit_contact);
            imgCall = v.findViewById(R.id.imageViewCall_contact);
            imgCall.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        int indice = getAdapterPosition();
                        Contact c = data.get(indice);
                        String phoneNumber = "tel:" + c.num;

                        // Ouvre l'application Téléphone sans passer l'appel directement
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        i.setData(Uri.parse(phoneNumber));
                        con.startActivity(i);
                    }
                });
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //affichage boit de dialog
                    int indice =getAdapterPosition();
                    Contact c=data.get(indice);
                    AlertDialog.Builder alert=new AlertDialog.Builder(con);
                    alert.setTitle("Suppression");
                    alert.setMessage("Confirmer la suppression?");
                    alert.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                /*
                  supprimer depuis la base et l'interface selon numero(id)
                 */
                            String numToDelete = c.num;  // Numéro du contact sélectionné

                            // Partie base : suppression du contact
                            ContactManager manager = new ContactManager(con);

                            // Ouvrir la base de données
                            manager.ouvrir();

                            // Vérification si le contact existe dans la base de données
                            ArrayList<Contact> contacts = manager.getAllContacts();
                            boolean contactExists = false;
                            for (Contact contact : contacts) {
                                if (contact.num.equals(numToDelete)) {
                                    contactExists = true;
                                    break;
                                }
                            }

                            if (contactExists) {
                                int deletedRows = manager.supprimer(numToDelete);

                                if (deletedRows > 0) {
                                    Toast.makeText(con, "Contact supprimé avec succès", Toast.LENGTH_SHORT).show();

                                    // Suppression correcte de l'élément
                                    data.remove(indice); // ← Utiliser l’indice correct
                                    notifyItemRemoved(indice); // ← Utiliser notifyItemRemoved() pour éviter les bugs d’affichage

                                } else {
                                    Toast.makeText(con, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(con, "Le contact n'existe pas dans la base", Toast.LENGTH_SHORT).show();
                            }
                            manager.fermer();
                            // Fermeture de la base de données
                            //db.close();
                        }
                    });
                    alert.setNegativeButton("Annuler", null);
                    // alert.setNeutralButton("Exit",null);
                    alert.show();

                }
            });

            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int indice =getAdapterPosition();
                    Contact c=data.get(indice);
                    AlertDialog.Builder alert=new AlertDialog.Builder(con);
                    alert.setTitle("Edition");
                    alert.setMessage("modifier les informations du contact");
                    //inflate convert java
                    LayoutInflater inf =LayoutInflater.from(con);
                    View d=inf.inflate(R.layout.view_dialog,null);
                    alert.setView(d);

                    // Initialiser les champs d'édition dans le dialogue
                    EditText edNom = d.findViewById(R.id.ednom_edit);
                    EditText edPrenom = d.findViewById(R.id.edprenom_edit);
                    EditText edNum = d.findViewById(R.id.ednum_edit);

                    // Remplir les champs avec les données existantes du contact
                    edNom.setText(c.nom);
                    edPrenom.setText(c.prenom);
                    edNum.setText(c.num);

                    // Ajouter le bouton de modification
                    alert.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Récupérer les nouvelles données
                            String newNom = edNom.getText().toString();
                            String newPrenom = edPrenom.getText().toString();
                            String newNum = edNum.getText().toString();

                            // Mettre à jour la base de données
                            ContactManager manager = new ContactManager(con);
                            manager.ouvrir();

                            // Mise à jour du contact dans la base
                            ContentValues values = new ContentValues();
                            values.put(ContactHelper.col_nom, newNom);
                            values.put(ContactHelper.col_prenom, newPrenom);
                            values.put(ContactHelper.col_num, newNum);

                            int updatedRows = manager.db.update(ContactHelper.table_contact, values, ContactHelper.col_num + "=?", new String[]{c.num});

                            if (updatedRows > 0) {
                                // Mise à jour réussie
                                Toast.makeText(con, "Contact modifié avec succès", Toast.LENGTH_SHORT).show();

                                // Mettre à jour l'interface
                                c.nom = newNom;
                                c.prenom = newPrenom;
                                c.num = newNum;

                                // Rafraîchir l'affichage
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(con, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                            }

                            //  manager.fermer();
                        }
                    });
                    alert.setNegativeButton("Annuler", null);
                    alert.show();
                    //recupere data et l'inserer dans edtext et faire l'enregistrement dans interface + data
                }
            });

        }



    }


}
