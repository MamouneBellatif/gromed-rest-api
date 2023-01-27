package fr.miage.gromed.controller.curstomResponse;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


public interface AbstractResponseEntity<E,String> {



    public E getCustomBody();

    public void setBody(E body);

}
