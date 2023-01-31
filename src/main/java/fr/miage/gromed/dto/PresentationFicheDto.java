package fr.miage.gromed.dto;

import fr.miage.gromed.model.medicament.Presentation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * A DTO for the {@link Presentation} entity
 */
@SuperBuilder
@Getter
@Setter
public class PresentationFicheDto extends PresentationDto implements Serializable {
    private final double stock;

//    private final String statutAdmin;
//    private final StockDto stock;
}