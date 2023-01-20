package fr.miage.gromed.model.medicament;

//import fr.miage.gromed.model.listeners.StockListener;
import fr.miage.gromed.model.Stock;
import fr.miage.gromed.service.listeners.StockListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EntityListeners(StockListener.class)
public class Presentation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "presentation_id", nullable = false)
    private Long id;

    @Column
    private int cip13;


    @Column
    private String libelle;

    @Column
    private double prix;

    @Column
    private double honoraire;

    @OneToOne
    private Stock stock;

    public static final int SEUIL = 100;

    @Version
    @Column(name = "transactional_version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private int version;


}
