package fr.miage.gromed.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import fr.miage.gromed.controller.customResponse.ResponseHandler;
import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.service.metier.PresentationService;
import fr.miage.gromed.service.mapper.PresentationMapper;
import org.apache.http.Header;
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

    public static final String ERREUR_INTERNE = "Erreur interne";

    private final PresentationService presentationService;

    @Autowired
    public PresentationController(PresentationService presentationService,
                                  PresentationRepository presentationRepository) {
        this.presentationService = presentationService;
    }



    @GetMapping(value = "/{searchQuery}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PresentationDto>> searchByString( @PathVariable String searchQuery, @PageableDefault(sort = "libelle", size = 10) Pageable pageable) throws FirebaseAuthException {
        var presentationPage = presentationService.searchPresentation(searchQuery,pageable);
        return ResponseEntity.ok(presentationPage);
    }

    @GetMapping(value = "/fiche/{idPresentation}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPresentation(@PathVariable Long idPresentation) {
            var presentationFicheDto = presentationService.getPresentationFiche(idPresentation);
            return ResponseHandler.generateResponse("Presentation de la fiche", HttpStatus.OK,presentationFicheDto);

    }

    @ExceptionHandler(PresentationNotFoundException.class)
    public ResponseEntity<Object> handlePresentationNotFoundException(PresentationNotFoundException e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
