package lt.vu.fmi.javatech.store.model;

import java.util.Date;

public class Milk extends Product implements DairyBeverage {

    private final String containerType;
    
    public Milk(long barcode, Date productionDate, Date expireDate, int price, int volume, String producer, String containerType) {
        super(barcode, productionDate, expireDate, price, volume, producer);
        this.containerType = containerType;
    }

    @Override
    public String getContainerType() {
        return containerType;
    }

}
