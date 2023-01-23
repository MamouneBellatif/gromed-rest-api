package fr.miage.gromed.model.medicament;

import fr.miage.gromed.model.Etablissement;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_medicament_denomination", columnList = "denomination"),
        @Index(name = "idx_medicament_codecis_unq", columnList = "codeCIS", unique = true)
})
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
    private Boolean isSurveillanceRenforcee;

    @Column
    private String etatCommercialisation;

    @Temporal(TemporalType.DATE)
    private Date dateAMM;

    @Column
    private String statutBDM;

    @Column
    private String statutAdministratif;

    @Column
    private String numeroAutorisationEuro;

    @Column
    private String conditionsPrescription;

    @Column
    private String informationImportantesHtmlAnchor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="etablissement_id_fk", referencedColumnName = "etablissement_id")
    private Etablissement laboratoire;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
    private Set<Presentation> presentationList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
    private Set<GroupeGenerique>  groupeGeneriqueList;

//    @OneToMany(mappedBy = "medicaments",cascade = CascadeType.ALL)
    @OneToMany( cascade  = CascadeType.ALL)
    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
    private Set<ComposantSubtance> composantList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id", referencedColumnName = "medicament_id")
    private Set<ConditionPrescription> conditionPrescriptionList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="medicament_id", referencedColumnName = "medicament_id")
    private Set<MedicamentAvis> medicamentAvisList;

    public void addPresentation(Presentation presentation){
        this.presentationList.add(presentation);
    }

    public void addAvis(MedicamentAvis avis){
        this.medicamentAvisList.add(avis);
    }

    public void addComposant(ComposantSubtance composantSubtance) {
        this.composantList.add(composantSubtance);
    }
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
