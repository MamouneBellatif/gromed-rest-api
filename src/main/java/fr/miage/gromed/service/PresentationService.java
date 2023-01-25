package fr.miage.gromed.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PresentationRepository;

public class PresentationService {
    @Autowired 
    private PresentationRepository presentationRepository;

    public List<Presentation> findAll() {
        return presentationRepository.findAll();
    }

    public Presentation findByCodeCip(String cip) {
        Optional<Presentation> presentation = presentationRepository.findByCodeCip(cip);
        if (presentation.isPresent()) {
            return presentation.get();
        } else {
            return null;
        }
    }

    public Presentation save(Presentation presentation) {
        return presentationRepository.save(presentation);
    }

    public boolean delete(String cip) {
        Optional<Presentation> existingPresentation = presentationRepository.findByCodeCip(cip);
        if (existingPresentation.isPresent()) {
            presentationRepository.delete(existingPresentation.get());
            return true;
        } else {
            return false;
        }
    }

    public void update(String cip, Presentation presentation) {
        Optional<Presentation> existingPresentation = presentationRepository.findByCodeCip(cip);
        if (existingPresentation.isPresent()) {
            Presentation presentationToUpdate = existingPresentation.get();
            presentationToUpdate.setLibelle(presentation.getLibelle());
            presentationToUpdate.setPrixDeBase(presentation.getPrixDeBase());
            presentationToUpdate.setHonoraireRemboursement(presentation.getHonoraireRemboursement());
            presentationToUpdate.setDateDeclaration(presentation.getDateDeclaration());
            presentationToUpdate.setEtatCommercialisation(presentation.getEtatCommercialisation());
            presentationToUpdate.setStock(presentation.getStock());
            save(presentationToUpdate);
        
        } else {
            presentationRepository.save(presentation);
        }
    }
}

