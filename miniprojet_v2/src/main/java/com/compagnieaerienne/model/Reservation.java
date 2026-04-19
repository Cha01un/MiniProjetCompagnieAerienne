package com.compagnieaerienne.model;

import java.time.LocalDateTime;

public class Reservation {
    private String numeroReservation;
    private LocalDateTime dateReservation;
    private String statut;
    private Passager passager;
    private Vol vol;

    public Reservation(String numeroReservation, Passager passager, Vol vol) {
        this(numeroReservation, passager, vol, "CONFIRMEE");
    }

    public Reservation(String numeroReservation, Passager passager, Vol vol, String statutInitial) {
        this.numeroReservation = numeroReservation;
        this.passager = passager;
        this.vol = vol;
        this.dateReservation = LocalDateTime.now();
        this.statut = statutInitial;
    }

    public void confirmerReservation() {
        this.statut = "CONFIRMEE";
    }

    public void mettreEnAttente() {
        this.statut = "EN_ATTENTE";
    }

    public void annulerReservation() {
        this.statut = "ANNULEE";
    }

    public void modifierReservation(Vol nouveauVol) {
        this.vol = nouveauVol;
        this.dateReservation = LocalDateTime.now();
    }

    public String getNumeroReservation() {
        return numeroReservation;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public String getStatut() {
        return statut;
    }

    public Passager getPassager() {
        return passager;
    }

    public Vol getVol() {
        return vol;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "numero='" + numeroReservation + '\'' +
                ", passager=" + passager.getNom() +
                ", vol=" + vol.getNumeroVol() +
                ", statut='" + statut + '\'' +
                '}';
    }
}
