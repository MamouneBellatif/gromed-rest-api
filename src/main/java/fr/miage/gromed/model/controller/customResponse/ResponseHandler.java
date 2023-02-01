package fr.miage.gromed.model.controller.customResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ResponseHandler {
    static Logger logger = Logger.getLogger(ResponseHandler.class.getName());
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj);
        logger.info("handler data: " + responseObj);
        return new ResponseEntity<Object>(map,status);
    }

    public static ResponseEntity<Object> generateFailureResponse(String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        return new ResponseEntity<Object>(map,status);
    }
}
