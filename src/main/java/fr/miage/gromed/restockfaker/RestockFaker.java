package fr.miage.gromed.restockfaker;

import fr.miage.gromed.model.medicament.Stock;
import fr.miage.gromed.repositories.StockRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class RestockFaker {

    Logger log = Logger.getLogger(RestockFaker.class.getName());
    @Autowired
    private StockRepository stockRepository;
    @Scheduled(cron ="0 0 0 * * *")
    public void restock() {
        List<Stock> toRestock = stockRepository.findByRestockAlertFlagTrue();
        toRestock.forEach(stock -> {
            stock.setQuantiteStockPhysique(stock.getQuantiteStockPhysique() + 100);
            stock.setRestockAlertFlag(false);
            stockRepository.save(stock);
        });
    log.info( "Restock done");

    }
}
