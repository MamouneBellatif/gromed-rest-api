package fr.miage.gromed.scheduler.restockfaker;

import fr.miage.gromed.model.Panier;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.service.metier.PanierService;
import fr.miage.gromed.service.metier.StockService;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PanierCleanExpired implements Runnable {

    Logger log = Logger.getLogger(PanierCleanExpired.class.getName());

    @Autowired
    private  StockService stockService;
//
//    @Autowired
//    private PanierService panierService;

    @Autowired
    private  PanierRepository panierRepository;

    @Setter
    private Long idPanier;


    @Override
    @Transactional
    public void run() {
        if (this.idPanier != null) {
//            LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(30);
//            List<Panier> expiredPaniers = panierRepository.findAllByDateExpirationAfter(LocalDateTime.now());
            Optional<Panier> panierOpt = panierRepository.findById(idPanier);
            panierOpt.ifPresent(panier -> {stockService.resetStockLogique(panier);
                                           panier.setExpired(true);
                                           panierRepository.save(panier);
            });
       }
    }
}
