package fr.miage.gromed.scheduler.restockfaker;

import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.PanierItem;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PanierItemRepository;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.service.metier.StockService;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

//TODO: delivrer lors du paiement et lors de la disponibilité du stock d'une commande a moitiée livrée
@Component
public class mockDeliverer {


    private final PresentationRepository presentationRepository;
    private final PanierRepository panierRepository;
    private final PanierItemRepository panierItemRepository;

    private final StockService stockService;

    public mockDeliverer(PresentationRepository presentationRepository, PanierRepository panierRepository, PanierItemRepository panierItemRepository, StockService stockService) {
        this.presentationRepository = presentationRepository;
        this.panierRepository = panierRepository;
        this.panierItemRepository = panierItemRepository;
        this.stockService = stockService;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void deliver(PanierItem panierItem, int quantite) throws PresentationNotFoundException {
            Presentation presentation = presentationRepository.findById(panierItem.getPresentation().getId()).orElseThrow(PresentationNotFoundException::new);
            Panier panier = panierItem.getPanier();
            panierItem.setDelivree(panierItem.getDelivree() + quantite);
            stockService.updateStock(presentation, quantite,false, false);
            panierItemRepository.save(panierItem);
            panierRepository.save(panier);
        }
        @Transactional
        public void deliverItem(PanierItem panierItem) throws PresentationNotFoundException {

            int quantite = panierItem.getQuantite() - panierItem.getDelivree();
            if (quantite > panierItem.getPresentation().getStock().getQuantiteStockPhysique()) {
//                quantite = panierItem.getPresentation().getStockLogique();
            }
            deliver(panierItem, quantite);
        }


    @Scheduled(fixedRate = 2,timeUnit = TimeUnit.HOURS)
    public void deliverOrders()  {
            panierRepository.findByPaidTrueAndDeliveredFalseOrderByDatePaiement().forEach(panier -> {
                panier.getItems().forEach(panierItem -> {
                    try {
                        if (panierItem.getDelivree() < panierItem.getQuantite()) {
                            deliverItem(panierItem);
                        }
                        deliver(panierItem, panierItem.getQuantite());
                    } catch (PresentationNotFoundException e) {
                        e.printStackTrace();
                    }
                });
                panier.setDelivered(true);
                panierRepository.save(panier);
            });

        }

        //order by datePaiement, tentative livraison des commandes non livrées, s'assurer d'avoir du stocl
    //pour quelqun qui a confirmé sa commande
}
