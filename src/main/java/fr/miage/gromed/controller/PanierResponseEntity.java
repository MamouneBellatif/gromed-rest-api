package fr.miage.gromed.controller;

import fr.miage.gromed.controller.curstomResponse.AbstractResponseEntity;
import fr.miage.gromed.dto.PanierDto;
import fr.miage.gromed.model.Panier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


public class PanierResponseEntity extends ResponseEntity<HttpStatus> implements AbstractResponseEntity<PanierDto,String>  {

    private PanierDto body;
    private String message;

    public PanierResponseEntity(PanierDto body, HttpStatus status) {
        super(status);
        this.body = body;
    }

    public PanierResponseEntity(String message, HttpStatus status) {
        super(status);
        this.message = message;
    }
    public PanierResponseEntity(String message, PanierDto body, HttpStatus status) {
        super(status);
        this.message = message;
        this.body = body;
    }

    @Override
    public PanierDto getCustomBody() {
        return null;
    }

    @Override
    public void setBody(PanierDto body) {

    }
}
