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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    public Page<PresentationDto> searchPresentation(String string, Pageable pageable){
            var searchResult = presentationRepository.searchQuery(string,0.1, pageable);
            return presentationMapper.toPageableDto(searchResult, pageable);
    }

    @Autowired
    PresentationMapper presentationeMapper;

    @Transactional(readOnly = true)
    public List<PresentationDto> getRandomPresentation() {
        Random random = new Random();
        int randomInt = random.nextInt(100);
        List<String> ints = Arrays.stream(random.ints(randomInt, 100, 45024).distinct().limit(5).toArray()).mapToObj(String::valueOf).toList();
        Pageable five = PageRequest.of(0, 5);
        Page<Presentation> pres = presentationRepository.findByCodeCIPIn(ints.get(1), ints.get(1), ints.get(2), ints.get(3), ints.get(4), five) ;
        return presentationeMapper.toListDto(pres, five);

    }
}
