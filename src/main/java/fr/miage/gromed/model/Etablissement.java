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

    @Column
    private String nom;

    @Column
    private String adresse;

    @Column
    private String telephone;
    
    @Column
    private String raisonSociale;

    @Column
    private String numSiret;
    @Column
    private String categorie;

}
