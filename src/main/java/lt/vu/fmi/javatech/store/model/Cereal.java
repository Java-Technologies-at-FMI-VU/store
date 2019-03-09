package lt.vu.fmi.javatech.store.model;

import java.util.Date;

public class Cereal extends Product {
    
    private final String type;

    public Cereal(long barcode, String type, Date productionDate, Date expireDate, int price, int volume, String producer) {
        super(barcode, productionDate, expireDate, price, volume, producer);
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
}
