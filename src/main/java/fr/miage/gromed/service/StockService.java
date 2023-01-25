package fr.miage.gromed.service;

import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.PanierItem;
import fr.miage.gromed.model.Stock;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.repositories.StockRepository;
import jakarta.persistence.LockModeType;
import jakarta.persistence.RollbackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private PanierRepository panierRepository;

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    public void updateStock(Presentation presentation, int quantity) {
//        Optional<Stock> stockOpt = stockRepository.findByPresentationId(productId);
//        Optional
//        if (stockOpt.isEmpty()) {
//            throw new RollbackException("Stock not found");
//        }
        Stock stock = presentation.getStock();
//        Stock stock = stockOpt.get();
        stock.setQuantiteStockLogique(stock.getQuantiteStockLogique() - quantity);
        stockRepository.save(stock);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addToPanier(Long panierId, Presentation presentation, int quantity) {
        Stock stock = presentation.getStock();
        if (stock.getQuantiteStockLogique() < quantity) {
            throw new RollbackException("Stock indisponible");
        }
        stock.setQuantiteStockLogique(stock.getQuantiteStockLogique() - quantity);
        Optional<Panier> panierOpt = panierRepository.findById(panierId);
        if (panierOpt.isEmpty()) {
            throw new RollbackException("Panier not found");
        }
        Panier panier = panierOpt.get();
//        panier.addItem(new PanierItem(stock.getPresentation(), quantity));
        panierRepository.save(panier);
    }
}