package lt.vu.fmi.javatech.store.model;

/**
 * 
 * @author javatech
 */
public interface DairyBeverage extends Beverage {
   
    /**
     * Labas.
     * @return .
     */
    default boolean isHealthy() {
        return !getContainerType().equals("plastic");
    }
    
//    private boolean isPlastic() {
//        return getContainerType().equals("plastic");
//    }
    
}
