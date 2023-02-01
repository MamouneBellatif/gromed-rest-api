package fr.miage.gromed.service.metier;

import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.dto.PresentationFicheDto;
import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.service.mapper.PresentationFicheMapper;
import fr.miage.gromed.service.mapper.PresentationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return presentationRepository.findByCodeCIP(presentationCip)
                .map(presentationFicheMapper::toDto)
                .orElseThrow(PresentationNotFoundException::new);
//        Optional<Presentation> presentationDto = presentationRepository.findByCodeCIP(presentationCip);
//        if (presentationDto.isEmpty()) {
//            throw new PresentationNotFoundException();
//        }
//        return presentationFicheMapper.toDto(presentationDto.get());
    }

    public Page<PresentationDto> searchPresentation(String string, Pageable pageable){
            var searchResult = presentationRepository.searchQuery(string,0.1, pageable);
            return presentationMapper.toPageableDto(searchResult, pageable);
    }
}
