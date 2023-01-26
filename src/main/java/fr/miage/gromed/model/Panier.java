package fr.miage.gromed.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.model.medicament.Presentation;
import org.hibernate.annotations.Type;

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



//    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "panier_id_fk", referencedColumnName = "panier_id")
    private List<PanierItem> items;

    //date with hour and minute

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateCreation;

    @Column
    private boolean isPaid;

    @Column
    private boolean isExpired;

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
    private boolean isShipped;

    @Column
    private boolean isDelivered;

    public void addItem(PanierItem item) {
        this.items.add(item);
    }

    //make the possibility to m

    @Override
    public Long getId() {
        return id;
    }
}
