package fr.miage.gromed.model.medicament;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Medicament} entity
 */
@Data
public class MedicamentDto implements Serializable {
    private final int codeCIS;
    private final String denomination;
    private final String formePharmaceutique;
    private final String voiesAdministration;
}