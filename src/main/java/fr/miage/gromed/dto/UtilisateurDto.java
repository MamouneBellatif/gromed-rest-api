package fr.miage.gromed.dto;

import fr.miage.gromed.model.Utilisateur;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link Utilisateur} entity
 */
@Builder
@Getter
@Setter
public class UtilisateurDto implements Serializable {
    private final String id;
    private final String nom;
    private final String prenom;
    private final String email;
//    private final EtablissementDto etablissement;
}