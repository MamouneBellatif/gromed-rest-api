package fr.miage.gromed.dto;

import fr.miage.gromed.model.medicament.Presentation;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Presentation} entity
 */
@Data
public class PresentationDto implements Serializable {
    private final Long codeCIP;
    private final String libelle;
    private final double prixDeBase;
    private final double honoraireRemboursement;
    private final String statutAdmin;
    private final StockDto stock;
    private final String tauxRemboursement;
}