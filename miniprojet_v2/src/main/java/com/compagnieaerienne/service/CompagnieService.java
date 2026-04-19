package com.compagnieaerienne.service;

import com.compagnieaerienne.io.GestionFichierReservations;
import com.compagnieaerienne.io.GestionFichierVols;
import com.compagnieaerienne.model.*;
import com.compagnieaerienne.repository.RepositoryEnMemoire;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CompagnieService {
    private final RepositoryEnMemoire<Passager, String> passagerRepository =
            new RepositoryEnMemoire<>(Passager::getIdentifiant);
    private final RepositoryEnMemoire<Employe, String> employeRepository =
            new RepositoryEnMemoire<>(Employe::getNumeroEmploye);
    private final RepositoryEnMemoire<Avion, String> avionRepository =
            new RepositoryEnMemoire<>(Avion::getImmatriculation);
    private final RepositoryEnMemoire<Vol, String> volRepository =
            new RepositoryEnMemoire<>(Vol::getNumeroVol);
    private final RepositoryEnMemoire<Reservation, String> reservationRepository =
            new RepositoryEnMemoire<>(Reservation::getNumeroReservation);

    private final GestionFichierVols gestionFichierVols = new GestionFichierVols();
    private final GestionFichierReservations gestionFichierReservations = new GestionFichierReservations();

    public void ajouterPassager(Passager passager) { passagerRepository.ajouter(passager); }
    public void ajouterEmploye(Employe employe) { employeRepository.ajouter(employe); }
    public void ajouterAvion(Avion avion) { avionRepository.ajouter(avion); }
    public void ajouterVol(Vol vol) { volRepository.ajouter(vol); }

    public Optional<Vol> obtenirVol(String numeroVol) {
        return volRepository.chercherParId(numeroVol);
    }

    public Optional<Reservation> obtenirReservation(String numeroReservation) {
        return reservationRepository.chercherParId(numeroReservation);
    }

    public Reservation reserverVol(String idPassager, String numeroReservation, String numeroVol) {
        if (reservationRepository.chercherParId(numeroReservation).isPresent()) {
            throw new IllegalArgumentException("Le numéro de réservation existe déjà.");
        }

        Passager passager = passagerRepository.chercherParId(idPassager)
                .orElseThrow(() -> new IllegalArgumentException("Passager introuvable"));
        Vol vol = volRepository.chercherParId(numeroVol)
                .orElseThrow(() -> new IllegalArgumentException("Vol introuvable"));

        if (vol.getAvion() == null) {
            throw new IllegalStateException("Impossible de réserver un vol sans avion affecté.");
        }

        Reservation reservation;
        if (vol.estComplet()) {
            vol.ajouterEnListeAttente(passager);
            reservation = passager.reserverVolEnAttente(numeroReservation, vol);
        } else {
            vol.ajouterPassager(passager);
            reservation = passager.reserverVol(numeroReservation, vol);
        }

        reservationRepository.ajouter(reservation);
        return reservation;
    }

    public boolean annulerReservation(String numeroReservation) {
        Optional<Reservation> reservationOpt = reservationRepository.chercherParId(numeroReservation);
        if (reservationOpt.isEmpty()) {
            return false;
        }

        Reservation reservation = reservationOpt.get();
        boolean etaitConfirmee = "CONFIRMEE".equals(reservation.getStatut());
        reservation.annulerReservation();
        reservation.getPassager().annulerReservation(numeroReservation);

        Vol vol = reservation.getVol();
        if (etaitConfirmee) {
            vol.retirerPassager(reservation.getPassager());
            Passager promu = vol.promouvoirDepuisListeAttente();
            if (promu != null) {
                promu.obtenirReservations().stream()
                        .filter(r -> r.getVol().equals(vol) && "EN_ATTENTE".equals(r.getStatut()))
                        .findFirst()
                        .ifPresent(Reservation::confirmerReservation);
            }
        }

        return reservationRepository.supprimer(numeroReservation);
    }

    public void planifierVol(String numeroVol) {
        obtenirVol(numeroVol).ifPresent(Vol::planifierVol);
    }

    public boolean annulerVol(String numeroVol) {
        Optional<Vol> volOpt = obtenirVol(numeroVol);
        if (volOpt.isEmpty()) {
            return false;
        }
        volOpt.get().annulerVol();
        return true;
    }

    public boolean affecterAvionAuVol(String immatriculation, String numeroVol) {
        Avion avion = avionRepository.chercherParId(immatriculation)
                .orElseThrow(() -> new IllegalArgumentException("Avion introuvable"));
        Vol vol = volRepository.chercherParId(numeroVol)
                .orElseThrow(() -> new IllegalArgumentException("Vol introuvable"));
        return avion.affecterVol(vol);
    }

    public boolean affecterEquipageAuVol(String numeroVol, Pilote pilote, List<PersonnelCabine> equipeCabine) {
        Vol vol = volRepository.chercherParId(numeroVol)
                .orElseThrow(() -> new IllegalArgumentException("Vol introuvable"));
        return vol.affecterVol(pilote, equipeCabine);
    }

    public List<Vol> planifierVolsSurUnePeriode(List<Vol> vols, LocalDateTime debut, LocalDateTime fin) {
        return vols.stream()
                .filter(v -> !v.getDateHeureDepart().isBefore(debut) && !v.getDateHeureArrivee().isAfter(fin))
                .peek(v -> {
                    v.planifierVol();
                    volRepository.modifier(v);
                })
                .toList();
    }

    public List<Vol> importerVolsDepuisCsv(Path chemin) throws IOException {
        List<Vol> vols = gestionFichierVols.importerVols(chemin);
        vols.forEach(this::ajouterVol);
        return vols;
    }

    public void exporterVolsVersCsv(Path chemin) throws IOException {
        gestionFichierVols.exporterVols(chemin, listerVols());
    }

    public void exporterReservationsVersCsv(Path chemin) throws IOException {
        gestionFichierReservations.exporterReservations(chemin, listerReservations());
    }

    public List<Passager> listerPassagers() {
        return passagerRepository.lister();
    }

    public List<Employe> listerEmployes() {
        return employeRepository.lister();
    }

    public List<Avion> listerAvions() {
        return avionRepository.lister();
    }

    public List<Vol> listerVols() {
        return volRepository.lister();
    }

    public List<Reservation> listerReservations() {
        return reservationRepository.lister();
    }
}
