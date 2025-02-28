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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyContactAdapter extends BaseAdapter {

    Context con;
    ArrayList<Contact> data;
    MyContactAdapter(Context con, ArrayList<Contact> data)
    {
        this.con=con;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();//return nbr de view à créer
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //creation d'un veiw
        //convertir un fichier xml =>parse xml
        LayoutInflater inf = LayoutInflater.from(con);
        View v = inf.inflate(R.layout.view_contact, null);

        //recupration des textview
        //v est nommé viewHolder
        //recupertion des Holders
        TextView tvnom = v.findViewById(R.id.tvnom_contact);
        TextView tvprenom = v.findViewById(R.id.tvprenom_contact);
        TextView tvnum = v.findViewById(R.id.tvnum_contact);

        //recuperation des btn
        ImageView imgDelete = v.findViewById(R.id.imageViewDelete_contact);
        ImageView imgEdit = v.findViewById(R.id.imageViewEdit_contact);
        ImageView imgCall = v.findViewById(R.id.imageViewCall_contact);

        //affectation des holders
        Contact c = data.get(position);
        tvnom.setText(c.nom);
        tvprenom.setText(c.prenom);
        tvnum.setText(c.num);

        //Evenement
imgCall.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent();
        i.setAction(Intent.ACTION_CALL);//permission requise
        i.setData(Uri.parse("tel:"+c.num));
        con.startActivity(i);
    }
});
imgDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //affichage boit de dialog
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
                    // Suppression du contact de la base
                    int deletedRows = manager.supprimer(numToDelete);

                    if (deletedRows > 0) {
                        // Mise à jour de l'interface utilisateur
                        Toast.makeText(con, "Contact supprimé avec succès", Toast.LENGTH_SHORT).show();

                        // Suppression de l'élément dans la liste et mise à jour de l'interface
                        data.remove(position);
                        notifyDataSetChanged();  // Rafraîchir l'affichage de la liste
                    } else {
                        Toast.makeText(con, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(con, "Le contact n'existe pas dans la base", Toast.LENGTH_SHORT).show();
                }
//manager.fermer();
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

        return v;

       /* Button btn=new Button(this.con);
        Contact c=data.get(position);
        btn.setText(c.num);

        return btn;//return 1 view à afficher
    }*/
    }
}
