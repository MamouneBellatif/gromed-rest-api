package fr.miage.gromed.scheduler.restockfaker;

import org.springframework.stereotype.Component;

@Component
public class mockDeliverer {


        public void deliver(String cip, int quantity) {
            System.out.println("Delivering " + quantity + " of " + cip);
        }
}
