package fr.miage.gromed.controller;


import fr.miage.gromed.controller.customResponse.ResponseHandler;
import fr.miage.gromed.dto.*;
import fr.miage.gromed.exceptions.*;

import fr.miage.gromed.model.Utilisateur;
import fr.miage.gromed.service.UtilisateurService;
import fr.miage.gromed.service.auth.UserContextHolder;
import fr.miage.gromed.service.metier.PanierAlreadyPaidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.miage.gromed.service.metier.PanierService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/panier")
public class PanierController {

    public static final String ERREUR_INTERNE = "Erreur interne";
    private final PanierService panierService;

    Logger logger = LoggerFactory.getLogger(PanierController.class);
    @Autowired
    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    @GetMapping("/{idPanier}")
    public ResponseEntity<Object> getPanier(@PathVariable Long idPanier){
            return ResponseHandler.generateResponse("panier en cours", HttpStatus.CREATED,  panierService.getPanier(idPanier));
    }

    @GetMapping("/")
    public ResponseEntity<Object> getCurrentPanier(@RequestParam String idUser){
            return ResponseHandler.generateResponse("panier en cours", HttpStatus.OK,  panierService.getCurrentPanier());
    }

    @PutMapping("/add/{idPanier}")
    public ResponseEntity<Object> addToPanier(@PathVariable Long idPanier, @RequestBody PanierItemDto panierItemDto){
            return ResponseHandler.generateResponse("item ajouté", HttpStatus.OK,  panierService.addToPanier(idPanier, panierItemDto));
    }

    @PutMapping("/cancel/{idPanier}")
    public ResponseEntity<Object> cancelPanier(@PathVariable Long idPanier){
            return ResponseHandler.generateResponse("panier annulé", HttpStatus.CREATED,  panierService.cancelPanier(idPanier));
    }

    @GetMapping("/commande-types")
    public ResponseEntity<Object> getCommandeTypes(){
            return ResponseHandler.generateResponse("types de commande", HttpStatus.CREATED,  panierService.getCommandeTypes());
    }


//    @PutMapping("/update")
//    public ResponseEntity<Object> updateItemPanier(@RequestBody PanierItemDto panierItemDto){
//            return ResponseHandler.generateResponse("item ajouté", HttpStatus.CREATED,  panierService.updatePanierItem(idPanier, panierItemDto));
//    }
    /**
     * Creer un panier avec un item, retourne le panier ou un message d'alerte
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createPanier(@RequestBody PanierItemDto panierItemDto){
            return ResponseHandler.generateResponse("Nouveau panier OK", HttpStatus.CREATED, panierService.createPanier(panierItemDto));
    }

    @PutMapping("/confirm/{idPanier}")
    public ResponseEntity<Object> confirmPanier(@PathVariable Long idPanier){
//            PanierDto panierDto = panierService.confirmPanier(idPanier);
            return ResponseHandler.generateResponse("Panier confirmé", HttpStatus.OK, panierService.confirmPanier(idPanier));
    }

    @PutMapping("/commandeType/{idPanier}")
    public ResponseEntity<Object> setCommandeType(@PathVariable Long idPanier){
            return ResponseHandler.generateResponse("commande type enregistrée", HttpStatus.OK, panierService.saveCommandeType(idPanier));
    }

    @PutMapping("/resolve")
    public ResponseEntity<Object> resolvePanier(@RequestBody AlerteStockDecisionDto decisionDto, @PathVariable Long idPanier){
        //TODO: verifier si le panier est nouveau ou pas
            PanierDto panierDto = panierService.resolve(decisionDto);
            return ResponseHandler.generateResponse("résolu", HttpStatus.GONE, panierDto);
    }

    @GetMapping("/historique")
    public ResponseEntity<Object> getHistorique(){
            return ResponseHandler.generateResponse("historique", HttpStatus.OK, panierService.getHistorique());
    }

    @Autowired
    private UtilisateurService utilisateurService;

    @ExceptionHandler(StockIndisponibleException.class)
    public ResponseEntity<Object> handleStockIndisponibleException(StockIndisponibleException e) {
        utilisateurService.await();
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.MULTIPLE_CHOICES);
    }

    @ExceptionHandler(ExpiredPanierException.class)
    public ResponseEntity<Object> expiredExceptionHandler(ExpiredPanierException e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(PresentationNotFoundException.class)
    public ResponseEntity<Object> presentationNotFoundExceptionHandler(PresentationNotFoundException e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PanierNotFoundException.class)
    public ResponseEntity<Object> panierNotFoundExceptionHandler(PanierNotFoundException e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception e) {
        logger.error("Erreur: ", e);
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PanierCantBeCanceledException.class)
    public ResponseEntity<Object> panierCantBeCanceledExceptionHandler(PanierCantBeCanceledException e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(PanierAlreadyPaidException.class)
    public ResponseEntity<Object> panierAlreadyPaidException(PanierCantBeCanceledException e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(PanierCanceledException.class)
    public ResponseEntity<Object> panierAlreadyPaidException(PanierCanceledException e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<Object> getCommandeType(@PathVariable Long idPanier){
        return null;
    }



}
