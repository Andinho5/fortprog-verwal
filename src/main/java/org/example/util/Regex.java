package org.example.util;

import java.util.regex.Pattern;

public final class Regex {

    private Regex(){}

    public static final Pattern EMAIL = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$");

}
