package org.example.data.parse;

public class ParseError {

    private final int line;
    public ParseError(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }



}
