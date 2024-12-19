import org.example.data.CsvDatasheet;
import org.example.util.Color;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileTest {

    /*
    CSV:

    correct@mail.com;100;Hi
    etwas@mail.com;123.000;Test
    */

    @Test
    void testFiles(){
        Color.init();
        CsvDatasheet datasheet = new CsvDatasheet(new File("C:/Users/bernd/Desktop/correct.csv"));
        boolean success = datasheet.load();
        System.out.println(datasheet.getAll());
    }

}
