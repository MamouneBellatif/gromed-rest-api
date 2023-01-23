package fr.miage.gromed.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Entity
@Data
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "panier_id", nullable = false)
    private Long id;

    @OneToMany
    @JoinColumn(name = "panier_id_fk", referencedColumnName = "panier_id")
    private List<PanierItem> items;

    //date with hour and minute
//    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date dateCreation;

    @Column
    private boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "user_id_fk", referencedColumnName = "user_id")
    private Utilisateur client;

    //    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date datePaiement;

    @Column
    private Date dateLivraison;

    @Column
    private String adresseLivraison;

    @Column
    private boolean isDelivered;

    public void addItem(PanierItem item) {
        this.items.add(item);
    }

    //make the possibility to m
}
