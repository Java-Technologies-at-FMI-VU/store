package lt.vu.fmi.javatech.store.model;

import java.util.Date;
import java.util.Objects;

public abstract class Product {
    
    private final Long barcode;
    private final Date productionDate;
    private final Date expireDate;
    private final int price;
    private final int volume;
    private final String producer;

    public Product(long barcode, Date productionDate, Date expireDate, int price, int volume, String producer) {
        this.barcode = barcode;
        this.productionDate = productionDate;
        this.expireDate = expireDate;
        this.price = price;
        this.volume = volume;
        this.producer = producer;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public int getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public String getProducer() {
        return producer;
    }

    public Long getBarcode() {
        return barcode;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.barcode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (!Objects.equals(this.barcode, other.barcode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Product:" + this.getClass().getSimpleName() + " {barcode=" + barcode + '}';
    }
    
}
