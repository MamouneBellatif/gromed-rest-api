package fr.miage.gromed.service.listeners;

import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.model.medicament.Stock;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.repositories.StockRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class StockListener {

    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private StockRepository stockRepository;

    //after persisting a stock, if it goes undes the SEUIL, we set the restockAlert to true
    @PostPersist
    public void postPersist(Stock stock) {
        if (stock.getQuantiteStockPhysique() < Presentation.SEUIL) {
            stock.setRestockAlertFlag(true);
            stockRepository.save(stock);
        }
    }

    //if the stock goes under 0, we rollback the transaction
    @PrePersist
    @Transactional
    public void prePersist(Presentation presentation) {
        if (presentation.getStock().getQuantiteStockLogique() < 0) {
            throw new RuntimeException("Le stock ne peut pas être négatif");
        }
    }

}
