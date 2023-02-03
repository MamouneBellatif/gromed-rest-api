package fr.miage.gromed.dto;

import fr.miage.gromed.model.medicament.ComposantSubtance;
import fr.miage.gromed.model.medicament.Presentation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link Presentation} entity
 */
@SuperBuilder
@Getter
@Setter
public class PresentationFicheDto extends PresentationDto implements Serializable {
    private final int stock;
    private final String statutAdmin;


//    private final String avis;
//    private final String typeGenerique;
//    private List<String> composantSubtances;

//    private final String statutAdmin;
//    private final StockDto stock;
}