package fr.miage.gromed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PanierSubmitDto {

    private String adresseLivraison;

    private List<PanierItemDto> items;

}
