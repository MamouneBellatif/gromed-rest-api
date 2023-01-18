package fr.miage.GroMed.model;

import fr.miage.GroMed.model.Presentation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Presentation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "presentation_id", nullable = false)
    private Long id;

    @Column
    private int cip13;


    @Column
    private String libelle;

    @Column
    private double prix;

    @Column
    private double honoraire;

    @Column
    private int stockLogique;

    @Column
    private int stockPhysique;


}
