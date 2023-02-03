package fr.miage.gromed.service.metier;

import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.dto.PresentationFicheDto;
import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.service.mapper.PresentationFicheMapper;
import fr.miage.gromed.service.mapper.PresentationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.springframework.data.util.Optionals.toStream;

@Service
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final PresentationMapper presentationMapper;
    private final PresentationFicheMapper presentationFicheMapper;
    private final PanierRepository panierRepository;

    @Autowired
    public PresentationService(PresentationRepository presentationRepository, PresentationMapper presentationMapper, PresentationFicheMapper presentationFicheMapper,
                               PanierRepository panierRepository) {
        this.presentationMapper = presentationMapper;
        this.presentationRepository = presentationRepository;
        this.presentationFicheMapper = presentationFicheMapper;
        this.panierRepository = panierRepository;
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

    public Page<PresentationDto> searchPresentation(String string, Pageable pageable) {
        var searchResult = presentationRepository.searchQuery(string, 0.1, pageable);
        return presentationMapper.toPageableDto(searchResult, pageable);
    }

    @Autowired
    PresentationMapper presentationeMapper;

    final static int NB_SUGESTION = 5;

    @Transactional(readOnly = true)
    public List<PresentationDto> getRandomPresentation() {
        long qty = presentationRepository.countAll();
        Random r =new Random();
        List<PresentationDto>  list = new ArrayList<>();
        for ( int i = 0  ;i < NB_SUGESTION ; i++) {
            presentationRepository.findById(r.nextLong(0, qty)).ifPresent(t -> list.add(presentationeMapper.toDto(t)));
        }
        return list;

    }

    @Transactional(readOnly = true)
    public List<PresentationDto> getSimilarPresentation(long CIP) {
        List<PresentationDto>  list = new ArrayList<>();
        presentationRepository.findByCodeCIP(CIP).ifPresent(presentation -> {
            presentation.getMedicament().getPresentationList().forEach(presentationBiq -> {
                    if (presentationBiq.getCodeCIP() != CIP) {
                        list.add(presentationeMapper.toDto(presentationBiq));
                    }
        });
    });
        return list;
    }
}
