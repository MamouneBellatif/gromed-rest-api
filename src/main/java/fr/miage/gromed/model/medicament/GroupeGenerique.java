package fr.miage.gromed.model.medicament;

import fr.miage.gromed.model.enums.TypeGenerique;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupeGenerique {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "groupe_generique_id", nullable = false)
    private Long id;

    @Column
    private String codeGenerique;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeGenerique typeGeneriqueEnum;

    @Column
    private String denominationGenerique;

}
