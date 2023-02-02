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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//Repasser sur les autorisations d'achat
@Service
public class PanierService implements PanierServiceInterface {

    private final PanierRepository panierRepository;
    private final PresentationRepository presentationRepository;
    private final StockService stockService;

    private final PanierMapper panierMapper;

    private final PanierItemMapper panierItemMapper;
    private final PanierItemRepository panierItemRepository;
    final private UtilisateurRepository utilisateurRepository;
    @Autowired
    public PanierService(PanierItemMapper panierItemMapper, PanierRepository panierRepository, StockService stockService, PanierMapper panierMapper, PanierItemRepository panierItemRepository, PresentationRepository presentationRepository, ComptabiliteInterneService comptabiliteInterneService, CommandeTypeRepository commandeTypeRepository, CommandeTypeMapper commandeTypeMapper, UtilisateurRepository utilisateurRepository) {
        this.panierRepository = panierRepository;
        this.stockService = stockService;
        this.panierMapper = panierMapper;
        this.panierItemMapper = panierItemMapper;
        this.panierItemRepository = panierItemRepository;
        this.presentationRepository = presentationRepository;
        this.comptabiliteInterneService = comptabiliteInterneService;
        this.commandeTypeRepository = commandeTypeRepository;
        this.commandeTypeMapper = commandeTypeMapper;
        this.utilisateurRepository = utilisateurRepository;
    }


    @Transactional(rollbackFor = Exception.class)
    public PanierDto getPanier(Long idPanier) {
        Panier panier = panierRepository.findById(idPanier).orElseThrow(PanierNotFoundException::new);
        checkUser(panier);
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
        panier.getItems().forEach(panierItem -> stockService.updateStock(panierItem.getPresentation(), - panierItem.getQuantite(), true, true));
        panierRepository.delete(panier);
        return true;
    }

    //TODO:payer le panier


    @Transactional(rollbackFor = Exception.class)
    public PanierItemDto sanitizeItemsInput(PanierItemDto panierItemDto) {
        if (panierItemDto.getQuantite() < 0) {
            throw new NegativeQuantityException();
        }
//        panierItemDto.setQuantite(panierItemDto.getQuantite() > 0 ? panierItemDto.getQuantite() : 1);
        return panierItemDto;
    }

    private boolean hasActivePanier(Utilisateur utilisateur) {
        return panierRepository.existsByClientAndExpiresAtAfterAndPaidFalse(utilisateur, LocalDateTime.now());
    }



    @Transactional(rollbackFor = Exception.class)
    public PanierDto createPanier(PanierItemDto itemDtoSet) {
        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
        if (hasActivePanier(utilisateur)) {
            return this.addToPanier(getCurrentPanier().getId(), itemDtoSet);
        }
        Panier panier = Panier.builder()
                        .dateCreation(LocalDateTime.now())
                        .paid(false)
                        .expired(false)
                        .expiresAt(LocalDateTime.now().plusMinutes(30))
                        .isShipped(false)
                        .delivered(false)
                        .canceled(false)
                        .items(new LinkedHashSet<>())
                        .client(UserContextHolder.getUtilisateur())
                        .build()
                ;
        PanierItemDto panierItemDto = sanitizeItemsInput(itemDtoSet);
        PanierItem panierItem = panierItemMapper.toEntity(panierItemDto);
        if (!isUserAllowedToBuy(panierItem, utilisateur)) {
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
        if (panier != null) {
            var isUpdating = panierItemRepository.existsByPanierAndPresentation_Id(panier, panierItem.getPresentation().getId());
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
                    .paid(false)
                    .expired(false)
                    .expiresAt(LocalDateTime.now().plusMinutes(30))
                    .isShipped(false)
                    .delivered(false)
                    .canceled(false)
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

    @Transactional(rollbackFor = Exception.class)
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
//        getCurrentPanier();
        if(!panier.getClient().getId().equals(utilisateur.getId())){
            throw new WrongUserException();
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
        LocalDateTime now = LocalDateTime.now();
        if (panier.getExpiresAt().isBefore(now) && !panier.isExpired()) {
            stockService.resetStockLogique(panier);
            panier.setExpired(true);
            panierRepository.save(panier);
            return true;
        }
        panier.setExpired(false);
        panierRepository.save(panier);
        return false;

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


//    private Runnable expirePanier(Panier panier) {
//        return new Runnable(){
//            @Override
//            public void run() {
//                if (!panier.isPaid()) {
//                    stockService.resetStockLogique(panier);
//                    panier.setExpired(true);
//                    panierRepository.save(panier);
//                }
//            }
//        };
//        }

        //TODO: verifier conditions de ventes (presentation active + agrément)
//TODO: recherche sur composant
    //TODO: connrecter strioe
    public boolean isUserAllowedToBuy(PanierItem panierItem, Utilisateur utilisateur)
    {
        // Vérifier agrément du medicmaent et et etablissement de l'utilisateur (si Hopital)
        return true;
    }
    public List<Panier> getPanierByUser() {
        return panierRepository.findByClient(UserContextHolder.getUtilisateur());
    }





    @Transactional(rollbackFor = Exception.class)
    public PanierDto addToPanier(Long idPanier, PanierItemDto panierItemDto) {
//        utilisateurService.setBuying
        Panier panier = panierRepository.findById(idPanier).orElseThrow(PanierNotFoundException::new);
        if (checkExpiredPanier(panier)) {
            throw new ExpiredPanierException();
        }
        Set<PanierItem> items = panier.getItems();
        Presentation presentation = presentationRepository.findByCodeCIP(panierItemDto.getPresentationCip()).orElseThrow(PresentationNotFoundException::new);
        var isDoublon = items.stream().anyMatch(panierItem -> panierItem.getPresentation().getCodeCIP().equals(presentation.getCodeCIP()));

        PanierItem panierItem = panierItemRepository.findByPanierAndPresentation(panier,presentation)
                                .orElse(PanierItem.builder()
                                                  .presentation(presentation)
                                                  .panier(panier)
                                                  .build());

        panierItem.setQuantite(panierItemDto.getQuantite());
        stockService.updateStock(presentation, panierItemDto.getQuantite(), false, false);
        panierItemRepository.save(panierItem);
        if (!isDoublon) {
            panier.addItem(panierItem);
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

    @Transactional(rollbackFor = Exception.class)
    public boolean removeItem(Long presentationCip){
        if (hasActifPanier()){
            throw new PanierNotFoundException();
        }
        Panier panier = this.getCurrentPanier();
        Presentation presentation = presentationRepository.findByCodeCIP(presentationCip).orElseThrow(PresentationNotFoundException::new);
        PanierItem panierItem = panierItemRepository.findByPanierAndPresentation(panier,presentation).orElseThrow(PanierItemNotFoundException::new);
        panierItemRepository.delete(panierItem);
        panier.getItems().remove(panierItem);
        stockService.updateStock(presentation, panierItem.getQuantite(), true, true);
        panierRepository.save(panier);
        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    public Panier getCurrentPanier() {
        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
//        Panier panier = panierRepository.findByClientAndExpiresAtAfter(utilisateur, LocalDateTime.now()).orElseThrow(() -> {
        Panier panier = panierRepository.findByClientAndExpiresAtAfterAndPaidFalseOrderByDateCreationDesc(utilisateur, LocalDateTime.now()).orElseThrow(() -> {
            throw new AucunPanierActifException();
        });
        if(checkExpiredPanier(panier)) {
            throw new ExpiredPanierException();
        }
        return panier;
    }

    final
    ComptabiliteInterneService comptabiliteInterneService;

    @Transactional(rollbackFor = Exception.class)
    public void checkUser(Panier panier) {
        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
         if (panier.getClient().getId().equals(utilisateur.getId())){
             throw new WrongUserException();
         }
    }
    @Transactional(rollbackFor = Exception.class)
    public Object confirmPanier() {
        Panier panier = getCurrentPanier();
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
        return comptabiliteInterneService.createFacture(panier);
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

    public boolean hasActifPanier() {
        return panierRepository.existsByClientAndExpiresAtAfterAndPaidFalseAndCanceledFalse(UserContextHolder.getUtilisateur(), LocalDateTime.now());
    }


    @Transactional(rollbackFor = Exception.class)
    public PanierDto getCurrentPanierDto() {
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

    @Transactional(rollbackFor = Exception.class)
    public boolean removeCurrentPanier() {
        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
        Panier panier = panierRepository.findByClientAndExpiresAtAfter(utilisateur, LocalDateTime.now()).orElseThrow(() -> {
            throw new AucunPanierActifException();
        });
        if (checkExpiredPanier(panier)) {
            throw new ExpiredPanierException();
        }
        panierRepository.delete(panier);
        return true;
    }
}
