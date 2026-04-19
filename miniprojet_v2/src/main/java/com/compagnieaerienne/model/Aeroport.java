package com.compagnieaerienne.model;

import java.util.ArrayList;
import java.util.List;

public class Aeroport {
    private String nom;
    private String ville;
    private String description;
    private final List<Vol> vols = new ArrayList<>();

    public Aeroport(String nom, String ville, String description) {
        this.nom = nom;
        this.ville = ville;
        this.description = description;
    }

    public void affecterVol(Vol vol) {
        vols.add(vol);
    }

    public String getNom() {
        return nom;
    }

    public String getVille() {
        return ville;
    }

    @Override
    public String toString() {
        return nom + " (" + ville + ")";
    }
}
