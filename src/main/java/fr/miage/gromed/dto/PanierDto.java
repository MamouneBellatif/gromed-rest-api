package fr.miage.gromed.dto;

import fr.miage.gromed.model.Panier;
import lombok.*;

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
@Setter
@Getter
@Data
public class PanierDto implements Serializable {
    private Long id;
    private List<PanierItemDto> items;
    private LocalDateTime dateCreation;


}