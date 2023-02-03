package fr.miage.gromed.scheduler.restockfaker;

import fr.miage.gromed.model.ComptabiliteInterne;
import fr.miage.gromed.model.Stock;
import fr.miage.gromed.model.enums.ComptabiliteParametres;
import fr.miage.gromed.repositories.StockRepository;
import fr.miage.gromed.service.metier.ComptabiliteInterneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Component
public class RestockFaker {

    Logger log = Logger.getLogger(RestockFaker.class.getName());
    final
    ComptabiliteInterneService comptabiliteInterneService;

    private final StockRepository stockRepository;

    @Autowired
    public RestockFaker(ComptabiliteInterneService comptabiliteInterneService, StockRepository stockRepository) {
        this.comptabiliteInterneService = comptabiliteInterneService;
        this.stockRepository = stockRepository;
    }

    @Scheduled(cron ="0 0 0 * * *")
        @Transactional
    public void restock() {
        log.info("restockage");
        List<Stock> toRestock = stockRepository.findByRestockAlertFlagTrue();
        toRestock.forEach(stock -> {
            Double prix = stock.getPresentation().getPrixDeBase()-0.5;
           comptabiliteInterneService.cashTransaction(ComptabiliteParametres.CHARGES, prix);
           stock.setQuantiteStockPhysique(stock.getQuantiteStockPhysique() + 100);
            stock.setRestockAlertFlag(false);
            stockRepository.save(stock);
        });
    log.info( "Restock done");


    }
}
