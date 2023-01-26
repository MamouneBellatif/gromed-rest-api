package fr.miage.gromed.controller;

import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.model.medicament.Presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import fr.miage.gromed.service.PanierService;


@RestController
@RequestMapping("/api/panier")
public class PanierController {

    private PanierService panierService;

    @Autowired
    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    @GetMapping("/{idPanier}")
    public PanierDto getPanier(@PathVariable int idPanier){
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
