package fr.miage.gromed.service.listeners;
//
//import fr.miage.gromed.model.dto.MedicamentDto;
import fr.miage.gromed.service.mapper.MedicamentMapper;
//import fr.miage.gromed.model.medicament.Medicament;
//import fr.miage.gromed.model.repository.MedicamentRepository;
import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.model.medicament.Medicament;
import fr.miage.gromed.repositories.MedicamentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MedicamentService {

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private MedicamentMapper medicamentMapper;

    public Page<MedicamentDto> findByCondition(MedicamentDto medicamentDto, Pageable pageable) {
        Page<Medicament> entityPage = medicamentRepository.findAll(pageable);
        List<Medicament> entities = entityPage.getContent();
        return new PageImpl<>(medicamentMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }
}