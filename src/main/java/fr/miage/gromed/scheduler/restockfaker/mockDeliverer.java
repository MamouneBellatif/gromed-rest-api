package fr.miage.gromed.scheduler.restockfaker;

import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.PanierItem;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PanierItemRepository;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.service.metier.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//TODO: delivrer lors du paiement et lors de la disponibilité du stock d'une commande a moitiée livrée
@Component
public class mockDeliverer {


    @Autowired
    private PresentationRepository presentationRepository;
    @Autowired
    private PanierRepository panierRepository;
    @Autowired
    private PanierItemRepository panierItemRepository;

    @Autowired
    private StockService stockService;

        public void deliver(PanierItem panierItem, int quantite) throws PresentationNotFoundException {


            Presentation presentation = presentationRepository.findById(panierItem.getPresentation().getId()).orElseThrow(PresentationNotFoundException::new);
            Panier panier = panierItem.getPanier();
            panierItem.setDelivree(panierItem.getDelivree() + quantite);
            stockService.updateStock(presentation, quantite,false, true);
            panierItemRepository.save(panierItem);
            panierRepository.save(panier);

        }
}
