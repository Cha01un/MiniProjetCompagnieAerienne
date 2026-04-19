package com.compagnieaerienne.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AvionTest {

    @Test
    void verifierDisponibiliteRetourneFalseSiChevauchement() {
        Aeroport cdg = new Aeroport("CDG", "Paris", "Test");
        Aeroport jfk = new Aeroport("JFK", "New York", "Test");
        Vol vol1 = new Vol("AF001", cdg, jfk,
                LocalDateTime.of(2025, 4, 20, 10, 0),
                LocalDateTime.of(2025, 4, 20, 12, 0), "PLANIFIE");
        Vol vol2 = new Vol("AF002", cdg, jfk,
                LocalDateTime.of(2025, 4, 20, 11, 0),
                LocalDateTime.of(2025, 4, 20, 13, 0), "PLANIFIE");

        Avion avion = new Avion("F-TEST", "A320", 180);

        assertTrue(avion.affecterVol(vol1));
        assertFalse(avion.affecterVol(vol2));
    }
}
