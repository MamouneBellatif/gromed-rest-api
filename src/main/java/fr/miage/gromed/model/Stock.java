package fr.miage.gromed.model;

import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.service.listeners.StockListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@EntityListeners(StockListener.class)
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stock_id", nullable = false)
    private Long id;

    @Setter
    @Column
    private int quantiteStockLogique;

    @Column
    private int quantiteStockPhysique;

    @Column
    private boolean restockAlertFlag;


    @OneToOne
    private Presentation presentation;

    @Version
    @Column(name = "transactional_version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private int version;


}
