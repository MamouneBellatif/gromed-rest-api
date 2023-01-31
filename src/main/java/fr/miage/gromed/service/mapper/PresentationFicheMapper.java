package fr.miage.gromed.service.mapper;

import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.dto.PresentationFicheDto;
import fr.miage.gromed.model.medicament.Medicament;
import fr.miage.gromed.model.medicament.Presentation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
public class PresentationFicheMapper implements EntityMapper<PresentationFicheDto, Presentation>{

    @Override
    public Presentation toEntity(PresentationFicheDto dto) {
        return null;
    }

    @Override
    public PresentationFicheDto toDto(Presentation entity) {
        Medicament medicament = entity.getMedicament();
        return PresentationFicheDto.builder().
                codeCIP(entity.getCodeCIP())
                .libelle(entity.getLibelle())
                .medicamentDenomination(medicament.getDenomination())
                .prixDeBase(entity.getPrixDeBase())
                .imageUrl(medicament.getUrlImage())
                .stock(entity.getStock().getQuantiteStockLogique())
                .honoraireRemboursement(entity.getHonoraireRemboursement())
                .medicamentDenomination(medicament.getDenomination())
                .build();
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

