package org.example.data.parse;

public final class CsvErrors {

    private CsvErrors(){}

    public static EntryError produceFromWrongEntry(int line, String entry, String reason){
        return new EntryError(line, entry, reason);
    }
    public static LengthError produceFromWrongLength(int line, int given, int desired){
        return new LengthError(line,given,desired);
    }

    public static final class EntryError extends ParseError{
        private final String entry, reason;
        public EntryError(int line, String entry, String reason) {
            super(line);
            this.entry = entry;
            this.reason = reason;
        }
        public String getEntry() {
            return entry;
        }
        public String getReason() {
            return reason;
        }
    }
    public static final class LengthError extends ParseError {
        private final int givenLength, desiredLength;
        public LengthError(int line, int given, int desired) {
            super(line);
            this.givenLength = given;
            this.desiredLength = desired;
        }
        public int getGivenLength() {
            return givenLength;
        }
        public int getDesiredLength() {
            return desiredLength;
        }
    }

}
