package fr.miage.gromed.service;

import fr.miage.gromed.dto.UtilisateurDto;
import fr.miage.gromed.exceptions.IncompleteUtilisateurException;
import fr.miage.gromed.exceptions.UtilisateurInexistantException;
import fr.miage.gromed.model.Utilisateur;
import fr.miage.gromed.repositories.UtilisateurRepository;
import fr.miage.gromed.service.mapper.UtilisateurMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }


//    public String checkUser(Principal principal) {
//        return principal.getName();
//    }
//        return utilisateurRepository.findById(principal.getId()).map(utilisateurMapper::toDto).orElse(
//                      utilisateurMapper.toDto(utilisateurRepository.save(Utilisateur.builder().id(id).build()))


    private UtilisateurDto register(String id){
        return null;
    }


    public Object login(String uid) {
                          return null;
    }
}
