package fr.miage.gromed.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class CommandeTypeDto {

    private Long id;
    private PanierDto panier;
}
