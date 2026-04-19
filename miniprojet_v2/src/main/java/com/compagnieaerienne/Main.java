package com.compagnieaerienne;

import com.compagnieaerienne.model.*;
import com.compagnieaerienne.service.CompagnieService;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CompagnieService service = new CompagnieService();

        Aeroport cdg = new Aeroport("Charles de Gaulle", "Paris", "Aéroport principal de Paris");
        Aeroport jfk = new Aeroport("JFK", "New York", "Aéroport international de New York");

        Vol vol1 = new Vol("AF001", cdg, jfk,
                LocalDateTime.of(2025, 4, 20, 10, 0),
                LocalDateTime.of(2025, 4, 20, 18, 0), "BROUILLON");

        Vol vol2 = new Vol("AF002", cdg, jfk,
                LocalDateTime.of(2025, 4, 20, 12, 0),
                LocalDateTime.of(2025, 4, 20, 19, 0), "BROUILLON");

        Vol vol3 = new Vol("AF003", cdg, jfk,
                LocalDateTime.of(2025, 4, 21, 9, 0),
                LocalDateTime.of(2025, 4, 21, 16, 0), "BROUILLON");

        Avion avionPetit = new Avion("F-ABCD", "Airbus A319", 1);
        Avion avionGrand = new Avion("F-EFGH", "Airbus A320", 180);
        service.ajouterAvion(avionPetit);
        service.ajouterAvion(avionGrand);
        service.ajouterVol(vol1);
        service.ajouterVol(vol2);
        service.ajouterVol(vol3);

        Pilote pilote = new Pilote("P1", "Jean Martin", "Paris", "0102030405",
                "EMP001", LocalDate.of(2020, 1, 15), "LIC-PIL-01", 3500);
        PersonnelCabine h1 = new PersonnelCabine("C1", "Sara Ali", "Paris", "0600000001",
                "EMP002", LocalDate.of(2021, 3, 10), "Sécurité cabine");
        PersonnelCabine h2 = new PersonnelCabine("C2", "Nina Roy", "Paris", "0600000002",
                "EMP003", LocalDate.of(2022, 6, 18), "Service passagers");

        service.ajouterEmploye(pilote);
        service.ajouterEmploye(h1);
        service.ajouterEmploye(h2);

        service.planifierVol("AF001");
        service.planifierVol("AF002");
        service.planifierVol("AF003");

        boolean avionAffecte = service.affecterAvionAuVol("F-ABCD", "AF001");
        service.affecterAvionAuVol("F-EFGH", "AF002");
        service.affecterAvionAuVol("F-EFGH", "AF003");

        boolean equipageVol1 = service.affecterEquipageAuVol("AF001", pilote, List.of(h1, h2));
        boolean equipageVol2 = service.affecterEquipageAuVol("AF002", pilote, List.of(h1, h2));
        boolean equipageVol3 = service.affecterEquipageAuVol("AF003", pilote, List.of(h1, h2));

        Passager passager1 = new Passager("PA001", "Amine Dupont", "Lyon", "0700000000", "FR123456");
        Passager passager2 = new Passager("PA002", "Leila Martin", "Lille", "0700000001", "FR654321");
        service.ajouterPassager(passager1);
        service.ajouterPassager(passager2);

        Reservation reservation1 = service.reserverVol("PA001", "RES001", "AF001");
        Reservation reservation2 = service.reserverVol("PA002", "RES002", "AF001");

        try {
            service.importerVolsDepuisCsv(Path.of("src/main/resources/data/vols.csv"));
            service.exporterVolsVersCsv(Path.of("exports/vols-export.csv"));
            service.exporterReservationsVersCsv(Path.of("exports/reservations-export.csv"));
        } catch (IOException e) {
            System.err.println("Erreur de lecture/écriture fichier : " + e.getMessage());
        }

        System.out.println("===== DEMONSTRATION V2 =====");
        System.out.println("Passager 1 : " + passager1.obtenirInfos());
        System.out.println("Role pilote : " + pilote.obtenirRole());
        System.out.println("Avion affecté au vol 1 : " + avionAffecte);
        System.out.println("Affectation équipage vol 1 : " + equipageVol1);
        System.out.println("Affectation équipage vol 2 (doit échouer car chevauchement) : " + equipageVol2);
        System.out.println("Affectation équipage vol 3 (doit réussir) : " + equipageVol3);
        System.out.println("Vols prévus du pilote : " + pilote.obtenirVol().stream().map(Vol::getNumeroVol).toList());
        System.out.println("Réservation 1 : " + reservation1);
        System.out.println("Réservation 2 : " + reservation2 + " (liste d'attente si le vol est complet)");
        System.out.println("Liste d'attente AF001 : " + service.obtenirVol("AF001").orElseThrow().getListeAttente());
        System.out.println("Nombre total de vols en mémoire après import CSV : " + service.listerVols().size());
        System.out.println("Exports générés dans le dossier exports/ si l'exécution réussit.");
    }
}
