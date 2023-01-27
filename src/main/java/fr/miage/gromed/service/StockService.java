package fr.miage.gromed.service;

import fr.miage.gromed.exceptions.PanierNotFoundException;
import fr.miage.gromed.exceptions.StockIndisponibleException;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.Stock;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.repositories.StockRepository;
import jakarta.persistence.LockModeType;
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
        panier.getItems().forEach(panierItem -> {
//            if (panierItem.getStock().getQuantiteStockLogique() < panierItem.getQuantity()) {
//                throw new StockIndisponibleException();
//            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Lock(LockModeType.OPTIMISTIC)
    public void updateStock(Presentation presentation, int quantity, boolean isCancellingOrder, boolean isLogicalStock) {
        Stock stock = presentation.getStock();
        var oldStock = isLogicalStock ? stock.getQuantiteStockLogique() : stock.getQuantiteStockPhysique();
        int newQuantity = oldStock +(isCancellingOrder ? (- quantity): quantity);
        if (!isCancellingOrder && newQuantity <  quantity){
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
            throw new StockIndisponibleException();
        }
        stock.setQuantiteStockLogique(stock.getQuantiteStockLogique() - quantity);
        Optional<Panier> panierOpt = panierRepository.findById(panierId);
        if (panierOpt.isEmpty()) {
            throw new PanierNotFoundException();
        }
        Panier panier = panierOpt.get();
        stockRepository.save(stock);
//        panier.addItem(new PanierItem(stock.getPresentation(), quantity));
//        panierRepository.save(panier);
    }
}
