package fr.miage.gromed.scheduler.restockfaker;

import fr.miage.gromed.model.Panier;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.service.metier.PanierService;
import fr.miage.gromed.service.metier.StockService;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PanierCleanExpired implements Runnable {

    Logger log = Logger.getLogger(PanierCleanExpired.class.getName());

    private StockService stockService;

    private PanierService panierService;

    private PanierRepository panierRepository;

//    @Autowired
    public PanierCleanExpired(StockService stockService, PanierService panierService) {
        this.stockService = stockService;
        this.panierService = panierService;
        this.panierRepository = panierRepository;
    }

    @Setter
    private Long idPanier;


    @Override
    @Transactional
    public void run() {
        if (this.idPanier != null) {
            LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(30);
            Optional<Panier> panierOpt = panierRepository.findById(idPanier);
            panierOpt.ifPresent(panier -> {panierService.resetStockLogique(panier);
                                           panier.setExpired(true);
                                           panierRepository.save(panier);
            });
       }
    }
}
