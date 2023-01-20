package fr.miage.gromed.model.medicament;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ConditionPrescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "condition_generique_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String libelle;


}
