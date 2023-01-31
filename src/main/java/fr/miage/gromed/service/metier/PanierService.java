package fr.miage.gromed.service.metier;

import fr.miage.gromed.dto.AlerteStockDecisionDto;
import fr.miage.gromed.dto.CommandeTypeDto;
import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.dto.PanierItemDto;
import fr.miage.gromed.exceptions.*;
import fr.miage.gromed.model.*;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.*;
import fr.miage.gromed.scheduler.restockfaker.PanierCleanExpired;
import fr.miage.gromed.service.auth.UserContextHolder;
import fr.miage.gromed.service.mapper.CommandeTypeMapper;
import fr.miage.gromed.service.mapper.PanierItemMapper;
import fr.miage.gromed.service.mapper.PanierMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//Repasser sur les autorisations d'achat
@Service
public class PanierService {

    private final PanierRepository panierRepository;

    private final StockService stockService;

    private final PanierMapper panierMapper;

    private final PanierItemMapper panierItemMapper;

    private final PanierCleanExpired panierCleanExpired;
    private final TaskScheduler taskScheduler;
    private final PanierItemRepository panierItemRepository;

    @Autowired
    public PanierService(TaskScheduler taskScheduler, PanierItemMapper panierItemMapper, PanierRepository panierRepository, StockService stockService, PanierMapper panierMapper, PanierCleanExpired panierCleanExpired, PanierItemRepository panierItemRepository, PresentationRepository presentationRepository, ComptabiliteInterneService comptabiliteInterneService, CommandeTypeRepository commandeTypeRepository, CommandeTypeMapper commandeTypeMapper) {
        this.panierRepository = panierRepository;
        this.stockService = stockService;
        this.panierMapper = panierMapper;
        this.panierItemMapper = panierItemMapper;
        this.taskScheduler = taskScheduler;
//        this.panierCleanExpired = panierCleanExpired;
        this.panierCleanExpired = panierCleanExpired;
        this.panierItemRepository = panierItemRepository;
        this.presentationRepository = presentationRepository;
        this.comptabiliteInterneService = comptabiliteInterneService;
        this.commandeTypeRepository = commandeTypeRepository;
        this.commandeTypeMapper = commandeTypeMapper;
    }


    @Transactional(rollbackFor = Exception.class)
    public PanierDto getPanier(Long idPanier) {
        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
        Optional<Panier> panierOpt = panierRepository.findById(idPanier);
        if(panierOpt.isEmpty()) {
            throw new PanierNotFoundException();
        }
        Panier panier = panierOpt.get();
        checkExpiredPanier(panier);
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


    private PanierItemDto sanitizeItemsInput(PanierItemDto panierItemDto) {
        panierItemDto.setQuantite(panierItemDto.getQuantite() > 0 ? panierItemDto.getQuantite() : 1);
        return panierItemDto;
    }

    private boolean hasActivePanier(Utilisateur utilisateur) {
        Optional<Panier> panierOpt = panierRepository.findByClientAndExpiresAtAfterAndPaidFalseAndCanceledFalse(utilisateur, LocalDateTime.now());
        return panierOpt.isPresent();
    }

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Transactional(rollbackFor = Exception.class)
    public PanierDto createPanier(PanierItemDto itemDtoSet) {
        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
        if (hasActivePanier(utilisateur)) {
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
                        .client(UserContextHolder.getUtilisateur())
                        .build()
                ;
        PanierItemDto panierItemDto = sanitizeItemsInput(itemDtoSet);
        PanierItem panierItem = panierItemMapper.toEntity(panierItemDto);
        if (isUserAllowedToBuy(panierItem, utilisateur)) {
            throw new UserNotAllowedToBuyException();
        }
        if (!checkCommercialisation(panierItem)) {
            throw new ArretCommercialisationException();
        }
        stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(),false, true);
        panierItem.setPanier(panier);
        panier.addItem(panierItem);
        Panier persistedPanier = panierRepository.save(panier);
        return panierMapper.toDto(persistedPanier);
    }

    private boolean checkCommercialisation(PanierItem panierItem) {
        return !panierItem.getPresentation().getEtatCommercialisation().toLowerCase().contains("arrêt");
    }

    //TODO: Complete this method
    @Transactional(rollbackFor = Exception.class)
    public PanierDto resolveItem(AlerteStockDecisionDto alerteStockDecisionDto) {
        PanierItem panierItem = panierItemMapper.toEntity(alerteStockDecisionDto.getPanierItemDto());
        Panier panier = panierItem.getPanier();
        var isUpdating = false;
        if (panier != null) {
            isUpdating = panierItemRepository.existsByPanierAndPresentation_Id(panier, panierItem.getPresentation().getId());
            if (checkExpiredPanier(panier)) {
                throw new ExpiredPanierException();
            }
            //Item existe dans panier et accepte livraison retardée
            if (isUpdating && alerteStockDecisionDto.isAccept()) {
                panierItem.setDelayed(true);
                panierRepository.save(panier);
                stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(), false, true);
                return panierMapper.toDto(panier);
            }//item existe et annule l'item
            if (isUpdating && !alerteStockDecisionDto.isAccept()) {
                panierItemRepository.delete(panierItem);
                return panierMapper.toDto(panier);
            }// item  n'existe pas dans panier et accepte livraison retardée
            if (!isUpdating && alerteStockDecisionDto.isAccept()) {
                panierItem.setDelayed(true);
                panierItem.setPanier(panier);
                panier.addItem(panierItem);
                stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(), false, true);
                panierRepository.save(panier);
                return panierMapper.toDto(panier);
            }
            if (!isUpdating && !alerteStockDecisionDto.isAccept()) {
                return panierMapper.toDto(panier);
            }
            panierItemRepository.delete(panierItem);
            stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(), true, true);
            return panierMapper.toDto(panier);
        }
       //panier n'existe pas et pas de panier actif pour l'utilisateur et user accepte la livraison retardée
        if (alerteStockDecisionDto.isAccept() && panierRepository.existsByClientAndExpiresAtAfterAndPaidFalseAndCanceledFalse(UserContextHolder.getUtilisateur(), LocalDateTime.now())) {
            Panier newPanier = Panier.builder()
                    .dateCreation(LocalDateTime.now())
                    .isPaid(false)
                    .expired(false)
                    .expiresAt(LocalDateTime.now().plusMinutes(30))
                    .isShipped(false)
                    .isDelivered(false)
                    .items(new LinkedHashSet<>())
                    .client(UserContextHolder.getUtilisateur())
                    .build();
            panierItemRepository.save(panierItem);
            newPanier.addItem(panierItem);
            panierRepository.save(newPanier);
            return panierMapper.toDto(newPanier);
        }


        return null;
    }
    private PanierDto resolvePanier(Panier panier, AlerteStockDecisionDto alerteStockDecisionDto){
            if (alerteStockDecisionDto.isAccept()) {
                panier.getItems().forEach(panierItem -> {
                    var missing = panierItem.getQuantite() - panierItem.getPresentation().getStock().getQuantiteStockPhysique();
                    var delayed = missing > 0;
                    panierItem.setDelayed(delayed);
                    stockService.updateStock(panierItem.getPresentation(), panierItem.getQuantite(), false, true);
                });
                panier.setDelayed(true);
                panierRepository.save(panier);
                return panierMapper.toDto(panier);
            }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public PanierDto resolve(AlerteStockDecisionDto alerteStockDecisionDto){

        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
        Panier panier = panierRepository.findById(alerteStockDecisionDto.getPanierId()).orElseThrow(PanierNotFoundException::new);
        if(panier.getClient().equals(utilisateur)){
            throw new UserNotAllowedToBuyException();
        }
        if (!utilisateur.isAwaitingResponse()){
            throw new NoConflictToResolveException();
        }
        if (checkExpiredPanier(panier)) {
            throw new ExpiredPanierException();
        }
        if (alerteStockDecisionDto.isItem()) {
            return resolveItem(alerteStockDecisionDto);
        }
        return resolvePanier(panier,alerteStockDecisionDto);
    }

    //TODO:Mettre a jour un panier

    //TODO: payer un panier


    public boolean checkExpiredPanier(Panier panier) {
//        return panier.isExpired();
        LocalDateTime expirationDate = LocalDateTime.now();
        if (panier.getExpiresAt().isBefore(expirationDate) && !panier.isExpired()) {
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

    public boolean isUserAllowedToBuy(PanierItem panierItem, Utilisateur utilisateur)
    {
        // Vérifier agrément du medicmaent et et etablissement de l'utilisateur (si Hopital)
        return true;
    }
    public List<Panier> getPanierByUser() {
        return panierRepository.findByClient(UserContextHolder.getUtilisateur());
    }

    final
    PresentationRepository presentationRepository;



    @Transactional(rollbackFor = Exception.class)
    public PanierDto addToPanier(Long idPanier, PanierItemDto panierItemDto) {
//        utilisateurService.setBuying
        Panier panier = panierRepository.findById(idPanier).orElseThrow(PanierNotFoundException::new);
        if (checkExpiredPanier(panier)) {
            throw new ExpiredPanierException();
        }
        Presentation presentation = presentationRepository.findById(panierItemDto.getPresentationCip()).orElseThrow(PresentationNotFoundException::new);
        PanierItem panierItem = panierItemRepository.findByPanier_IdAndPresentation_Id(
                            panier.getId(),
                            panierItemDto.getPresentationCip())
                                .orElse(
                                PanierItem.builder()
                                .presentation(presentation)
                                .panier(panier)
                                .build());
        panierItem.setQuantite(panierItem.getQuantite() + panierItemDto.getQuantite());//TODO: Ajouter ou remplacer la quantité ?
        stockService.updateStock(presentation, panierItemDto.getQuantite(), false, false);
        if (panierItem.getId() == null) {
            panierItemRepository.save(panierItem);
            panier.addItem(panierItem);
        }else {
            panierItemRepository.save(panierItem);
        }
        if (panierItem.getQuantite() < 0 ) {
            panierItemRepository.delete(panierItem);
        }
        panierRepository.save(panier);
        return panierMapper.toDto(panier);
    }

        @Transactional(rollbackFor = Exception.class)
        public Long cancelPanier(Long idPanier) {
            Panier panier = panierRepository.findById(idPanier).orElseThrow(PanierNotFoundException::new);
            if (checkExpiredPanier(panier) && !panier.isPaid()) {
                throw new ExpiredPanierException();
            }
            if (!canCancel(panier)) {
              throw new PanierCantBeCanceledException();
            }
            stockService.resetStockLogique(panier);
            panier.setCanceled(true);
            panierRepository.save(panier);
            return panier.getId();
        }

    private boolean canCancel(Panier panier) {
        LocalDateTime cancelLimit =panier.getDatePaiement().plusMinutes(30);
        return LocalDateTime.now().isBefore(cancelLimit);
    }

    final
    ComptabiliteInterneService comptabiliteInterneService;

    private boolean checkUser(Panier panier) {
        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
        return panier.getClient().getId().equals(utilisateur.getId());
    }
    @Transactional(rollbackFor = Exception.class)
    public Object confirmPanier(Long idPanier) {
        Panier panier = panierRepository.findById(idPanier).orElseThrow(PanierNotFoundException::new);
        if (!checkUser(panier)) {
            throw new WrongUserException();
        }
        if (checkExpiredPanier(panier)) {
            throw new ExpiredPanierException();
        }
        if (panier.isPaid()) {
            throw new PanierAlreadyPaidException();
        }
        if (panier.isCanceled()) {
            throw new PanierCanceledException();
        }
        if (panier.isExpired()) {
            throw new ExpiredPanierException();
        }
        panier.setPaid(true);
        panier.setDatePaiement(LocalDateTime.now());
        panierRepository.save(panier);
        comptabiliteInterneService.createFacture(panier);
        return panierMapper.toDto(panier);
    }

    final
    CommandeTypeRepository commandeTypeRepository;
    final
    CommandeTypeMapper commandeTypeMapper ;
    @Transactional(rollbackFor = Exception.class)
    public Long saveCommandeType(Long idPanier) {
        Panier panier = panierRepository.findById(idPanier).orElseThrow(PanierNotFoundException::new);
        if (!panier.isPaid()) {
            throw new PanierNotPaidException();
        }
        CommandeType ct = commandeTypeRepository.save(CommandeType.builder()
                .panier(panier)
                .build());
        return ct.getId();
    }

    public Set<CommandeTypeDto> getCommandeTypes() {
        return commandeTypeRepository.findAllByPanierClient(UserContextHolder.getUtilisateur()).stream().map(commandeTypeMapper::toDto).collect(Collectors.toSet());
    }

    public boolean hasActifPanier(Utilisateur utilisateur) {
        return panierRepository.existsByClientAndExpiresAtAfterAndPaidFalseAndCanceledFalse(UserContextHolder.getUtilisateur(), LocalDateTime.now());
    }

    public PanierDto getCurrentPanier() {
        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
        Panier panier = panierRepository.findByClientAndExpiresAtAfter(utilisateur, LocalDateTime.now()).orElseThrow(() -> {
           throw new AucunPanierActifException();
        });
        if(checkExpiredPanier(panier)) {
            throw new ExpiredPanierException();
        }
    return panierMapper.toDto(panier);
    }

    public List<PanierDto> getHistorique() {
Utilisateur utilisateur = UserContextHolder.getUtilisateur();
        return panierRepository.findByClientAndExpiresAtBefore(utilisateur, LocalDateTime.now()).stream().map(panierMapper::toDto).collect(Collectors.toList());

    }
}
