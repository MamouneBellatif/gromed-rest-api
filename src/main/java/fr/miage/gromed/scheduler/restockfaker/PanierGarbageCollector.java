package fr.miage.gromed.scheduler.restockfaker;

import fr.miage.gromed.model.Panier;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.repositories.StockRepository;
import fr.miage.gromed.service.PanierService;
import fr.miage.gromed.service.StockService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Component
public class PanierGarbageCollector {

    Logger log = Logger.getLogger(PanierGarbageCollector.class.getName());

    private StockService stockService;

    private PanierService panierService;

    private PanierRepository panierRepository;

    @Autowired
    public PanierGarbageCollector(StockService stockService, PanierService panierService) {
         this.stockService = stockService;
         this.panierService = panierService;
         this.panierRepository = panierRepository;

    }


    @Scheduled(cron ="0 * * * *")
    @Transactional
    public void checkExpiredPanier() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
//       30 minutes
        List<Panier> expiredPaniers = panierRepository.findByDateCreationBefore(timestamp);

            expiredPaniers.forEach(panier -> {
            panier.getItems().forEach(panierItem -> {
                stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite());
            });
            panierRepository.delete(panier);
        });
    }
}
