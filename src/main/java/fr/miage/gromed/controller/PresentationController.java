package fr.miage.gromed.controller;

import fr.miage.gromed.controller.customResponse.ResponseHandler;
import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.dto.PresentationFicheDto;
import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.service.PresentationService;
import fr.miage.gromed.service.mapper.PresentationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path="/api/presentation/",produces = MediaType.APPLICATION_JSON_VALUE)
public class PresentationController {

    private PresentationMapper presentationMapper;
    private final PresentationService presentationService;
    private final PresentationRepository presentationRepository;

    @Autowired
    public PresentationController(PresentationService presentationService,
                                  PresentationRepository presentationRepository) {
        this.presentationService = presentationService;
        this.presentationRepository = presentationRepository;
    }



    @GetMapping(value = "/{searchQuery}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PresentationDto>> searchByString(@PathVariable String searchQuery, @PageableDefault(sort = "libelle", size = 10) Pageable pageable) {
        var presentationPage = presentationService.searchPresentation(searchQuery,pageable);
        return ResponseEntity.ok(presentationPage);
    }

    @GetMapping(value = "/{idPresentation}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPresentation(@PathVariable Long idPresentation) {
        try {
            var presentationFicheDto = presentationService.getPresentationFiche(idPresentation);
            return ResponseEntity.ok(presentationFicheDto);
        } catch (PresentationNotFoundException pe) {
            return ResponseEntity.notFound().build();
        }
    }
}
