package fr.miage.gromed.model;

import fr.miage.gromed.model.medicament.Utilisateur;
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Column
    private boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "user_id_fk", referencedColumnName = "user_id")
    private Utilisateur client;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaiement;

    @Temporal(TemporalType.TIMESTAMP)
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
