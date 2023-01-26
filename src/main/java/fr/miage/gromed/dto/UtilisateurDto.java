package fr.miage.gromed.dto;

import fr.miage.gromed.model.Utilisateur;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Utilisateur} entity
 */
@Data
public class UtilisateurDto implements Serializable {
    private final String nom;
    private final String prenom;
    private final String email;
    private final EtablissementDto etablissement;
}