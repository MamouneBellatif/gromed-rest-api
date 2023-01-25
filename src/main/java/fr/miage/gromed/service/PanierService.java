package fr.miage.gromed.service;

import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.repositories.PanierElementRepository;
import fr.miage.gromed.repositories.PanierRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class
PanierService {

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private PanierElementRepository panierElementRepository;

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    public void updatePanier(long productId, int quantity) {
        Panier panier = panierRepository.findById(productId).get();
        panier.getItems().forEach(panierItem -> {
//            panierItem.getPresentation().
            stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite());
        panierItem.setQuantite(panierItem.getQuantite() + quantity);
        });
        panierRepository.save(panier);
    }

    public ResponseEntity<MedicamentDto> getPanier(int idPanier) {
        return null;
    }
}
