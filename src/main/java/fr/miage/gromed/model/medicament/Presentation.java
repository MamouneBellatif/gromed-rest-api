package fr.miage.gromed.model.medicament;

//import fr.miage.gromed.model.listeners.StockListener;
import fr.miage.gromed.model.Stock;
import fr.miage.gromed.service.listeners.StockListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
//@EntityListeners(StockListener.class)
@NoArgsConstructor
public class Presentation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "presentation_id", nullable = false)
    private Long id;

    @Column
    private Long codeCIP;

    @Column
    private String libelle;

    @Column
    private double prixDeBase;

    @Column
    private double honoraireRemboursement;

    @Temporal(TemporalType.DATE)
    private Date dateDeclaration;

    @Column
    private String etatCommercialisation;

    @Column
    private String statutAdmin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id_fk",referencedColumnName = "stock_id")
    private Stock stock;

//    Si c'est faux un utilisateur de type etablissement hopital
//    ne peut pas acheter ce produit
    @Column
    private Boolean isAgrement;

    @Column
    private String tauxRemboursement;

//    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
//    private Pres medicament;






}
