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
import fr.miage.gromed.service.PanierService;


@RestController
@CrossOrigin(origins = "*")
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
        try {
            PanierDto panierDto = panierService.getPanier(idPanier);
            return ResponseHandler.generateResponse("Nouveau panier OK", HttpStatus.CREATED, panierDto);
        } catch (ExpiredPanierException epe) {
            return ResponseHandler.generateFailureResponse(epe.getMessage(), HttpStatus.GONE);
        } catch (PanierNotFoundException pnfe) {
            return ResponseHandler.generateFailureResponse(pnfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur: ", e);
            return ResponseHandler.generateFailureResponse(ERREUR_INTERNE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creer un panier avec un item, retourne le panier ou un message d'alerte
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createPanier(@RequestBody PanierItemDto panierItemDto){
        try {
            System.out.println(panierItemDto);
            //TODO: verifier utilisateur
            PanierDto panierDto = panierService.createPanier(panierItemDto);
            logger.info(panierDto.toString() + " "+panierDto);
            return ResponseHandler.generateResponse("Nouveau panier OK", HttpStatus.CREATED, panierDto);
        } catch (StockIndisponibleException e) {
            return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.GONE);
        } catch (PresentationNotFoundException enf) {
            return ResponseHandler.generateFailureResponse(enf.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur: ", e);
            return ResponseHandler.generateFailureResponse(ERREUR_INTERNE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/resolve")
    public ResponseEntity<Object> resolvePanier(@RequestBody AlerteStockDecisionDto decisionDto, @PathVariable Long idPanier){
        try {
            //TODO: verifier utilisateur
            PanierDto panierDto = panierService.resolve(decisionDto);
            return ResponseHandler.generateResponse("résolu", HttpStatus.GONE, panierDto);
        } catch (ExpiredPanierException epe) {
            return ResponseHandler.generateFailureResponse(epe.getMessage(), HttpStatus.GONE);
        } catch (PanierNotFoundException pnfe) {
            return ResponseHandler.generateFailureResponse(pnfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur: ", e);
            return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
