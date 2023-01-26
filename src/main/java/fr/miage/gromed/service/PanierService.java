package fr.miage.gromed.service;

import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PanierElementRepository;
import fr.miage.gromed.repositories.PanierRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
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
    public PanierDto updatePanier(int codeCIP, int quantite, Presentation presentation, long idPanier) {
        Panier panier = panierRepository.findById(idPanier).get();
        panier.getItems().forEach(panierItem -> {
//            panierItem.getPresentation().
            stockService.updateStock(panierItem.getId(), panierItem.getQuantite());
        });
      
         panierRepository.save(panier);
    }
    

    public PanierDto getPanier(long idPanier) {
        Panier panier = panierRepository.findById(idPanier).get();
        PanierDto panierDto = new PanierDto();
        panierDto.setId(panier.getId());
        panierDto.setItems(panier.getItems());
        return panierDto;
    }
    
    public ResponseEntity<PanierDto> delete(long idPanier) {
        Panier panier = panierRepository.findById(idPanier).get();
        panier.getItems().forEach(panierItem -> {
            stockService.updateStock(panierItem.getId(), panierItem.getQuantite());
        });
        panierRepository.delete(panier);
    }

    @Transactional
    public ResponseEntity<PanierDto> addItemsToPanier(int idPanier, PresentationDto presentationDto) {
        Panier panier = panierRepository.findById(idPanier);
        PresentationDto presentation = mapDtoToPresentation(presentationDto);
        panier.addItemsToPanier(presentation);
        panierRepository.save(panier);
        return new ResponseEntity<>(mapPanierToDto(panier), HttpStatus.OK);
    }
}
