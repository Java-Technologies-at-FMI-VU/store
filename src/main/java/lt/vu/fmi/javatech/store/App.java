package lt.vu.fmi.javatech.store;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.nio.file.Paths;
import java.util.Date;
import lt.vu.fmi.javatech.store.database.Database;
import lt.vu.fmi.javatech.store.model.Cereal;
import lt.vu.fmi.javatech.store.model.Milk;

public class App {
    
    public static void main(String[] args) throws Exception {
        
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                
                bind(Database.class).toInstance(new Database(Paths.get("store.dat")));
                
            }
        });
        
        
        Store store = injector.getInstance(Store.class);
        Milk m = new Milk(22222L, new Date(), new Date(), 0, 0, "Vilkiskiu", "plastic");
        store.add(m, 1000);
        
//        Milk m = new Milk(12345L, new Date(), new Date(), 0, 0, "Rokiškio", "paper");
//        store.add(m, 100);
//        
//        store.add(new Cereal(123456L, "Corn", new Date(), new Date(), 0, 0, "Kellogg's"), 1000);
//        
//        store.rem(123456L, 50);
        
        System.out.println(store);
        
        store.save();
        
        store.test();

        //File f = new File("./src/main/resources/banking/test.txt");


    }
    
}
