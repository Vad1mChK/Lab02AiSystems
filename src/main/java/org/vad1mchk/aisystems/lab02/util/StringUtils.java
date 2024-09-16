package org.vad1mchk.aisystems.lab02.util;

public class StringUtils {
    // Method to check if a string starts with a given prefix, ignoring case
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;  // Null check
        }
        if (prefix.length() > str.length()) {
            return false;  // Prefix can't be longer than the string
        }
        return str.substring(0, prefix.length()).equalsIgnoreCase(prefix);
    }

    public static String stripSingleQuotes(String input) {
        if (input != null && input.startsWith("'") && input.endsWith("'")) {
            return input.substring(1, input.length() - 1);
        }
        return input;
    }
}