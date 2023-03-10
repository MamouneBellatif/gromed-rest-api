package fr.miage.gromed.model;

import fr.miage.gromed.model.medicament.Presentation;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PanierItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "panier_element_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private int quantite;

    @ManyToOne
    @JoinColumn(name = "presentation_id_fk", referencedColumnName = "presentation_id")
    private Presentation presentation;

    @ManyToOne(cascade = CascadeType.ALL)
    private Panier panier;

    //Nombre livré de cet item pour la commande
    @Column
    private int delivree;

    @Column
    private int quantiteReservee;

    @Column
    private boolean conflict;

    @Column
    private boolean delayed;

}
