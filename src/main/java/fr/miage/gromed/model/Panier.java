package fr.miage.gromed.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Panier implements AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "panier_id", nullable = false)
    private Long id;



//    @JoinColumn(name = "panier_id_fk", referencedColumnName = "panier_id")
    @OneToMany(fetch = FetchType.EAGER ,mappedBy="panier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PanierItem> items;

    //date with hour and minute
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime expiresAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateCreation;

    @Column
    private boolean paid;

    @Column
    private boolean canceled;

    @Column
    private boolean awaitingResponse;

    @Column
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id_fk", referencedColumnName = "user_id")
    private Utilisateur client;

    //    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime datePaiement;


    @Column
    private Date dateLivraison;

    @Column
    private String adresseLivraison;

    @Column
    private boolean isShipped;

    @Column
    private boolean delivered;

    @Column
    private boolean delayed;

    public void addItem(PanierItem item) {
        this.items.add(item);
    }

    //make the possibility to m

    @Override
    public Long getId() {
        return id;
    }

}
