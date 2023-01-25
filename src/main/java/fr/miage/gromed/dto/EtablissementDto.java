package fr.miage.gromed.dto;

import fr.miage.gromed.model.Etablissement;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Etablissement} entity
 */
@Data
public class EtablissementDto implements Serializable {
    private final String nom;
    private final String adresse;
}