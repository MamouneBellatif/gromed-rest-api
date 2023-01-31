package fr.miage.gromed.service.metier;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private PanierRepository panierRepository;
    @Autowired
    private PresentationRepository presentationRepository;



    public static void checkStockDisponible(Panier panier) {
        panier.getItems().forEach(panierItem -> {
            if (panierItem.getPresentation().getStock().getQuantiteStockLogique() < panierItem.getQuantite()) {
                throw new StockIndisponibleException(panierItem.getPresentation().getCodeCIP(), panierItem.getQuantite());
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Lock(LockModeType.OPTIMISTIC)
    public void updateStock(Presentation presentation, int quantity, boolean isCancellingOrder, boolean isLogicalStock) {
        Stock stock = presentation.getStock();
        var oldStock = isLogicalStock ? stock.getQuantiteStockLogique() : stock.getQuantiteStockPhysique();
        int newQuantity = oldStock +(isCancellingOrder ? (quantity): -quantity);
        if (!isCancellingOrder && newQuantity <  0){
            throw new StockIndisponibleException(presentation.getCodeCIP(), quantity);
        }
        stock.setQuantiteStockLogique(newQuantity);
        if (isLogicalStock && stock.getQuantiteStockLogique() < Stock.SEUIL) {
            stock.setRestockAlertFlag(true);
        }
        stockRepository.save(stock);
        presentationRepository.save(presentation);

        }

    @Transactional
    public void resetStockLogique(List<Panier> expiredCarts) {
        expiredCarts.forEach(this::resetStockLogique);
    }

    @Transactional
    public void resetStockLogique(Panier panier) {
        panier.getItems().forEach(panierItem -> {
            this.updateStock(panierItem.getPresentation(), panierItem.getQuantite(), true, true);
        });
    }

    Logger logger = Logger.getLogger(StockService.class.getName());

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void cleanExpiredCarts() {
        logger.info("Cleaning expired carts");
        List<Panier> expiredCarts = panierRepository.findAllByExpiresAtAfterAndExpired(LocalDateTime.now(), false);
        this.resetStockLogique(expiredCarts);
        expiredCarts.forEach(panier -> {
            panier.setExpired(true);
            panierRepository.save(panier);
        });
    }

}
