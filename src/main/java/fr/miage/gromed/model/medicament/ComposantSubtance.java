package fr.miage.gromed.model.medicament;

import fr.miage.gromed.model.enums.NatureComposant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class ComposantSubtance {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "composant_id", nullable = false)
    private Long id;

    @ElementCollection
    private  List<String>  denominationList;

    @Column
    private String designationElementPharmaceutique;

    @Column(nullable = false, unique = true)
    private String code;

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
