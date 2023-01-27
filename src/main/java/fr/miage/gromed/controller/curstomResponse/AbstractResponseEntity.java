package fr.miage.gromed.controller.curstomResponse;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Builder
public abstract class AbstractResponseEntity<E,String> extends ResponseEntity<HttpStatus> {
    private E body;
    private String message;

        public AbstractResponseEntity(E body,
                                  HttpStatus status) {
        super(status);
        this.body = body;
    }

//    public AbstractResponseEntity(String message, org.springframework.http.HttpStatusCode  status) {
//        super(status);
//        this.message = message;
//    }

    public E getCustomBody() {
        return body;
    }

    public void setBody(E body) {
        this.body = body;
    }

}
