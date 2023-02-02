package fr.miage.gromed.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class ItemFacture implements Serializable {
    private String libelle;
    private int quantite;
    private double prixUnitaire;
}
