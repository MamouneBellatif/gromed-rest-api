package fr.miage.gromed.service.mapper;

import fr.miage.gromed.dto.CommandeTypeDto;
import fr.miage.gromed.model.CommandeType;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CommandeTypeMapper implements EntityMapper<CommandeTypeDto, CommandeType> {

    @Autowired
    private PanierMapper panierMapper;

    @Override
    public CommandeType toEntity(CommandeTypeDto dto) {
        return null;
    }

    @Override
    public CommandeTypeDto toDto(CommandeType entity) {
        return CommandeTypeDto.builder()
                .id(entity.getId())
                .panier(panierMapper.toDto(entity.getPanier()))
                .build();
    }

    @Override
    public List<CommandeType> toEntity(List<CommandeTypeDto> dtoList) {
        return null;
    }

    @Override
    public List<CommandeTypeDto> toDto(List<CommandeType> entityList) {
        return null;
    }

    @Override
    public Set<CommandeTypeDto> toDto(Set<CommandeType> entityList) {
        return null;
    }
}
