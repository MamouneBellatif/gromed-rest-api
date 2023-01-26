package fr.miage.gromed.service;

import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.model.medicament.Medicament;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.MedicamentRepository;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.service.mapper.MedicamentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicamentService {

    private MedicamentRepository medicamentRepository;

    private MedicamentMapper medicamentMapper;

    @Autowired
    public MedicamentService(MedicamentRepository medicamentRepository, MedicamentMapper medicamentMapper) {
        this.medicamentRepository = medicamentRepository;
        this.medicamentMapper = medicamentMapper;
    }
    public int medicamentCount(){
        return medicamentRepository.findAll().size();
    }

    /**
     * Retourne la liste des formes pharmaceutiques
     *
     * Parser une fois plutôt que de le faire à chaque fois ?
     * */
    public Set<String> getFormes(){
        return medicamentRepository.findAll().stream().map(medicament -> medicament.getFormePharmaceutique()).collect(Collectors.toSet());
    }

    public Page<MedicamentDto> findByCondition(MedicamentDto medicamentDto, Pageable pageable) {
        Page<Medicament> entityPage = medicamentRepository.findAll(pageable);
        List<Medicament> entities = entityPage.getContent();
        return new PageImpl<>(medicamentMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }
    @Autowired
    PresentationRepository presentationRepository;

    public Set<Medicament> pageToList(Page<Medicament> page){
        return page.stream().collect(Collectors.toSet());
    }
    public Page<Medicament> searchByString(String string, Page<Integer> page, Pageable pageable){
//        List<Medicament> medicaments = medicamentRepository.findByDenominationContainingIgnoreCaseOrFormePharmaceutiqueContainingIgnoreCase(string, string);
//        Page<Presentation> presentationList = presentationRepository.findByLibelleContainingIgnoreCase(string, pageable);
        //recherche par composant
        //collect all presentations from medicaments
//        List<Presentation> presentationList1 = medicaments.stream().map(Medicament::getPresentationList).flatMap(Collection::stream).toList();
        //combine with presentationList
//        presentationList.addAll(presentationList1);
//        PageImpl ret = new PageImpl<>(presentationList, pageable, presentationList.size());
    return null;
    }
    
//        private final MedicamentRepository repository;
//    private final MedicamentMapper medicamentMapper;
//
//    public MedicamentService(MedicamentRepository repository, MedicamentMapper medicamentMapper) {
//        this.repository = repository;
//        this.medicamentMapper = medicamentMapper;
//    }


//
//    public MedicamentDto save(MedicamentDto medicamentDto) {
//        Medicament entity = medicamentMapper.toEntity(medicamentDto);
//        return medicamentMapper.toDto(repository.save(entity));
//    }
//
//    public void deleteById(Long id) {
//        repository.deleteById(id);
//    }
//
//    public MedicamentDto findById(Long id) {
//        return medicamentMapper.toDto(repository.findById(id).orElseThrow(ResourceNotFoundException::new));
//    }
//
//    public Page<MedicamentDto> findByCondition(MedicamentDto medicamentDto, Pageable pageable) {
//        Page<Medicament> entityPage = repository.findAll(pageable);
//        List<Medicament> entities = entityPage.getContent();
//        return new PageImpl<>(medicamentMapper.toDto(entities), pageable, entityPage.getTotalElements());
//    }
//
//    public MedicamentDto update(MedicamentDto medicamentDto, Long id) {
//        MedicamentDto data = findById(id);
//        Medicament entity = medicamentMapper.toEntity(medicamentDto);
//        BeanUtil.copyProperties(data, entity);
//        return save(medicamentMapper.toDto(entity));
//    }
}
