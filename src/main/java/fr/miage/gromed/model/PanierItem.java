package fr.miage.gromed.model;

import fr.miage.gromed.model.medicament.Presentation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Setter
@Getter
public class PanierItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "panier_element_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private int quantite;

    @OneToOne
    @JoinColumn(name = "panier_id_fk", referencedColumnName = "panier_id")
    private Panier panier;

    @OneToOne
    @JoinColumn(name = "presentation_id_fk", referencedColumnName = "presentation_id")
    private Presentation presentation;

    private int delivree;

    public PanierItem(Presentation presentation, int quantity) {
        this.presentation = presentation;
        this.quantite = quantity;
    }

    public PanierItem() {

    }
}
