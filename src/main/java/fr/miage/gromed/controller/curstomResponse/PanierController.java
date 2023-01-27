package fr.miage.gromed.controller.curstomResponse;

import fr.miage.gromed.controller.PanierResponseEntity;
import fr.miage.gromed.dto.*;
import fr.miage.gromed.exceptions.ExpiredPanierException;
import fr.miage.gromed.exceptions.PanierNotFoundException;
import fr.miage.gromed.exceptions.StockIndisponibleException;
import fr.miage.gromed.model.medicament.Presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.miage.gromed.service.PanierService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/panier")
public class PanierController {

    private final PanierService panierService;

    @Autowired
    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    @GetMapping("/{idPanier}")
    public PanierResponseEntity getPanier(@PathVariable Long idPanier){
        try {
            PanierDto panierDto = panierService.getPanier(idPanier);
            return new PanierResponseEntity(panierDto, HttpStatus.OK);
        } catch (ExpiredPanierException epe) {
            return new PanierResponseEntity(epe.getMessage(),HttpStatus.GONE);
        } catch (PanierNotFoundException pnfe) {
            return new PanierResponseEntity(pnfe.getMessage(),(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new PanierResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creer un panier avec un item, retourne le panier ou un message d'alerte
     */
    @PostMapping("/create")
    public PanierResponseEntity createPanier(@RequestBody PanierItemDto panierItemDto){
        try {
            //TODO: verifier utilisateur
            PanierDto panierDto = panierService.createPanier(panierItemDto);
            return new PanierResponseEntity(panierDto, HttpStatus.OK);
        }   catch (StockIndisponibleException e) {
            return new PanierResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new PanierResponseEntity("Erreur interne", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/resolve")
    public PanierResponseEntity resolvePanier(@RequestBody AlerteStockDecisionDto decisionDto, @PathVariable Long idPanier){
        try {
            //TODO: verifier utilisateur
            PanierDto panierDto = panierService.resolvePanier(decisionDto);
            return new PanierResponseEntity(panierDto, HttpStatus.OK);
        } catch (Exception e) {
            return new PanierResponseEntity("Erreur interne", HttpStatus.INTERNAL_SERVER_ERROR);
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
