package fr.miage.gromed.service.mapper;

import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.model.medicament.Medicament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Component
public class MedicamentMapper {


    public Medicament toEntity(MedicamentDto dto) {



        return null;
    }

    public Page<MedicamentDto> toPageableDto(Page<Medicament> entityPage, Pageable pageable) {
        List<Medicament> entities = entityPage.getContent();
        return new PageImpl<MedicamentDto>(this.toDto(entities), pageable, entityPage.getTotalElements());
    }


    public MedicamentDto toDto(Medicament entity) {
        return  MedicamentDto.builder()
                .codeCIS(entity.getCodeCIS())
                .denomination(entity.getDenomination())
                .formePharmaceutique(entity.getFormePharmaceutique())
                .voiesAdministration(entity.getVoiesAdministration())
                .build();
    }


    public List<Medicament> toEntity(List<MedicamentDto> dtoList) {
        return null;
    }


    public List<MedicamentDto> toDto(List<Medicament> entityList) {
        return null;
    }


    public Set<MedicamentDto> toDto(Set<Medicament> entityList) {
        return null;
    }
}
