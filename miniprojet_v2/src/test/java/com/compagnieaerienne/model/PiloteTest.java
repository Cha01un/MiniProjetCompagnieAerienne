package com.compagnieaerienne.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PiloteTest {

    @Test
    void unPilotePeutAvoirPlusieursVolsPrevusSansChevauchement() {
        Pilote pilote = new Pilote("P1", "Jean Martin", "Paris", "0102030405",
                "EMP001", LocalDate.of(2020, 1, 15), "LIC-01", 1200);
        Aeroport cdg = new Aeroport("CDG", "Paris", "Test");
        Aeroport jfk = new Aeroport("JFK", "New York", "Test");

        Vol vol1 = new Vol("AF001", cdg, jfk,
                LocalDateTime.of(2025, 4, 20, 8, 0),
                LocalDateTime.of(2025, 4, 20, 10, 0), "PLANIFIE");
        Vol vol2 = new Vol("AF002", cdg, jfk,
                LocalDateTime.of(2025, 4, 20, 11, 0),
                LocalDateTime.of(2025, 4, 20, 13, 0), "PLANIFIE");
        Vol vol3 = new Vol("AF003", cdg, jfk,
                LocalDateTime.of(2025, 4, 20, 9, 30),
                LocalDateTime.of(2025, 4, 20, 12, 0), "PLANIFIE");

        assertTrue(pilote.affecterVol(vol1));
        assertTrue(pilote.affecterVol(vol2));
        assertFalse(pilote.affecterVol(vol3));
        assertEquals(2, pilote.obtenirVol().size());
    }
}
