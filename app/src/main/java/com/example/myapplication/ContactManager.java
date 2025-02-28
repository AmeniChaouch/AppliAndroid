package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ContactManager {
    SQLiteDatabase db=null;
    Context con;

    ContactManager(Context con){
       this.con=con;
    }
    public void ouvrir()
    {
        ContactHelper helper=new ContactHelper(con,"contactbase.db",null,2);
        db=helper.getWritableDatabase();
    }


    public long ajout(String num,String nom,String prenom)
    {
//retourne le nbr des elts insérer
        long a=0;
        ContentValues values=new ContentValues();
        values.put(ContactHelper.col_num,num);
        values.put(ContactHelper.col_nom,nom);
        values.put(ContactHelper.col_prenom,prenom);
a=db.insert(ContactHelper.table_contact,null,values);
return a;
    }
    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> l = new ArrayList<Contact>();
        Cursor cr = db.query(ContactHelper.table_contact,
                new String[]{ContactHelper.col_num,
                        ContactHelper.col_nom,
                        ContactHelper.col_prenom}, null, null, null, null, null);

        cr.moveToFirst();
        while (!cr.isAfterLast())
        { String i1 = cr.getString(0);
        String i2 = cr.getString(1);
        String i3 = cr.getString(2);
        l.add(new Contact(i1, i2, i3));
        cr.moveToNext();
        }
        return l;
    }
    public int supprimer(String num) {
       // SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(ContactHelper.table_contact, ContactHelper.col_num+"= ?", new String[]{num});
        db.close();
        return deletedRows;
    }

    public void fermer() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }


}
