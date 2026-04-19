package com.compagnieaerienne.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Passager extends Personne {
    private String passport;
    private final List<Reservation> reservations = new ArrayList<>();

    public Passager(String identifiant, String nom, String adresse, String contact, String passport) {
        super(identifiant, nom, adresse, contact);
        this.passport = passport;
    }

    public Reservation reserverVol(String numeroReservation, Vol vol) {
        Reservation reservation = new Reservation(numeroReservation, this, vol);
        reservations.add(reservation);
        return reservation;
    }

    public Reservation reserverVolEnAttente(String numeroReservation, Vol vol) {
        Reservation reservation = new Reservation(numeroReservation, this, vol, "EN_ATTENTE");
        reservations.add(reservation);
        return reservation;
    }

    public boolean annulerReservation(String numeroReservation) {
        return reservations.removeIf(r -> r.getNumeroReservation().equals(numeroReservation));
    }

    public List<Reservation> obtenirReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public String getPassport() {
        return passport;
    }
    @Override
    public String toString() {
        return nom + " [passport=" + passport + "]";
    }
}

