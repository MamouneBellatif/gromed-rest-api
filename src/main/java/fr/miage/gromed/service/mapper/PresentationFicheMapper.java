package fr.miage.gromed.service.mapper;

import fr.miage.gromed.dto.PresentationFicheDto;
import fr.miage.gromed.model.medicament.Presentation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Component
public class PresentationFicheMapper implements EntityMapper<PresentationFicheDto, Presentation>{

    @Override
    public Presentation toEntity(PresentationFicheDto dto) {
        return null;
    }

    @Override
    public PresentationFicheDto toDto(Presentation entity) {
        return null;
    }

    @Override
    public List<Presentation> toEntity(List<PresentationFicheDto> dtoList) {
        return null;
    }

    @Override
    public List<PresentationFicheDto> toDto(List<Presentation> entityList) {
        return null;
    }

    @Override
    public Set<PresentationFicheDto> toDto(Set<Presentation> entityList) {
        return null;
    }
}

