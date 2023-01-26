package fr.miage.gromed.service;

import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.dto.PanierItemDto;
import fr.miage.gromed.exceptions.StockIndisponibleException;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.scheduler.restockfaker.PanierCleanExpired;
import fr.miage.gromed.service.mapper.PanierItemMapper;
import fr.miage.gromed.service.mapper.PanierMapper;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.time.Instant.now;

@Service
public class
PanierService {

    private final PanierRepository panierRepository;

    private final StockService stockService;

    private final PanierMapper panierMapper;

    private final PanierItemMapper panierItemMapper;



    @Autowired
    public PanierService(PanierItemMapper panierItemMapper, PanierRepository panierRepository, StockService stockService, PanierMapper panierMapper) {
        this.panierRepository = panierRepository;
        this.stockService = stockService;
        this.panierMapper = panierMapper;
        this.panierItemMapper = panierItemMapper;

    }

    @Transactional
    public void confirmOrder(Long idPanier) throws StockIndisponibleException {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if(panierOpt.isEmpty()) {
            return;
        }
        Panier panier = panierOpt.get();
        StockService.checkStock(panier);

        panierRepository.delete(panier);
//        return true;
        return;

    }

    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    public void updatePanier(long productId, int quantity) {
        Panier panier = panierRepository.findById(productId).get();
        panier.getItems().forEach(panierItem -> {
//            panierItem.getPresentation().
            stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(), true);
        panierItem.setQuantite(panierItem.getQuantite() + quantity);
        });
        panierRepository.save(panier);
    }

    @Transactional
    public PanierDto getPanier(Long idPanier) {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if(panierOpt.isEmpty()) {
            return null;
        }
        Panier panier = panierOpt.get();
        return panierMapper.toDto(panier);
    }

    @Transactional
    public boolean delete(Long idPanier) {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if(panierOpt.isEmpty()) {
            return false;
        }
        Panier panier = panierOpt.get();
        panier.getItems().forEach(panierItem -> {
            stockService.updateStock(panierItem.getPresentation(), - panierItem.getQuantite(), true);
        });
        panierRepository.delete(panier);
        return true;
    }

    @Transactional
    public PanierDto addItemsToPanier(Long idPanier, PanierItemDto panierItemDto) {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if(panierOpt.isEmpty()) {
            return null;
        }
        Panier panier = panierOpt.get();

        panier.addItem(panierItemMapper.toEntity(panierItemDto));
        panier.getItems().forEach(panierItem -> {
            stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(),false);
        });

//        PresentationDto presentation = mapDtoToPresentation(presentationDto);
//        panier.addItemsToPanier(presentation);
//        panierRepository.save(panier);
        return null;
    }

    private PanierItemDto sanitizeItemsInput(PanierItemDto panierItemDto) {
        panierItemDto.setQuantite(panierItemDto.getQuantite() > 0 ? panierItemDto.getQuantite() : 1);
        return panierItemDto;
    }

    //Ajouter USER apres l'auth
    @Transactional
    public PanierDto createPanier(Set<PanierItemDto> itemDtoSet) {
        Panier panier = Panier.builder()
                        .dateCreation(LocalDateTime.now())
                        .isPaid(false)
                        .isExpired(false)
                .isShipped(false)
                .isDelivered(false)
//                .items()

        .build();

        panierRepository.save(panier);
//        PanierCleanExpired.run();
        return panierMapper.toDto(panier);
    }

    @Transactional
    public void resetStockLogique(List<Panier> expiredCarts) {
        expiredCarts.forEach(this::resetStockLogique);
    }

    @Transactional
    public void resetStockLogique(Panier panier) {
        panier.getItems().forEach(panierItem -> {
            stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(), true);
        });
    }
}
