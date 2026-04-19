package com.compagnieaerienne.model;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Vol {
    private String numeroVol;
    private Aeroport origine;
    private Aeroport destination;
    private LocalDateTime dateHeureDepart;
    private LocalDateTime dateHeureArrivee;
    private String etat;
    private Avion avion;
    private Pilote pilote;
    private final List<PersonnelCabine> equipeCabine = new ArrayList<>();
    private final List<Passager> passagers = new ArrayList<>();
    private final Deque<Passager> listeAttente = new ArrayDeque<>();

    public Vol(String numeroVol, Aeroport origine, Aeroport destination,
               LocalDateTime dateHeureDepart, LocalDateTime dateHeureArrivee, String etat) {
        this.numeroVol = numeroVol;
        this.origine = origine;
        this.destination = destination;
        this.dateHeureDepart = dateHeureDepart;
        this.dateHeureArrivee = dateHeureArrivee;
        this.etat = etat;
    }

    public void planifierVol() {
        this.etat = "PLANIFIE";
    }

    public void annulerVol() {
        this.etat = "ANNULE";
    }

    public void modifierVol(LocalDateTime nouveauDepart, LocalDateTime nouvelleArrivee) {
        this.dateHeureDepart = nouveauDepart;
        this.dateHeureArrivee = nouvelleArrivee;
    }

    public boolean affecterVol(Pilote pilote, List<PersonnelCabine> personnelsCabine) {
        if (!pilote.affecterVol(this)) {
            return false;
        }

        for (PersonnelCabine personnelCabine : personnelsCabine) {
            if (!personnelCabine.verifierDisponibilite(this.dateHeureDepart, this.dateHeureArrivee)) {
                return false;
            }
        }

        this.pilote = pilote;
        this.equipeCabine.clear();
        for (PersonnelCabine personnelCabine : personnelsCabine) {
            personnelCabine.affecterVol(this);
            this.equipeCabine.add(personnelCabine);
        }
        return true;
    }

    public boolean estComplet() {
        return avion != null && passagers.size() >= avion.getCapacite();
    }

    public boolean ajouterPassager(Passager passager) {
        if (estComplet()) {
            return false;
        }
        if (!passagers.contains(passager)) {
            passagers.add(passager);
        }
        return true;
    }

    public void ajouterEnListeAttente(Passager passager) {
        if (!listeAttente.contains(passager)) {
            listeAttente.addLast(passager);
        }
    }

    public boolean retirerPassager(Passager passager) {
        return passagers.remove(passager);
    }

    public Passager promouvoirDepuisListeAttente() {
        Passager prochain = listeAttente.pollFirst();
        if (prochain != null) {
            passagers.add(prochain);
        }
        return prochain;
    }

    public List<Passager> listerPassager() {
        return Collections.unmodifiableList(passagers);
    }

    public List<Passager> getListeAttente() {
        return List.copyOf(listeAttente);
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    public Aeroport getOrigine() {
        return origine;
    }

    public Aeroport getDestination() {
        return destination;
    }

    public LocalDateTime getDateHeureDepart() {
        return dateHeureDepart;
    }

    public LocalDateTime getDateHeureArrivee() {
        return dateHeureArrivee;
    }

    public String getEtat() {
        return etat;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Avion getAvion() {
        return avion;
    }

    public Pilote getPilote() {
        return pilote;
    }

    public List<PersonnelCabine> getEquipeCabine() {
        return Collections.unmodifiableList(equipeCabine);
    }

    @Override
    public String toString() {
        return "Vol{" +
                "numeroVol='" + numeroVol + '\'' +
                ", origine=" + origine +
                ", destination=" + destination +
                ", depart=" + dateHeureDepart +
                ", arrivee=" + dateHeureArrivee +
                ", etat='" + etat + '\'' +
                '}';
    }
}
