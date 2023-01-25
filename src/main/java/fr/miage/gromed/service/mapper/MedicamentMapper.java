package fr.miage.gromed.service.mapper;

import fr.miage.gromed.dto.MedicamentDto;
import fr.miage.gromed.model.medicament.Medicament;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

@Mapper(componentModel = "spring")
@Repository
public interface MedicamentMapper extends EntityMapper<MedicamentDto, Medicament> {
}