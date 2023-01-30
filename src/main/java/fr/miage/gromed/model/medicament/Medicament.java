package fr.miage.gromed.model.medicament;

import fr.miage.gromed.model.Etablissement;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;


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
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

//    @Column
//    private String conditionsPrescription;

    @Column(name = "url_image", length = 4000)
    private String urlImage;

    @ElementCollection
    @Column(name="url_info", length = 4000)
    private Set<String> informationImportantesHtmlAnchor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="etablissement_id_fk", referencedColumnName = "etablissement_id")
    private Etablissement laboratoire;

//    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
    @OneToMany(mappedBy = "medicament",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Presentation> presentationList;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
    private Set<GroupeGenerique>  groupeGeneriqueList;

//    @OneToMany(mappedBy = "medicaments",cascade = CascadeType.ALL)
    @OneToMany( cascade  = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
    private Set<ComposantSubtance> composantList;

    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<ConditionPrescription> conditionPrescriptionList;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="medicament_id_fk", referencedColumnName = "medicament_id")
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

    public void addInfo(String info){
        this.informationImportantesHtmlAnchor.add(info);
    }

    public void addConditionPrescription(ConditionPrescription conditionPrescription){
        this.conditionPrescriptionList.add(conditionPrescription);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Medicament that = (Medicament) o;
        return id != null && Objects.equals(id, that.id) || Objects.equals(codeCIS, that.codeCIS);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString(){
        return "medoc CIS "+this.codeCIS+" id: "+this.id+" denom "+this.denomination;
    }


    public void addConditionsPrescription(ConditionPrescription conditionPrescription) {
        this.conditionPrescriptionList.add(conditionPrescription);
    }
}
