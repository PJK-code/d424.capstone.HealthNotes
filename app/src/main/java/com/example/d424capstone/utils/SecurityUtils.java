package com.example.d424capstone.utils;

import android.text.TextUtils;
import java.util.regex.Pattern;

public class SecurityUtils {

    // security pattern to allow safe characters
    private static final Pattern SAFE_TEXT_PATTERN = Pattern.compile("^[\\p{L}\\p{N}\\p{P}\\p{Z}\\n]*$");

    // cleans user input by removing whitespace and removing not safe characters
    public static String sanitizeInput(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        // removes control characters except newlines, carriage returns, and tabs
        String cleaned = input.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        return cleaned.trim();
    }

    // validates that text does not exceed max length and only has allowed characters
    public static boolean isValidText(String input, int maxLength) {
        if (input == null) {
            return true;
        }
        if (input.length() > maxLength) {
            return false;
        }
        return SAFE_TEXT_PATTERN.matcher(input).matches();
    }
}
