package fr.miage.gromed.dto;

import fr.miage.gromed.model.Panier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * A DTO for the {@link Panier} entity
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanierDto implements Serializable {
    private Long id;
    private Set<PanierItemDto> items;
    private LocalDateTime dateCreation;
}