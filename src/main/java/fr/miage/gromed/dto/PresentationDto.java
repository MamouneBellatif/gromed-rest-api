package fr.miage.gromed.dto;

import fr.miage.gromed.model.medicament.Presentation;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * A DTO for the {@link Presentation} entity
 */
@Data
@SuperBuilder
public class PresentationDto implements Serializable {
    private final Long codeCIP;
    private final String libelle;
    private final String medicamentDenomination;
    private final String imageUrl;
    private final double prixDeBase;
    private final double honoraireRemboursement;
//    private final String statutAdmin;
//    private final StockDto stock;
    private final String tauxRemboursement;
}