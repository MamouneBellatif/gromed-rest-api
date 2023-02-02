package fr.miage.gromed.dto;

import java.io.Serializable;

import fr.miage.gromed.model.medicament.Presentation;
import lombok.*;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PanierItemDto implements Serializable{
    private int quantite;

    private String presentationDenomination;
    private double prix;
    private Long presentationCip;
    private boolean delayed;

    @Override
    public String toString() {
        return "PanierItemDto{" +
                "quantite=" + quantite +
                ", presentationCip=" + presentationCip +
                ", delayed=" + delayed +
                '}';
    }
}
