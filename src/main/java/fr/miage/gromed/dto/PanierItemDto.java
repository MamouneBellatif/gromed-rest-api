package fr.miage.gromed.dto;

import java.io.Serializable;

import fr.miage.gromed.model.medicament.Presentation;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;




@Builder
@Setter
@Getter
public class PanierItemDto implements Serializable{
    private int quantite;
    private Long presentationCip;

    private boolean delayed;
}
