package fr.miage.gromed.dto;

import fr.miage.gromed.model.medicament.Medicament;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link Medicament} entity
 */
@Data
@Setter
@Getter
public class MedicamentDto implements Serializable {
    private final int codeCIS;
    private final String denomination;
    private final String formePharmaceutique;
    private final String voiesAdministration;
}