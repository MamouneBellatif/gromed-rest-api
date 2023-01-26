package fr.miage.gromed.model.medicament;

import fr.miage.gromed.dto.PresentationDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * A DTO for the {@link Medicament} entity
 */
@Data
public class MedicamentDto implements Serializable {
    private final int codeCIS;
    private final String denomination;
    private final String formePharmaceutique;
    private final String voiesAdministration;
    private final String typeProcedureAMM;
    private final Boolean isSurveillanceRenforcee;
    private final String etatCommercialisation;
    private final Date dateAMM;
    private final String statutAdministratif;
    private final Set<String> informationImportantesHtmlAnchor;
    private final Set<PresentationDto> presentationList;
}