package fr.miage.gromed.controller;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import fr.miage.gromed.controller.customResponse.ResponseHandler;
import fr.miage.gromed.dto.UtilisateurDto;
import fr.miage.gromed.exceptions.IncompleteUtilisateurException;
import fr.miage.gromed.exceptions.UtilisateurInexistantException;
import fr.miage.gromed.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController("/api/utilisateur")
public class UtilisateurController {


    private final UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/login/{uid}")
    public ResponseEntity<Object> verifyToken(@PathVariable String uid,  @RequestHeader("Authorization") String authorization) throws Exception {
//        String idToken = authorization.replace("Bearer ", "");
//
//        FirebaseAuth.getInstance().verifyIdToken(idToken);
        return ResponseHandler.generateResponse("Utilisateur connecté", HttpStatus.OK, utilisateurService.login(uid));
    }
//
//    @GetMapping("/login")
//    public ResponseEntity<Object> verifyToken(Principal principal)   {
//        return ResponseHandler.generateResponse("Utilisateur connecté", HttpStatus.OK, utilisateurService.checkUser(principal));
//    }

//    @PostMapping("/login/{id}")
//    public ResponseEntity<Object> login(@PathVariable String id, @RequestBody UtilisateurDto utilisateurDto){
//       return ResponseHandler.generateResponse("Utilisateur connecté", HttpStatus.OK, utilisateurService.login(id));
//    }
//
//    @PutMapping('/register')
//    public ResponseEntity<Object> register(@RequestBody UtilisateurDto utilisateurDto, @PathVariable Long id){
//       return ResponseHandler.generateResponse("Utilisateur connecté", HttpStatus.OK, utilisateurService.login(id));
//    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(IncompleteUtilisateurException e) {
        return ResponseHandler.generateFailureResponse(e.getMessage(), HttpStatus.OK);

//        return ResponseHandler.generateFailureResponse(ERREUR_INTERNE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
