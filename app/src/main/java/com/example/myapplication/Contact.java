package com.example.myapplication;

public class Contact {
    public String num,nom,prenom;

    public Contact(  String num, String nom,String prenom) {

        this.num = num;
        this.nom = nom;
        this.prenom = prenom;

    }

    @Override
    public String toString() {
        return "Contact{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
