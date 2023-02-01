package fr.miage.gromed.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class Facture implements Serializable {

    private String id_panier;
    private Double montant;
    private LocalDateTime date;
    private String nomClient;
    private Map<String, Integer> produitsAchetes;
    private Map<String, Double> prixProduits;


}
