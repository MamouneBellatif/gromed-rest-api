package fr.miage.gromed.service;

import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.dto.PresentationFicheDto;
import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.service.mapper.PresentationFicheMapper;
import fr.miage.gromed.service.mapper.PresentationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final PresentationMapper presentationMapper;
    private final PresentationFicheMapper presentationFicheMapper;

    @Autowired
    public PresentationService(PresentationRepository presentationRepository, PresentationMapper presentationMapper, PresentationFicheMapper presentationFicheMapper) {
        this.presentationMapper = presentationMapper;
        this.presentationRepository = presentationRepository;
        this.presentationFicheMapper = presentationFicheMapper;
    }

    public PresentationFicheDto getPresentationFiche(Long presentationCip) {
        Optional<Presentation> presentationDto = presentationRepository.findByCodeCIP(presentationCip);
        if (presentationDto.isEmpty()) {
            throw new PresentationNotFoundException();
        }
        return presentationFicheMapper.toDto(presentationDto.get());
    }

    public Page<PresentationDto> searchPresentation(String string, Pageable pageable){
        try {
            var cip = Long.parseLong(string);
             return presentationMapper.toPageableDto(presentationRepository.findByCodeCIP(cip,pageable),pageable);
        }catch (NumberFormatException e){
//            var presentationPage = presentationRepository.findByLibelleContainingIgnoreCaseOrMedicamentDenominationContainingIgnoreCase(string,string, pageable);
            var presentationPage = presentationRepository.findByLibelleContainingIgnoreCaseOrMedicamentDenominationContainingIgnoreCaseAndPrixDeBaseNotNullAndPrixDeBaseGreaterThan(string,string, pageable, 0.1);
            System.out.println(presentationPage.getSize());
            return presentationMapper.toPageableDto(presentationPage, pageable);
        }
    }
}
