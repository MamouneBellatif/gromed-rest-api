package fr.miage.gromed.service.mapper;

import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.model.medicament.Medicament;
import fr.miage.gromed.model.medicament.Presentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PresentationMapper implements EntityMapper<PresentationDto, Presentation>{
    @Override
    public Presentation toEntity(PresentationDto dto) {
        return null;
    }

    @Override
    public PresentationDto toDto(Presentation entity) {
        return PresentationDto.builder().
                codeCIP(entity.getCodeCIP())
                .libelle(entity.getLibelle())
                .prixDeBase(entity.getPrixDeBase())
                .honoraireRemboursement(entity.getHonoraireRemboursement())
                .medicamentDenomination(entity.getMedicament().getDenomination())
                .build();
    }

    public Page<PresentationDto> toPageableDto(Page<Presentation> entityPage, Pageable pageable) {
        List<Presentation> entities = entityPage.getContent();
        return new PageImpl<PresentationDto>(this.toDto(entities), pageable, entityPage.getTotalElements());
    }

    @Override
    public List<Presentation> toEntity(List<PresentationDto> dtoList) {
        return null;
    }

    @Override
    public List<PresentationDto> toDto(List<Presentation> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Set<PresentationDto> toDto(Set<Presentation> entityList) {
        return null;
    }
}
