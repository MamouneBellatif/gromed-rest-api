package fr.miage.gromed.controller;

import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.service.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/presentation/",produces = MediaType.APPLICATION_JSON_VALUE)
public class PresentationController {

    private PresentationService presentationService;
    @Autowired
    public PresentationController(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @PostMapping(value = "/{searchQuery}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PresentationDto>> searchByString(@PathVariable String searchQuery, @PageableDefault(sort = "libelle", size = 10) Pageable pageable) {
        var presentationPage = presentationService.searchPresentation(searchQuery,pageable);
        return ResponseEntity.ok(presentationPage);
    }
}
