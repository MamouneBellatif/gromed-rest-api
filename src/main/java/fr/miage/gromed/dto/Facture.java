package fr.miage.gromed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@AllArgsConstructor
public class Facture {

    private String id_panier;
    private Double montant;
    private LocalDateTime date;
    private String nomClient;
    private Map<String, Integer> produitsAchetes;
    private Map<String, Double> prixProduits;


}
