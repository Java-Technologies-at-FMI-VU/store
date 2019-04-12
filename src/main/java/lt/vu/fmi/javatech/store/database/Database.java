package lt.vu.fmi.javatech.store.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;

public class Database {

    private final Path file;

    public Database(Path file) {
        this.file = file;
    }
    
    public <T extends Serializable> void save(T o) throws IOException {
        try (FileOutputStream fout = new FileOutputStream(file.toFile())) {
            try (ObjectOutputStream out = new ObjectOutputStream(fout)) {
                out.writeObject(o);
            }
        }
    }

    public <T extends Serializable> T load() throws FileNotFoundException, IOException, ClassNotFoundException {
        try (FileInputStream fin = new FileInputStream(file.toFile())) {
            try (ObjectInputStream in = new ObjectInputStream(fin)) {
                return (T) in.readObject();
            }
        }
    }
    
}
