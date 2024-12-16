import org.example.data.CsvDatasheet;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileTest {

    @Test
    void testFiles(){
        CsvDatasheet datasheet = new CsvDatasheet(new File("C:/Users/bernd/Desktop/Mappe1.csv"));
        datasheet.load();
        System.out.println(datasheet.getAll());
    }

}
