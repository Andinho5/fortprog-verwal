import org.example.data.CsvParser;
import org.example.util.Color;
import org.example.util.StringTableFormatter;
import org.junit.jupiter.api.Test;

import java.io.File;

public class PrintTest {

    @Test
    void testTable(){
        Color.init();
        StringTableFormatter formatter = new StringTableFormatter("|", 20, "Empfänger-Addresse", "Menge", "Grund");
        formatter.add("max@mustermann.de", "100", "Whatever");
        formatter.add("heinrich@hotmail.com", "101230", "Works");
        formatter.add("julian@gmail.com", "100.6789", "Works");
        formatter.print();

        CsvParser.printWrongSyntax(new File(""));

    }

}
