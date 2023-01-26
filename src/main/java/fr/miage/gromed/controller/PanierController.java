package fr.miage.gromed.controller;

import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.model.medicament.Presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.miage.gromed.service.PanierService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/panier")
public class PanierController {

    private PanierService panierService;

    @Autowired
    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    @GetMapping("/{idPanier}")
    public ResponseEntity<PanierDto> getPanier(@PathVariable Long idPanier){
        return panierService.getPanier(idPanier);
    }

    @PostMapping("/{idPanier}")
    public ResponseEntity<PanierDto> addItemsToPanier(@PathVariable int idPanier, @RequestBody PresentationDto presentationDto){
        return panierService.addItemsToPanier(idPanier, presentationDto);
    }

    @PutMapping("/{idPanier}")
    public ResponseEntity<PanierDto> updatePanier(@PathVariable int idPanier, @RequestBody PresentationDto presentationDto){
        return panierService.updatePanier(idPanier, presentationDto);
    }

    @DeleteMapping("/{idPanier}")
    public ResponseEntity<PanierDto> deleteItemsPanier(@PathVariable int idPanier, @RequestBody PresentationDto presentationDto){
        return panierService.delete(idPanier);
    }
}
