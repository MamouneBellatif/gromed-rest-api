package fr.miage.gromed.model;

import fr.miage.gromed.model.Etablissement;
import fr.miage.gromed.model.enums.PerimetreUtilisateur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @Column
    private String role;

    @ManyToOne
    @JoinColumn(name = "etablissement_id_fk", referencedColumnName = "etablissement_id")
    private Etablissement etablissement;

    @Column
    @Enumerated(EnumType.STRING)
    private PerimetreUtilisateur perimetre;

}
