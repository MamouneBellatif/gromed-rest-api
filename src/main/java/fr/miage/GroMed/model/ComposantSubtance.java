package fr.miage.GroMed.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class ComposantSubtance {
    private enum NatureComposantEnum  {
        SA,
        FT
    }

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
    private NatureComposantEnum natureComposant;

    @Column
    private String Dosage;

    @Column
    private String referenceDosage;


//    @ManyToOne(optional = false)
//    private Medicament medicaments;


}
