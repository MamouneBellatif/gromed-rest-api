package fr.miage.gromed.controller;


import fr.miage.gromed.controller.customResponse.ResponseHandler;
import fr.miage.gromed.dto.*;
import fr.miage.gromed.exceptions.ExpiredPanierException;
import fr.miage.gromed.exceptions.PanierNotFoundException;
import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.exceptions.StockIndisponibleException;

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

    @GetMapping("/user/{idUser}")
    public ResponseEntity<Object> getPanierByUser(@PathVariable Long idUser){
            return ResponseHandler.generateResponse("panier en cours", HttpStatus.CREATED,  panierService.getPanierByUser(idUser));
    }

    @PutMapping("/add/{idPanier}")
    public ResponseEntity<Object> addPanierItem(@PathVariable Long idPanier, @RequestBody PanierItemDto panierItemDto){
            return ResponseHandler.generateResponse("item ajouté", HttpStatus.CREATED,  panierService.addPanierItem(idPanier, panierItemDto));
    }
    /**
     * Creer un panier avec un item, retourne le panier ou un message d'alerte
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createPanier(@RequestBody PanierItemDto panierItemDto){
            //TODO: verifier token utilisateur
            return ResponseHandler.generateResponse("Nouveau panier OK", HttpStatus.CREATED, panierService.createPanier(panierItemDto));
    }

    @PutMapping("/confirm/{idPanier}")
    public ResponseEntity<Object> confirmPanier(@PathVariable Long idPanier){
//            PanierDto panierDto = panierService.confirmPanier(idPanier);
            PanierDto panierDto = null;
            return ResponseHandler.generateResponse("Panier confirmé", HttpStatus.OK, panierDto);
    }

    @PutMapping("/resolve")
    public ResponseEntity<Object> resolvePanier(@RequestBody AlerteStockDecisionDto decisionDto, @PathVariable Long idPanier){
            //TODO: verifier utilisateur
        //TODO: verifier si le panier est nouveau ou pas
            PanierDto panierDto = panierService.resolve(decisionDto);
            return ResponseHandler.generateResponse("résolu", HttpStatus.GONE, panierDto);
    }

    @ExceptionHandler(StockIndisponibleException.class)
    public ResponseEntity<Object> handleStockIndisponibleException(StockIndisponibleException e) {
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


    public ResponseEntity<Object> getCommandeType(@PathVariable Long idPanier){
        return null;
    }


    @PutMapping("/{idPanier}")
    public ResponseEntity<PanierDto> updatePanier(@PathVariable int idPanier, @RequestBody PresentationDto presentationDto){
//        return panierService.updatePanier(idPanier, presentationDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idPanier}")
    public ResponseEntity<PanierDto> deleteItemsPanier(@PathVariable int idPanier, @RequestBody PresentationDto presentationDto){
//        return panierService.delete(idPanier);
        return ResponseEntity.noContent().build();
    }
}
