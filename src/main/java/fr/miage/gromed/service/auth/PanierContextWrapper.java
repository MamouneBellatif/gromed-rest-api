package fr.miage.gromed.service.auth;

import fr.miage.gromed.dto.PanierItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class PanierContextWrapper {
    private PanierItemDto panierItemDto;
    private int tentative;
}
