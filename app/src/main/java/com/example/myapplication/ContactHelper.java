package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ContactHelper extends SQLiteOpenHelper {
    public static final String table_contact="Contacts";
    public static final String col_num="Numero";
    public static final String col_nom="Nom";
    public static final String col_prenom="Prenom";
    String req="create table "+table_contact+" ("+col_num+" Text Primary Key,"
            +" "+col_nom+" Text not null,"
            +" "+col_prenom+" Text not null )";
    public ContactHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//lors de l'ouverture de la base pour la 1er fois
        db.execSQL(req);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
//cas de modification de  la version
        db.execSQL(" drop table "+table_contact);
        onCreate(db);
    }
}
