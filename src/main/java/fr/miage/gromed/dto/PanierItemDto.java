package fr.miage.gromed.dto;

import java.io.Serializable;

import fr.miage.gromed.model.medicament.Presentation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Data
@Setter
@Getter
public class PanierItemDto implements Serializable{
    private int quantite;
    private Presentation presentation;
}
