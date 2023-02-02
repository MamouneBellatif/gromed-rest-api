package fr.miage.gromed.service.listeners;

import fr.miage.gromed.model.Stock;

public class StockUpdateEvent {
    private Stock stock;

    public StockUpdateEvent(Stock stock) {
        this.stock = stock;
    }

    public Stock getStock() {
        return this.stock;

}
}
