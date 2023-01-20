package fr.miage.gromed.model;

//import fr.miage.gromed.model.listeners.StockListener;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.service.listeners.StockListener;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(StockListener.class)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stock_id", nullable = false)
    private Long id;

    private int quantiteStockLogique;

    private int quantiteStockPhysique;

    @Column
    private boolean restockAlertFlag;

    @OneToOne
    @JoinColumn(name = "presentation_id_fk", referencedColumnName = "presentation_id", unique = true)
    private Presentation presentation;

}
