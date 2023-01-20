package fr.miage.gromed.model.medicament;


import fr.miage.gromed.model.enums.TypeAvis;
import fr.miage.gromed.model.enums.ValeurAvis;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
public class MedicamentAvis {



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
    private ValeurAvis valeur;

    @Column
    private String libelle;

    //Discriminant type enum, on évite l'héritage
    @Column
    @Enumerated(EnumType.STRING)
    private TypeAvis typeAvisEnum;
}
