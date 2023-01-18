package fr.miage.GroMed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;

@Entity
@Getter
@Setter
public class Composant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "composant_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String denomination;

    @Column(nullable = false, unique = true)
    private String code;

    @Column
    private NatureComposantEnum dosage;

    @Column
    private String referenceDosage;
    private enum NatureComposantEnum  {
        SA,
        FT
    }


}
