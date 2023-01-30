package fr.miage.gromed.dto;

import fr.miage.gromed.model.enums.AlerteDecision;
import fr.miage.gromed.model.enums.TypeAlerte;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlerteStockDecisionDto {
    private AlerteDecision decision;
    private TypeAlerte typeAlerte;

    //panier ItemDto ou PanierDto (exclusif)
    private PanierItemDto panierItemDto;
    private PanierDto panierDto;

    public boolean isAccept() {
       return decision == AlerteDecision.DELAI_LIVRAISON;
    }

    public boolean isItem() {
        return typeAlerte ==  TypeAlerte.PANIER_ITEM;
    }

    public boolean isRefusePanier() {
        return decision == AlerteDecision.ANNULER_ITEM;
    }

    public boolean isRefuseItem() {
        return decision == AlerteDecision.ANNULER_PANIER;
    }
}
