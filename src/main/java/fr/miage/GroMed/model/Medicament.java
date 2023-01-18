package fr.miage.GroMed.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "medicament_id", nullable = false)
    private Long id;

    @Column(nullable= false, unique = true)
    private int codeCIS;

    @Column(unique=true)
    private String denomination;

    @Column
    private String formePharmaceutique;

    @Column
    private String voiesAdministration;

    @Column
    private String typeProcedureAMM;

    @Column
    private String etatCommercialisation;

    @Column
    private String dateAMM;

    @Column
    private String statutBDM;

    @Column
    private String statutAdministratif;

    @Column
    private String numeroAutorisation;

    @Column
    private String conditionsPrescription;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id", referencedColumnName = "medicament_id")
    private List<Presentation> presentationList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id", referencedColumnName = "medicament_id")
    private List<GroupeGenerique>  groupeGeneriqueList;

    @OneToMany(mappedBy = "medicaments",cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id", referencedColumnName = "medicament_id")
    private List<ComposantSubtance> composantList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id", referencedColumnName = "medicament_id")
    private List<ConditionPrescription> conditionPrescriptionList;


}
