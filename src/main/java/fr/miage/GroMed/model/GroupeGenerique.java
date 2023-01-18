package fr.miage.GroMed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GroupeGenerique {
    public enum TypeGeneriqueEnum {
        GENERIQUE,
        GENERIQUE_SUBSTITUABLE,
        PRINCEPS,
        GENERIQUE_COMPTABILITE_POSOLOGIQUE
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "groupe_generique_id", nullable = false)
    private Long id;

    //Dans le modèle, le type generique est un heritage de medicament
    // mais pour le code on choisit d'éviter l'héritage et on ajoute un attribut discriminant le type de médicament
    @Column
    @Enumerated(EnumType.STRING)
    private TypeGeneriqueEnum typeGeneriqueEnum;

    @Column
    private String denominationGenerique;

}
