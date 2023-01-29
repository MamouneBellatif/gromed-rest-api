package fr.miage.gromed.service.metier;

import fr.miage.gromed.dto.AlerteStockDecisionDto;
import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.dto.PanierItemDto;
import fr.miage.gromed.exceptions.*;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.PanierItem;
import fr.miage.gromed.model.Utilisateur;
import fr.miage.gromed.model.medicament.Medicament;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.scheduler.restockfaker.PanierCleanExpired;
import fr.miage.gromed.service.mapper.PanierItemMapper;
import fr.miage.gromed.service.mapper.PanierMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class PanierService {

    private final PanierRepository panierRepository;

    private final StockService stockService;

    private final PanierMapper panierMapper;

    private final PanierItemMapper panierItemMapper;

    @Autowired
    private  PanierCleanExpired panierCleanExpired;
    private final TaskScheduler taskScheduler;

    @Autowired
    public PanierService(TaskScheduler taskScheduler,PanierItemMapper panierItemMapper, PanierRepository panierRepository, StockService stockService, PanierMapper panierMapper) {
        this.panierRepository = panierRepository;
        this.stockService = stockService;
        this.panierMapper = panierMapper;
        this.panierItemMapper = panierItemMapper;
        this.taskScheduler = taskScheduler;
//        this.panierCleanExpired = panierCleanExpired;
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirmOrder(Long idPanier) throws StockIndisponibleException {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if(panierOpt.isEmpty()) {
            return;
        }
        Panier panier = panierOpt.get();
        StockService.checkStockDisponible(panier);
        panierRepository.delete(panier);
//        return true;
        return;

    }

    @Transactional(rollbackFor = Exception.class)
    @Lock(LockModeType.OPTIMISTIC)
    public void updatePanier(long productId, int quantity) {

        Panier panier = panierRepository.findById(productId).get();

        panier.getItems().forEach(panierItem -> {
//            panierItem.getPresentation().
            stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(), true, true);
        panierItem.setQuantite(panierItem.getQuantite() + quantity);
        });
        panierRepository.save(panier);
    }

    @Transactional(rollbackFor = Exception.class)
    public PanierDto getPanier(Long idPanier) {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if(panierOpt.isEmpty()) {
            throw new PanierNotFoundException();
        }

        Panier panier = panierOpt.get();
        if (checkExpiredPanier(panier)){
            throw new ExpiredPanierException();
        }
        return panierMapper.toDto(panier);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long idPanier) {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if(panierOpt.isEmpty()) {
            return false;
        }
        Panier panier = panierOpt.get();
        panier.getItems().forEach(panierItem -> {
            stockService.updateStock(panierItem.getPresentation(), - panierItem.getQuantite(), true, true);
        });
        panierRepository.delete(panier);
        return true;
    }

    //TODO: Confirmer et payer le panier

    @Transactional(rollbackFor = Exception.class)
    public PanierDto addItemsToPanier(Long idPanier, PanierItemDto panierItemDto) {
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if(panierOpt.isEmpty()) {
            return null;
        }
        Panier panier = panierOpt.get();

        panier.addItem(panierItemMapper.toEntity(panierItemDto));
        panier.getItems().forEach(panierItem -> {
            stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(),false, true);
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

    private boolean hasActivePanier(Utilisateur utilisateur) {
        return false;
//        return panierRepository.findByUtilisateurAndDateCreationAfterAndDateCrea(utilisateur, LocalDateTime.now()).isPresent();
    }

    //TODO: Lier utilisateur à panier
    @Transactional
    public PanierDto createPanier(PanierItemDto itemDtoSet) {
        //TODO: verifier si l'utilisateur a deja un panier actif
        //TODO: verifier si l'utilisateur a le droit d'acheter le produit
        //this.isAllowedToBuy(panier, user);
        if (hasActivePanier(null)) {
            throw new HasActivePanierException();
        }
        Panier panier = Panier.builder()
                        .dateCreation(LocalDateTime.now())
                        .isPaid(false)
                        .expired(false)
                        .expiresAt(LocalDateTime.now().plusMinutes(30))
                        .isShipped(false)
                        .isDelivered(false)
                        .items(new LinkedHashSet<>())
                        .build();
        PanierItemDto panierItemDto = sanitizeItemsInput(itemDtoSet);
        PanierItem panierItem = panierItemMapper.toEntity(panierItemDto);
        if (!checkCommercialisation(panierItem)) {
            throw new ArretCommercialisationException();
        }
        stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(),false, true);
        panierItem.setPanier(panier);
        panier.addItem(panierItem);
//        StockService.checkStockDisponible(panier);

//        panierItem = panierRepository.save(panier).getItems().iterator().next();

        Panier persistedPanier = panierRepository.save(panier);
        return panierMapper.toDto(persistedPanier);
    }

    private boolean checkCommercialisation(PanierItem panierItem) {
        return !panierItem.getPresentation().getEtatCommercialisation().toLowerCase().contains("arrêt");
    }



    @Transactional
    public PanierDto resolveItem(AlerteStockDecisionDto alerteStockDecisionDto){
        PanierItem panierItem = panierItemMapper.toEntity(alerteStockDecisionDto.getPanierItemDto());
        if (alerteStockDecisionDto.isAccept()) {
            panierItem.setDelayed(true);
            Panier panier = panierRepository.findByItemsId(panierItem.getId());
            panierRepository.save(panier);
            return panierMapper.toDto(panier);
        }
        Panier panier = panierRepository.findByItemsId(panierItem.getId());
        panier.setCanceled(true);
        stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(), true, true);
        panierRepository.save(panier);
        return panierMapper.toDto(panier);
    }

    private PanierDto resolvePanier(AlerteStockDecisionDto alerteStockDecisionDto){
            Panier panier = panierMapper.toEntity(alerteStockDecisionDto.getPanierDto());
            if (alerteStockDecisionDto.isAccept()) {
                panier.setDelayed(true);
                panierRepository.save(panier);
                return panierMapper.toDto(panier);
            }
        return null;
    }

    @Transactional
    public PanierDto resolve(AlerteStockDecisionDto alerteStockDecisionDto){
        if (checkExpiredPanier(panierMapper.toEntity(alerteStockDecisionDto.getPanierDto()))) {
            throw new ExpiredPanierException();
        }
        if (alerteStockDecisionDto.isItem()) {
            return resolveItem(alerteStockDecisionDto);
        }
        return resolvePanier(alerteStockDecisionDto);
    }

    //TODO:Mettre a jour un panier

    //TODO: payer un panier


    public boolean checkExpiredPanier(Panier panier) {
//        return panier.isExpired();
        LocalDateTime expirationDate = LocalDateTime.now();
        if (panier.getExpiresAt().isBefore(expirationDate)) {
            stockService.resetStockLogique(panier);
            panier.setExpired(true);
            panierRepository.save(panier);

        }
           return panier.isExpired();

    }

//    @Bean
//    public ThreadPoolTaskScheduler getScheduler(){
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(10);
//        return scheduler;
//    }

//    @Autowired
//    ThreadPoolTaskScheduler scheduler;

//    private void initPanierExpiration(Panier panier) {
//        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
//        ex..(() -> serviceCallB, 10, TimeUnit.SECONDS);
//       ScheduledFuture<?> scheduledExpiration = scheduledExecutorService.scheduleWithFixedDelay(this.expirePanier(panier), 30, 30,TimeUnit.MINUTES);
//       scheduledExpiration.cancel(true);
////        taskScheduler.schedule(() -> this.expirePanier(panier)
////                                ,new SimpleTrigger(Date.from(panier.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant())));
//    }


    private Runnable expirePanier(Panier panier) {
        return new Runnable(){
            @Override
            public void run() {
                if (!panier.isPaid()) {
                    stockService.resetStockLogique(panier);
                    panier.setExpired(true);
                    panierRepository.save(panier);
                }
            }
        };
        }

        //TODO: verifier conditions de ventes

    public boolean isUserAllowedToBuy(Medicament medicament, Utilisateur utilisateur)
    {
        // Vérifier agrément du medicmaent et et etablissement de l'utilisateur (si Hopital)
        return true;
    }

    //TODO: Lier utilisateur à panier et verifier si l'utilisateur a deja un panier actif
    public List<Panier> getPanierByUser(Long idUser) {
        return panierRepository.findByClientId(idUser);
    }
}
