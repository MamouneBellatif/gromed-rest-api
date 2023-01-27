package fr.miage.gromed.controller;

import fr.miage.gromed.controller.curstomResponse.AbstractResponseEntity;
import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.model.Panier;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
public class PanierResponseEntity extends AbstractResponseEntity<PanierDto,String> {
    public PanierResponseEntity(PanierDto body, HttpStatus status) {
        super(body, status);
    }
    public PanierResponseEntity(String message, HttpStatus status) {
        super(null, status);
    }
}
