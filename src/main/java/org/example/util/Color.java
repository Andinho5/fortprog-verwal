package org.example.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ThreadLocalRandom;

public enum Color {
    BLACK("\u001B[30m", "0", true),
    RED("\u001B[31m", "4", true),
    LIGHTRED ("\u001B[91m", "c", true),
    GREEN("\u001B[32m", "2", true),
    YELLOW("\u001B[33m", "e", true),
    BLUE("\u001B[34m", "1", true),
    PURPLE("\u001B[35m", "5", true),
    CYAN("\u001B[36m", "b", true),
    WHITE("\u001B[37m","f", true),
    RESET("\u001B[0m", "r",false),
    BOLD("\u001B[1m", "b", false),
    ITALIC("\u001B[3m", "o", false),
    UNDERLINE("\u001B[4m", "n", false),
    STRIKETHROUGH("\u001B[27m", "m", false),
    BOXED("\u001B[51m", "?", false),
    ;

    public static void init(){
        System.setOut(new ColorPrinter(System.out));
    }

    public static Color randomColor(boolean includeAll){
        if(includeAll){
            return values()[ThreadLocalRandom.current().nextInt(0, values().length)];
        }else{
            Color color;
            do {
                color = values()[ThreadLocalRandom.current().nextInt(0, values().length)];
            }while (!color.isColor);
            return color;
        }
    }
    public static Color randomColor(){
        return randomColor(false);
    }

    public static String t(String i){
        return translateColors(i);
    }

    public static String translateColors(String input, boolean addReset){
        for(Color color : values()){
            input = color.translate(input);
        }
        return addReset ? input + RESET : input;
    }

    public static String translateColors(String input){
        return translateColors(input, true);
    }

    private final String ansi;
    private final String colorCode;
    private final boolean isColor;
    Color(String ansi, String colorCode, boolean isColor){
        this.ansi = ansi;
        this.colorCode = colorCode;
        this.isColor = isColor;
    }
    public String getAnsi() {
        return ansi;
    }
    public String getColorCode() {
        return colorCode;
    }
    public boolean isColor() {
        return isColor;
    }
    public String translate(String input){
        input = input.replace("&"+colorCode, ansi).
                replace("§"+colorCode, ansi).replace(toVariable(), ansi);
        return input;
    }

    public String toVariable(){
        return '[' + name() + ']';
    }

    @Override
    public String toString() {
        return ansi;
    }

    private static final class ColorPrinter extends PrintStream {
        public ColorPrinter(OutputStream out) {
            super(out);
        }

        @Override
        public void print(String s) {
            super.print(translateColors(s, false));
        }

        @Override
        public void print(Object obj) {
            super.print(translateColors(String.valueOf(obj), false));
        }

        @Override
        public void println(String x) {
            super.println(translateColors(x));
        }

        @Override
        public void println(Object x) {
            super.println(translateColors(String.valueOf(x)));
        }
    }

}
