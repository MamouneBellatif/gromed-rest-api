package fr.miage.gromed.model;

import fr.miage.gromed.model.enums.ComptabiliteParametres;
import fr.miage.gromed.service.listeners.ComptabiliteListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EntityListeners(ComptabiliteListener.class)
public class ComptabiliteInterne {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ComptabiliteParametres parametre;

    @Column
    private double valeur;

    @Version
    private Long version;
}
