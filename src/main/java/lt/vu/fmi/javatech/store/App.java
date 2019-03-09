package lt.vu.fmi.javatech.store;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import lt.vu.fmi.javatech.store.model.Cereal;
import lt.vu.fmi.javatech.store.model.Milk;

public class App {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        Store store = new Store();
        
        Milk m = new Milk(12345L, new Date(), new Date(), 0, 0, "Rokiškio", "paper");
        store.add(m, 100);
        
        store.add(new Cereal(123456L, "Corn", new Date(), new Date(), 0, 0, "Kellogg's"), 1000);
        
        store.rem(123456L, 50);
        
        System.out.println(store);

        //File f = new File("./src/main/resources/banking/test.txt");


    }
    
}
