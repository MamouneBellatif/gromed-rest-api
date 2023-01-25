package fr.miage.gromed.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link fr.miage.gromed.model.Stock} entity
 */
@Data
public class StockDto implements Serializable {
    private final int quantiteStockLogique;
}