package fr.miage.gromed.service.mapper;

import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.dto.PanierItemDto;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.PanierItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class PanierMapper implements EntityMapper<PanierDto, Panier> {

    Logger logger = LoggerFactory.getLogger(PanierMapper.class);
    @Autowired
    private PanierItemMapper panierItemMapper;

    @Override
    public Panier toEntity(PanierDto dto) {
        return null;
    }

    @Override
    public PanierDto toDto(Panier entity) {
        Set<PanierItem> listItems = entity.getItems();
        logger.info("PanierMapper.toDto: listItems = "+listItems.size());
        List<PanierItemDto> listItemsDto = listItems.stream().map(item -> panierItemMapper.toDto(item)).toList();
        logger.info("PanierMapper.toDto: listItemsDto = "+listItemsDto.size());

        return PanierDto.builder()
                .id(entity.getId())
                .items(listItemsDto)
                .dateCreation(entity.getDateCreation())
                .expiresAt(entity.getExpiresAt())
                .status(this.getStatus(entity))
                .build();
    }

    private String getStatus(Panier entity){
        var isActif = entity.getExpiresAt().isAfter(LocalDateTime.now()) && !entity.isPaid() && !entity.isCanceled() && !entity.isExpired();;
        if (isActif) return "Actif";
        if(entity.isExpired()) return "Expiré";
        if(entity.isPaid()) return "Payé";
        if(entity.isCanceled()) return "Annulé";
        if(entity.isDelivered()) return "Livré";
        if(entity.isPaid() && !entity.isDelivered()) return "En cours de livraison";
        return "";
    }

    public PanierDto toDtoCreation(Panier entity, String status){
        PanierDto pDto = this.toDto(entity);
        pDto.setStatus(status);
        return pDto;
    }

    @Override
    public List<Panier> toEntity(List<PanierDto> dtoList) {
        return null;
    }

    @Override
    public List<PanierDto> toDto(List<Panier> entityList) {
        return null;
    }

    @Override
    public Set<PanierDto> toDto(Set<Panier> entityList) {
        return null;
    }
}
