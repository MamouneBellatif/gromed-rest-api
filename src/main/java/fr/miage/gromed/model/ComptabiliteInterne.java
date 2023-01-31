package fr.miage.gromed.model;

import fr.miage.gromed.model.enums.ComptabiliteParametres;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ComptabiliteInterne {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private ComptabiliteParametres parametre;

    @Column
    private double valeur;

    @Version
    private Long version;
}
