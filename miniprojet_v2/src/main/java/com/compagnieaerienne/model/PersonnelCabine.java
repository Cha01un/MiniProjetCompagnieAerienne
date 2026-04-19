package com.compagnieaerienne.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonnelCabine extends Employe {
    private String qualification;
    private final List<Vol> volsAssignes = new ArrayList<>();

    public PersonnelCabine(String identifiant, String nom, String adresse, String contact,
                           String numeroEmploye, LocalDate dateEmbauche, String qualification) {
        super(identifiant, nom, adresse, contact, numeroEmploye, dateEmbauche);
        this.qualification = qualification;
    }

    public boolean verifierDisponibilite(LocalDateTime depart, LocalDateTime arrivee) {
        return volsAssignes.stream().noneMatch(vol ->
                depart.isBefore(vol.getDateHeureArrivee()) && arrivee.isAfter(vol.getDateHeureDepart()));
    }

    public boolean affecterVol(Vol vol) {
        if (!verifierDisponibilite(vol.getDateHeureDepart(), vol.getDateHeureArrivee())) {
            return false;
        }
        if (!volsAssignes.contains(vol)) {
            volsAssignes.add(vol);
        }
        return true;
    }

    public List<Vol> obtenirVol() {
        return Collections.unmodifiableList(volsAssignes);
    }

    @Override
    public String obtenirRole() {
        return "Personnel Cabine";
    }

    public String getQualification() {
        return qualification;
    }
}
