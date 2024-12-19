package org.example.util;

import java.util.ArrayList;

/**
 * Simple class that can create and print pretty formatted tables.
 * @author Benjamin Leidolph
 */
public class StringTableFormatter {

    private final ArrayList<String[]> data = new ArrayList<>();
    private final String delimiter;
    private final String[] header;
    private final int headerSize;
    private boolean lineSeparator = true;
    public StringTableFormatter(String delimiter, int headerSize, String... header) {
        this.delimiter = delimiter;
        this.header = header;
        this.headerSize = headerSize;
        centerArray(header);
    }

    /**
     * Adds a row of entries to table.
     * @param data The data to add.
     */
    public void add(String... data){
        centerArray(data);
        this.data.add(data);
    }

    /**
     * Centers all objects of an array so they align perfectly with the table.
     * @param array The array with elements to align.
     */
    private void centerArray(String[] array){
        for(int i = 0; i < array.length; i++){
            array[i] = center(array[i]);
        }
    }

    /**
     * Centers a text to fit in the size of the table.
     * @param text The text to center.
     * @return The centered text.
     */
    private String center(String text){
        int padding = (headerSize - text.length()) / 2;
        int paddingRight = padding + (headerSize - text.length()) % 2;
        return " ".repeat(Math.max(0, padding)) + text + " ".repeat(Math.max(0, paddingRight));
    }

    /**
     * Enables/Disables a line separator after the table header.
     * Default is {@code true}.
     * @param lineSeparator Whether to enable the line separator.
     */
    public void setLineSeparator(boolean lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    /**
     * Prints the pretty made table.
     * @param color Defines a color to print the table in. This is optional and can be {@code null}.
     */
    public void print(Color color){
        String code = color != null ? color.getAnsi() : "";
        System.out.println(code+String.join(delimiter, header));
        if(lineSeparator)System.out.println(code+"-".repeat(headerSize * header.length + (delimiter.length() * header.length * 2)));
        for(String[] row : data){
            System.out.println(code+String.join(delimiter, row));
        }
        if(!code.isEmpty())System.out.print(Color.RESET); //reset color
    }
    public void print(){
        print(null);
    }

    public boolean hasData(){
        return !data.isEmpty();
    }

}
