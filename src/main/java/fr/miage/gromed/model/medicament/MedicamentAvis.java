package fr.miage.gromed.model.medicament;


import fr.miage.gromed.model.enums.TypeAvis;
import fr.miage.gromed.model.enums.ValeurAvis;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentAvis {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "medicament_avis_id", nullable = false)
    private Long id;

    @Column
    private String codeDossier;

    @Column
    private String motif;

    @Temporal(TemporalType.DATE)
    private Date dateAvis;

    @Enumerated(EnumType.STRING)
    private ValeurAvis valeur;

    @Column(length = 4000)
    private String libelle;

    //Discriminant type enum, on évite l'héritage
    @Column
    @Enumerated(EnumType.STRING)
    private TypeAvis typeAvisEnum;
}
