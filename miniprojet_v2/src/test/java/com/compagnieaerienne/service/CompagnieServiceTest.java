package com.compagnieaerienne.service;

import com.compagnieaerienne.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompagnieServiceTest {

    @Test
    void reservationConfirmeeSiCapaciteDisponible() {
        CompagnieService service = preparerService(2);
        Reservation reservation = service.reserverVol("PA001", "RES001", "AF001");

        assertEquals("CONFIRMEE", reservation.getStatut());
    }

    @Test
    void reservationEnAttenteSiVolComplet() {
        CompagnieService service = preparerService(1);
        service.reserverVol("PA001", "RES001", "AF001");
        Reservation reservation2 = service.reserverVol("PA002", "RES002", "AF001");

        assertEquals("EN_ATTENTE", reservation2.getStatut());
        assertEquals(1, service.obtenirVol("AF001").orElseThrow().getListeAttente().size());
    }

    @Test
    void annulationDuneReservationConfirmeePromeutLePremierPassagerEnAttente() {
        CompagnieService service = preparerService(1);
        service.reserverVol("PA001", "RES001", "AF001");
        service.reserverVol("PA002", "RES002", "AF001");

        assertTrue(service.annulerReservation("RES001"));

        Reservation reservation2 = service.obtenirReservation("RES002").orElseThrow();
        assertEquals("CONFIRMEE", reservation2.getStatut());
        assertEquals(1, service.obtenirVol("AF001").orElseThrow().listerPassager().size());
    }

    @Test
    void affectationEquipageRefuseeSiLePiloteEstDejaOccupe() {
        CompagnieService service = preparerService(10);
        Pilote pilote = (Pilote) service.listerEmployes().stream()
                .filter(e -> e instanceof Pilote)
                .findFirst()
                .orElseThrow();
        PersonnelCabine cabine = (PersonnelCabine) service.listerEmployes().stream()
                .filter(e -> e instanceof PersonnelCabine)
                .findFirst()
                .orElseThrow();

        Aeroport cdg = new Aeroport("CDG", "Paris", "Test");
        Aeroport jfk = new Aeroport("JFK", "New York", "Test");
        Vol vol2 = new Vol("AF002", cdg, jfk,
                LocalDateTime.of(2025, 4, 20, 11, 0),
                LocalDateTime.of(2025, 4, 20, 15, 0), "PLANIFIE");
        service.ajouterVol(vol2);

        assertTrue(service.affecterEquipageAuVol("AF001", pilote, List.of(cabine)));
        assertFalse(service.affecterEquipageAuVol("AF002", pilote, List.of(cabine)));
    }

    @Test
    void importCsvAjouteDesVols() throws IOException {
        CompagnieService service = new CompagnieService();
        Path fichierTemporaire = Files.createTempFile("vols-test", ".csv");
        Files.writeString(fichierTemporaire,
                "numeroVol;origineNom;origineVille;destinationNom;destinationVille;depart;arrivee;etat\n" +
                "TO350;Paris CDG;Paris;Rome FCO;Rome;2025-12-25T10:15;2025-12-25T12:15;PLANIFIE\n");

        List<Vol> vols = service.importerVolsDepuisCsv(fichierTemporaire);

        assertEquals(1, vols.size());
        assertEquals("TO350", vols.get(0).getNumeroVol());
    }

    private CompagnieService preparerService(int capaciteAvion) {
        CompagnieService service = new CompagnieService();

        Aeroport cdg = new Aeroport("CDG", "Paris", "Test");
        Aeroport jfk = new Aeroport("JFK", "New York", "Test");
        Vol vol = new Vol("AF001", cdg, jfk,
                LocalDateTime.of(2025, 4, 20, 10, 0),
                LocalDateTime.of(2025, 4, 20, 14, 0), "PLANIFIE");
        Avion avion = new Avion("F-TEST", "A320", capaciteAvion);
        Pilote pilote = new Pilote("P1", "Jean Martin", "Paris", "0102030405",
                "EMP001", LocalDate.of(2020, 1, 15), "LIC-01", 1200);
        PersonnelCabine cabine = new PersonnelCabine("C1", "Sara Ali", "Paris", "0600000001",
                "EMP002", LocalDate.of(2021, 3, 10), "Sécurité cabine");
        Passager passager1 = new Passager("PA001", "Amine Dupont", "Lyon", "0700000000", "FR123456");
        Passager passager2 = new Passager("PA002", "Leila Martin", "Lille", "0700000001", "FR654321");

        service.ajouterVol(vol);
        service.ajouterAvion(avion);
        service.ajouterEmploye(pilote);
        service.ajouterEmploye(cabine);
        service.ajouterPassager(passager1);
        service.ajouterPassager(passager2);
        service.affecterAvionAuVol("F-TEST", "AF001");

        return service;
    }
}
