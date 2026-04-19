package com.compagnieaerienne.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pilote extends Employe {
    private String licence;
    private int heuresDeVol;
    private final List<Vol> volsPrevus = new ArrayList<>();

    public Pilote(String identifiant, String nom, String adresse, String contact,
                  String numeroEmploye, LocalDate dateEmbauche, String licence, int heuresDeVol) {
        super(identifiant, nom, adresse, contact, numeroEmploye, dateEmbauche);
        this.licence = licence;
        this.heuresDeVol = heuresDeVol;
    }

    public boolean verifierDisponibilite(LocalDateTime depart, LocalDateTime arrivee) {
        return volsPrevus.stream().noneMatch(vol ->
                depart.isBefore(vol.getDateHeureArrivee()) && arrivee.isAfter(vol.getDateHeureDepart()));
    }

    public boolean affecterVol(Vol vol) {
        if (!verifierDisponibilite(vol.getDateHeureDepart(), vol.getDateHeureArrivee())) {
            return false;
        }
        if (!volsPrevus.contains(vol)) {
            volsPrevus.add(vol);
        }
        return true;
    }

    public List<Vol> obtenirVol() {
        return Collections.unmodifiableList(volsPrevus);
    }

    public String obtenirProchainVol() {
        return volsPrevus.stream()
                .sorted((v1, v2) -> v1.getDateHeureDepart().compareTo(v2.getDateHeureDepart()))
                .map(Vol::getNumeroVol)
                .findFirst()
                .orElse("Aucun vol prévu");
    }

    @Override
    public String obtenirRole() {
        return "Pilote";
    }

    public String getLicence() {
        return licence;
    }

    public int getHeuresDeVol() {
        return heuresDeVol;
    }
}
