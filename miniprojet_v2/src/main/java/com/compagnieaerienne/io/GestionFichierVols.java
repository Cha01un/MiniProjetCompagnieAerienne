package com.compagnieaerienne.io;

import com.compagnieaerienne.model.Aeroport;
import com.compagnieaerienne.model.Vol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lecture / écriture des vols dans un fichier CSV simple.
 * Format : numeroVol;origineNom;origineVille;destinationNom;destinationVille;depart;arrivee;etat
 */
public class GestionFichierVols {

    public List<Vol> importerVols(Path chemin) throws IOException {
        List<Vol> vols = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(chemin, StandardCharsets.UTF_8)) {
            String ligne;
            boolean premiereLigne = true;

            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) {
                    continue;
                }
                if (premiereLigne) {
                    premiereLigne = false;
                    continue;
                }

                String[] morceaux = ligne.split(";");
                if (morceaux.length != 8) {
                    throw new IOException("Ligne CSV invalide : " + ligne);
                }

                Aeroport origine = new Aeroport(morceaux[1], morceaux[2], "Import CSV");
                Aeroport destination = new Aeroport(morceaux[3], morceaux[4], "Import CSV");
                Vol vol = new Vol(
                        morceaux[0],
                        origine,
                        destination,
                        LocalDateTime.parse(morceaux[5]),
                        LocalDateTime.parse(morceaux[6]),
                        morceaux[7]
                );
                vols.add(vol);
            }
        }

        return vols;
    }

    public void exporterVols(Path chemin, List<Vol> vols) throws IOException {
        if (chemin.getParent() != null) {
            Files.createDirectories(chemin.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(chemin, StandardCharsets.UTF_8)) {
            writer.write("numeroVol;origineNom;origineVille;destinationNom;destinationVille;depart;arrivee;etat");
            writer.newLine();

            for (Vol vol : vols) {
                writer.write(String.join(";",
                        vol.getNumeroVol(),
                        vol.getOrigine().getNom(),
                        vol.getOrigine().getVille(),
                        vol.getDestination().getNom(),
                        vol.getDestination().getVille(),
                        vol.getDateHeureDepart().toString(),
                        vol.getDateHeureArrivee().toString(),
                        vol.getEtat()));
                writer.newLine();
            }
        }
    }
}
