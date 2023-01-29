package fr.miage.gromed.controller;

import fr.miage.gromed.service.listeners.SSEservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


import org.springframework.http.HttpStatus;
//TODO: abonnement mis a jour de sock

@RestController
@CrossOrigin
public class SSEController {

//    @Autowired
    private SSEservice service;

    final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping("/notification")
    public ResponseEntity<SseEmitter> doNotify() throws InterruptedException, IOException {
//        final SseEmitter emitter = new SseEmitter();
//        service.addEmitter(emitter);
//        service.doNotify();
//        emitter.onCompletion(() -> service.removeEmitter(emitter));
//        emitter.onTimeout(() -> service.removeEmitter(emitter));
//        return new ResponseEntity<>(emitter, HttpStatus.OK);
        return null;
    }

     @GetMapping("/stockSubscription")
        public ResponseEntity<Object> stockSubscription() throws InterruptedException, IOException {
//        final SseEmitter emitter = new SseEmitter();
//        service.addEmitter(emitter);
//        service.doNotify();
//        emitter.onCompletion(() -> service.removeEmitter(emitter));
//        emitter.onTimeout(() -> service.removeEmitter(emitter));
//        return new ResponseEntity<>(emitter, HttpStatus.OK);
         return null;
    }

    @GetMapping("/backOfficeSubscription")
        public ResponseEntity<Object> backOfficeSubscription() throws InterruptedException, IOException {
//        final SseEmitter emitter = new SseEmitter();
//        service.addEmitter(emitter);
//        service.doNotify();
//        emitter.onCompletion(() -> service.removeEmitter(emitter));
//        emitter.onTimeout(() -> service.removeEmitter(emitter));
//        return new ResponseEntity<>(emitter, HttpStatus.OK);
        return null;
    }

    @GetMapping("/panierSubscription/{id}")
    public ResponseEntity<Object> panierSubscription(@PathVariable Long id ) throws InterruptedException, IOException {
//        final SseEmitter emitter = new SseEmitter();
//        service.addEmitter(emitter);
//        service.doNotify();
//        emitter.onCompletion(() -> service.removeEmitter(emitter));
//        emitter.onTimeout(() -> service.removeEmitter(emitter));
//        return new ResponseEntity<>(emitter, HttpStatus.OK);
        return null;
    }
}