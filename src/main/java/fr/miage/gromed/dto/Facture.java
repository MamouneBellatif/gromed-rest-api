package fr.miage.gromed.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class Facture implements Serializable {

    private Long idPanier;
    private Double montant;
    private LocalDateTime date;
    private String nomClient;
    private List<ItemFacture> produitsAchetes;


}
