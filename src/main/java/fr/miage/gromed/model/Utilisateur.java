package fr.miage.gromed.model;

import fr.miage.gromed.model.Etablissement;
import fr.miage.gromed.model.enums.PerimetreUtilisateur;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Utilisateur {

    @Id
    @Column(name = "user_id", nullable = false)
    private String id;

    @Column
    private String nom;

    @Column( unique = true)
    private String email;

    @Column
    private String role;

    @ManyToOne
    @JoinColumn(name = "etablissement_id_fk", referencedColumnName = "etablissement_id")
    private Etablissement etablissement;

    @Column
    @Enumerated(EnumType.STRING)
    private PerimetreUtilisateur perimetre;

    @Column
    private boolean awaitingResponse;

    @Column
    private boolean isBuying;

    @ElementCollection
    private List<String> notifications;


}
