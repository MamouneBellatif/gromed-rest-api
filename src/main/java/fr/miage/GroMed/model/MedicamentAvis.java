package fr.miage.GroMed.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
public class MedicamentAvis {
    public enum TypeAvisEnum {
        ASMR,
        SMR
    }

    public enum ValeurAvisEnum {
        I,
        II,
        III,
        IV,
        V,
        IMPORTANT,
        MODERE,
        FAIBLE,
        INSUFFISANT,

    }

    @Id
    @Column(name = "medicament_avis_id", nullable = false)
    private Long id;

    @Column
    private String codeHAS;

    @Column
    private String motif;

    @Column
    private Date dateAvis;

    @Enumerated(EnumType.STRING)
    private ValeurAvisEnum valeur;

    @Column
    private String libelle;

    //Discriminant type enum, on évite l'héritage
    @Column
    private TypeAvisEnum typeAvisEnum;
}
