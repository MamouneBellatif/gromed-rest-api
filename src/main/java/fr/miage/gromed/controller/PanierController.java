package fr.miage.gromed.controller;

import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.dto.PanierDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<MedicamentDto> getPanier(@PathVariable int idPanier){
        return panierService.getPanier(idPanier);
    }
}
