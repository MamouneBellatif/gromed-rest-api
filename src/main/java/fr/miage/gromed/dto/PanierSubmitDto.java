package fr.miage.gromed.dto;

import lombok.Builder;

@Builder
public class PanierSubmitDto extends PanierItemDto{

    private String adresseLivraison;

}
