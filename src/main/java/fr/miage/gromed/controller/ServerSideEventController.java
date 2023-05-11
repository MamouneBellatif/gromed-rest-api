package fr.miage.gromed.controller;

import fr.miage.gromed.model.Stock;
import fr.miage.gromed.service.listeners.StockUpdateEvent;
import fr.miage.gromed.service.metier.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController("/api/stock")
public class ServerSideEventController {

   private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

   @GetMapping({ "/subscribe/{productId}" })
   public SseEmitter stockSubscription(@PathVariable long productId) {
       SseEmitter emitter = new SseEmitter();
       emitters.computeIfAbsent(productId, k -> new ArrayList<>()).add(emitter);
       emitter.onCompletion(() -> emitters.get(productId).remove(emitter));
       return emitter;
   }
//
//    private final List<SseEmitter> backEmitter = new CopyOnWriteArrayList<>();
//    @GetMapping({ "/back/" })
//    public SseEmitter stockSubscription(@PathVariable long productId) {
//        SseEmitter emitter = new SseEmitter();
//        emitters.computeIfAbsent(productId, k -> new ArrayList<>()).add(emitter);
//        emitter.onCompletion(() -> emitters.get(productId).remove(emitter));
//        return emitter;
//    }

   @Autowired
   private StockService stockService;

   @EventListener
   public void handleStockUpdate(StockUpdateEvent event) {
       Stock stock = event.getStock();
       long productId = stock.getPresentation().getId();
       List<SseEmitter> productEmitters = emitters.get(productId);
       if (productEmitters != null) {
           List<SseEmitter> deadEmitters = new ArrayList<>();
           for (SseEmitter emitter : productEmitters) {
               try {
                   emitter.send(SseEmitter.event().data(stock));
               } catch (Exception ex) {
                   deadEmitters.add(emitter);
                   emitter.completeWithError(ex);
               }
           }
           productEmitters.removeAll(deadEmitters);
       }
   }



   @GetMapping("/backOfficeSubscription")
       public ResponseEntity<Object> backOfficeSubscription() throws InterruptedException, IOException {
       final SseEmitter emitter = new SseEmitter();
       service.addEmitter(emitter);
       service.doNotify();
       emitter.onCompletion(() -> service.removeEmitter(emitter));
       emitter.onTimeout(() -> service.removeEmitter(emitter));
       return new ResponseEntity<>(emitter, HttpStatus.OK);
       return null;
   }

}
