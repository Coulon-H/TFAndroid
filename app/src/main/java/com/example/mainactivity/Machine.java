package com.example.mainactivity;

import java.io.Serializable;

public class Machine implements Serializable {
    private String nom;
    private String addresse;

    Machine(String one, String two) {
        this.nom = one;
        this.addresse = two;
    }

    public String getNom() {
        return this.nom;
    }

    public String getAddresse() {
        return this.addresse;
    }
}
