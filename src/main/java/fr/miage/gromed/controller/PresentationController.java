package fr.miage.gromed.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.service.PresentationService;

@RestController

public class PresentationController{
    
    
    @Autowired
    private PresentationService presentationService;

    @GetMapping
    public ResponseEntity<List<Presentation>> getAllPresentations() {
        List<Presentation> presentations = presentationService.findAll();
        return new ResponseEntity<>(presentations, HttpStatus.OK);
    }

    @GetMapping("/{codeCip}")
    public ResponseEntity<Presentation> getPresentationByCip(@PathVariable String cip) {
        Presentation presentation = presentationService.findByCodeCip(cip);
        if (presentation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(presentation, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Presentation> createPresentation(@RequestBody Presentation presentation) {
        Presentation savedPresentation = presentationService.save(presentation);
        return new ResponseEntity<>(savedPresentation, HttpStatus.CREATED);
    }

    @PutMapping("/{codeCip}")
    public ResponseEntity<Presentation> updatePresentation(@PathVariable String cip, @RequestBody Presentation presentation) {
        presentationService.update(cip, presentation);
        if (updatePresentation(cip, presentation)== null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(presentation, HttpStatus.OK);
    }

    @DeleteMapping("/{cip}")
    public ResponseEntity<Void> deletePresentation(@PathVariable String cip) {
        boolean isDeleted = presentationService.delete(cip);
        if (!isDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

