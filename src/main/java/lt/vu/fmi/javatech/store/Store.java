package lt.vu.fmi.javatech.store;

import java.util.Map;
import lt.vu.fmi.javatech.store.model.Product;
import lt.vu.fmi.javatech.store.util.StoreHashMap;

public class Store {

    private final Map<Long, StockItem> stock = new StoreHashMap<>();

    private StockItem getAdd(Product p) {
        if (!stock.containsKey(p.getBarcode())) {
            StockItem si = new StockItem(p);
            stock.put(p.getBarcode(), si);
            return si;
        }
        return stock.get(p.getBarcode());
    }
    
    public void add(Product p, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bad amount provided!");
        }
        StockItem si = getAdd(p);
        si.amount += amount;
    }

    public void add(Long barcode, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bad amount provided!");
        }
        StockItem si = stock.get(barcode);
        if (si == null) {
            throw new IllegalArgumentException("Barcode not found!");
        }
        si.amount += amount;
    }
    
    public void rem(Product p, int amount) {
        rem(p.getBarcode(), amount);
    }

    public void rem(Long barcode, int amount) {
        
        if (!stock.containsKey(barcode)) {
            throw new IllegalArgumentException("No product!");
        }
        
        StockItem si = stock.get(barcode);
        
        if (amount <= 0 || si.amount < amount) {
            throw new IllegalArgumentException("Bad amount provided!");
        }
        
        si.amount -= amount;
    }
    
    private class StockItem {
        
        Product product;
        int amount = 0;

        public StockItem(Product p) {
            this.product = p;
        }
        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (StockItem si: stock.values()) {
            sb.append(si.product).append(" = ").append(si.amount).append("\n");
        }
        return sb.toString();
    }
    
}
