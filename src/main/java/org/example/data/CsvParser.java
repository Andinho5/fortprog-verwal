package org.example.data;

import org.example.util.Color;
import org.example.util.StringTableFormatter;

import java.io.File;

public class CsvParser {

    private static final String READ_FORMAT = "Empfänger-Addresse | Menge | Grund";

    private static final StringTableFormatter PRINT_TABLE = new StringTableFormatter("|", 20,
            "Empfänger-Addresse", "Menge", "Grund");
    static {
        PRINT_TABLE.add("max@mustermann.de", "30", "Taschengeld");
        PRINT_TABLE.add("frank@gmail.com", "123.4", "Weihnachtsgeld");
        PRINT_TABLE.add("tuete@schips.com", "69", "Skibidy");
    }

    public static void printWrongSyntax(File file){
        System.out.println();
        System.out.println("[RED]Die Datei '"+file.getAbsolutePath()+"' konnte nicht erkannt werden!");
        System.out.println("[RED]Dies liegt an falschem Syntax. Die csv Tabelle sollte wie folgt aussehen:");
        PRINT_TABLE.print(Color.YELLOW);
        System.out.println("[YELLOW][BOLD]» [CYAN]Beachte: Der Kopf darf nicht mit in der Tabelle stehen, alleine die Werte " +
                "(wenn richtig geordnet) reichen!");
        System.out.println();
    }

    static String formatText(String text){
        return text != null ? text.trim() : null;
    }

}
