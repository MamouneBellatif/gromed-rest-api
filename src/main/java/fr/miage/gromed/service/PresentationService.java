package fr.miage.gromed.service;

import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.service.mapper.MedicamentMapper;
import fr.miage.gromed.service.mapper.PresentationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

@Service
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final PresentationMapper presentationMapper;

    @Autowired
    public PresentationService(PresentationRepository presentationRepository, PresentationMapper presentationMapper) {
        this.presentationMapper = presentationMapper;
        this.presentationRepository = presentationRepository;
    }

    public Page<PresentationDto> searchPresentation(String string, Pageable pageable){
        var presentationPage = presentationRepository.findByLibelleContainingIgnoreCaseOrMedicamentDenominationContainingIgnoreCaseOrCodeCIPContaining(string, pageable);
        return presentationMapper.toPageableDto(presentationPage, pageable);
    }
}
