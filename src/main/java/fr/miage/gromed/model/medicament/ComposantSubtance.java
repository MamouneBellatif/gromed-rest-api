package fr.miage.gromed.model.medicament;

import fr.miage.gromed.model.enums.NatureComposant;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
public class ComposantSubtance {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "composant_id", nullable = false)
    private Long id;

    @Column
    private int codeSubstance;

//    @ElementCollection
//    private Set<String> denominationList;
    @Column
    private String denomination;

    @Column
    private String designationElementPharmaceutique;

    @Column
    @Enumerated(EnumType.STRING)
    private NatureComposant natureComposant;

    @Column
    private String dosage;

    @Column
    private String referenceDosage;


//    @ManyToOne(optional = false)
//    private Medicament medicaments;


}
