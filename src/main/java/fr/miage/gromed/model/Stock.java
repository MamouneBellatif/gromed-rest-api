package fr.miage.gromed.model;

import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.service.listeners.StockListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EntityListeners(StockListener.class)
@NoArgsConstructor
public class Stock {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "stock_id", nullable = false)
    private Long id;

    @Column
    private int quantiteStockLogique;

    @Column
    private int quantiteStockPhysique;

    @Column
    private boolean restockAlertFlag;

    @Version
    @Column(name = "transactional_version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private int version;

    public static final int SEUIL = 100;

}
