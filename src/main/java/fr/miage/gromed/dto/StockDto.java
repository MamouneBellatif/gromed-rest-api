package fr.miage.gromed.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link fr.miage.gromed.model.Stock} entity
 */
@Data
@Builder
public class StockDto implements Serializable {
    private final int quantiteStockLogique;
    private final int presentationCIP;
}