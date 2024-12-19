import org.example.data.parse.CsvErrors;
import org.example.data.parse.CsvUtil;
import org.example.data.parse.ParseError;
import org.example.util.Color;
import org.example.util.StringTableFormatter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashSet;

public class PrintTest {

    @Test
    void testTable(){
        Color.init();
        StringTableFormatter formatter = new StringTableFormatter("|", 20, "Empfänger-Addresse", "Menge", "Grund");
        formatter.add("max@mustermann.de", "100", "Whatever");
        formatter.add("heinrich@hotmail.com", "101230", "Works");
        formatter.add("julian@gmail.com", "100.6789", "Works");
        formatter.print();

        HashSet<ParseError> test = new HashSet<>();
        test.add(CsvErrors.produceFromWrongLength(1, 2, 3));
        test.add(CsvErrors.produceFromWrongLength(10, 2, 3));
        test.add(CsvErrors.produceFromWrongLength(2, 1, 3));

        test.add(CsvErrors.produceFromWrongEntry(3, "12314/qweqre.de", "Keine valide E-Mail"));
        test.add(CsvErrors.produceFromWrongEntry(23, "123r", "Keine valide Zahl"));
        test.add(CsvErrors.produceFromWrongEntry(123, "-5", "Negative Zahl"));

        CsvUtil.printWrongSyntax(new File(""), test);

    }

}
