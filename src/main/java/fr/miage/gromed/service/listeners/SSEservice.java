//package fr.miage.gromed.service.listeners;
//
//import java.io.IOException;
//import java.util.*;
//
//import org.springframework.http.MediaType;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//
////@Service
////@EnableScheduling
//public class SSEservice {
//
//    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
//    public void addEmitter(final SseEmitter emitter) {
//        emitters.add(emitter);
//    }
//
//    public void removeEmitter(final SseEmitter emitter) {
//        emitters.remove(emitter);
//    }
//
//    // @Scheduled(fixedRate = 5000)
//    @Async
//    public void doNotify() throws IOException {
//        List<SseEmitter> deadEmitters = new ArrayList<>();
//        emitters.forEach(emitter -> {
//            try {
//                emitter.send(SseEmitter.event()
//                        .data(DATE_FORMATTER.format(new Date()) + " : " + UUID.randomUUID().toString()));
//            } catch (Exception e) {
//                deadEmitters.add(emitter);
//            }
//        });
//        emitters.removeAll(deadEmitters);
//    }
//

