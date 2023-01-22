package fr.miage.gromed.model.medicament;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "Medicament", indexes = {
        @Index(name = "idx_medicament_denomination", columnList = "denomination"),
        @Index(name = "idx_medicament_codecis_unq", columnList = "codeCIS", unique = true)
})

@Getter
@Setter
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "medicament_id", nullable = false)
    private Long id;

    @Column(nullable= false, unique = true)
    private int codeCIS;

    @Column
    private String denomination;

    @Column
    private String formePharmaceutique;

    @Column
    private String voiesAdministration;

    @Column
    private String typeProcedureAMM;

    @Column
    private boolean surveillanceRenforcee;

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

    @Column
    private String informationImportantesHtmlAnchor;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
    private List<Presentation> presentationList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
    private List<GroupeGenerique>  groupeGeneriqueList;

//    @OneToMany(mappedBy = "medicaments",cascade = CascadeType.ALL)
    @OneToMany( cascade  = CascadeType.ALL)
    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
    private List<ComposantSubtance> composantList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id", referencedColumnName = "medicament_id")
    private List<ConditionPrescription> conditionPrescriptionList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id", referencedColumnName = "medicament_id")
    private List<MedicamentAvis> medicamentAvisList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Medicament that = (Medicament) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
