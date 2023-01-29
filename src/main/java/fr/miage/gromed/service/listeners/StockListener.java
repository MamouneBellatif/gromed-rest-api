package fr.miage.gromed.service.listeners;

import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.model.Stock;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.repositories.StockRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockListener {

    @Autowired
    private StockRepository stockRepository;

    @PostPersist
    @Transactional
    public void postPersist(Stock stock) {
        if (stock.getQuantiteStockLogique() < Stock.SEUIL) {
            stock.setRestockAlertFlag(true);
            stockRepository.save(stock);
        }
    }

    @PrePersist
    @Transactional
    public void prePersist(Stock stock) {
        if (stock.getQuantiteStockPhysique() < 0) {
            throw new RuntimeException("Le stock ne peut pas être négatif");
        }
    }

}
