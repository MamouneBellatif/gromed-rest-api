package fr.miage.gromed.service;

import com.google.firebase.auth.UserRecord;
import fr.miage.gromed.model.Utilisateur;
import fr.miage.gromed.model.enums.PerimetreUtilisateur;
import fr.miage.gromed.repositories.UtilisateurRepository;
import fr.miage.gromed.service.auth.UserContextHolder;
import fr.miage.gromed.service.mapper.UtilisateurMapper;
import fr.miage.gromed.service.metier.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    @Autowired
    private PanierService panierService;

    @Transactional
    public Utilisateur syncUser(UserRecord userRecord, String route) {
        Utilisateur user = utilisateurRepository.findById(userRecord.getUid()).orElse(registerUtilisateur(userRecord));
        if (user.isAwaitingResponse() && !route.contains("resolve")) {
            user.setAwaitingResponse(false);
//            panierService.getCurrentPanier()
        }
        return user;
    }

    private Utilisateur registerUtilisateur(UserRecord userFB){
        return  utilisateurRepository.save(Utilisateur.builder()
                .id(userFB.getUid())
                .nom(userFB.getDisplayName())
                .email(userFB.getEmail())
                .isBuying(false)
                .awaitingResponse(false)
                .perimetre(PerimetreUtilisateur.FRONT_OFFICE)
                .build());
    }

    public void setBuying(){
//        utilisateurRepository.isBuying(true);
        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
        if (utilisateur != null) {
            utilisateur.setBuying(true);
            utilisateurRepository.save(utilisateur);
        }
    }

    public Object login(String uid) {
                          return null;
    }
    Logger logger = Logger.getLogger(UtilisateurService.class.getName());
    public void await() {

        Utilisateur utilisateur = UserContextHolder.getUtilisateur();
//        if (utilisateur != null && !utilisateur.isAwaitingResponse() && utilisateur.isBuying()) {
        if (utilisateur != null && !utilisateur.isAwaitingResponse() ) {
            utilisateur.setAwaitingResponse(true);
            logger.info("StockIndisponible pour l'utilisateur " + utilisateur.getId());
        }
    }
}
