package fr.miage.gromed.dto;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
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
@JacksonStdImpl
public class PanierDto {
    private Long id;
    private List<PanierItemDto> items;
    private LocalDateTime dateCreation;
}