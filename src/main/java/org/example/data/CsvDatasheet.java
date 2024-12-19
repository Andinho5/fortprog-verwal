package org.example.data;

import org.example.data.parse.CsvErrors;
import org.example.data.parse.CsvUtil;
import org.example.data.parse.ParseError;
import org.example.transaction.BaseTransaction;
import org.example.util.Color;
import org.example.util.Regex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

/**
 * A class that implements {@link AbstractDatasheet} and is used for .csv-file format.
 * This class supports two different formats: write and read.
 * <p>The write format is used when data is received from the database and written to the file and looks like this:
 * <pre><code>
 *  id;receiverId;amount;date;purpose
 *  1;123;40;12.10.2024 16:40:13;rent
 * </code></pre></p>
 * <p>The read format uses a much simpler abstraction as it must be as user-friendly as possible and looks like:
 * <pre><code>
 * to@receiver.com;23.30;Take my money
 * </code></pre>
 * Note that the first parameter represents the {@code receivers email}, the second parameter the {@code amount} and the
 * third one the {@code purpose message}.
 * </p>
 */
public class CsvDatasheet extends AbstractDatasheet {

    private static final char DELIMITER = ';';


    public CsvDatasheet(File file) {
        super(file);
    }

    @Override
    public void save() {
        try(FileWriter writer = new FileWriter(_file, StandardCharsets.UTF_8, false)) {
            //print header
            writer.write("id;receiverId;amount;date;purpose");
            //write data
            for(BaseTransaction transaction : transactions){
                writer.write(System.lineSeparator());
                writer.write(transaction.getTransactionId() + DELIMITER);
                writer.write(transaction.getReceiverMail() + DELIMITER);
                writer.write(String.valueOf(transaction.getAmount()) + DELIMITER);
                writer.write(transaction.getDate().toString() + DELIMITER);
                writer.write(transaction.getPurposeMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error in saving to file "+_file.getAbsolutePath()+"'!", e);
        }
    }

    @Override
    public boolean load() {
        try(Scanner scanner = new Scanner(_file)) {
            LinkedList<ParseError> errors = new LinkedList<>();
            int line = 1;
            while (scanner.hasNext()){
                String mail = "", money = "", message = "";
                try {
                    String input = scanner.nextLine();
                    String[] split = input.split(String.valueOf(DELIMITER));
                    if (split.length < CsvUtil.READ_ELEMENTS){
                        errors.add(CsvErrors.produceFromWrongLength(line, split.length, CsvUtil.READ_ELEMENTS));
                        continue;
                    }

                    mail = split[0].trim();
                    money = split[1].trim();
                    message = split[2].trim();

                    String receiverMail = CsvUtil.formatText(mail);
                    if(!Regex.EMAIL.matcher(receiverMail).matches())throw new PatternSyntaxException(
                            "No valid email found!", receiverMail, 0);
                    double amount = Double.parseDouble(money);
                    if(amount < 0){
                        errors.add(CsvErrors.produceFromWrongEntry(line, money, "Negative Menge"));
                        continue;
                    }
                    String purpose = message;
                    append(new SimpleTransaction(receiverMail, amount, purpose));
                } catch (NumberFormatException e){
                    errors.add(CsvErrors.produceFromWrongEntry(line, money, "Keine valide Zahl"));
                } catch (PatternSyntaxException e){
                    errors.add(CsvErrors.produceFromWrongEntry(line, mail, "Keine valide E-Mail"));
                } finally{
                    line++;
                }
            }
            if(!errors.isEmpty()){
                CsvUtil.printWrongSyntax(_file, errors);
                return false;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fehler beim laden der Datei '"+_file.getAbsolutePath()+"'! Die Datei exisitert nicht");
            return false;
        }
        return true;
    }

}
