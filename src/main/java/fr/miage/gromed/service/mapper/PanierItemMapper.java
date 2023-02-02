package fr.miage.gromed.service.mapper;

import fr.miage.gromed.dto.PanierItemDto;
import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.model.PanierItem;
import fr.miage.gromed.model.medicament.Presentation;
import fr.miage.gromed.repositories.PresentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class PanierItemMapper implements EntityMapper<PanierItemDto, PanierItem>{

    @Autowired
    private PresentationRepository presentationRepository;

    @Override
    public PanierItem toEntity(PanierItemDto dto) {
        Optional<Presentation> presentationOpt = presentationRepository.findByCodeCIP(dto.getPresentationCip());
        System.out.println(dto.getPresentationCip());
        if(presentationOpt.isEmpty()){
            throw new PresentationNotFoundException();
        }
        return PanierItem.builder()
                .quantite(dto.getQuantite())
                .presentation(presentationOpt.get())
                .delivree(0)
                .build();
    }

    @Override
    public PanierItemDto toDto(PanierItem entity) {
        return PanierItemDto.builder()
                .quantite(entity.getQuantite())
                .prix(entity.getPresentation().getHonoraireRemboursement()+entity.getPresentation().getPrixDeBase())
                .presentationDenomination(entity.getPresentation().getLibelle()+" "+entity.getPresentation().getMedicament().getDenomination())
                .presentationCip(entity.getPresentation().getCodeCIP())
                .build();
    }

    @Override
    public List<PanierItem> toEntity(List<PanierItemDto> dtoList) {
        return null;
    }

    @Override
    public List<PanierItemDto> toDto(List<PanierItem> entityList) {
        return null;
    }

    @Override
    public Set<PanierItemDto> toDto(Set<PanierItem> entityList) {
        return null;
    }
}
