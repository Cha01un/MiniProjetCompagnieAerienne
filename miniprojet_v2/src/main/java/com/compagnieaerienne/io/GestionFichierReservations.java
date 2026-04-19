package com.compagnieaerienne.io;

import com.compagnieaerienne.model.Reservation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Export simple des réservations pour illustrer l'écriture dans un fichier externe.
 */
public class GestionFichierReservations {

    public void exporterReservations(Path chemin, List<Reservation> reservations) throws IOException {
        if (chemin.getParent() != null) {
            Files.createDirectories(chemin.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(chemin, StandardCharsets.UTF_8)) {
            writer.write("numeroReservation;passager;passport;numeroVol;statut;dateReservation");
            writer.newLine();

            for (Reservation reservation : reservations) {
                writer.write(String.join(";",
                        reservation.getNumeroReservation(),
                        reservation.getPassager().getNom(),
                        reservation.getPassager().getPassport(),
                        reservation.getVol().getNumeroVol(),
                        reservation.getStatut(),
                        reservation.getDateReservation().toString()));
                writer.newLine();
            }
        }
    }
}
