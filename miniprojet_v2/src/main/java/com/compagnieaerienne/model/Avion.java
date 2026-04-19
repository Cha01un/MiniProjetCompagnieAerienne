package com.compagnieaerienne.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Avion {
    private String immatriculation;
    private String modele;
    private int capacite;
    private final List<Vol> volsAssignes = new ArrayList<>();

    public Avion(String immatriculation, String modele, int capacite) {
        this.immatriculation = immatriculation;
        this.modele = modele;
        this.capacite = capacite;
    }

    public boolean verifierDisponibilite(LocalDateTime depart, LocalDateTime arrivee) {
        return volsAssignes.stream().noneMatch(v ->
                depart.isBefore(v.getDateHeureArrivee()) && arrivee.isAfter(v.getDateHeureDepart()));
    }

    public boolean affecterVol(Vol vol) {
        if (!verifierDisponibilite(vol.getDateHeureDepart(), vol.getDateHeureArrivee())) {
            return false;
        }
        volsAssignes.add(vol);
        vol.setAvion(this);
        return true;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public String getModele() {
        return modele;
    }

    public int getCapacite() {
        return capacite;
    }

    @Override
    public String toString() {
        return modele + " [" + immatriculation + "]";
    }
}
