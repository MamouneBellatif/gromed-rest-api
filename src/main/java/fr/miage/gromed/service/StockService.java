package fr.miage.gromed.service;

import fr.miage.gromed.exceptions.StockIndisponibleException;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.Stock;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.repositories.PresentationRepository;
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
    @Autowired
    private PresentationRepository presentationRepository;

    public static void checkStock(Panier panier) {
    }

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    public void updateStock(Presentation presentation, int quantity, boolean isCancellingOrder) {
        Stock stock = presentation.getStock();
        int newQuantity = stock.getQuantiteStockLogique() +(isCancellingOrder ? (- quantity): quantity);
        if (stock.getQuantiteStockLogique() < quantity) { //h
            throw new StockIndisponibleException();
        }
            stock.setQuantiteStockLogique(newQuantity);
            stockRepository.save(stock);
            presentationRepository.save(presentation);
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
