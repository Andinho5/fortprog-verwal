package org.example.data.parse;

import org.example.util.Color;
import org.example.util.StringTableFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public final class CsvUtil {
    private CsvUtil(){}

    public static final int READ_ELEMENTS = 3; //3 headers

    public static StringTableFormatter buildEmptyTable(){
        return new StringTableFormatter("|", 25,
                "Empfänger-Addresse", "Menge", "Grund");
    }

    static final StringTableFormatter PRINT_TABLE = buildEmptyTable();
    static {
        PRINT_TABLE.add("max@mustermann.de", "30", "Taschengeld");
        PRINT_TABLE.add("frank@gmail.com", "123.4", "Weihnachtsgeld");
        PRINT_TABLE.add("tuete@schips.com", "69", "Skibidy");
    }

    public static void printWrongSyntax(File file, Collection<ParseError> errors){
        System.out.println();
        System.out.println("[RED]Die Datei '"+file.getAbsolutePath()+"' konnte nicht erkannt werden!");
        System.out.println("[RED]Es wurden [BOLD]"+errors.size()+" [RED]Fehler gefunden!");
        //load errors
        ArrayList<CsvErrors.LengthError> lengthLines = new ArrayList<>(errors.size());
        for(ParseError error : errors){
            if(error instanceof CsvErrors.LengthError)lengthLines.add((CsvErrors.LengthError) error);
        }
        if(!lengthLines.isEmpty()) {
            System.out.println("[RED]In folgenden Zeilen wurde eine falsche Länge festgestellt:");
            for(CsvErrors.LengthError error : lengthLines){
                System.out.println("[YELLOW]Zeile "+error.getLine()+": Angegebene Elemente "+
                        error.getGivenLength()+"/"+error.getDesiredLength());
            }
            System.out.println();
        }
        if(errors.size() - lengthLines.size() > 0){ //there are more errors, must be syntax related.
            System.out.println("[RED]» Die Tabelle hält einige falsche Einträge! Sie sollte wie folgt aussehen:");
            PRINT_TABLE.print(Color.YELLOW);
            System.out.println();
            System.out.println("[RED]In den folgenden Zeilen sind folgende Fehler aufgetreten:");
            for(ParseError error : errors){
                if(!(error instanceof CsvErrors.EntryError))continue;
                System.out.println("[YELLOW]("+error.getLine()+") [LIGHTRED]'"+
                        ((CsvErrors.EntryError) error).getEntry()+"' > "+((CsvErrors.EntryError) error).getReason());
            }
            System.out.println();
            System.out.println("[YELLOW][BOLD]» [CYAN]Beachte: Der Kopf darf nicht mit in der Tabelle stehen, alleine die Werte " +
               "(wenn richtig geordnet) reichen!");
        }
    }

    public static String formatText(String text){
        return text != null ? text.trim() : null;
    }

}
