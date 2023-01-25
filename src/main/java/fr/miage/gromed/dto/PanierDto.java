package fr.miage.gromed.dto;

import fr.miage.gromed.model.Panier;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * A DTO for the {@link Panier} entity
 */
@Data
public class PanierDto implements Serializable {
    private final List<PanierItemDto> items;
    private final Date dateCreation;
}