package fr.miage.gromed.service.mapper;

import fr.miage.gromed.dto.UtilisateurDto;
import fr.miage.gromed.model.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UtilisateurMapper implements EntityMapper<UtilisateurDto, Utilisateur>{

    @Override
    public Utilisateur toEntity(UtilisateurDto dto) {
        return null;
    }

    @Override
    public UtilisateurDto toDto(Utilisateur entity) {
        return UtilisateurDto.builder()
                .nom(entity.getNom())
                .email(entity.getEmail())
                .id(entity.getId())
                .build();
    }

    @Override
    public List<Utilisateur> toEntity(List<UtilisateurDto> dtoList) {
        return null;
    }

    @Override
    public List<UtilisateurDto> toDto(List<Utilisateur> entityList) {
        return null;
    }

    @Override
    public Set<UtilisateurDto> toDto(Set<Utilisateur> entityList) {
        return null;
    }
}
