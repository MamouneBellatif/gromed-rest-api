package fr.miage.gromed.scheduler.restockfaker;

import fr.miage.gromed.model.Stock;
import fr.miage.gromed.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Component
public class RestockFaker {

    Logger log = Logger.getLogger(RestockFaker.class.getName());
    @Autowired
    private StockRepository stockRepository;
        @Scheduled(cron ="0 0 0 * * *")
        @Transactional
    public void restock() {
        List<Stock> toRestock = stockRepository.findByRestockAlertFlagTrue();
        toRestock.forEach(stock -> {
            Double prix = stock.getPresentation().getPrixDeBase()-0.5;
//            stock.setPrix(prix); TODO:  rajouter le prix aux charges
            stock.setQuantiteStockPhysique(stock.getQuantiteStockPhysique() + 100);
            stock.setRestockAlertFlag(false);
            stockRepository.save(stock);
        });
    log.info( "Restock done");

        //TODO: livrer les commandes en attente

    }
}
