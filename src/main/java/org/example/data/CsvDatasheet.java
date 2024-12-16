package org.example.data;

import org.example.transaction.BaseTransaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Scanner;

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
    public void load() {
        try(Scanner scanner = new Scanner(_file)) {
            while (scanner.hasNext()){
                String input = scanner.nextLine();
                String[] split = input.split(String.valueOf(DELIMITER));
                String receiverMail = split[0];
                double amount = Double.parseDouble(split[1]);
                String purpose = split[2];
                append(new SimpleTransaction(receiverMail, amount, purpose));
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new RuntimeException("Error in loading the file '"+_file.getAbsolutePath()+"'!", e);
        }
    }

}
