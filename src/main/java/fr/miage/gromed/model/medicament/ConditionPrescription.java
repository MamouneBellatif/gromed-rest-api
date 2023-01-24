package fr.miage.gromed.model.medicament;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_condition_generique", columnList = "libelle", unique = true)
})
public class ConditionPrescription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "condition_generique_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String libelle;

}
