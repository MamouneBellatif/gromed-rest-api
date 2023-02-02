package fr.miage.gromed.model.controller;


import fr.miage.gromed.model.controller.customResponse.ResponseHandler;
import fr.miage.gromed.dto.*;
import fr.miage.gromed.exceptions.*;

import fr.miage.gromed.service.UtilisateurService;
import fr.miage.gromed.service.auth.PanierContextHolder;
import fr.miage.gromed.service.auth.PanierContextWrapper;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.miage.gromed.service.metier.PanierService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.Thread.currentThread;

@RestController
@RequestMapping("/api/panier/")
public class PanierController {

    public static final String ERREUR_INTERNE = "Erreur interne";
    private final PanierService panierService;
    private final UtilisateurService utilisateurService;

    Logger logger = LoggerFactory.getLogger(PanierController.class);
    @Autowired
    public PanierController(PanierService panierService, UtilisateurService utilisateurService) {
        this.panierService = panierService;
        this.utilisateurService = utilisateurService;
    }
//
    @GetMapping("/view/{idPanier}")
    public ResponseEntity<Object> getPanier(@PathVariable Long idPanier){
            return ResponseHandler.generateResponse("panier en cours", HttpStatus.OK,  panierService.getPanier(idPanier));
    }

    @GetMapping("/")
    public ResponseEntity<Object> getCurrentPanier(@RequestParam String idUser){
            return ResponseHandler.generateResponse("panier en cours", HttpStatus.OK,  panierService.getCurrentPanierDto());
    }

    @DeleteMapping(value = "/remove/item/{presentationCip}")
    public ResponseEntity<Object> addPanier(@PathVariable Long presentationCip){
            return ResponseHandler.generateResponse("panier ajouté", HttpStatus.CREATED,  panierService.removeItem(presentationCip));
    }

    @DeleteMapping(value = "/remove/panier")
    public ResponseEntity<Object> addPanier(){
        return ResponseHandler.generateResponse("panier ajouté", HttpStatus.CREATED,  panierService.removeCurrentPanier());
    }


    @PutMapping("/cancel/{idPanier}")
    public ResponseEntity<Object> cancelPanier(@PathVariable Long idPanier){
            return ResponseHandler.generateResponse("panier annulé", HttpStatus.CREATED,  panierService.cancelPanier(idPanier));
    }

    @GetMapping("/commande-types")
    public ResponseEntity<Object> getCommandeTypes(){
            return ResponseHandler.generateResponse("types de commande", HttpStatus.CREATED,  panierService.getCommandeTypes());
    }


    /**
     * Creer un panier avec un item, retourne le panier ou un message d'alerte
     */
    @PostMapping("/add")
    public ResponseEntity<Object> createPanier(@RequestBody PanierItemDto panierItemDto){
        PanierContextHolder.setPanierItemDto(panierItemDto);
        return ResponseHandler.generateResponse("Produit ajouté", HttpStatus.CREATED, panierService.createPanier(panierItemDto));
    }

    @PutMapping("/confirm")
    public ResponseEntity<Object> confirmPanier(){
            return ResponseHandler.generateResponse("Panier confirmé", HttpStatus.OK, panierService.confirmPanier());
    }

    @PutMapping("/commandeType/{idPanier}")
    public ResponseEntity<Object> setCommandeType(@PathVariable Long idPanier){
            return ResponseHandler.generateResponse("commande type enregistrée", HttpStatus.OK, panierService.saveCommandeType(idPanier));
    }

    @PutMapping("/resolve")
    public ResponseEntity<Object> resolvePanier(@RequestBody AlerteStockDecisionDto decisionDto){
        //TODO: verifier si le panier est nouveau ou pas
            PanierDto panierDto = panierService.resolve(decisionDto);
            return ResponseHandler.generateResponse("résolu", HttpStatus.GONE, panierDto);
    }

    @GetMapping("/historique")
    public ResponseEntity<Object> getHistorique(){
            return ResponseHandler.generateResponse("historique", HttpStatus.OK, panierService.getHistorique());
    }


    @SuppressWarnings("ReassignedVariable")
    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<Object> handleOptimisticLockException(OptimisticLockException e) throws InterruptedException {
        if (PanierContextHolder.getPanierItemDto() != null)  return ResponseHandler.generateFailureResponse("Erreur de concurrence, veuillez réessayer", HttpStatus.CONFLICT);
        for (int i = 0; i < PanierContextHolder.MAX_TRY; i++) {
            waitForLock();
            try {
                return ResponseHandler.generateResponse("Produit ajouté", HttpStatus.CREATED, panierService.createPanier(PanierContextHolder.getPanierItemDto()));
            } catch (OptimisticLockException optimisticLockException) {
                logger.info("Accès concurrent sur un produit, essai numero "+ i);
            }
        }
        return ResponseHandler.generateFailureResponse("Erreur de concurrence, veuillez réessayer", HttpStatus.CONFLICT);
    }

    private void waitForLock(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
            if (currentThread().isInterrupted()) {
                currentThread().interrupt();
            }
        }
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception e) {
        logger.error("Erreur: ", e);
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Object> getCommandeType(@PathVariable Long idPanier){
        return null;
    }



}
