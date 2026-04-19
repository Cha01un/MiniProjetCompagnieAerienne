package com.compagnieaerienne.model;

import java.time.LocalDate;

public abstract class Employe extends Personne {
    protected String numeroEmploye;
    protected LocalDate dateEmbauche;

    public Employe(String identifiant, String nom, String adresse, String contact,
                   String numeroEmploye, LocalDate dateEmbauche) {
        super(identifiant, nom, adresse, contact);
        this.numeroEmploye = numeroEmploye;
        this.dateEmbauche = dateEmbauche;
    }

    public abstract String obtenirRole();

    public String getNumeroEmploye() {
        return numeroEmploye;
    }
}
