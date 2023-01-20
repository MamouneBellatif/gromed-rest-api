package fr.miage.gromed.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Etablissement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "etablissement_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(nullable = false, unique = true)
    private String adresse;

    @Column( unique = true)
    private String telephone;

    @Column
    private String type;

}
