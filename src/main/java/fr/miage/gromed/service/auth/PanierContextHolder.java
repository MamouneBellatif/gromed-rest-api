package fr.miage.gromed.service.auth;

import fr.miage.gromed.dto.PanierItemDto;

import java.util.Map;

public class PanierContextHolder {

    /**
     * Item a ajouter au panier et nombre de tentative d'ajout
     */
    private static final ThreadLocal<PanierItemDto> PANIER = ThreadLocal.withInitial(() -> null);
    public  static final int MAX_TRY = 3;
    public static PanierItemDto getPanierItemDto() {
        return PANIER.get();
    }

    public static void setPanierItemDto(PanierItemDto panierItemDto) {
        if (panierItemDto == null) {
            throw new NullPointerException("panierItem null");
        }
        PANIER.set(panierItemDto);
    }

    public static void clear() {
        PANIER.remove();
    }
}
